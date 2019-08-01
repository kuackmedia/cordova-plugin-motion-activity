package com.kuack.plugins.motion;

import android.util.Log;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.app.IntentService;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.ActivityTransitionResult;
import com.google.android.gms.location.ActivityTransitionEvent;

import com.google.android.gms.location.ActivityRecognitionResult;

import static com.kuack.plugins.motion.MotionActivity.onMotionActivity;

import android.widget.Toast;

public class MotionService extends IntentService {

    public MotionService() {
        super("MotionService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            if (ActivityRecognitionResult.hasResult(intent)) {
                ActivityRecognitionResult activityResult = ActivityRecognitionResult.extractResult(intent);
                DetectedActivity activity = activityResult.getMostProbableActivity();
                onMotionActivity(activity);
                // int activityType = detectedActivity.getType();
                // switch (activityType) {
                //     case DetectedActivity.STILL:
                //     case DetectedActivity.WALKING:
                //     case DetectedActivity.RUNNING:
                //     case DetectedActivity.IN_VEHICLE:
                //             onMotionActivity(activityType);
                //         break;
                //     default:
                //         break;
                // }
            }
        }
    }

}
