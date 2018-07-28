package com.bsm.mobile.domain.professor.admin.user;

import com.bsm.mobile.backend.user.IUserRepository;
import com.bsm.mobile.legacy.model.User;

import io.reactivex.Single;
import lombok.RequiredArgsConstructor;

import static com.bsm.mobile.domain.professor.admin.user.EditUserActivityMVP.Model;
import static java.util.concurrent.TimeUnit.SECONDS;

@RequiredArgsConstructor
public class EditUserModel implements Model {

    private final IUserRepository userRepository;

    @Override
    public Single<Boolean> updateUser(User user) {
        return userRepository.updateUser(user)
                .timeout(10, SECONDS)
                .onErrorResumeNext(Single.just(false));
    }
}
