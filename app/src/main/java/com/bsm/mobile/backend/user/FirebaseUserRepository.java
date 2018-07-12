package com.bsm.mobile.backend.user;

import android.support.annotation.NonNull;

import com.bsm.mobile.backend.AbstractFirebaseRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import com.bsm.mobile.model.User;
import com.google.firebase.database.ValueEventListener;

import io.reactivex.Observable;

import static com.bsm.mobile.Constants.*;

public class FirebaseUserRepository extends AbstractFirebaseRepository implements IUserRepository{

    private DatabaseReference getUserDataReference(String userId) {
        return getRoot().child(BRANCH_USERS).child(userId);
    }

    @Override
    public void updateUserDetails(String userId, User userDetails) {
        getUserDataReference(userId).setValue(userDetails);
    }

    @Override
    public Observable<User> getUser(String userId) {

        DatabaseReference userDataReference = getUserDataReference(userId);

        return Observable.create(emitter -> {
            new AbstractValueEventListener<User>(emitter, userDataReference) {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    emitter.onNext(dataSnapshot.getValue(User.class));
                }
            };
        });
    }
}
