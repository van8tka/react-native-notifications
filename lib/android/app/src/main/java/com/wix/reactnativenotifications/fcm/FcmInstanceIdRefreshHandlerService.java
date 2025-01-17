package com.wix.reactnativenotifications.fcm;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import android.content.Context;
import android.content.Intent;
import com.facebook.react.bridge.ReactContext;

public class FcmInstanceIdRefreshHandlerService extends JobIntentService {

    public static String EXTRA_IS_APP_INIT = "isAppInit";
    public static String EXTRA_MANUAL_REFRESH = "doManualRefresh";
    public static final int JOB_ID = 2400;
    private static ReactContext mReactContext;

    public static void enqueueWork(Context context, Intent work, ReactContext reactContext) {
        mReactContext = reactContext;
        enqueueWork(context, FcmInstanceIdRefreshHandlerService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        IFcmToken fcmToken = FcmToken.get(this, mReactContext);
        if (fcmToken == null) {
            return;
        }

        if (intent.getBooleanExtra(EXTRA_IS_APP_INIT, false)) {
            fcmToken.onAppReady();
        } else if (intent.getBooleanExtra(EXTRA_MANUAL_REFRESH, false)) {
            fcmToken.onManualRefresh();
        } else {
            fcmToken.onNewTokenReady();
        }
    }
}
