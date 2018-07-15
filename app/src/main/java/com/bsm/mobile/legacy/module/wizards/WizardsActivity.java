package com.bsm.mobile.legacy.module.wizards;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bsm.mobile.R;
import com.bsm.mobile.legacy.model.User;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mlody Danon on 7/24/2017.
 */

public class WizardsActivity extends AppCompatActivity {

    @BindView(R.id.simple_recycler)
    RecyclerView usersRecycler;

    private static DatabaseReference mRootRef;

    private UsersAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_recycler_list);
        ButterKnife.bind(this);

        mRootRef = FirebaseDatabase.getInstance().getReference();

        initializeUsersRecycler();
    }


    private void initializeUsersRecycler() {
        Query query = mRootRef.child("users").orderByChild("team").startAt(true);

        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(query, User.class)
                        .build();

        adapter = new UsersAdapter(options);
        usersRecycler.setAdapter(adapter);
        usersRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
