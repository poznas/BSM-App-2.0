package com.bsm.mobile.backend.user;

import com.bsm.mobile.legacy.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import io.reactivex.Maybe;
import io.reactivex.Observable;

public interface IUserAuthService {

    Observable<Boolean> isUserSignedIn();

    Maybe<Boolean> authWithGoogle(GoogleSignInAccount account);

    Observable<User> getCurrentUser();

    String getCurrentUserId();

    void signOut();
}

