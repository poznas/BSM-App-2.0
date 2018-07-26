package com.bsm.mobile.backend.user;

import com.bsm.mobile.legacy.model.User;

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
public interface IUserDetailsRepository {

    Single<Boolean> deleteUser(User user);

    Single<Boolean> updateUser(User user);

    Single<Boolean> insertUser(User user);
}
