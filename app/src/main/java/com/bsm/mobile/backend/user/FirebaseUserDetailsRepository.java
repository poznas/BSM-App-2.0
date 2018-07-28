package com.bsm.mobile.backend.user;

import com.bsm.mobile.backend.AbstractFirebaseRepository;
import com.bsm.mobile.common.utils.NonNullObjectMapper;
import com.bsm.mobile.legacy.model.User;
import com.google.firebase.database.DatabaseReference;

import io.reactivex.Single;

import static com.bsm.mobile.Constants.BRANCH_USER_DETAILS;
import static com.bsm.mobile.Constants.LABEL_JUDGE;
import static com.bsm.mobile.common.utils.UserDataUtils.getUserDetailsId;
import static com.bsm.mobile.common.utils.UserDataUtils.validGender;
import static com.bsm.mobile.common.utils.UserDataUtils.validLabel;
import static com.bsm.mobile.common.utils.UserDataUtils.validTeam;

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
        return getRoot().child(BRANCH_USER_DETAILS);
    }


    @Override
    public Single<Boolean> deleteUserDetails(User user) {
        return Single.create(emitter ->
            getRepositoryReference().child(getUserDetailsId(user)).setValue(null)
                .addOnCompleteListener(task -> emitter.onSuccess(task.isSuccessful())));
    }

    @Override
    public Single<Boolean> updateUserDetails(User user) {

        User newUserDetails = (
                user.getLabel().equals(LABEL_JUDGE) ?
                        User.builder()
                                .label(LABEL_JUDGE) :
                        User.builder()
                                .displayName(user.getDisplayName())
                                .facebook(user.getFacebook())
                                .gender(validGender(user) ? user.getGender() : null)
                                .label(validLabel(user) ? user.getLabel() : null)
                                .team(validTeam(user) ? user.getTeam() : null)
        ).build();

        return Single.create(emitter ->
            getRepositoryReference().child(getUserDetailsId(user))
                    .updateChildren(NonNullObjectMapper.map(newUserDetails))
                    .addOnCompleteListener(task -> emitter.onSuccess(task.isSuccessful())));
    }

}
