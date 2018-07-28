package com.bsm.mobile.backend.user;

import com.bsm.mobile.legacy.model.User;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface IUserRepository {

    void updateMainUserData(String userId, User userData);

    Observable<User> getUser(String userId);

    Single<Boolean> deleteUser(User user);

    Single<Boolean> updateUser(User user);

    Observable<List<User>> getUserList();
}
