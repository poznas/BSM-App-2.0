package com.bsm.mobile.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bsm.mobile.R;
import com.bsm.mobile.backend.google.GoogleAuthService;
import com.bsm.mobile.home.HomeActivity;
import com.bsm.mobile.login.LoginActivityMVP.*;
import com.bsm.mobile.root.App;
import com.google.android.gms.common.SignInButton;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class LoginActivity extends AppCompatActivity implements View{

    @Inject
    Presenter presenter;

    public static final int REQUEST_SIGN_GOOGLE = 9001;

    @BindView(R.id.login_activity_root)
    ViewGroup rootView;
    @BindView(R.id.activity_login_google_button)
    SignInButton googleSignInButton;
    @BindView(R.id.activity_login_progress)
    ProgressBar progressBar;

    private boolean isLoading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        ((App) getApplication()).getComponent().inject(this);
        displayLoadingState();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.attachView(this);
        presenter.subscribeForAuth();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.unsubscribe();
    }

    @Override
    public void goHomeActivity() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @Override
    public void showMessage(String message) {
        popMessage(rootView, message);
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

    private void displayLoadingState() {
        progressBar.setVisibility(isLoading ? VISIBLE : GONE);
        googleSignInButton.setVisibility(isLoading ? GONE : VISIBLE);
    }

    @OnClick(R.id.activity_login_google_button)
    public void attemptGoogleSignIn(){
        Intent intent = GoogleAuthService.getSignInIntent(this);
        startActivityForResult(intent, REQUEST_SIGN_GOOGLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( resultCode == RESULT_OK && requestCode == REQUEST_SIGN_GOOGLE){
            presenter.handleGoogleSignInResult(GoogleAuthService.getGoogleSignInAccount(data));
        }
    }
}
