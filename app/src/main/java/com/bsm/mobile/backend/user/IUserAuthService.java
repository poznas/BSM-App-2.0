package com.bsm.mobile.backend.user;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthResult;

import io.reactivex.Observable;

public interface IUserAuthService {

    Observable<Boolean> isUserSignedIn();

    Observable<Boolean> authWithGoogle(GoogleSignInAccount account);

    String getCurrentUserId();

    void signOut();
}

