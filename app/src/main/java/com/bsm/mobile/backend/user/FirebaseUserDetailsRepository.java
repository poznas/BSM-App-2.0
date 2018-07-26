package com.bsm.mobile.backend.user;

import com.bsm.mobile.backend.AbstractFirebaseRepository;
import com.bsm.mobile.legacy.model.User;
import com.google.firebase.database.DatabaseReference;

import io.reactivex.Single;

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
public class FirebaseUserDetailsRepository extends AbstractFirebaseRepository implements IUserDetailsRepository {

    @Override
    protected DatabaseReference getRepositoryReference() {
        return null;
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
}
