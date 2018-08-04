package com.bsm.mobile.legacy.domain.professor.rate.sm;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bsm.mobile.R;
import com.bsm.mobile.legacy.domain.points.ReportMediaAdapter;
import com.bsm.mobile.legacy.domain.wizard.list.WizardsActivity;
import com.bsm.mobile.legacy.model.SideMissionInfo;
import com.bsm.mobile.legacy.model.User;
import com.bsm.mobile.legacy.model.sidemission.PropertyDetails;
import com.bsm.mobile.legacy.model.sidemission.ReportBasicFirebase;
import com.bsm.mobile.legacy.model.sidemission.ReportSingleMedia;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfRateSideMissionActivity extends AppCompatActivity {

    @BindView(R.id.media_recycler)
    RecyclerView mediaRecycler;
    @BindView(R.id.sm_name_view)
    TextView smNameView;
    @BindView(R.id.google_drive_view)
    ImageView googleDriveView;
    @BindView(R.id.team_name_view)
    TextView teamView;
    @BindView(R.id.info_view)
    ImageView infoView;
    @BindView(R.id.performing_user_image_view)
    CircleImageView performingUserImage;
    @BindView(R.id.performing_user_name_view)
    TextView performingUserName;
    @BindView(R.id.rate_properities_list_view)
    ListView properitiesListView;
    @BindView(R.id.send_button_view)
    View sendButtonView;

    private String bSMName;
    private String bUserImageURL;
    private String bUserName;
    private String bRpid;

    private DatabaseReference mDatabaseSMDocsRef;
    private ValueEventListener mSMDocsValueEventListener;
    private static SideMissionInfo mSMInfo;
    private Intent googleDriveIntent;

    private Intent wizzardsIntent;

    private static DatabaseReference mRootRef;
    private DatabaseReference mDatabaseInReportsRef;
    private ValueEventListener mInReportValueEventListener;
    private DatabaseReference mDatabaseInReportsMediaRef;
    private ValueEventListener mInReportMediaValueEventListener;
    private DatabaseReference mDatabasePerformingUserRef;
    private ValueEventListener mPerformingUserValueEventListener;
    private DatabaseReference mDatabaseSMPProperitiesRef;
    private ValueEventListener mSMPProperitiesValueEventListener;
    private DatabaseReference mDatabaseSMPProperitiesHintsRef;
    private ValueEventListener mSMPProperitiesHintsValueEventListener;

    private static ReportBasicFirebase mReportBasicFirebase;
    private static List<ReportSingleMedia> mMedia;
    private static User mPerformingUser;

    private List<String> properitiesNames;
    private List<String> properitiesSymbols;
    private List<String> properitiesTypes;
    private List<String> properitiesHintsNames;
    private List<String> properitiesHints;

    private boolean properitiesHintsLoaded = false;
    private boolean properitiesDetailsLoaded = false;

    private List<PropertyDetails> properitiesDetails;
    private List<ValueEventListener> typeListeners;
    private List<DatabaseReference> typeReferences;
    private boolean[] properitiesReady;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judge_side_mission);
        ButterKnife.bind(this);

        initializeFirebaseComponents();
        InitializeInReportMediaListener();
        InitializeInReportListener();
        InitializeSMPProperitiesListener();
        InitializeSMPProperitiesHintsListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dettachFirebaseListeners();
    }

    private void createProperitiesDetails() {
        properitiesDetails = new ArrayList<>();
        for( int i=0; i<properitiesNames.size(); i++ ){

            if( properitiesTypes.get(i).equals("professor_value")
                    && !properitiesNames.get(i).equals("płeć_wykonawcy")){

                for( int j=0; j<properitiesHintsNames.size(); j++ ){
                    if( properitiesNames.get(i).equals(properitiesHintsNames.get(j))){

                        properitiesDetails.add(
                                PropertyDetails.builder()
                                        .name(properitiesHintsNames.get(j))
                                        .hint(properitiesHints.get(j))
                                        .symbol(properitiesSymbols.get(i))
                                        .type(properitiesTypes.get(i))
                                        .build()
                        );
                    }
                }
            }
        }
        completeProperitiesDetails();
    }

    private void completeProperitiesDetails() {

        properitiesReady = new boolean[properitiesDetails.size()];
        typeListeners = new ArrayList<>();
        typeReferences = new ArrayList<>();

        for( int i=0; i<properitiesDetails.size(); i++ ){
            typeListeners.add(null);
            typeReferences.add( mDatabaseSMPProperitiesRef
                    .child(properitiesDetails.get(i).getName())
                    .child("type")
                    .child(properitiesDetails.get(i).getType()));
        }

        for( int i=0; i<properitiesDetails.size(); i++ ){
            if( typeListeners.get(i) == null ){
                final int finalI = i;
                typeListeners.set(i, new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        properitiesDetails.get(finalI).setProfType(String.valueOf(dataSnapshot.getValue()));

                        properitiesReady[finalI] = true;
                        if( readyToLaunch() ){
                            InitializeProperitiesListView();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                });
                typeReferences.get(i).addValueEventListener(typeListeners.get(i));
            }
        }
    }

    private void InitializeProperitiesListView(){
        ProfRateProperitiesAdapter adapter = new ProfRateProperitiesAdapter(this, properitiesDetails);
        properitiesListView.setAdapter(adapter);
        InitializeSendButton();
    }

    private void InitializeSendButton() {
        sendButtonView.setOnClickListener(v -> {
            if( correctJudgeInput() ){
                sendReportRate();
            }
        });
    }

    private void sendReportRate() {
        Map<String, Object> profRate = new HashMap<>();
        for( int i=0; i<properitiesListView.getChildCount(); i++ ){

            PropertyDetails current = (PropertyDetails) properitiesListView.getAdapter().getItem(i);

            Spinner spinner;
            EditText editText;
            String selectedKey;

            switch (current.getProfType()){
                case "true":
                    editText = properitiesListView
                            .getChildAt(i).findViewById(R.id.item_properity_edit_text);
                    profRate.put(current.getSymbol(),
                            Double.valueOf(editText.getText().toString()));
                    break;
                case "boolean":
                    spinner = properitiesListView
                            .getChildAt(i).findViewById(R.id.item_properity_spinner);
                    selectedKey = spinner.getSelectedItem().toString();
                    if( selectedKey.equals("TAK") ){
                        profRate.put(current.getSymbol(), (double) 1);
                    }else {
                        profRate.put(current.getSymbol(), (double) 0);
                    }
                    break;
                default:
                    break;
            }
        }
        mRootRef.child("FinalReportRate").child(bRpid).child("properities").updateChildren(profRate);
        mRootRef.child("FinalReportRate").child(bRpid).child("requireProfessor").setValue(false);
        mRootRef.child("requireProfRate").child(bRpid).setValue(null);

        finish();
    }

    private boolean correctJudgeInput() {

        for( int i=0; i<properitiesListView.getChildCount(); i++ ){

            PropertyDetails current = (PropertyDetails) properitiesListView.getAdapter().getItem(i);
            switch (current.getProfType()){
                case "true":
                    EditText editText = properitiesListView
                            .getChildAt(i).findViewById(R.id.item_properity_edit_text);
                    if(editText.getText().toString().matches("")){
                        Toast.makeText(this, "Uzupełnij "+current.getName(), Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    break;
                case "boolean":
                    Spinner spinner = properitiesListView
                            .getChildAt(i).findViewById(R.id.item_properity_spinner);
                    if( spinner.getSelectedItem().toString().equals("<none>") ){
                        Toast.makeText(this, "Uzupełnij "+current.getName(), Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    private boolean readyToLaunch(){
        for( int j=0; j<properitiesReady.length; j++ ){
            if( !properitiesReady[j] ){
                return false;
            }
        }
        return true;
    }

    private void InitializeSMPProperitiesHintsListener() {
        if( mSMPProperitiesHintsValueEventListener == null ){
            mSMPProperitiesHintsValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    properitiesHints = new ArrayList<>();
                    properitiesHintsNames = new ArrayList<>();
                    for( DataSnapshot child : dataSnapshot.getChildren() ){
                        properitiesHintsNames.add(child.getKey());
                        properitiesHints.add(String.valueOf(child.getValue()));
                    }
                    properitiesHintsLoaded = true;
                    if( properitiesDetailsLoaded ){
                        createProperitiesDetails();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            };
            mDatabaseSMPProperitiesHintsRef.addValueEventListener(mSMPProperitiesHintsValueEventListener);
        }
    }

    private void InitializeSMPProperitiesListener() {
        if( mSMPProperitiesValueEventListener == null ){
            mSMPProperitiesValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    properitiesNames = new ArrayList<>();
                    properitiesTypes = new ArrayList<>();
                    properitiesSymbols = new ArrayList<>();
                    for( DataSnapshot data : dataSnapshot.getChildren() ){
                        properitiesNames.add(data.getKey());
                        properitiesSymbols.add(String.valueOf(data.child("symbol").getValue()));
                        for( DataSnapshot type : data.child("type").getChildren() ){
                            properitiesTypes.add(type.getKey());
                        }
                    }
                    properitiesDetailsLoaded = true;
                    if( properitiesHintsLoaded ){
                        createProperitiesDetails();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            };
            mDatabaseSMPProperitiesRef.addValueEventListener(mSMPProperitiesValueEventListener);
        }
    }

    private void InitializeInReportListener() {
        if( mInReportValueEventListener == null ){
            mInReportValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mReportBasicFirebase = dataSnapshot.getValue(ReportBasicFirebase.class);
                    mDatabasePerformingUserRef = mRootRef.child("users").child(mReportBasicFirebase.getPerforming_user());

                    InitializePerformingUserListener();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            };
            mDatabaseInReportsRef.addValueEventListener(mInReportValueEventListener);
        }
    }

    private void InitializePerformingUserListener(){
        if( mPerformingUserValueEventListener == null ){
            mPerformingUserValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mPerformingUser = dataSnapshot.getValue(User.class);
                    if( mPerformingUser != null ){
                        InitializeWizzardTeam();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            };
            mDatabasePerformingUserRef.addValueEventListener(mPerformingUserValueEventListener);
        }
    }

    private void InitializeWizzardTeam() {
        teamView.setText(mPerformingUser.getTeam());
        switch (mPerformingUser.getTeam()){
            case "cormeum":
                teamView.setTextColor(ContextCompat.getColor(this, R.color.red));
                break;
            case "sensum":
                teamView.setTextColor(ContextCompat.getColor(this, R.color.blue));
                break;
            case "mutinium":
                teamView.setTextColor(ContextCompat.getColor(this, R.color.green));
                break;
            default:
                break;
        }
        wizzardsIntent = new Intent(this, WizardsActivity.class );
        infoView.setVisibility(View.VISIBLE);
        infoView.setOnClickListener(v -> {
            if( wizzardsIntent != null ){
                startActivity(wizzardsIntent);
            }
        });
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
                    InilializeSMDocsListener();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            };
            mDatabaseInReportsMediaRef.addValueEventListener(mInReportMediaValueEventListener);
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
        smNameView.setText(bSMName);
        googleDriveView.setVisibility(View.VISIBLE);

        googleDriveView.setOnClickListener(v -> {
            if( googleDriveIntent != null ){
                startActivity(googleDriveIntent);
            }
        });
    }

    private void initializeMediaRecycler(){
        ReportMediaAdapter adapter = new ReportMediaAdapter(this, mMedia );
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false );
        mediaRecycler.setAdapter(adapter);
        mediaRecycler.setLayoutManager(layoutManager);
    }

    private void initializeFirebaseComponents() {
        Bundle bundle = this.getIntent().getExtras();
        if( bundle != null ){
            bSMName = bundle.getString("sm_name");
            bUserImageURL = bundle.getString("user_image_url");
            bUserName = bundle.getString("user_name");
            bRpid = bundle.getString("rpid");
        }
        setTitle(bSMName);
        performingUserName.setText(bUserName);
        Glide.with(this)
                .load(bUserImageURL)
                .into(performingUserImage);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mDatabaseInReportsRef = mRootRef.child("Reports").child(bRpid);
        mDatabaseInReportsMediaRef = mDatabaseInReportsRef.child("mediaUrls");
        mDatabaseSMDocsRef = mRootRef.child("SideMissionsDocs").child(bSMName);
        mDatabaseSMPProperitiesRef = mRootRef.child("SideMissionsProperities").child(bSMName).child("properities");
        mDatabaseSMPProperitiesHintsRef = mRootRef.child("SideMissionsProperities").child(bSMName).child("properitiesHints");
    }


    private void dettachFirebaseListeners() {
        if( mInReportMediaValueEventListener != null ){
            mDatabaseInReportsMediaRef.removeEventListener(mInReportMediaValueEventListener);
            mInReportMediaValueEventListener = null;
        }
        if( mSMDocsValueEventListener != null ){
            mDatabaseSMDocsRef.removeEventListener(mSMDocsValueEventListener);
            mSMDocsValueEventListener = null;
        }
        if( mInReportValueEventListener != null ){
            mDatabaseInReportsRef.removeEventListener(mInReportValueEventListener);
            mInReportValueEventListener = null;
        }
        if( mPerformingUserValueEventListener != null ){
            mDatabasePerformingUserRef.removeEventListener(mPerformingUserValueEventListener);
            mPerformingUserValueEventListener = null;
        }
        if( mSMPProperitiesValueEventListener != null ){
            mDatabaseSMPProperitiesRef.removeEventListener(mSMPProperitiesValueEventListener);
            mSMPProperitiesValueEventListener = null;
        }
        if( mSMPProperitiesHintsValueEventListener != null ){
            mDatabaseSMPProperitiesHintsRef.removeEventListener(mSMPProperitiesHintsValueEventListener);
            mSMPProperitiesHintsValueEventListener = null;
        }

        if( typeListeners == null ){ return; }
        for( int i=0; i<typeListeners.size(); i++ ){
            if( typeListeners.get(i) != null ){
                typeReferences.get(i).removeEventListener(typeListeners.get(i));
                typeListeners.set(i, null);
            }
        }
    }
}
