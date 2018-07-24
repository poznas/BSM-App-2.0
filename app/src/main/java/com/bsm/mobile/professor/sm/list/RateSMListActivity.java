package com.bsm.mobile.professor.sm.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

import com.bsm.mobile.R;
import com.bsm.mobile.common.PendingReportAdapter;
import com.bsm.mobile.legacy.model.PendingReport;
import com.bsm.mobile.legacy.module.professor.rate.sm.ProfRateSMPostActivity;
import com.bsm.mobile.legacy.module.professor.rate.sm.ProfRateSideMissionActivity;
import com.bsm.mobile.root.App;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.bsm.mobile.professor.sm.list.RateSMListActivityMVP.Presenter;
import static com.bsm.mobile.professor.sm.list.RateSMListActivityMVP.View;

public class RateSMListActivity extends AppCompatActivity implements View {

    @Inject
    Presenter presenter;

    @BindView(R.id.simple_recycler)
    RecyclerView pendingReportsRecycler;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    PendingReportAdapter pendingReportAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judge_sm_list);
        ButterKnife.bind(this);
        ((App) getApplication()).getComponent().inject(this);
        initializePendingReportsRecycler();
    }

    private void initializePendingReportsRecycler() {
        pendingReportAdapter = new PendingReportAdapter(
                report -> report.isPost() ?
                        new Intent(this, ProfRateSMPostActivity.class) :
                        new Intent(this, ProfRateSideMissionActivity.class)
        );
        pendingReportsRecycler.setAdapter(pendingReportAdapter);
        pendingReportsRecycler.setItemAnimator(new DefaultItemAnimator());
        pendingReportsRecycler.setLayoutManager(new LinearLayoutManager(this));
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
    public void showProgress() {
        displayLoadingState(true);
    }

    @Override
    public void hideProgress() {
        displayLoadingState(false);
    }

    private void displayLoadingState(boolean isLoading) {
        progressBar.setVisibility(isLoading ? VISIBLE : GONE);
    }

    @Override
    public void updateRequireProfessorRateReports(List<PendingReport> reports) {
        pendingReportAdapter.updatePendingReports(reports);
    }
}
