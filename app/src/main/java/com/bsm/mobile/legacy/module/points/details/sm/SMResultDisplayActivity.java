package com.bsm.mobile.legacy.module.points.details.sm;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsm.mobile.R;
import com.bsm.mobile.legacy.model.SideMissionInfo;
import com.bsm.mobile.legacy.model.User;
import com.bsm.mobile.legacy.model.sidemission.PropertyToDisplay;
import com.bsm.mobile.legacy.model.sidemission.ReportBasicFirebase;
import com.bsm.mobile.legacy.model.sidemission.ReportSingleMedia;
import com.bsm.mobile.legacy.model.sidemission.SingleReportProperty;
import com.bsm.mobile.legacy.module.points.ReportMediaAdapter;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class SMResultDisplayActivity extends AppCompatActivity {

    @BindView(R.id.media_recycler)
    RecyclerView mediaRecycler;
    @BindView(R.id.sm_name_view)
    TextView smNameView;
    @BindView(R.id.google_drive_view)
    ImageView googleDriveView;
    @BindView(R.id.performing_user_image_view)
    CircleImageView performingUserImage;
    @BindView(R.id.performing_user_name_view)
    TextView performingUserName;
    @BindView(R.id.recording_user_image_view)
    CircleImageView recordingUserImage;
    @BindView(R.id.recording_user_name_view)
    TextView recordingUserName;
    @BindView(R.id.points_view)
    TextView pointsView;
    @BindView(R.id.properities_recycler)
    RecyclerView properitiesRecycler;
    @BindView(R.id.time_text_view)
    TextView timeView;
    @BindView(R.id.date_text_view)
    TextView dateView;

    private String bDate;
    private String bTime;
    private Long bPoints;
    private String bRpid;

    private static DatabaseReference mRootRef;
    private DatabaseReference mDatabaseInReportsRef;
    private ValueEventListener mInReportValueEventListener;
    private DatabaseReference mDatabaseInReportsMediaRef;
    private ValueEventListener mInReportMediaValueEventListener;
    private DatabaseReference mDatabasePerformingUserRef;
    private ValueEventListener mPerformingUserValueEventListener;
    private DatabaseReference mDatabaseRecordingUserRef;
    private ValueEventListener mRecordingUserValueEventListener;
    private DatabaseReference mDatabaseSMPProperitiesRef;
    private ValueEventListener mSMPProperitiesValueEventListener;
    private DatabaseReference mDatabaseFRRProperitiesRef;
    private ValueEventListener mFRRProperitiesValueEventListener;

    private static ReportBasicFirebase mReportBasicFirebase;
    private static List<ReportSingleMedia> mMedia;
    private static User mPerformingUser;
    private static User mRecordingUser;

    private static List<String> properityFRRSymbols;
    private static List<Double> properityFRRGrades;
    private static List<String> properitySMPNames;
    private static List<SingleReportProperty> properitySMPSymbolsObj;
    private static List<PropertyToDisplay> properitiesToDisplay;

    private DatabaseReference mDatabaseSMDocsRef;
    private ValueEventListener mSMDocsValueEventListener;
    private static SideMissionInfo mSMInfo;
    private Intent googleDriveIntent;

    @Override
    protected void onStop() {
        super.onStop();
        dettachFirebaseListeners();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sm_result_display);
        ButterKnife.bind(this);

        handleBundleExtras();
        initializeBasicFirebaseReferences();
    }

    @Override
    protected void onStart() {
        super.onStart();
        InitializeInReportListener();
        InitializeInReportMediaListener();
    }

    private void InitializeInReportListener() {
        if( mInReportValueEventListener == null ){
            mInReportValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mReportBasicFirebase = dataSnapshot.getValue(ReportBasicFirebase.class);

                    mDatabasePerformingUserRef = mRootRef.child("users").child(mReportBasicFirebase.getPerforming_user());
                    mDatabaseRecordingUserRef = mRootRef.child("users").child(mReportBasicFirebase.getRecording_user());
                    mDatabaseSMPProperitiesRef = mRootRef.child("SideMissionsProperities")
                            .child(mReportBasicFirebase.getSm_name()).child("properities");
                    mDatabaseSMDocsRef = mRootRef.child("SideMissionsDocs")
                            .child(mReportBasicFirebase.getSm_name());

                    InitializePerformingUserListener();
                    InitializeRecordingUserListener();
                    InitializeSMPProperitiesListener();
                    InilializeSMDocsListener();
                    setDataFromInReports();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            };
            mDatabaseInReportsRef.addValueEventListener(mInReportValueEventListener);
        }
    }

    private void InilializeSMDocsListener(){
        if( mSMDocsValueEventListener == null ){
            mSMDocsValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mSMInfo = dataSnapshot.getValue(SideMissionInfo.class);
                    setGoogleDriveButton();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            };
            mDatabaseSMDocsRef.addValueEventListener(mSMDocsValueEventListener);
        }
    }

    private void setGoogleDriveButton(){
        googleDriveIntent = new Intent(Intent.ACTION_VIEW);
        googleDriveIntent.setData(Uri.parse(mSMInfo.getLink()));

        googleDriveView.setOnClickListener(v -> {
            if( googleDriveIntent != null ){
                startActivity(googleDriveIntent);
            }
        });
    }

    private void setDataFromInReports(){
        smNameView.setText(mReportBasicFirebase.getSm_name());
        googleDriveView.setVisibility(View.VISIBLE);
    }

    private void InitializeInReportMediaListener(){
        if ( mInReportMediaValueEventListener == null ){
            mInReportMediaValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mMedia = new ArrayList<>();
                    for( DataSnapshot snap : dataSnapshot.getChildren()){
                        mMedia.add( snap.getValue(ReportSingleMedia.class));
                    }
                    initializeMediaRecycler();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            };
            mDatabaseInReportsMediaRef.addValueEventListener(mInReportMediaValueEventListener);
        }
    }

    private void initializeMediaRecycler(){
        ReportMediaAdapter adapter = new ReportMediaAdapter(this, mMedia );
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false );
        mediaRecycler.setAdapter(adapter);
        mediaRecycler.setLayoutManager(layoutManager);
    }

    private void InitializePerformingUserListener(){
        if( mPerformingUserValueEventListener == null ){
            mPerformingUserValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mPerformingUser = dataSnapshot.getValue(User.class);
                    setPerformingUserData();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            };
            mDatabasePerformingUserRef.addValueEventListener(mPerformingUserValueEventListener);
        }
    }

    private void setPerformingUserData(){
        performingUserName.setText(mPerformingUser.getDisplayName());
        Glide.with(this)
                .load(mPerformingUser.getPhotoUrl())
                .into(performingUserImage);
    }

    private void InitializeRecordingUserListener(){
        if( mRecordingUserValueEventListener == null ){
            mRecordingUserValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mRecordingUser = dataSnapshot.getValue(User.class);
                    setRecordingUserData();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            };
            mDatabaseRecordingUserRef.addValueEventListener(mRecordingUserValueEventListener);
        }
    }

    private void setRecordingUserData(){
        recordingUserName.setText(mRecordingUser.getDisplayName());
        Glide.with(this)
                .load(mRecordingUser.getPhotoUrl())
                .into(recordingUserImage);
    }

    private void InitializeSMPProperitiesListener(){
        if( mSMPProperitiesValueEventListener == null ){
            mSMPProperitiesValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    properitySMPNames = new ArrayList<>();
                    properitySMPSymbolsObj = new ArrayList<>();
                    for( DataSnapshot snap : dataSnapshot.getChildren()){
                        properitySMPNames.add(snap.getKey());
                        properitySMPSymbolsObj.add(snap.getValue(SingleReportProperty.class));
                    }
                    InitializeFRRProperitiesListener();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            };
            mDatabaseSMPProperitiesRef.addValueEventListener(mSMPProperitiesValueEventListener);
        }
    }

    private void InitializeFRRProperitiesListener(){
        if( mFRRProperitiesValueEventListener == null ){
            mFRRProperitiesValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    properityFRRGrades = new ArrayList<>();
                    properityFRRSymbols = new ArrayList<>();
                    for( DataSnapshot snap : dataSnapshot.getChildren()){
                        properityFRRGrades.add(snap.getValue(Double.class));
                        properityFRRSymbols.add(snap.getKey());
                    }
                    generateProperitiesToDisplay();
                    InitializeProperitiesRecycler();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            };
            mDatabaseFRRProperitiesRef.addValueEventListener(mFRRProperitiesValueEventListener);
        }
    }

    private void generateProperitiesToDisplay(){
        properitiesToDisplay = new ArrayList<>();
        for( int i=0; i<properityFRRSymbols.size(); i++){
            for( int j=0; j<properityFRRSymbols.size(); j++){
                if( properityFRRSymbols.get(i).equals(properitySMPSymbolsObj.get(j).getSymbol()) ){

                    properitiesToDisplay.add(
                      PropertyToDisplay.builder()
                              .name(properitySMPNames.get(j))
                              .grade(properityFRRGrades.get(i))
                              .build()
                    );
                }
            }
        }
    }

    private void InitializeProperitiesRecycler(){
        PropertiesAdapter adapter = new PropertiesAdapter(properitiesToDisplay);
        properitiesRecycler.setAdapter(adapter);
        properitiesRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setDataFromBundle(){
        pointsView.setText(String.valueOf(bPoints));
        timeView.setText(bTime);
        dateView.setText(bDate);
    }

    private void handleBundleExtras() {

        Bundle bundle = this.getIntent().getExtras();
        if( bundle != null ){
            String bTeam = bundle.getString("team");
            bDate = bundle.getString("date");
            bTime = bundle.getString("time");
            bPoints = bundle.getLong("points");
            bRpid = bundle.getString("rpid");

            switch (bTeam){
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
            setDataFromBundle();
        }
    }

    private void initializeBasicFirebaseReferences() {
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mDatabaseInReportsRef = mRootRef.child("Reports").child(bRpid);
        mDatabaseInReportsMediaRef = mDatabaseInReportsRef.child("mediaUrls");
        mDatabaseFRRProperitiesRef = mRootRef.child("FinalReportRate")
                .child(bRpid).child("properities");
    }

    private void dettachFirebaseListeners(){

        if( mInReportValueEventListener != null ){
            mDatabaseInReportsRef.removeEventListener(mInReportValueEventListener);
            mInReportValueEventListener = null;
        }
        if( mRecordingUserValueEventListener != null ){
            mDatabaseRecordingUserRef.removeEventListener(mRecordingUserValueEventListener);
            mRecordingUserValueEventListener = null;
        }
        if( mInReportMediaValueEventListener != null ){
            mDatabaseInReportsMediaRef.removeEventListener(mInReportMediaValueEventListener);
            mInReportMediaValueEventListener = null;
        }
        if( mFRRProperitiesValueEventListener != null ){
            mDatabaseFRRProperitiesRef.removeEventListener(mFRRProperitiesValueEventListener);
            mFRRProperitiesValueEventListener = null;
        }
        if( mSMPProperitiesValueEventListener != null ){
            mDatabaseSMPProperitiesRef.removeEventListener(mSMPProperitiesValueEventListener);
            mSMPProperitiesValueEventListener = null;
        }
        if(mPerformingUserValueEventListener != null ){
            mDatabasePerformingUserRef.removeEventListener(mPerformingUserValueEventListener);
            mPerformingUserValueEventListener = null;
        }
        if( mSMDocsValueEventListener != null ){
            mDatabaseSMDocsRef.removeEventListener(mSMDocsValueEventListener);
            mSMDocsValueEventListener = null;
        }
    }
}
