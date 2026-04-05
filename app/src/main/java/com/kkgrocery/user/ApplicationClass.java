package com.kkgrocery.user;

import androidx.multidex.MultiDexApplication;

import com.onesignal.OneSignal;

public class ApplicationClass extends MultiDexApplication {

        private static final String ONESIGNAL_APP_ID = "7b9988d6-2c7c-49ba-b39c-2c8759356327";
        @Override
        public void onCreate() {
            super.onCreate();

            // Enable verbose OneSignal logging to debug issues if needed.
            OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

            // OneSignal Initialization
            OneSignal.initWithContext(this);
            OneSignal.setAppId(ONESIGNAL_APP_ID);

            // promptForPushNotifications will show the native Android notification permission prompt.
            // We recommend removing the following code and instead using an In-App Message to prompt for notification permission (See step 7)
            OneSignal.promptForPushNotifications();

            OneSignal.getDeviceState();
            OneSignal.getDeviceState().getUserId();
        }
    }


