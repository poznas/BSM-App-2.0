package com.bsm.mobile.professor.admin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Switch;

import com.bsm.mobile.R;
import com.bsm.mobile.legacy.model.User;
import com.bsm.mobile.root.App;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.bsm.mobile.professor.admin.AdminActivityMVP.Presenter;
import static com.bsm.mobile.professor.admin.AdminActivityMVP.View;


public class AdminActivity extends AppCompatActivity implements View {

    @Inject
    Presenter presenter;

    private UserAdapter userAdapter;

    @BindView(R.id.layout_parent)
    ViewGroup rootView;
    @BindView(R.id.report_lock_progress_bar)
    ProgressBar reportLockProgressBar;
    @BindView(R.id.report_lock_switch)
    Switch reportLockSwitch;
    @BindView(R.id.users_progress_bar)
    ProgressBar usersProgressBar;
    @BindView(R.id.users_recycler_view)
    RecyclerView usersRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_admin);
        ((App) getApplication()).getComponent().inject(this);
        ButterKnife.bind(this);
        presenter.attachView(this);

        initializeUserRecyclerView();
    }

    private void initializeUserRecyclerView() {
        userAdapter = new UserAdapter(presenter);
        usersRecyclerView.setAdapter(userAdapter);
        usersRecyclerView.setItemAnimator(new DefaultItemAnimator());
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
    public void setReportLockSwitchListener() {
        reportLockSwitch.setOnCheckedChangeListener(
                (button, isChecked) -> presenter.handleSwitchChange(isChecked));
    }

    @Override
    public void setReportLockProgress(boolean loading) {
        reportLockProgressBar.setVisibility(loading ? VISIBLE : GONE);
        reportLockSwitch.setVisibility(loading ? GONE : VISIBLE);
    }

    @Override
    public void setUserListProgress(boolean loading) {
        usersProgressBar.setVisibility(loading ? VISIBLE : GONE);
    }


    @Override
    public void showMessage(String message) {
        popMessage(rootView, message);
    }

    @Override
    public void updateUsers(List<User> users) {
        userAdapter.updateUsers(users);
    }

    @Override
    public void updateReportLock(boolean unlocked) {
        reportLockSwitch.setChecked(unlocked);
        setReportLockSwitchListener();
    }
}
