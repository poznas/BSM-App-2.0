package com.bsm.mobile.legacy.domain.points.details.mc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.bsm.mobile.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mlody Danon on 7/26/2017.
 */

public class MCResultDisplayActivity extends AppCompatActivity {

    @BindView(R.id.points_view)
    TextView pointsView;
    @BindView(R.id.name_view)
    TextView nameView;
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
        setContentView(R.layout.activity_mc_result_display);
        ButterKnife.bind(this);

        bundle = this.getIntent().getExtras();
        setMCResultInfo();
    }

    private void setMCResultInfo() {
        if(bundle != null){

            pointsView.setText(bundle.getString("points"));
            nameView.setText(bundle.getString("name"));
            infoView.setText(bundle.getString("info"));
            timeView.setText(bundle.getString("time"));
            dateView.setText(bundle.getString("date"));

            switch (bundle.getString("team")){
                case "cormeum":
                    pointsView.setTextColor(ContextCompat.getColor(this, R.color.red));
                    setTitle("Cormeum");
                    break;
                case "sensum":
                    pointsView.setTextColor(ContextCompat.getColor(this, R.color.blue));
                    setTitle("Sensum");
                    break;
                case "mutinium":
                    pointsView.setTextColor(ContextCompat.getColor(this, R.color.green));
                    setTitle("Mutinium");
                    break;
                default:
                    break;
            }
        }
    }
}
