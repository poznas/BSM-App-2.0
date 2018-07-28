package com.bsm.mobile.backend;

import android.support.annotation.NonNull;
import android.util.Log;

import com.bsm.mobile.common.Tagable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import io.reactivex.ObservableEmitter;
import lombok.Setter;

public abstract class AbstractFirebaseRepository implements Tagable{

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference root;

    protected Query repositoryReference;

    protected abstract Query getRepositoryReference();

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

    @Setter
    public class SimpleValueEventListener implements ValueEventListener{

        private DataChangeHandler onDataChange;

        public SimpleValueEventListener(@NonNull ObservableEmitter emitter, @NonNull final Query reference) {
            Log.d(getTag(), "accessing firebase reference : " + reference.toString());

            emitter.setCancellable(() -> reference.removeEventListener(this));
            reference.addValueEventListener(this);
        }

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            onDataChange.handleDataSnapshot(dataSnapshot);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.w(getTag(), " on Cancelled : " + databaseError.getMessage());
        }
    }

    public interface DataChangeHandler{

        void handleDataSnapshot(DataSnapshot dataSnapshot);
    }
}
