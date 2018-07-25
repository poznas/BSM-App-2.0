package com.bsm.mobile.professor.admin.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Switch;

import com.bsm.mobile.R;
import com.bsm.mobile.legacy.model.User;

import java.util.List;

import butterknife.BindView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.bsm.mobile.professor.admin.user.AdminActivityMVP.View;


public class AdminActivity extends AppCompatActivity implements View {

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
    }

    @Override
    public void setReportLockSwitchListener() {

    }

    @Override
    public void setReportLockProgress(boolean loading) {
        reportLockProgressBar.setVisibility(loading ? VISIBLE : GONE);
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

    }

    @Override
    public void updateReportLock(boolean unlocked) {

    }
}
