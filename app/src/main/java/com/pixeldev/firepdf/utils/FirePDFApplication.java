package com.pixeldev.firepdf.utils;

import android.app.Application;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.FirebaseApp;
import com.onesignal.OneSignal;

public class FirePDFApplication extends Application {
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(getApplicationContext(), new OnInitializationCompleteListener() {
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AudienceNetworkAds.initialize(this);
        OneSignal.initWithContext(this);
        OneSignal.setAppId(AdsConfig.ONESIGNAL_APP_ID);
        FirebaseApp.initializeApp(this);
    }
}
