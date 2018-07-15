package com.bsm.mobile.backend.user;

import com.bsm.mobile.legacy.model.Privilege;
import com.bsm.mobile.legacy.model.User;

import java.util.List;

import io.reactivex.Observable;

public interface IUserPrivilegeRepository {

    Observable<List<Privilege>> getUserPrivileges(User user);
}
