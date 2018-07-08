package com.bsm.android.firebase.user;

import com.bsm.android.model.User;

import io.reactivex.Observable;

public interface IUserRepository {

    void updateUserDetails(String userId, User userDetails);

    Observable<User> getUser(String userId);
}
