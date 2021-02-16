package com.forbitbd.myplayer;

import android.app.Application;

import com.smaato.sdk.core.Config;
import com.smaato.sdk.core.SmaatoSdk;
import com.smaato.sdk.core.log.LogLevel;

public class PlayerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Config config = Config.builder()
                // log errors only
                .setLogLevel(LogLevel.ERROR)
                // allow HTTPS traffic only
                .setHttpsOnly(true)
                .build();
        // initialize the Smaato SDK
        SmaatoSdk.init(this, config,getString(R.string.publisher_id));
        SmaatoSdk.setGPSEnabled(true);
    }
}
