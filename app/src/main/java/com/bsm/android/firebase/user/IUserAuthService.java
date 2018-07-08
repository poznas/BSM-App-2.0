package com.bsm.android.firebase.user;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.Observable;

public interface IUserAuthService {

    Observable<Boolean> isUserSignedIn();

    Observable<AuthResult> authWithGoogle(GoogleSignInAccount account);

    String getCurrentUserId();

    void signOut();
}

