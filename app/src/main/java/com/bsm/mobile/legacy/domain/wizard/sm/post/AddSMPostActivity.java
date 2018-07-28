package com.bsm.mobile.legacy.domain.wizard.sm.post;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bsm.mobile.R;
import com.bsm.mobile.common.Tagable;
import com.bsm.mobile.legacy.domain.wizard.sm.UploadStorageWakefulReceiver;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class AddSMPostActivity extends AppCompatActivity implements Tagable, EasyPermissions.PermissionCallbacks{

    @BindView(R.id.title_edit_text)
    EditText titleEditText;
    @BindView(R.id.body_edit_text)
    EditText bodyEditText;
    @BindView(R.id.pick_files_button)
    View pickFilesButton;
    @BindView(R.id.progress_bar_view)
    ProgressBar progressBar;
    @BindView(R.id.ok_image_view)
    ImageView okView;
    @BindView(R.id.send_button)
    View sendButton;

    private String bSMName;

    static final int PICK_FILES_REQUEST = 2137;
    static final int PICK_FILES_KITKAT = 1488;
    private Uri[] mediaUris;
    private String performingUserId;

    UploadStorageWakefulReceiver mReceiver;
    private boolean isProviderRegistered = false;
    public static final String BROADCAST =
            "com.bsm.mobile.legacy.module.wizard.sm.android.action.broadcast";

    private static final int RC_WRITE_STORAGE_PERM = 123;
    private static final String[] STORAGE_PERMS =
            {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private String mTitle;
    private String mBody;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        setContentView(R.layout.activity_add_sm_post);
        ButterKnife.bind(this);

        initializeFirebaseComponents();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setPickFileButton();
        }
        setSendButton();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if( isProviderRegistered ){
            unregisterReceiver(mReceiver);
            isProviderRegistered = false;
        }
    }

    private void setSendButton() {
        sendButton.setOnClickListener(v -> {
            if( isPostCorrect() ){
                okView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                setUpUpload();
            }
        });
    }

    private void setUpUpload() {

        if( mReceiver == null ){
            mReceiver = new UploadStorageWakefulReceiver();
            IntentFilter intentFilter = new IntentFilter(BROADCAST);
            registerReceiver( mReceiver , intentFilter);
            isProviderRegistered = true;

            Intent intent = new Intent(BROADCAST);
            Bundle bundle = new Bundle();
            bundle.putString("sm_name",bSMName);
            bundle.putString("performingUser",performingUserId);
            bundle.putString("body",mBody);
            bundle.putString("title",mTitle);
            if( mediaUris != null ){
                for( int i=0; i<mediaUris.length; i++ ){
                    bundle.putString("mediaUri"+ String.valueOf(i),mediaUris[i].toString());
                }
                bundle.putInt("mediaUrisAmount",mediaUris.length);
            } else {
                bundle.putInt("mediaUrisAmount",0);
            }

            intent.putExtras(bundle);
            sendBroadcast(intent);
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        finish();
    }


    private boolean isPostCorrect() {
        mTitle = titleEditText.getText().toString();
        mBody = bodyEditText.getText().toString();

        if( mTitle.matches("")){
            Toast.makeText(this, "tytuł?", Toast.LENGTH_SHORT).show();
            return false;
        }
        if( mBody.matches("")){
            Toast.makeText(this, "Jeszcze treść", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setPickFileButton() {
        final Intent intent;

        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        intent.setType("*/*");
        String[] mimetypes = {"image/*", "video/*"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        pickFilesButton.setOnClickListener(v -> {

            if(!hasStoragePermissions()) return;

            if (Build.VERSION.SDK_INT < 19){
                startActivityForResult(Intent.createChooser(intent, "Wybierz pliki"),
                        PICK_FILES_REQUEST);
            }else{
                startActivityForResult(intent, PICK_FILES_KITKAT);
            }

        });
    }

    private boolean hasStoragePermissions() {
        if(!EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || !EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)){

            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale_ask),
                    RC_WRITE_STORAGE_PERM,
                    STORAGE_PERMS
            );
            return false;
        }
        return true;
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.d(getTag(), "onPermissionsGranted: " + perms);
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.d(getTag(), "onPermissionsDenied: " + perms);

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( resultCode == RESULT_OK && (requestCode == PICK_FILES_REQUEST || requestCode == PICK_FILES_KITKAT)) {
            pickFilesButton.setOnClickListener(null);
            progressBar.setVisibility(View.VISIBLE);
            if (data.getData() != null) {
                mediaUris = new Uri[1];
                if( requestCode == PICK_FILES_REQUEST ){
                    mediaUris[0] = data.getData();
                }else{
                    mediaUris[0] = data.getData();
                    grantUriPermission(getPackageName(), mediaUris[0],
                            Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    final int takeFlags = data.getFlags()
                            & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    getContentResolver().takePersistableUriPermission(mediaUris[0], takeFlags);
                }
                Log.d(getTag(), "mediaUris[0]: " + mediaUris[0]);
                progressBar.setVisibility(View.GONE);
                okView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initializeFirebaseComponents() {
        Bundle bundle = this.getIntent().getExtras();
        if( bundle != null ){
            bSMName = bundle.getString("sm_name");
        }
        setTitle(bSMName);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
        performingUserId = mFirebaseUser != null ? mFirebaseUser.getUid() : null;
    }


}
