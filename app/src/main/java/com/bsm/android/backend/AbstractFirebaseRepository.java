package com.bsm.android.backend;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
}
