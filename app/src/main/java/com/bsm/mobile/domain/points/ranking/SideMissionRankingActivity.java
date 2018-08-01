package com.bsm.mobile.domain.points.ranking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

import com.bsm.mobile.R;
import com.bsm.mobile.legacy.model.PointsInfo;
import com.bsm.mobile.root.App;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.bsm.mobile.common.resource.Constants.BRAND_RANKING;
import static com.bsm.mobile.domain.points.ranking.SideMissionRankingActivityMVP.Presenter;
import static com.bsm.mobile.domain.points.ranking.SideMissionRankingActivityMVP.View;

public class SideMissionRankingActivity extends AppCompatActivity implements View {

    @Inject
    Presenter presenter;

    @BindView(R.id.simple_recycler)
    RecyclerView rankingRecycler;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    RankingAdapter rankingAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judge_sm_list);
        ButterKnife.bind(this);
        ((App) getApplication()).getComponent().inject(this);
        presenter.attachView(this);
        setTitle(BRAND_RANKING);

        initializeRankingRecycler();
    }

    private void initializeRankingRecycler() {
        rankingAdapter = new RankingAdapter();
        rankingRecycler.setAdapter(rankingAdapter);
        rankingRecycler.setItemAnimator(new DefaultItemAnimator());
        rankingRecycler.setLayoutManager(new LinearLayoutManager(this));
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
    public void updateRankingList(List<PointsInfo> list) {
        rankingAdapter.updateRanking(list);
    }
}
