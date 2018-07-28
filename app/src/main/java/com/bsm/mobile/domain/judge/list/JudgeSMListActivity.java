package com.bsm.mobile.domain.judge.list;

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
import com.bsm.mobile.legacy.domain.judge.rate.JudgeRateSMActivity;
import com.bsm.mobile.legacy.domain.judge.rate.JudgeRateSMPostActivity;
import com.bsm.mobile.legacy.model.PendingReport;
import com.bsm.mobile.root.App;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.bsm.mobile.domain.judge.list.JudgeSMListActivityMVP.Presenter;
import static com.bsm.mobile.domain.judge.list.JudgeSMListActivityMVP.View;

public class JudgeSMListActivity extends AppCompatActivity implements View {

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
                        new Intent(this, JudgeRateSMPostActivity.class) :
                        new Intent(this, JudgeRateSMActivity.class)
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
    public void updatePendingReports(List<PendingReport> reports) {
        pendingReportAdapter.updatePendingReports(reports);
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
}
