package com.bsm.mobile.backend.user;

import android.util.Log;

import com.bsm.mobile.core.Tagable;
import com.bsm.mobile.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.Objects;

import io.reactivex.Observable;

import static com.bsm.mobile.Constants.DEFAULT_USER_PHOTO_URL;

public class FirebaseUserAuthService implements IUserAuthService, Tagable {

    private IUserRepository userRepository;
    private FirebaseAuth serviceFirebaseAuth;
    private AuthStateListener listener;

    public FirebaseUserAuthService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Observable<FirebaseAuth> getUserAuthState() {

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
    public Observable<AuthResult> authWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        return Observable.create(
                emitter -> getServiceFirebaseAuth()
                        .signInWithCredential(credential)
                        .addOnCompleteListener(task -> {
                            try {
                                FirebaseInstanceId.getInstance().deleteInstanceId();
                            } catch (IOException e) {
                                Log.d(getTag(), ".deleteInstanceId() exc: " + e.getMessage());
                                emitter.onError(e);
                            }
                            if(!task.isSuccessful()){
                                emitter.onError(task.getException());
                            }else {
                                emitter.onNext(task.getResult());
                            }
                        }));
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
