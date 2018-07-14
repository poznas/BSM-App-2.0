package com.bsm.mobile.backend.google;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.bsm.mobile.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GoogleAuthService{


    @NonNull
    private static GoogleSignInClient getGoogleApiClient(Context context) {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .build();

        return GoogleSignIn.getClient(context, googleSignInOptions);
    }

    public static Intent getSignInIntent(Context context){
        return getGoogleApiClient(context).getSignInIntent();
    }

    public static Task<GoogleSignInAccount> getGoogleSignInAccount(Intent data) {
        return GoogleSignIn.getSignedInAccountFromIntent(data);
    }

    public static void signOut(Context context){
        getGoogleApiClient(context).signOut();
    }
}
