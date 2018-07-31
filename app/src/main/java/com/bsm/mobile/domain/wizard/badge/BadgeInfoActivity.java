package com.bsm.mobile.domain.wizard.badge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

import com.bsm.mobile.R;
import com.bsm.mobile.domain.wizard.badge.model.BadgeInfoView;
import com.bsm.mobile.root.App;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.bsm.mobile.common.resource.Constants.BRAND_BADGE;
import static com.bsm.mobile.domain.wizard.badge.BadgeInfoActivityMVP.Presenter;
import static com.bsm.mobile.domain.wizard.badge.BadgeInfoActivityMVP.View;

public class BadgeInfoActivity extends AppCompatActivity implements View {

    @Inject
    Presenter presenter;

    @BindView(R.id.simple_recycler)
    RecyclerView badgeInfoRecycler;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    BadgeInfoAdapter badgeInfoAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judge_sm_list);
        ButterKnife.bind(this);
        ((App) getApplication()).getComponent().inject(this);
        presenter.attachView(this);
        setTitle(BRAND_BADGE);

        initializeBadgeInfoRecycler();
    }

    private void initializeBadgeInfoRecycler() {
        badgeInfoAdapter = new BadgeInfoAdapter();
        badgeInfoRecycler.setAdapter(badgeInfoAdapter);
        badgeInfoRecycler.setItemAnimator(new DefaultItemAnimator());
        badgeInfoRecycler.setLayoutManager(new LinearLayoutManager(this));
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
    public void updateBadgeInfoList(List<BadgeInfoView> list) {
        badgeInfoAdapter.updateBadgeInfoList(list);
    }
}
