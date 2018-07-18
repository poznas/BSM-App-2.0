package com.bsm.mobile.backend.user;

import android.util.Log;

import com.bsm.mobile.common.NullFighter;
import com.bsm.mobile.common.Tagable;
import com.bsm.mobile.legacy.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

import io.reactivex.Maybe;
import io.reactivex.Observable;

import static com.bsm.mobile.Constants.DEFAULT_USER_PHOTO_URL;

public class FirebaseUserAuthService implements IUserAuthService, Tagable, NullFighter {

    private IUserRepository userRepository;
    private FirebaseAuth serviceFirebaseAuth;
    private AuthStateListener listener;

    public FirebaseUserAuthService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private Observable<FirebaseAuth> getUserAuthState() {

        return Observable.create(emitter -> {
            listener = firebaseAuth -> {
                emitter.onNext(firebaseAuth);
                setInitialUserData(firebaseAuth.getCurrentUser());
            };
            getServiceFirebaseAuth().addAuthStateListener(listener);
            emitter.setCancellable(
                    () -> getServiceFirebaseAuth().removeAuthStateListener(listener));
        });
    }

    private void setInitialUserData(FirebaseUser firebaseUser) {
        if(firebaseUser == null){return;}

        userRepository.updateUserDetails(
                firebaseUser.getUid(),
                User.builder()
                    .displayName(firebaseUser.getDisplayName())
                    .email(firebaseUser.getEmail())
                    .photoUrl( firebaseUser.getPhotoUrl() != null
                            ? firebaseUser.getPhotoUrl().toString()
                            : DEFAULT_USER_PHOTO_URL).build());
    }

    @Override
    public Observable<Boolean> isUserSignedIn() {
        return getUserAuthState().map(
                firebaseAuth -> firebaseAuth.getCurrentUser() != null
        );
    }

    @Override
    public Maybe<Boolean> authWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        return firebaseAuthWithGoogle(credential).map(authResult -> authResult.getUser() != null);
    }

    @Override
    public Observable<User> getCurrentUser() {
        return userRepository.getUser(getCurrentUserId());
    }

    private Maybe<AuthResult> firebaseAuthWithGoogle(AuthCredential credential){
        Log.d(getTag(), "attempt to auth with provider: " + credential.getProvider());

        return Maybe.create(emitter -> {
            getServiceFirebaseAuth().signInWithCredential(credential)
                    .addOnSuccessListener(emitter::onSuccess)
                    .addOnFailureListener(e -> {
                        if(!emitter.isDisposed()) emitter.onError(e);
                    })
                    .addOnCompleteListener(task -> {emitter.onComplete();});
        });
    }

    @Override
    public String getCurrentUserId() {
        return Objects.requireNonNull(getCurrentFirebaseUser()).getUid();
    }


    private FirebaseUser getCurrentFirebaseUser(){
        return getServiceFirebaseAuth().getCurrentUser();
    }

    @Override
    public void signOut() {
        serviceFirebaseAuth.signOut();
    }

    private FirebaseAuth getServiceFirebaseAuth() {
        if(serviceFirebaseAuth == null){
            serviceFirebaseAuth = FirebaseAuth.getInstance();
        }
        return serviceFirebaseAuth;
    }
}
