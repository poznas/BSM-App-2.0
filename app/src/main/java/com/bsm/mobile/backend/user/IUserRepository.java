package com.bsm.mobile.backend.user;

import com.bsm.mobile.legacy.model.User;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface IUserRepository {

    void updateUserDetails(String userId, User userDetails);

    Observable<User> getUser(String userId);

    Single<Boolean> deleteUser(User user);

    Single<Boolean> updateUser(User user);

    Single<Boolean> insertUser(User user);

    Observable<List<User>> getUserList();
}
