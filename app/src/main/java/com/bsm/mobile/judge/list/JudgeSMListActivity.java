package com.bsm.mobile.judge.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bsm.mobile.R;
import com.bsm.mobile.common.Tagable;
import com.bsm.mobile.legacy.model.PendingReport;
import com.bsm.mobile.legacy.module.judge.list.PendingReportAdapter;
import com.bsm.mobile.root.App;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.bsm.mobile.judge.list.JudgeSMListActivityMVP.*;

public class JudgeSMListActivity extends AppCompatActivity implements View, Tagable {

    @Inject
    Presenter presenter;

    @BindView(R.id.root_view)
    ViewGroup rootView;
    @BindView(R.id.simple_recycler)
    RecyclerView pendingReportsRecycler;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    private boolean isLoading;

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
        pendingReportAdapter = new PendingReportAdapter();
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
    }
}
