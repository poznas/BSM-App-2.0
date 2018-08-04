package com.bsm.mobile.legacy.domain.judge.rate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bsm.mobile.R;
import com.bsm.mobile.common.Tagable;
import com.bsm.mobile.domain.home.HomeActivity;
import com.bsm.mobile.legacy.domain.points.ReportMediaAdapter;
import com.bsm.mobile.legacy.domain.wizard.list.WizardsActivity;
import com.bsm.mobile.legacy.model.SideMissionInfo;
import com.bsm.mobile.legacy.model.User;
import com.bsm.mobile.legacy.model.sidemission.PropertyDetails;
import com.bsm.mobile.legacy.model.sidemission.ReportBasicFirebase;
import com.bsm.mobile.legacy.model.sidemission.ReportSingleMedia;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class JudgeRateSMActivity extends AppCompatActivity implements Tagable {


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
    ListView propertiesListView;
    @BindView(R.id.send_button_view)
    View sendButtonView;

    private String bSMName;
    private String bUserImageURL;
    private String bUserName;
    private String bRpid;

    private DatabaseReference mDatabaseSMDocsRef;
    private ValueEventListener mSMDocsValueEventListener;
    private SideMissionInfo mSMInfo;
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

    private FirebaseAuth mAuth;
    private static FirebaseUser mFirebaseUser;

    private static ReportBasicFirebase mReportBasicFirebase;
    private static List<ReportSingleMedia> mMedia;
    private static User mPerformingUser;

    private List<String> propertiesNames;
    private List<String> propertiesSymbols;
    private List<String> propertiesTypes;
    private List<String> propertiesHintsNames;
    private List<String> propertiesHints;

    private boolean propertiesHintsLoaded = false;
    private boolean properitiesDetailsLoaded = false;

    private List<PropertyDetails> propertiesDetails;
    private List<ValueEventListener> typeListeners;
    private List<DatabaseReference> typeReferences;
    private boolean[] propertiesReady;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.judge_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.judge_opluj:
                makeReportInvalid();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judge_side_mission);
        ButterKnife.bind(this);

        initializeFirebaseComponents();
        InitializeInReportMediaListener();
        InitializeInReportListener();
        InitializeSMPProperitiesListener();
        InitializeSMPPropertiesHintsListener();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        detachFirebaseListeners();
    }


    private void makeReportInvalid() {
        if( mFirebaseUser != null ){
            mRootRef.child("InvalidReports").child(bRpid).setValue(mFirebaseUser.getUid());
            mDatabaseInReportsRef.child("valid").setValue(false);
            mRootRef.child("pendingReports").child(bRpid).setValue(null);
            mRootRef.child("requireProfRate").child(bRpid).setValue(null);

            exitJudgeActivity();
        }
    }

    private void createPropertiesDetails() {
        propertiesDetails = new ArrayList<>();
        for(int i = 0; i< propertiesNames.size(); i++ ){

            if( !propertiesTypes.get(i).equals("professor_value")
                    && !propertiesNames.get(i).equals("płeć_wykonawcy")){

                for(int j = 0; j< propertiesHintsNames.size(); j++ ){
                    if( propertiesNames.get(i).equals(propertiesHintsNames.get(j))){

                        propertiesDetails.add(
                                PropertyDetails.builder()
                                        .name(propertiesHintsNames.get(j))
                                        .hint(propertiesHints.get(j))
                                        .symbol(propertiesSymbols.get(i))
                                        .type(propertiesTypes.get(i))
                                        .build()
                        );
                    }
                }
            }
        }
        completePropertiesDetails();
    }

    private void completePropertiesDetails() {

        propertiesReady = new boolean[propertiesDetails.size()];
        typeListeners = new ArrayList<>();
        typeReferences = new ArrayList<>();

        for(int i = 0; i< propertiesDetails.size(); i++ ){
            typeListeners.add(null);
            typeReferences.add( mDatabaseSMPProperitiesRef
                    .child(propertiesDetails.get(i).getName())
                    .child("type")
                    .child(propertiesDetails.get(i).getType()));
        }

        for(int i = 0; i< propertiesDetails.size(); i++ ){
            if( typeListeners.get(i) == null ){
                final int finalI = i;
                typeListeners.set(i, new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        switch (propertiesDetails.get(finalI).getType()){
                            case "spinner":
                                List<String> keys = new ArrayList<>();
                                List<Long> values = new ArrayList<>();
                                for( DataSnapshot data : dataSnapshot.getChildren() ){
                                    keys.add(data.getKey());
                                    values.add(data.getValue(Long.class));
                                }
                                propertiesDetails.get(finalI).setSpinnerKeys(keys);
                                propertiesDetails.get(finalI).setSpinnerValues(values);
                                break;
                            case "limited_value":
                                propertiesDetails.get(finalI).setLimitedValue(dataSnapshot.getValue(Long.class));
                                break;
                            case "professor_value":
                                propertiesDetails.get(finalI).setProfType(String.valueOf(dataSnapshot.getValue()));
                            default:
                                break;
                        }

                        propertiesReady[finalI] = true;
                        if( readyToLaunch() ){
                            InitializePropertiesListView();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                });
                typeReferences.get(i).addValueEventListener(typeListeners.get(i));
            }
        }
    }

    private boolean readyToLaunch(){
        for (boolean aPropertiesReady : propertiesReady) {
            if (!aPropertiesReady) {
                return false;
            }
        }
        return true;
    }

    private void InitializePropertiesListView() {
        Log.d(getTag(), "initialize properties list view : " + propertiesDetails);
        RatePropertiesAdapter adapter = new RatePropertiesAdapter(this, propertiesDetails);
        propertiesListView.setAdapter(adapter);
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
        Map<String, Object> reportRate = new HashMap<>();
        for(int i = 0; i< propertiesListView.getChildCount(); i++ ){

            PropertyDetails current = (PropertyDetails) propertiesListView.getAdapter().getItem(i);

            Spinner spinner;
            EditText editText;
            String selectedKey;

            switch (current.getType()){
                case "normal_value":
                    editText = propertiesListView
                            .getChildAt(i).findViewById(R.id.item_properity_edit_text);
                    reportRate.put(current.getSymbol(),
                            Long.valueOf(editText.getText().toString()));
                    break;
                case "limited_value":
                    spinner = propertiesListView
                            .getChildAt(i).findViewById(R.id.item_properity_spinner);
                    reportRate.put(current.getSymbol(),
                            Long.valueOf(spinner.getSelectedItem().toString()));
                    break;
                case "spinner":
                    spinner = propertiesListView
                            .getChildAt(i).findViewById(R.id.item_properity_spinner);
                    selectedKey = spinner.getSelectedItem().toString();
                    for( int j=0; j<current.getSpinnerKeys().size(); j++ ){
                        if( selectedKey.equals(current.getSpinnerKeys().get(j))){
                            reportRate.put(current.getSymbol(),
                                    current.getSpinnerValues().get(j));
                        }
                    }
                    break;
                case "boolean_value":
                    spinner = propertiesListView
                            .getChildAt(i).findViewById(R.id.item_properity_spinner);
                    selectedKey = spinner.getSelectedItem().toString();
                    if( selectedKey.equals("TAK") ){
                        reportRate.put(current.getSymbol(), (long) 1);
                    }else {
                        reportRate.put(current.getSymbol(), (long) 0);
                    }
                    break;
                default:
                    break;
            }
        }
        mRootRef.child("ReportRates").child(bRpid)
                .child(mFirebaseUser.getUid()).updateChildren(reportRate);
        exitJudgeActivity();
    }

    private void exitJudgeActivity(){
        Intent home = new Intent(this, HomeActivity.class);
        startActivity(home);
        finish();
    }

    private boolean correctJudgeInput() {

        for(int i = 0; i< propertiesListView.getChildCount(); i++ ){

            PropertyDetails current = (PropertyDetails) propertiesListView.getAdapter().getItem(i);
            switch (current.getType()){
                case "normal_value":
                    EditText editText = propertiesListView
                            .getChildAt(i).findViewById(R.id.item_properity_edit_text);
                    if(editText.getText().toString().matches("")){
                        Toast.makeText(this, "Uzupełnij "+current.getName(), Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    break;
                default:
                    Spinner spinner = propertiesListView
                            .getChildAt(i).findViewById(R.id.item_properity_spinner);
                    if( spinner.getSelectedItem().toString().equals("<none>") ){
                        Toast.makeText(this, "Uzupełnij "+current.getName(), Toast.LENGTH_SHORT).show();
                        return false;
                    }
            }
        }
        return true;
    }

    private void InitializeSMPPropertiesHintsListener() {
        if( mSMPProperitiesHintsValueEventListener == null ){
            mSMPProperitiesHintsValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    propertiesHints = new ArrayList<>();
                    propertiesHintsNames = new ArrayList<>();
                    for( DataSnapshot child : dataSnapshot.getChildren() ){
                        propertiesHintsNames.add(child.getKey());
                        propertiesHints.add(String.valueOf(child.getValue()));
                    }
                    propertiesHintsLoaded = true;
                    if( properitiesDetailsLoaded ){
                        createPropertiesDetails();
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
                    propertiesNames = new ArrayList<>();
                    propertiesTypes = new ArrayList<>();
                    propertiesSymbols = new ArrayList<>();
                    for( DataSnapshot data : dataSnapshot.getChildren() ){
                        propertiesNames.add(data.getKey());
                        propertiesSymbols.add(String.valueOf(data.child("symbol").getValue()));
                        for( DataSnapshot type : data.child("type").getChildren() ){
                            propertiesTypes.add(type.getKey());
                        }
                    }
                    properitiesDetailsLoaded = true;
                    if(propertiesHintsLoaded){
                        createPropertiesDetails();
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
                    if(mPerformingUser != null) InitializeWizardTeam();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            };
            mDatabasePerformingUserRef.addValueEventListener(mPerformingUserValueEventListener);
        }
    }

    private void InitializeWizardTeam() {
        teamView.setText(mPerformingUser.getTeam());
        if(mPerformingUser.getTeam() != null){
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
        }
        wizzardsIntent = new Intent(this, WizardsActivity.class );
        infoView.setVisibility(View.VISIBLE);
        infoView.setOnClickListener(v -> {
            if( wizzardsIntent != null ){
                startActivity(wizzardsIntent);
            }
        });
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

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mDatabaseInReportsRef = mRootRef.child("Reports").child(bRpid);
        mDatabaseInReportsMediaRef = mDatabaseInReportsRef.child("mediaUrls");
        mDatabaseSMDocsRef = mRootRef.child("SideMissionsDocs").child(bSMName);
        mDatabaseSMPProperitiesRef = mRootRef.child("SideMissionsProperities").child(bSMName).child("properities");
        mDatabaseSMPProperitiesHintsRef = mRootRef.child("SideMissionsProperities").child(bSMName).child("properitiesHints");
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
                    InitializeSMDocsListener();
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

    private void InitializeSMDocsListener(){
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

    private void detachFirebaseListeners() {
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

        if( typeListeners != null ){
            for( int i=0; i<typeListeners.size(); i++ ){
                if( typeListeners.get(i) != null ){
                    typeReferences.get(i).removeEventListener(typeListeners.get(i));
                    typeListeners.set(i, null);
                }
            }
        }
    }
}
