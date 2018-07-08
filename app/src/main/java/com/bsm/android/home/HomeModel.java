package com.bsm.android.home;

import com.bsm.android.firebase.user.IUserAuthService;

import lombok.AllArgsConstructor;

import static com.bsm.android.home.HomeActivityMVP.*;

@AllArgsConstructor
public class HomeModel implements Model {

    private IUserAuthService userAuthService;

    @Override
    public void signOut() {
        userAuthService.signOut();
    }
}
