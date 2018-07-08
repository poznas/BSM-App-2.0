package com.bsm.android.firebase.user;

import android.support.annotation.NonNull;

import com.bsm.android.firebase.AbstractFirebaseRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import com.bsm.android.model.User;
import com.google.firebase.database.ValueEventListener;

import io.reactivex.Observable;

import static com.bsm.android.Constants.*;

public class FirebaseUserRepository extends AbstractFirebaseRepository implements IUserRepository{

    private ValueEventListener userListener;

    private DatabaseReference getUserDataReference(String userId) {
        return getRoot().child(USERS_BRANCH).child(userId);
    }

    @Override
    public void updateUserDetails(String userId, User userDetails) {
        getUserDataReference(userId).setValue(userDetails);
    }

    @Override
    public Observable<User> getUser(String userId) {

        DatabaseReference userDataReference = getUserDataReference(userId);

        return Observable.create(emitter -> {
            userListener = new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    emitter.onNext(dataSnapshot.getValue(User.class));
                    userDataReference.removeEventListener(userListener);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            };
            userDataReference.addValueEventListener(userListener);
        });
    }
}
