package com.bsm.mobile.points.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bsm.mobile.R;
import com.bsm.mobile.TeamResources;
import com.bsm.mobile.common.Tagable;
import com.bsm.mobile.legacy.model.PointsInfo;
import com.bsm.mobile.root.App;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.bsm.mobile.points.list.PointsListActivityMVP.*;

public class PointsListActivity extends AppCompatActivity implements View, Tagable {

    @Inject
    Presenter presenter;

    private PointsAdapter pointsAdapter;

    @BindView(R.id.points_recycler)
    RecyclerView pointsRecycler;
    @BindView(R.id.team_points_view)
    TextView teamPointsView;
    @BindView(R.id.team_image_view)
    ImageView teamImageView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_list);
        ButterKnife.bind(this);

        ((App) getApplication()).getComponent().inject(this);
        presenter.attachView(this);
        presenter.handleBundle(getIntent().getExtras());

        initializePointsRecycler();
    }

    private void initializePointsRecycler() {

        pointsAdapter = new PointsAdapter(presenter);
        pointsRecycler.setAdapter(pointsAdapter);
        pointsRecycler.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        pointsRecycler.setLayoutManager(manager);
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
    public void updatePointsRecords(List<PointsInfo> records) {
        pointsAdapter.updatePointsRecords(records);
    }

    @Override
    public void setTeamDisplay(String teamId) {
        teamPointsView.setTextColor(TeamResources.COLORS(this).get(teamId));
        teamImageView.setImageResource(TeamResources.IMAGES.get(teamId));
        setTitle(TeamResources.DISPLAY_NAMES.get(teamId));
    }

    @Override
    public void updateScore(long score) {
        teamPointsView.setText(String.valueOf(score));
    }

    @Override
    public void setPointsInvalidationPermission(boolean permission) {
        pointsAdapter.setInvalidationPermission(permission);
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
        Log.d(getTag(), "display loading state : " + isLoading);
        progressBar.setVisibility(isLoading ? VISIBLE : GONE);
    }
}
