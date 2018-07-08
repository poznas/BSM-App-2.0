package com.bsm.android.home;

import com.bsm.android.core.Tagable;

public interface HomeActivityMVP {

    interface View{

        void showProgress();

        void hideProgress();

        void goLoginActivity();

        void signOutFromGoogle();
    }

    interface Presenter extends Tagable {

        void attachView(View view);

        void unsubscribe();

        void subscribeForData();

        boolean signOut();
    }

    interface Model{

        void signOut();
    }
}
