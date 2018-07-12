package com.bsm.mobile.backend.user;

import com.bsm.mobile.model.Privilege;
import com.bsm.mobile.model.User;

import java.util.List;

import io.reactivex.Observable;

public interface IUserPrivilegeRepository {

    Observable<List<Privilege>> getUserPrivileges(User user);
}
