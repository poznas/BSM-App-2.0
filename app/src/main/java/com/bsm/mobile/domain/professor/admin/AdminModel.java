package com.bsm.mobile.professor.admin;

import com.bsm.mobile.backend.report.IReportRepository;
import com.bsm.mobile.backend.user.IUserRepository;
import com.bsm.mobile.legacy.model.User;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;

import static com.bsm.mobile.professor.admin.AdminActivityMVP.Model;

@RequiredArgsConstructor
public class AdminModel implements Model{

    private final IReportRepository reportRepository;
    private final IUserRepository userRepository;

    @Override
    public Single<Boolean> deleteUser(User user) {
        return userRepository.deleteUser(user);
    }

    @Override
    public Single<Boolean> setReportLockState(boolean unlocked) {
        return reportRepository.setReportLockState(unlocked);
    }

    @Override
    public Observable<Boolean> getReportLockState() {
        return reportRepository.getReportLockState();
    }

    @Override
    public Observable<List<User>> getUserList() {
        return userRepository.getUserList()
                .observeOn(Schedulers.computation())
                .doOnEach(users -> Collections.sort(users.getValue()));
    }
}
