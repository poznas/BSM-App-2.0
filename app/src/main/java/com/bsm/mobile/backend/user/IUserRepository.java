package com.bsm.mobile.backend.user;

import com.bsm.mobile.model.User;

import io.reactivex.Observable;

public interface IUserRepository {

    void updateUserDetails(String userId, User userDetails);

    Observable<User> getUser(String userId);
}
