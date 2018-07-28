package com.bsm.mobile.legacy.domain.professor.medal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bsm.mobile.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mlody Danon on 7/26/2017.
 */

public class AddMedalActivity extends AppCompatActivity {

    @BindView(R.id.merito_view)
    View meritoView;
    @BindView(R.id.infamia_view)
    View infamiaView;

    private Intent editDetailsIntent;
    private Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medal);
        ButterKnife.bind(this);

        editDetailsIntent = new Intent(this, MedalEditDetailsActivity.class);
        bundle = new Bundle();

        meritoView.setOnClickListener(v -> {
            bundle.putString("medal", "merito");
            editDetailsIntent.putExtras(bundle);
            startActivity(editDetailsIntent);
            finish();
        });
        infamiaView.setOnClickListener(v -> {
            bundle.putString("medal", "infamia");
            editDetailsIntent.putExtras(bundle);
            startActivity(editDetailsIntent);
            finish();
        });
    }
}
