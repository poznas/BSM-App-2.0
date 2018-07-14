package com.bsm.mobile.backend.google;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bsm.mobile.R;
import com.bsm.mobile.core.Tagable;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class GoogleAuthService implements Tagable{

    /**
     * For sign out purpose
     */
    private GoogleApiClient googleApiClient;

    public GoogleAuthService(Context context){

        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();
    }

    public GoogleAuthService connect(){
        if( googleApiClient != null ){
            Log.i(getTag(), "connecting google API Client");
            googleApiClient.connect();
        }
        return this;
    }

    public GoogleAuthService disconnect(){
        if( googleApiClient != null ){
            if( googleApiClient.isConnected()){
                Log.i(getTag(), "disconnecting google API Client");
                googleApiClient.disconnect();
            }
        }
        return this;
    }

    public void signOut(){
        if( googleApiClient != null ){
            if( googleApiClient.isConnected()){
                Auth.GoogleSignInApi.signOut(googleApiClient);
                googleApiClient.disconnect();
                googleApiClient.connect();
                Log.i(getTag(), "Google API Client : signed out ");
            }
        }
    }

    /**
     * For sign in purpose
     */
    @NonNull
    private static GoogleApiClient getGoogleApiClient(Context context) {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestIdToken(context.getString(R.string.default_web_client_id))
                .build();

        return new GoogleApiClient.Builder(context)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
    }

    public static Intent getSignInIntent(Context context){
        return Auth.GoogleSignInApi.getSignInIntent(getGoogleApiClient(context));
    }


    public static GoogleSignInResult getIntentResult(Intent data) {
        return Auth.GoogleSignInApi.getSignInResultFromIntent(data);
    }
}
