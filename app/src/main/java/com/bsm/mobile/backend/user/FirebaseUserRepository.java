package com.bsm.mobile.backend.user;

import android.util.Log;

import com.bsm.mobile.backend.AbstractFirebaseRepository;
import com.bsm.mobile.common.utils.NonNullObjectMapper;
import com.bsm.mobile.legacy.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import lombok.RequiredArgsConstructor;

import static com.bsm.mobile.common.resource.Constants.BRANCH_USERS;

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
    protected DatabaseReference getRepositoryQuery() {
        return getRoot().child(BRANCH_USERS);
    }

    @Override
    public void updateMainUserData(String userId, User userData) {
        getRepositoryQuery().child(userId)
                .updateChildren(NonNullObjectMapper.map(userData));
    }

    @Override
    public Observable<User> getUser(String userId) {

        DatabaseReference userDataReference = getRepositoryQuery().child(userId);

        return Observable.create(emitter ->
            new SimpleValueEventListener(emitter, userDataReference)
                    .setOnDataChange(dataSnapshot -> {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) user.setId(userId);
                        Log.d(getTag(), "retrieved user : " + user);
                        emitter.onNext(user);
                    }));
    }

    @Override
    public Observable<List<User>> getUserList() {
        return Observable.create(emitter ->
                new SimpleValueEventListener(emitter, getRepositoryQuery())
                    .setOnDataChange(dataSnapshot -> {
                        List<User> users = new LinkedList<>();
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            User user = child.getValue(User.class);
                            if (user != null) user.setId(child.getKey());
                            users.add(user);
                        }
                        emitter.onNext(users);
                    }));
    }

    @Override
    public Single<Boolean> deleteUser(User user) {
        return Single.zip(deleteMainUserData(user),
                userDetailsRepository.deleteUserDetails(user),
                (mainSuccess, detailsSuccess) -> mainSuccess && detailsSuccess);
    }

    @Override
    public Single<Boolean> updateUser(User user) {
        return Single.zip(updateMainUserData(user),
                userDetailsRepository.updateUserDetails(user),
                (mainSuccess, detailsSuccess) -> mainSuccess && detailsSuccess);
    }

    private Single<Boolean> deleteMainUserData(User user) {
        Log.d(getTag(), "attempt to DELETE main user data : " + user);
        return Single.create(emitter ->
                getRepositoryQuery().child(user.getId()).setValue(null)
                        .addOnCompleteListener(task -> emitter.onSuccess(task.isSuccessful())));
    }

    private Single<Boolean> updateMainUserData(User user) {
        Log.d(getTag(), "attempt to UPDATE main user data : " + user);
        return Single.create(emitter ->
            getRepositoryQuery().child(user.getId())
                    .updateChildren(NonNullObjectMapper.map(user))
                    .addOnCompleteListener(task -> emitter.onSuccess(task.isSuccessful())));
    }

}
