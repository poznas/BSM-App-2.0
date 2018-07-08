package com.bsm.android.backend.user;

import com.bsm.android.model.Privilege;
import com.bsm.android.model.User;

import java.util.List;

import io.reactivex.Observable;

public interface IUserPrivilegeRepository {

    Observable<List<Privilege>> getUserPrivileges(User user);
}
