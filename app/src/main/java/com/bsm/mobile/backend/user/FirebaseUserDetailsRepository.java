package com.bsm.mobile.backend.user;

import com.bsm.mobile.backend.AbstractFirebaseRepository;
import com.bsm.mobile.legacy.model.User;
import com.google.firebase.database.DatabaseReference;

import io.reactivex.Single;

import static com.bsm.mobile.common.resource.Constants.BRANCH_USER_DETAILS;
import static com.bsm.mobile.common.utils.UserDataValidator.getValidData;

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
    protected DatabaseReference getRepositoryQuery() {
        return getRoot().child(BRANCH_USER_DETAILS);
    }


    @Override
    public Single<Boolean> deleteUserDetails(User user) {
        return Single.create(emitter ->
            getRepositoryQuery().child(getUserDetailsId(user)).setValue(null)
                .addOnCompleteListener(task -> emitter.onSuccess(task.isSuccessful())));
    }

    @Override
    public Single<Boolean> updateUserDetails(User user) {
        return Single.create(emitter ->
            getRepositoryQuery().child(getUserDetailsId(user)).setValue(getValidData(user, true))
                    .addOnCompleteListener(task -> emitter.onSuccess(task.isSuccessful())));
    }

    private static String getUserDetailsId(User user){
        return user.getEmail().replaceAll("\\.", "ś");
    }

}
