package com.bsm.mobile.legacy.domain.zongler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bsm.mobile.R;
import com.bsm.mobile.legacy.model.ZonglerPost;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bsm.mobile.common.resource.Constants.BRAND_ZONGLER;

public class ZonglerActivity extends AppCompatActivity {


    @BindView(R.id.simple_recycler)
    RecyclerView PostRecycler;

    private static DatabaseReference mRootRef;

    private PostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_recycler_list);
        ButterKnife.bind(this);
        setTitle(BRAND_ZONGLER);

        mRootRef = FirebaseDatabase.getInstance().getReference();

        initializePostRecycler();

        adapter.startListening();
    }

    private void initializePostRecycler() {
        Query query = mRootRef.child("Zongler").orderByChild("timestamp");
        FirebaseRecyclerOptions<ZonglerPost> options =
                new FirebaseRecyclerOptions.Builder<ZonglerPost>()
                        .setQuery(query, ZonglerPost.class)
                        .build();

        adapter = new PostAdapter(options);
        PostRecycler.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        PostRecycler.setLayoutManager(layoutManager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.stopListening();
    }
    
}

