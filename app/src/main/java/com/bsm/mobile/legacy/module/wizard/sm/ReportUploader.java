package com.bsm.mobile.legacy.module.wizard.sm;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Process;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.bsm.mobile.R;
import com.bsm.mobile.common.Tagable;
import com.bsm.mobile.legacy.model.sidemission.ReportSingleMedia;
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class ReportUploader extends Service implements Tagable{

    private boolean timeout;

    private String bSMName;
    private String recordingUserId;
    private String performingUserId;

    private String bTitle;
    private String bBody;

    private Uri[] mediaUris;
    private String[] mediaRealPaths;
    private Bitmap[] mediaThumbnails;

    private ReportSingleMedia[] mediaShipment;

    private static DatabaseReference mRootRef;
    private DatabaseReference mDatabaseReportsRef;
    StorageReference mStorageReference;

    private String rpid;
    private String[] ReportMediaIds;

    private int FOREGROUND_ID = 2137;

    private Intent parentIntent;

    FFmpeg mFFmpeg;
    private boolean mFFmpegLoaded;
    private boolean[] doneCompressing;

    private ExecutorService[] OrginalUriExecutors;
    private ExecutorService[] ThumbnailExecutors;

    private boolean[] ThumbnailUploaded;
    private String[] ThumbnailDownloadUrls;
    private boolean[] OrginalUploaded;
    private String[] OrginalDownloadUrls;
    private boolean success;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(getTag(), "ReportUploader onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(getTag(), "ReportUploader onStartCommand");

        if( parentIntent == null ){
            handleBundles(intent);
            parentIntent = intent;

            new Thread(() -> {
                startForeground(FOREGROUND_ID,
                        buildForegroundNotification());

                try {
                    setServiceTimeout(600000);
                    uploadReport();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }).start();
        }
        return Service.START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(getTag(), "ReportUploader onDestroy");
        if( timeout ){
            Process.killProcess(Process.myPid());
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(getTag(), "ReportUploader onBind");

        return null;
    }

    private Notification buildForegroundNotification() {
        Notification.Builder b = new Notification.Builder(this)
                .setContentTitle(bSMName)
                .setContentText("Meldowanie Misji Pobocznej...")
                .setSmallIcon(R.mipmap.report_icon);

        return b.build();
    }

    private void setServiceTimeout(final long milliseconds) {
        new Thread(() -> {
            try {
                Thread.sleep(milliseconds);
                Log.d(getTag(), "Thread timeout -> kill Report Upload");
                timeout = true;
                KillService();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @SuppressLint("CheckResult")
    private void KillService() throws InterruptedException {
        Log.d(getTag(), "invoke KillService()");

        Single<Boolean> successSingle = Single.create(emitter -> emitter.onSuccess(Boolean.valueOf(success)));
        successSingle
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> {
                    if(success) Toast.makeText(this, "misja zameldowana", Toast.LENGTH_SHORT).show();
                });




        shutdownAllExecutors();
        stopForeground(true);
        stopSelf();
        UploadStorageWakefulReceiver.completeWakefulIntent(parentIntent);
    }

    private void handleBundles(@Nullable Intent intent){
        Bundle bundle = null;
        if (intent != null) {
            bundle = intent.getExtras();
        }
        if( bundle != null ){
            bSMName = bundle.getString("sm_name");
            performingUserId = bundle.getString("performingUser");
            recordingUserId = bundle.getString("recordingUser");
            bBody = bundle.getString("body");
            bTitle = bundle.getString("title");

            if( bundle.getInt("mediaUrisAmount") == 0 ){ return; }
            mediaUris = new Uri[bundle.getInt("mediaUrisAmount")];
            for( int i=0; i< mediaUris.length; i++ ){
                mediaUris[i] = Uri.parse(bundle.getString("mediaUri"+ String.valueOf(i)));
            }
        }
    }

    private void shutdownAllExecutors() throws InterruptedException {
        if( OrginalUriExecutors == null ){ return; }

        for( int i=0; i<OrginalUriExecutors.length; i++ ){

            final String ThumbnailExId = String.valueOf(ThumbnailExecutors[i])
                    .substring(String.valueOf(ThumbnailExecutors[i]).lastIndexOf("@")+1);
            final String orginalExecutorId = String.valueOf(OrginalUriExecutors[i])
                    .substring(String.valueOf(OrginalUriExecutors[i]).lastIndexOf("@")+1);

            ThumbnailExecutors[i].shutdown();
            ThumbnailExecutors[i].awaitTermination(10, TimeUnit.SECONDS);
            Log.d(getTag(), "ThumbnailExecutors["+ThumbnailExId+" shutdown");

            OrginalUriExecutors[i].shutdown();
            OrginalUriExecutors[i].awaitTermination(10, TimeUnit.SECONDS);
            Log.d(getTag(), "OrginalUriExecutors["+orginalExecutorId+" shutdown");
        }
    }


    private void uploadReport() throws InterruptedException {

        if( mRootRef != null ){ KillService(); }

        FirebaseApp.initializeApp(this);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mDatabaseReportsRef = mRootRef.child("Reports");
        rpid = mDatabaseReportsRef.push().getKey();

        mStorageReference = FirebaseStorage.getInstance()
                .getReference(performingUserId).child(rpid);

        if( mediaUris != null ){
            initializeShipmentArray();
            getRealPathsfromUris();
            createMediaThumbnails();
            checkPhotoExtensions();

            loadFFMpegBinary();
            while( !mFFmpegLoaded ){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d(getTag(), "FFmpegLoaded: "+ String.valueOf(mFFmpegLoaded));
            }

            compressOrginalMedia();

            while ( !allCompressed() ){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d(getTag(), "allCompressed: "+ String.valueOf(allCompressed()));
            }

            setReportMediaIds();
            initializeUploadFlags();

            putUrisInStorage();
            putBitmapsInStorage();

            while( !allUploaded() ){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d(getTag(), "allUploaded: "+ String.valueOf(allUploaded()));
            }
        }
        finishUploading();
    }

    private void checkPhotoExtensions() throws InterruptedException {
        for( int i=0; i<mediaShipment.length; i++ ){
            if( mediaShipment[i].getType().equals("photo") ){
                String imageExtension = mediaRealPaths[i]
                        .substring(mediaRealPaths[i].lastIndexOf("."));
                if( !imageExtension.equals(".jpg")
                        && !imageExtension.equals(".jpeg")
                        && !imageExtension.equals(".png")
                        && !imageExtension.equals(".JPG")){
                    Log.d(getTag(), "Unsupported image extintion: ( "
                            + String.valueOf(imageExtension)+" ) -> abort Report Upload");
                    KillService();
                }
            }
        }
    }

    private boolean allCompressed() {
        for( int i=0; i<doneCompressing.length; i++ ){
            if( !doneCompressing[i] ){
                return false;
            }
        }
        return true;
    }

    private void compressOrginalMedia() {
        doneCompressing = new boolean[mediaRealPaths.length];

        File CompressedVideosDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES);

        File CompressedImageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

        File dest;
        String compressedFilePath;

        for( int i=0; i<mediaRealPaths.length; i++ ){
            switch (mediaShipment[i].getType()){
                case "video":
                    dest = new File(CompressedVideosDir,
                            "BSM-"+bSMName+"-proof-"+ String.valueOf(i+1)+".mp4");

                    compressedFilePath = dest.getAbsolutePath();
                    Log.d(getTag(), "startTrim src: "+mediaRealPaths[i]);
                    Log.d(getTag(), "startTrim dest: "+compressedFilePath);

                    String[] complexCommand = {"-y", "-i", mediaRealPaths[i],
                            "-vcodec", "mpeg4", "-b:v", "150k", "-b:a", "48000",
                            "-ac", "2", "-ar", "22050", compressedFilePath};

                    executeFFMpegBinary(complexCommand, i, compressedFilePath);
                    break;
                case "photo":
                    String imageExtension = mediaRealPaths[i]
                            .substring(mediaRealPaths[i].lastIndexOf("."));

                    dest = new File(CompressedImageDir,
                            "BSM-"+bSMName+"-proof-"+ String.valueOf(i+1)+imageExtension);

                    compressedFilePath = dest.getAbsolutePath();
                    Log.d(getTag(), "startTrim src: "+mediaRealPaths[i]);
                    Log.d(getTag(), "startTrim dest: "+compressedFilePath);

                    runImageCompression( i, mediaRealPaths[i], dest );
                    break;
                default:
                    break;
            }
        }
    }

    private void runImageCompression(
            final int orginalMediaIndex, final String mediaRealPath, final File dest ) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String imageExtension = mediaRealPath.substring(mediaRealPath.lastIndexOf("."));
                try {
                    Bitmap orginal = BitmapFactory.decodeFile(mediaRealPath);
                    if( orginal.getWidth() > 1920 || orginal.getHeight() > 1920 ){
                        Bitmap scaled = getResizedBitmap(orginal, 1920);
                        orginal = scaled;
                    }
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    switch ( imageExtension ){
                        case ".JPG":
                        case ".jpeg":
                        case ".jpg":
                            orginal.compress(Bitmap.CompressFormat.JPEG, 70, out);
                            break;
                        case ".png":
                            orginal.compress(Bitmap.CompressFormat.PNG, 70, out);
                            break;
                        default:
                            break;
                    }
                    dest.createNewFile();
                    FileOutputStream fos = new FileOutputStream(dest);
                    fos.write(out.toByteArray());
                    fos.flush();
                    fos.close();

                    mediaRealPaths[orginalMediaIndex] = dest.getAbsolutePath();
                    doneCompressing[orginalMediaIndex] = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void loadFFMpegBinary() {
        try{
            if( mFFmpeg == null ){
                mFFmpeg = FFmpeg.getInstance(this);
            }
            mFFmpeg.loadBinary(new LoadBinaryResponseHandler(){

                @Override
                public void onSuccess() {
                    super.onSuccess();
                    Log.d(getTag(), "ffmpeg loaded correctly");
                    mFFmpegLoaded = true;
                }
            });
        } catch (FFmpegNotSupportedException e) {
            e.printStackTrace();
        }

    }

    private void executeFFMpegBinary(
            final String[] command, final int orginalMediaIndex, final String compressedFilePath ){

        try{
            mFFmpeg.execute(command, new ExecuteBinaryResponseHandler(){
                @Override
                public void onSuccess(String message) {
                    super.onSuccess(message);
                    Log.d(getTag(), "SUCCESS with output: "+message);
                }

                @Override
                public void onFailure(String message) {
                    super.onFailure(message);
                    Log.d(getTag(), "FAILED with output: "+message);
                }

                @Override
                public void onStart() {
                    super.onStart();
                    Log.d(getTag(), "Started command: ffmpeg "+ Arrays.toString(command));
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    Log.d(getTag(), "Finished command: ffmpeg "+ Arrays.toString(command));
                    mediaRealPaths[orginalMediaIndex] = compressedFilePath;
                    doneCompressing[orginalMediaIndex] = true;
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
        }
    }

    private void initializeUploadFlags(){
        OrginalUploaded = new boolean[mediaUris.length];
        ThumbnailUploaded = new boolean[mediaUris.length];
    }

    private boolean allUploaded(){
        for( int i=0; i<mediaUris.length; i++ ){
            if( !OrginalUploaded[i] || !ThumbnailUploaded[i] ){
                return false;
            }
        }
        return true;
    }

    private void setReportMediaIds() {
        ReportMediaIds = new String[mediaShipment.length];

        for (int i = 0; i < mediaShipment.length; i++) {
            ReportMediaIds[i] = mDatabaseReportsRef
                    .child(rpid).child("mediaUrls").push().getKey();
        }
    }

    private void getRealPathsfromUris() {
        mediaRealPaths = new String[mediaUris.length];

        for (int i = 0; i < mediaUris.length; i++) {
            mediaRealPaths[i] = getPathFromURI(this, mediaUris[i]);
            Log.d(getTag(), "mediaRealPaths[i]: " + mediaRealPaths[i]);
        }
    }

    private void initializeShipmentArray() {
        mediaShipment = new ReportSingleMedia[mediaUris.length];
        for (int i = 0; i < mediaUris.length; i++) {
            mediaShipment[i] = new ReportSingleMedia();
        }
    }

    private void createMediaThumbnails() {

        mediaThumbnails = new Bitmap[mediaUris.length];
        ContentResolver cR = this.getContentResolver();
        String mime;

        for (int i = 0; i < mediaUris.length; i++) {
            mime = cR.getType(mediaUris[i]);
            if (mime != null) {
                if (mime.startsWith("image")) {

                    mediaShipment[i].setType("photo");
                    Log.d(getTag(), "mediaRealPaths[i]: " + mediaRealPaths[i]);
                    mediaThumbnails[i] = ThumbnailUtils.extractThumbnail(
                            BitmapFactory.decodeFile(mediaRealPaths[i]),
                            320,
                            240);
                } else {
                    mediaShipment[i].setType("video");

                    try {
                        mediaThumbnails[i] = Bitmap.createBitmap(
                                ThumbnailUtils.createVideoThumbnail(mediaRealPaths[i],
                                        MediaStore.Video.Thumbnails.MINI_KIND));
                    }catch ( Exception e ){
                        Log.d(getTag(), e.toString());
                    }
                }
            }
        }
    }

    private void putUrisInStorage() {

        if( OrginalUriExecutors != null ){ return; }

        OrginalUriExecutors = new ExecutorService[mediaUris.length];
        List<Future<?>> orginalfutures = new ArrayList<>();
        OrginalDownloadUrls = new String[mediaUris.length];
        StorageReference[] references = new StorageReference[mediaUris.length];

        for (int i = 0; i < mediaUris.length; i++) {

            final int finalI = i;
            OrginalUriExecutors[finalI] = Executors.newFixedThreadPool(5);
            Future<?> f = OrginalUriExecutors[finalI].submit(() -> {

                final String ExecutorId = String.valueOf(OrginalUriExecutors[finalI])
                        .substring(String.valueOf(OrginalUriExecutors[finalI]).lastIndexOf("@")+1);

                Log.d(getTag(), "OrginalUriExecutors["+ String.valueOf(finalI)+"]"+ExecutorId+" running");

                references[finalI] = mStorageReference
                        .child(mediaRealPaths[finalI]
                                .substring(mediaRealPaths[finalI].lastIndexOf("/") + 1));

                references[finalI].putFile(Uri.fromFile(new File(mediaRealPaths[finalI])))
                        .addOnCompleteListener(OrginalUriExecutors[finalI], task -> {
                            if (task.isSuccessful()) {
                                 references[finalI]
                                        .getDownloadUrl().addOnCompleteListener(downloadUrlTask -> {
                                           if(downloadUrlTask.isSuccessful()){
                                               OrginalDownloadUrls[finalI] = downloadUrlTask.getResult().toString();
                                               Log.i(getTag(), " Upload task was successful orginalURL: " + OrginalDownloadUrls[finalI]);
                                               OrginalUploaded[finalI] = true;
                                           }
                                        });

                            } else {
                                Log.w(getTag(), " Upload task was not successful.",
                                        task.getException());
                            }
                        });

            });
            orginalfutures.add(f);
        }
    }

    private void putBitmapsInStorage() {

        if( ThumbnailExecutors != null ){ return; }

        ThumbnailExecutors = new ExecutorService[mediaUris.length];
        List<Future<?>> thumbnailfutures = new ArrayList<>();
        ThumbnailDownloadUrls = new String[mediaUris.length];
        StorageReference[] references = new StorageReference[mediaUris.length];

        for (int i = 0; i < mediaThumbnails.length; i++) {
            final int finalI = i;

            ThumbnailExecutors[finalI] = Executors.newFixedThreadPool(5);
            Future<?> f = ThumbnailExecutors[finalI].submit(() -> {

                final String ExecutorId = String.valueOf(ThumbnailExecutors[finalI])
                        .substring(String.valueOf(ThumbnailExecutors[finalI]).lastIndexOf("@")+1);

                Log.d(getTag(), "ThumbnailExecutors["+ExecutorId+" running");

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                mediaThumbnails[finalI].compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();


                references[finalI] = mStorageReference
                        .child("thumb"+mediaRealPaths[finalI]
                                .substring(mediaRealPaths[finalI].lastIndexOf("/") + 1));

                references[finalI].putBytes(data).addOnCompleteListener(ThumbnailExecutors[finalI],
                        task -> {
                            if (task.isSuccessful()) {
                                references[finalI].getDownloadUrl().addOnCompleteListener(downloadUrlTask -> {
                                    if(downloadUrlTask.isSuccessful()){
                                        ThumbnailDownloadUrls[finalI] = downloadUrlTask.getResult().toString();
                                        Log.i(getTag(), " Upload task was successful thumbnailURL: " + ThumbnailDownloadUrls[finalI]);
                                        ThumbnailUploaded[finalI] = true;
                                    }
                                });

                            } else {
                                Log.w(getTag(), "Thumbnail upload task was not successful.",
                                        task.getException());
                            }
                        });

            });
            thumbnailfutures.add(f);
            Log.d(getTag(), "Thumbnailfutures.size(): "+ String.valueOf(thumbnailfutures));


        }
    }
    private void finishUploading() throws InterruptedException {

        this.success = true;

        mDatabaseReportsRef.child(rpid).child("timestamp").setValue(ServerValue.TIMESTAMP);
        mDatabaseReportsRef.child(rpid).child("valid").setValue(true);
        mDatabaseReportsRef.child(rpid).child("rated").setValue(false);
        mDatabaseReportsRef.child(rpid).child("performing_user").setValue(performingUserId);
        mDatabaseReportsRef.child(rpid).child("sm_name").setValue(bSMName);

        if( bTitle == null ){
            mDatabaseReportsRef.child(rpid).child("recording_user").setValue(recordingUserId);
        } else {
            mDatabaseReportsRef.child(rpid).child("text").child("body").setValue(bBody);
            mDatabaseReportsRef.child(rpid).child("text").child("title").setValue(bTitle);
        }

        if( mediaUris != null ){
            for (int i = 0; i < mediaShipment.length; i++) {

                DatabaseReference mediaRef = mDatabaseReportsRef.child(rpid)
                        .child("mediaUrls")
                        .child(ReportMediaIds[i]);

                mediaRef.child("type").setValue(mediaShipment[i].getType());
                mediaRef.child("orginalUrl").setValue(OrginalDownloadUrls[i]);
                mediaRef.child("thumbnailUrl").setValue(ThumbnailDownloadUrls[i]);
            }
        }
        KillService();
    }

    // answered Oct 4 '14 at 19:30 Michal Heneš <3 <3 <3

    @SuppressLint("NewApi")
    public static String getPathFromURI(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}