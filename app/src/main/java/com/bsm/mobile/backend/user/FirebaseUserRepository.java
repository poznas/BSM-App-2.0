package com.bsm.mobile.backend.user;

import android.support.annotation.NonNull;
import android.util.Log;

import com.bsm.mobile.backend.AbstractFirebaseRepository;
import com.bsm.mobile.common.NonNullObjectMapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import com.bsm.mobile.legacy.model.User;

import io.reactivex.Observable;

import static com.bsm.mobile.Constants.*;

public class FirebaseUserRepository extends AbstractFirebaseRepository implements IUserRepository{

    @Override
    public void updateUserDetails(String userId, User userDetails) {
        getRepositoryReference().child(userId)
                .updateChildren(NonNullObjectMapper.map(userDetails));
    }

    @Override
    public Observable<User> getUser(String userId) {

        DatabaseReference userDataReference = getRepositoryReference().child(userId);

        return Observable.create(emitter -> {
            new AbstractValueEventListener<User>(emitter, userDataReference) {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    Log.d(getTag(), "retrieved user id : "
                            + userId + " : " + (user != null ? user.toString() : null));
                    emitter.onNext(user);
                }
            };
        });
    }

    @Override
    protected DatabaseReference getRepositoryReference() {
        return getRoot().child(BRANCH_USERS);
    }
}
