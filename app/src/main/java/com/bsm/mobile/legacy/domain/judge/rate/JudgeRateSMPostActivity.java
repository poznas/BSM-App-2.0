package com.bsm.mobile.legacy.domain.judge.rate;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bsm.mobile.R;
import com.bsm.mobile.domain.home.HomeActivity;
import com.bsm.mobile.legacy.domain.PhotoVideoFullscreenDisplay;
import com.bsm.mobile.legacy.domain.wizard.list.WizardsActivity;
import com.bsm.mobile.legacy.model.SideMissionInfo;
import com.bsm.mobile.legacy.model.User;
import com.bsm.mobile.legacy.model.ZonglerPost;
import com.bsm.mobile.legacy.model.sidemission.PropertyDetails;
import com.bsm.mobile.legacy.model.sidemission.ReportBasicFirebase;
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

public class JudgeRateSMPostActivity extends AppCompatActivity {

    private Context context;

    @BindView(R.id.item_user_name_view)
    TextView userNameView;
    @BindView(R.id.item_user_image_view)
    CircleImageView userImageView;
    @BindView(R.id.item_title_view)
    TextView titleView;
    @BindView(R.id.thumbnail_parent_view)
    View thumbnailParentView;
    @BindView(R.id.thumbnail_view)
    ImageView thumbnailView;
    @BindView(R.id.play_button_view)
    ImageView playButtonView;
    @BindView(R.id.item_body_view)
    TextView bodyView;
    @BindView(R.id.item_time_view)
    TextView timeView;
    @BindView(R.id.item_date_view)
    TextView dateView;

    @BindView(R.id.sm_name_view)
    TextView smNameView;
    @BindView(R.id.google_drive_view)
    ImageView googleDriveView;
    @BindView(R.id.team_name_view)
    TextView teamView;
    @BindView(R.id.info_view)
    ImageView infoView;
    @BindView(R.id.rate_properities_list_view)
    ListView propertiesListView;
    @BindView(R.id.send_button_view)
    View sendButtonView;

    private String bSMName;
    private String bUserImageURL;
    private String bUserName;
    private String bRpid;
    private String bTime;
    private String bDate;

    private DatabaseReference mDatabaseSMDocsRef;
    private ValueEventListener mSMDocsValueEventListener;
    private static SideMissionInfo mSMInfo;
    private Intent googleDriveIntent;

    private Intent wizardsIntent;

    private Intent fullscreenIntent;

    private static DatabaseReference mRootRef;
    private DatabaseReference mDatabaseInReportsRef;
    private ValueEventListener mInReportValueEventListener;
    private DatabaseReference mDatabasePerformingUserRef;
    private ValueEventListener mPerformingUserValueEventListener;
    private DatabaseReference mDatabaseSMPPropertiesRef;
    private ValueEventListener mSMPPropertiesValueEventListener;
    private DatabaseReference mDatabaseSMPPropertiesHintsRef;
    private ValueEventListener mSMPPropertiesHintsValueEventListener;

    private static FirebaseUser mFirebaseUser;

    private static ReportBasicFirebase mReportBasicFirebase;
    private static User mPerformingUser;
    private static ZonglerPost mZonglerPost;

    private List<String> propertiesNames;
    private List<String> propertiesSymbols;
    private List<String> propertiesTypes;
    private List<String> propertiesHintsNames;
    private List<String> propertiesHints;

    private boolean propertiesHintsLoaded = false;
    private boolean propertiesDetailsLoaded = false;

