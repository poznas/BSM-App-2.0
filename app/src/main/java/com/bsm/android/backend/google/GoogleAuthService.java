package com.bsm.android.backend.google;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.bsm.android.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class GoogleAuthService {

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

    public static void signOut(Context context){
        Auth.GoogleSignInApi.signOut(getGoogleApiClient(context));
    }
}
