package com.bsm.mobile.login;

import com.bsm.mobile.backend.user.IUserAuthService;
import com.bsm.mobile.login.LoginActivityMVP.*;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LoginModel implements Model {

    private IUserAuthService userAuthService;

    @Override
    public Observable<Boolean> getSignInStatus() {
        return userAuthService.isUserSignedIn();
    }

    @Override
    public Maybe<Boolean> authWithGoogle(GoogleSignInAccount account) {
        return userAuthService.authWithGoogle(account);
    }
}
