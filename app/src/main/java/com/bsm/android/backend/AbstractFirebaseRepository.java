package com.bsm.android.backend;

import android.support.annotation.NonNull;

import com.bsm.android.model.User;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.reactivex.ObservableEmitter;

public abstract class AbstractFirebaseRepository {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference root;

    protected FirebaseDatabase getFirebaseDatabase() {
        if( firebaseDatabase == null ){
            firebaseDatabase = FirebaseDatabase.getInstance();
        }
        return firebaseDatabase;
    }

    public DatabaseReference getRoot() {
        if( root == null ){
            root = getFirebaseDatabase().getReference().getRoot();
        }
        return root;
    }

    public abstract class AbstractValueEventListener<T> implements ValueEventListener{

        private final ObservableEmitter<T> emitter;

        protected AbstractValueEventListener(ObservableEmitter<T> emitter, DatabaseReference reference) {
            this.emitter = emitter;
            reference.addValueEventListener(this);
            emitter.setCancellable(() -> reference.removeEventListener(this));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            if( !emitter.isDisposed() ) emitter.onError(databaseError.toException());
        }
    }
}
