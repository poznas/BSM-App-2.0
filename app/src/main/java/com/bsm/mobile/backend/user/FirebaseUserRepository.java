package com.bsm.mobile.backend.user;

import android.util.Log;

import com.bsm.mobile.backend.AbstractFirebaseRepository;
import com.bsm.mobile.common.NonNullObjectMapper;
import com.bsm.mobile.legacy.model.User;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import lombok.RequiredArgsConstructor;

import static com.bsm.mobile.Constants.BRANCH_USERS;

/**
 * server side BSM 2017 application was implemented in such a way that:
 *
 *      deleting user data from db is not supported and not really needed
 *      each time user is signing in:
 *              main user data '/users' is updated with new display name and image URL from Google
 *              user data is filled with data stored in '/UserDetails'
 *
 *      -> so in order to "delete" (block) user:
 *                  delete user details from '/UserDetails'
 *                  delete remaining user data from '/users'
 */
@RequiredArgsConstructor
public class FirebaseUserRepository extends AbstractFirebaseRepository implements IUserRepository{

    private final IUserDetailsRepository userDetailsRepository;

    @Override
    protected DatabaseReference getRepositoryReference() {
        return getRoot().child(BRANCH_USERS);
    }

    @Override
    public void updateUserDetails(String userId, User userDetails) {
        getRepositoryReference().child(userId)
                .updateChildren(NonNullObjectMapper.map(userDetails));
    }

    @Override
    public Observable<User> getUser(String userId) {

        DatabaseReference userDataReference = getRepositoryReference().child(userId);

        return Observable.create(emitter ->
            new SimpleValueEventListener(emitter, userDataReference)
                    .setOnDataChange(dataSnapshot -> {
                        User user = dataSnapshot.getValue(User.class);
                        Log.d(getTag(), "retrieved user id : " + userId + " : " + user);
                        emitter.onNext(user);
                    })
        );
    }

    @Override
    public Single<Boolean> deleteUser(User user) {
        return null;
    }

    @Override
    public Single<Boolean> updateUser(User user) {
        return null;
    }

    @Override
    public Single<Boolean> insertUser(User user) {
        return null;
    }

    @Override
    public Observable<List<User>> getUserList() {
        return null;
    }
}
