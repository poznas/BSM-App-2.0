package com.bsm.mobile.backend;

import android.support.annotation.NonNull;
import android.util.Log;

import com.bsm.mobile.core.Tagable;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.reactivex.ObservableEmitter;

public abstract class AbstractFirebaseRepository implements Tagable{

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference root;

    protected DatabaseReference repositoryReference;

    protected abstract DatabaseReference getRepositoryReference();

    private FirebaseDatabase getFirebaseDatabase() {
        if( firebaseDatabase == null ){
            firebaseDatabase = FirebaseDatabase.getInstance();
        }
        return firebaseDatabase;
    }

    protected DatabaseReference getRoot() {
        if( root == null ){
            root = getFirebaseDatabase().getReference().getRoot();
        }
        return root;
    }

    public abstract class AbstractValueEventListener<T> implements ValueEventListener{

        private final ObservableEmitter<T> emitter;

        protected AbstractValueEventListener(@NonNull ObservableEmitter<T> emitter,
                                             @NonNull final DatabaseReference reference) {
            this.emitter = emitter;
            reference.addValueEventListener(this);
            emitter.setCancellable(() -> reference.removeEventListener(this));
            Log.d(getTag(), "accessing firebase reference : " + reference.toString());
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.w(getTag(), " on Cancelled : " + databaseError.getMessage());

            //if( !emitter.isDisposed() ) emitter.onError(databaseError.toException());
        }
    }
}