    private List<PropertyDetails> propertiesDetails;
    private List<ValueEventListener> typeListeners;
    private List<DatabaseReference> typeReferences;
    private boolean[] propertiesReady;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judge_sm_post);
        ButterKnife.bind(this);
        context = this;

        initializeFirebaseComponents();
        InilializeSMDocsListener();
        InitializeInReportsListener();
        InitializeSMPPropertiesListener();
        InitializeSMPProperitiesHintsListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detachFirebaseListeners();
    }

    private void InitializeInReportsListener() {
        if(mInReportValueEventListener == null ){
            mInReportValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mReportBasicFirebase = dataSnapshot.getValue(ReportBasicFirebase.class);
                    mDatabasePerformingUserRef = mRootRef.child("users").child(mReportBasicFirebase.getPerforming_user());
                    mZonglerPost = new ZonglerPost();
                    mZonglerPost.setBody(String.valueOf(dataSnapshot.child("text").child("body").getValue()));
                    mZonglerPost.setTitle(String.valueOf(dataSnapshot.child("text").child("title").getValue()));

                    String[] videoFormats = {
                    ".avi", ".mp4", ".mov", ".wmv", ".3gp", ".mpg", ".flv"};

                    for( DataSnapshot media : dataSnapshot.child("mediaUrls").getChildren() ){
                        String orginalUrl = String.valueOf(media.child("orginalUrl").getValue());

                        for (String videoFormat : videoFormats) {
                            if (orginalUrl.contains(videoFormat)) {
                                mZonglerPost.setVideoUrl(orginalUrl);
                            }
                        }
                        if( mZonglerPost.getVideoUrl() == null ){
                            mZonglerPost.setImageUrl(orginalUrl);
                        }
                        mZonglerPost.setThumbnailUrl(String.valueOf(media.child("thumbnailUrl").getValue()));
                    }
                    setPostDisplay();
                    InitializePerformingUserListener();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            };
            mDatabaseInReportsRef.addValueEventListener(mInReportValueEventListener);
        }
    }

    private void setPostDisplay() {
        titleView.setText(mZonglerPost.getTitle());
        bodyView.setText(mZonglerPost.getBody());

        String thumbnailUrl = mZonglerPost.getThumbnailUrl();
        if( thumbnailUrl != null ){

            thumbnailParentView.setVisibility(View.VISIBLE);

            Glide.with(this)
                    .load(thumbnailUrl)
                    .into(thumbnailView);

            if( mZonglerPost.getVideoUrl() != null ){
                playButtonView.setVisibility(View.VISIBLE);
            }
            setThumbnailClickListener();
        }
    }

    private void setThumbnailClickListener(){

        fullscreenIntent = new Intent(this, PhotoVideoFullscreenDisplay.class);
        Bundle fullscreenBundle = new Bundle();

        String ImageUrl = mZonglerPost.getImageUrl();
        String videoUrl = mZonglerPost.getVideoUrl();
        if( ImageUrl != null ){
            fullscreenBundle.putString("type","photo");
            fullscreenBundle.putString("URL",ImageUrl);
        }else{
            fullscreenBundle.putString("type","video");
            fullscreenBundle.putString("URL",videoUrl);
        }
        fullscreenIntent.putExtras(fullscreenBundle);

        thumbnailParentView.setOnClickListener(v -> {
            if( fullscreenIntent != null ){
                startActivity(fullscreenIntent);
            }
        });
    }

    private void InitializePerformingUserListener(){
        if( mPerformingUserValueEventListener == null ){
            mPerformingUserValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mPerformingUser = dataSnapshot.getValue(User.class);
                    InitializeWizardTeam();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            };
            mDatabasePerformingUserRef.addValueEventListener(mPerformingUserValueEventListener);
        }
    }

    private void InitializeWizardTeam() {
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
        wizardsIntent = new Intent(context, WizardsActivity.class );
        infoView.setVisibility(View.VISIBLE);
        infoView.setOnClickListener(v -> {
            if( wizardsIntent != null ){
                context.startActivity(wizardsIntent);
            }
        });
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

    private void InitializeSMPPropertiesListener() {
        if( mSMPPropertiesValueEventListener == null ){
            mSMPPropertiesValueEventListener = new ValueEventListener() {
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
                    propertiesDetailsLoaded = true;
                    if(propertiesHintsLoaded){
                        createProperitiesDetails();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            };
            mDatabaseSMPPropertiesRef.addValueEventListener(mSMPPropertiesValueEventListener);
        }
    }

    private void InitializeSMPProperitiesHintsListener() {
        if( mSMPPropertiesHintsValueEventListener == null ){
            mSMPPropertiesHintsValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    propertiesHints = new ArrayList<>();
                    propertiesHintsNames = new ArrayList<>();
                    for( DataSnapshot child : dataSnapshot.getChildren() ){
                        propertiesHintsNames.add(child.getKey());
                        propertiesHints.add(String.valueOf(child.getValue()));
                    }
                    propertiesHintsLoaded = true;
                    if(propertiesDetailsLoaded){
                        createProperitiesDetails();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            };
            mDatabaseSMPPropertiesHintsRef.addValueEventListener(mSMPPropertiesHintsValueEventListener);
        }
    }

    private void createProperitiesDetails() {
        propertiesDetails = new ArrayList<>();
        for(int i = 0; i< propertiesNames.size(); i++ ){

            if( !propertiesTypes.get(i).equals("professor_value")
                    && !propertiesNames.get(i).equals("płeć_wykonawcy")){

                for(int j = 0; j< propertiesHintsNames.size(); j++ ){
                    if( propertiesNames.get(i).equals(propertiesHintsNames.get(j))){

                        propertiesDetails.add(PropertyDetails.builder()
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
            typeReferences.add( mDatabaseSMPPropertiesRef
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
        Map<String, Long> reportRate = new HashMap<>();
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

            mRootRef.child("ReportRates").child(bRpid).child(mFirebaseUser.getUid()).setValue(reportRate);

            Intent home = new Intent(this, HomeActivity.class);
            startActivity(home);
            finish();
        }
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

    private void initializeFirebaseComponents() {
        Bundle bundle = this.getIntent().getExtras();
        if( bundle != null ){
            bSMName = bundle.getString("sm_name");
            bUserImageURL = bundle.getString("user_image_url");
            bUserName = bundle.getString("user_name");
            bRpid = bundle.getString("rpid");
            bTime = bundle.getString("time");
            bDate = bundle.getString("date");
        }
        setTitle(bSMName);
        timeView.setText(bTime);
        dateView.setText(bDate);
        userNameView.setText(bUserName);
        Glide.with(context)
                .load(bUserImageURL)
                .into(userImageView);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mDatabaseInReportsRef = mRootRef.child("Reports").child(bRpid);
        mDatabaseSMDocsRef = mRootRef.child("SideMissionsDocs").child(bSMName);
        mDatabaseSMPPropertiesRef = mRootRef.child("SideMissionsProperities").child(bSMName).child("properities");
        mDatabaseSMPPropertiesHintsRef = mRootRef.child("SideMissionsProperities").child(bSMName).child("properitiesHints");
    }

    private void detachFirebaseListeners() {
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
        if( mSMPPropertiesValueEventListener != null ){
            mDatabaseSMPPropertiesRef.removeEventListener(mSMPPropertiesValueEventListener);
            mSMPPropertiesValueEventListener = null;
        }
        if( mSMPPropertiesHintsValueEventListener != null ){
            mDatabaseSMPPropertiesHintsRef.removeEventListener(mSMPPropertiesHintsValueEventListener);
            mSMPPropertiesHintsValueEventListener = null;
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
