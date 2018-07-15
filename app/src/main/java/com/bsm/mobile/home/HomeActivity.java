package com.bsm.mobile.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bsm.mobile.R;
import com.bsm.mobile.backend.google.GoogleAuthService;
import com.bsm.mobile.login.LoginActivity;
import com.bsm.mobile.legacy.model.Privilege;
import com.bsm.mobile.root.App;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.bsm.mobile.Constants.TEAM_CORMEUM;
import static com.bsm.mobile.Constants.TEAM_MUTINIUM;
import static com.bsm.mobile.Constants.TEAM_SENSUM;
import static com.bsm.mobile.home.HomeActivityMVP.*;

public class HomeActivity extends AppCompatActivity implements View {

    @Inject
    Presenter presenter;

    private PrivilegeAdapter privilegeAdapter;

    @BindView(R.id.main_activity_root)
    ViewGroup rootView;
    @BindView(R.id.activity_main_privileges_recycler)
    RecyclerView privilegeRecyclerView;
    @BindView(R.id.privileges_progress_bar)
    ProgressBar privilegesProgressBar;

    private boolean isLoading;

    @BindView(R.id.cormeum_points)
    TextView cormeumPointsTextView;
    @BindView(R.id.sensum_points)
    TextView sensumPointsTextView;
    @BindView(R.id.mutinium_points)
    TextView mutiniumPointsTextView;

    @BindView(R.id.cormeum_image)
    ImageView cormeumImageView;
    @BindView(R.id.sensum_image)
    ImageView sensumImageView;
    @BindView(R.id.mutinium_image)
    ImageView mutiniumImageView;

    private HashMap<String, Intent> teamIntentMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ((App) getApplication()).getComponent().inject(this);
        presenter.attachView(this);

        teamIntentMap = HomeIntentFactory.getTeamIntentMap(this);
        initializePrivilegesRecycler();
    }

    private void initializePrivilegesRecycler() {

        privilegeAdapter = new PrivilegeAdapter(this);
        privilegeRecyclerView.setAdapter(privilegeAdapter);
        privilegeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        privilegeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.subscribeForData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.unsubscribe();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                return presenter.signOut();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showProgress() {
        isLoading = true;
        displayLoadingState();
    }

    @Override
    public void hideProgress() {
        isLoading = false;
        displayLoadingState();
    }

    @Override
    public void showMessage(String message) {
        popMessage(rootView, message);
    }

    @Override
    public void goLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void updatePrivileges(List<Privilege> privileges) {
        privilegeAdapter.updatePrivileges(privileges);
    }

    @Override
    public void setTeamImagesClickListeners() {
        cormeumImageView.setOnClickListener((v) -> startActivity(teamIntentMap.get(TEAM_CORMEUM)));
        sensumImageView.setOnClickListener((v) -> startActivity(teamIntentMap.get(TEAM_SENSUM)));
        mutiniumImageView.setOnClickListener((v) -> startActivity(teamIntentMap.get(TEAM_MUTINIUM)));
    }

    @Override
    public void updateScores(HashMap<String, Long> scores) {
        cormeumPointsTextView.setText(String.valueOf(scores.get(TEAM_CORMEUM)));
        sensumPointsTextView.setText(String.valueOf(scores.get(TEAM_SENSUM)));
        mutiniumPointsTextView.setText(String.valueOf(scores.get(TEAM_MUTINIUM)));
    }

    private void displayLoadingState() {
        privilegesProgressBar.setVisibility(isLoading ? VISIBLE : GONE);
    }
}
