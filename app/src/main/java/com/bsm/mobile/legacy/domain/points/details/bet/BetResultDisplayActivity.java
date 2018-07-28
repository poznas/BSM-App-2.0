package com.bsm.mobile.legacy.domain.points.details.bet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.bsm.mobile.R;
import com.bsm.mobile.common.resource.TeamResources;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bsm.mobile.common.resource.Constants.KEY_LOSER;
import static com.bsm.mobile.common.resource.Constants.KEY_POINTS;
import static com.bsm.mobile.common.resource.Constants.KEY_WINNER;

/**
 * Created by Mlody Danon on 7/26/2017.
 */

public class BetResultDisplayActivity extends AppCompatActivity {

    @BindView(R.id.winner_view)
    TextView winnerView;
    @BindView(R.id.losser_view)
    TextView loserView;
    @BindView(R.id.points_view)
    TextView pointsView;
    @BindView(R.id.info_view)
    TextView infoView;
    @BindView(R.id.time_text_view)
    TextView timeView;
    @BindView(R.id.date_text_view)
    TextView dateView;

    private Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bet_result_display);
        ButterKnife.bind(this);

        bundle = this.getIntent().getExtras();
        setBetResultInfo();
    }

    private void setBetResultInfo() {
        if(bundle != null){

            setTitle(bundle.getLong(KEY_POINTS) > 0 ?
                    TeamResources.DISPLAY_NAMES.get(bundle.getString(KEY_WINNER)) :
                    TeamResources.DISPLAY_NAMES.get(bundle.getString(KEY_LOSER))
            );

            pointsView.setText(String.valueOf(bundle.getLong("points")));
            infoView.setText(bundle.getString("info"));
            timeView.setText(bundle.getString("time"));
            dateView.setText(bundle.getString("date"));
            winnerView.setText(bundle.getString("winner"));
            loserView.setText(bundle.getString("loser"));

            switch (bundle.getString("winner")){
                case "cormeum":
                    winnerView.setTextColor(ContextCompat.getColor(this, R.color.red));
                    break;
                case "sensum":
                    winnerView.setTextColor(ContextCompat.getColor(this, R.color.blue));
                    break;
                case "mutinium":
                    winnerView.setTextColor(ContextCompat.getColor(this, R.color.green));
                    break;
                default:
                    break;
            }
            switch (bundle.getString("loser")){
                case "cormeum":
                    loserView.setTextColor(ContextCompat.getColor(this, R.color.red));
                    break;
                case "sensum":
                    loserView.setTextColor(ContextCompat.getColor(this, R.color.blue));
                    break;
                case "mutinium":
                    loserView.setTextColor(ContextCompat.getColor(this, R.color.green));
                    break;
                default:
                    break;
            }
            switch (bundle.getString("team")){
                case "cormeum":
                    pointsView.setTextColor(ContextCompat.getColor(this, R.color.red));
                    break;
                case "sensum":
                    pointsView.setTextColor(ContextCompat.getColor(this, R.color.blue));
                    break;
                case "mutinium":
                    pointsView.setTextColor(ContextCompat.getColor(this, R.color.green));
                    break;
                default:
                    break;
            }
        }
    }
}
