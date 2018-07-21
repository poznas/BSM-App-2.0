package com.bsm.mobile.legacy.module.professor.medal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bsm.mobile.R;
import com.bsm.mobile.legacy.model.shipment.MedalShipment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mlody Danon on 7/27/2017.
 */

public class MedalEditDetailsActivity extends AppCompatActivity {

    @BindView(R.id.points_edit_text)
    EditText pointsEditText;
    @BindView(R.id.team_spinner)
    Spinner teamSpinner;
    @BindView(R.id.info_edit_text)
    EditText infoEditText;
    @BindView(R.id.button_view)
    View buttonView;
    @BindView(R.id.button_image)
    ImageView buttonImageView;

    private String medal;

    private DatabaseReference mDatabaseSpecialPointsRef;
    private String mPushId;

    private String mTeam;
    private Long mPoints;
    private String mInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medal_edit_details);
        ButterKnife.bind(this);

        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        mDatabaseSpecialPointsRef = mRootRef.child("SpecialPoints");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            medal = bundle.getString("medal");
            handleTeamSpinner();
            setOnClickListenerSendButton();
            setTitle(medal);
        }
    }

    private void setOnClickListenerSendButton() {
        buttonView.setOnClickListener(v -> {
            if( checkCorrectness() ){
                mPushId = mDatabaseSpecialPointsRef.push().getKey();
                if(medal.equals("infamia")){ mPoints *= (-1);}
                MedalShipment medalShipment = new MedalShipment(mPoints,mTeam,mInfo, true);
                mDatabaseSpecialPointsRef.child(mPushId).setValue(medalShipment);
                mDatabaseSpecialPointsRef.child(mPushId).child("timestamp").setValue(ServerValue.TIMESTAMP);
                finish();
            }
        });
    }

    private boolean checkCorrectness() {

        mTeam = teamSpinner.getSelectedItem().toString();
        mInfo = infoEditText.getText().toString();
        String pointsString = pointsEditText.getText().toString();

        if( mInfo.matches("")){
            Toast.makeText(this, "Opis dziubnij", Toast.LENGTH_SHORT).show();
            return false;
        }
        if( mTeam.equals("<none>")){
            Toast.makeText(this, "Ale dom to byś wybrał", Toast.LENGTH_SHORT).show();
            return false;
        }
        if( pointsString.matches("")){
            Toast.makeText(this, "Kurwa punkciki typie", Toast.LENGTH_SHORT).show();
            return false;
        }
        mPoints = Long.parseLong(pointsString);
        if( mPoints <= 0 ){
            Toast.makeText(this, "Punkty muszą być dodatnie", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void handleTeamSpinner() {
        List<String> teams = new ArrayList<>();
        teams.add("<none>"); teams.add("cormeum"); teams.add("sensum"); teams.add("mutinium");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, teams);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamSpinner.setAdapter(dataAdapter);
    }
}
