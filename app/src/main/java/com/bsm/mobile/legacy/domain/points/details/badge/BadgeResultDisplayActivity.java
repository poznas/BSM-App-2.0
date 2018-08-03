package com.bsm.mobile.legacy.domain.points.details.badge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.bsm.mobile.R;
import com.bsm.mobile.backend.score.points.badge.IBadgeInfoRepository;
import com.bsm.mobile.common.NullFighter;
import com.bsm.mobile.common.resource.TeamResources;
import com.bsm.mobile.legacy.model.PointsInfo;
import com.bsm.mobile.root.App;
import com.bumptech.glide.Glide;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.bsm.mobile.common.resource.Constants.KEY_INFO;
import static com.bsm.mobile.common.utils.DateTimeUtils.getDate;
import static com.bsm.mobile.common.utils.DateTimeUtils.getTime;

public class BadgeResultDisplayActivity extends AppCompatActivity implements NullFighter{

    @BindView(R.id.sm_image_view)
    CircleImageView sideMissionImageView;
    @BindView(R.id.sm_name_view)
    TextView sideMissionNameView;
    @BindView(R.id.performing_user_image_view)
    CircleImageView userImageView;
    @BindView(R.id.performing_user_name_view)
    TextView userNameView;
    @BindView(R.id.points_view)
    TextView pointsView;
    @BindView(R.id.time_text_view)
    TextView timeView;
    @BindView(R.id.date_text_view)
    TextView dateView;

    @Inject
    IBadgeInfoRepository badgeInfoRepository;

    CompositeDisposable compositeDisposable;

    private PointsInfo pointsInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badge_result_display);
        ((App) getApplication()).getComponent().inject(this);
        ButterKnife.bind(this);

        pointsInfo = (PointsInfo) getIntent().getSerializableExtra(KEY_INFO);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if( isNull(pointsInfo) ) return;
        compositeDisposable.add(subscribeForSideMissionImage());
        setViews();
    }

    @Override
    protected void onStop() {
        super.onStop();
        compositeDisposable.dispose();
    }

    private void setViews() {
        sideMissionNameView.setText(pointsInfo.getName());
        Glide.with(this).load(pointsInfo.getUser_photo()).into(userImageView);
        userNameView.setText(pointsInfo.getUser_name());
        pointsView.setText(String.valueOf(pointsInfo.getPoints()));
        pointsView.setTextColor(TeamResources.getColor(this, pointsInfo.getTeam()));
        timeView.setText(getTime(pointsInfo.getTimestamp()));
        dateView.setText(getDate(pointsInfo.getTimestamp()));

        setTitle(TeamResources.DISPLAY_NAMES.get(pointsInfo.getTeam()));
    }

    private Disposable subscribeForSideMissionImage() {
        return badgeInfoRepository.getBadgeInfo(pointsInfo.getName()).take(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(badgeInfo ->
                   Glide.with(this).load(badgeInfo.getSideMissionImageUrl())
                           .into(sideMissionImageView)
                );
    }
}
