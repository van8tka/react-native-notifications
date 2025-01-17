package com.wix.reactnativenotifications.fcm;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class FcmInstanceIdRefreshHandlerService extends Worker {

    public static final String EXTRA_IS_APP_INIT = "isAppInit";
    public static final String EXTRA_MANUAL_REFRESH = "doManualRefresh";

    public FcmInstanceIdRefreshHandlerService(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        IFcmToken fcmToken = FcmToken.get(getApplicationContext());
        if (fcmToken == null) {
            return Result.failure();
        }

        boolean isAppInit = getInputData().getBoolean(EXTRA_IS_APP_INIT, false);
        boolean doManualRefresh = getInputData().getBoolean(EXTRA_MANUAL_REFRESH, false);

        if (isAppInit) {
            fcmToken.onAppReady();
        } else if (doManualRefresh) {
            fcmToken.onManualRefresh();
        } else {
            fcmToken.onNewTokenReady();
        }

        return Result.success();
    }
}
