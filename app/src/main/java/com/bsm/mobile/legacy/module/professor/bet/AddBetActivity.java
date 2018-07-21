package com.bsm.mobile.legacy.module.professor.bet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bsm.mobile.R;
import com.bsm.mobile.legacy.model.shipment.BetResultsShipment;
import com.bsm.mobile.legacy.model.shipment.BetShipment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mlody Danon on 7/26/2017.
 */

public class AddBetActivity extends AppCompatActivity {

    @BindView(R.id.winner_team_spinner)
    Spinner winnerTeamSpinner;
    @BindView(R.id.loser_team_spinner)
    Spinner loserTeamSpinner;
    @BindView(R.id.points_edit_text)
    EditText pointsEditText;
    @BindView(R.id.info_edit_text)
    EditText infoEditText;
    @BindView(R.id.button_view)
    View buttonView;

    private DatabaseReference mDatabaseBetPointsRef;
    private String mPushId;

    private String mWinnerTeam;
    private String mLoserTeam;
    private Long mPoints;
    private String mInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bet);
        ButterKnife.bind(this);

        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        mDatabaseBetPointsRef = mRootRef.child("BetPoints");

        handleTeamSpinners();
        setOnClickListenerSendButton();
    }

    private void setOnClickListenerSendButton() {
        buttonView.setOnClickListener(v -> {
            if( checkCorrectness() ){
                mPushId = mDatabaseBetPointsRef.push().getKey();

                BetShipment betShipment = BetShipment.builder()
                        .info(mInfo)
                        .points(mPoints)
                        .valid(true)
                        .build();

                BetResultsShipment loserResult = new BetResultsShipment();
                BetResultsShipment winnerResult = new BetResultsShipment();
                loserResult.setLoss(mLoserTeam);
                winnerResult.setWin(mWinnerTeam);
                Map<String, BetResultsShipment> results = new HashMap<>();
                results.put("loss"+mPushId,loserResult);
                results.put("win"+mPushId,winnerResult);

                mDatabaseBetPointsRef.child(mPushId).setValue(betShipment);
                mDatabaseBetPointsRef.child(mPushId).child("results").setValue(results);
                mDatabaseBetPointsRef.child(mPushId).child("timestamp").setValue(ServerValue.TIMESTAMP);

                finish();
            }
        });
    }

    private boolean checkCorrectness() {

        mInfo = infoEditText.getText().toString();
        String pointsString = pointsEditText.getText().toString();
        mWinnerTeam = winnerTeamSpinner.getSelectedItem().toString();
        mLoserTeam = loserTeamSpinner.getSelectedItem().toString();

        if( mInfo.matches("")){
            Toast.makeText(this, "Opis dziubnij", Toast.LENGTH_SHORT).show();
            return false;
        }
        if( mWinnerTeam.equals("<none>")){
            Toast.makeText(this, "Wybierz Zwycięski Dom", Toast.LENGTH_SHORT).show();
            return false;
        }
        if( mLoserTeam.equals("<none>")){
            Toast.makeText(this, "Wybierz Przegrany Dom", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ( mWinnerTeam.equals(mLoserTeam)){
            Toast.makeText(this, "Dom nie może wygrać sam ze sobą", Toast.LENGTH_SHORT).show();
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

    private void handleTeamSpinners() {

        List<String> teams = new ArrayList<>();
        teams.add("<none>"); teams.add("cormeum"); teams.add("sensum"); teams.add("mutinium");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, teams);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        winnerTeamSpinner.setAdapter(dataAdapter);
        loserTeamSpinner.setAdapter(dataAdapter);
    }
}
