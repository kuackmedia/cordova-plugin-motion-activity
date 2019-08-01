package com.kuack.plugins.motion;

import android.util.Log;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.Intent;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.ActivityTransitionResult;
import com.google.android.gms.location.ActivityTransitionEvent;

import static com.kuack.plugins.motion.MotionActivity.onMotionActivity;

import android.widget.Toast;

public class MotionReceiver extends BroadcastReceiver {
    
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("MotionActivity", "onReceive: " + ActivityTransitionResult.hasResult(intent));
        if (ActivityTransitionResult.hasResult(intent)) {
            ActivityTransitionResult activityResult = ActivityTransitionResult.extractResult(intent);
            // DetectedActivity currentActivity = activityResult.getMostProbableActivity();
            for (ActivityTransitionEvent event : activityResult.getTransitionEvents()) {
                Log.d("MotionActivity", "MotionReceiver event: " + event.toString());
                onMotionActivity(event, context);
                // Toast.makeText(context, event.toString(), Toast.LENGTH_LONG).show();
            }
            // PluginResult result = new PluginResult(PluginResult.Status.OK, activitiesInfo);
            // result.setKeepCallback(true);
            // context.callbackContext.sendPluginResult(result);
        }
    }

}
