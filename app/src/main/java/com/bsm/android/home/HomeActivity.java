package com.bsm.android.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bsm.android.R;
import com.bsm.android.firebase.google.GoogleAuthService;
import com.bsm.android.login.LoginActivity;
import com.bsm.android.root.App;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.bsm.android.home.HomeActivityMVP.*;

public class HomeActivity extends AppCompatActivity implements View {

    @Inject
    Presenter presenter;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ((App) getApplication()).getComponent().inject(this);
        displayLoadingState();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.attachView(this);
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
    public void goLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void signOutFromGoogle() {
        GoogleAuthService.signOut(this);
    }

    private void displayLoadingState() {
        privilegesProgressBar.setVisibility(isLoading ? VISIBLE : GONE);
    }
}
