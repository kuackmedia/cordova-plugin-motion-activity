package com.kuack.plugins.motion;

import java.lang.Integer;
import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import java.util.ArrayList;
import java.io.IOException;
import android.util.Log;
import android.content.Intent;
import android.app.PendingIntent;
import java.lang.ref.WeakReference;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;

import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.ActivityTransition;
import com.google.android.gms.location.ActivityTransitionRequest;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.ActivityTransitionEvent;

import android.widget.Toast;

public class MotionActivity extends CordovaPlugin {

    private static WeakReference<CordovaWebView> webView = null;
    private static int lastActivityType = -1;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        MotionActivity.webView = new WeakReference<CordovaWebView>(webView);
        super.initialize(cordova, webView);
        Log.d("MotionActivity", "initialize");
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        Context context = cordova.getActivity().getApplicationContext();
        if ("init".equals(action)) {

        } else if ("startUpdates".equals(action)) {
            Task task = ActivityRecognition.getClient(context).requestActivityUpdates(5000, getServicePendingIntent());
            task.addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    PluginResult result = new  PluginResult(PluginResult.Status.OK); 
                    result.setKeepCallback(false);
                    callbackContext.sendPluginResult(result);
                }
            });
            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    PluginResult result = new  PluginResult(PluginResult.Status.ERROR, e.getMessage()); 
                    result.setKeepCallback(false);
                    callbackContext.sendPluginResult(result);
                }
            });
            PluginResult result = new  PluginResult(PluginResult.Status.NO_RESULT); 
            result.setKeepCallback(true);
            callbackContext.sendPluginResult(result);
            return true;

        } else if ("stopUpdates".equals(action)) {
            Task task = ActivityRecognition.getClient(context).removeActivityUpdates(getServicePendingIntent());
            task.addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    PluginResult result = new  PluginResult(PluginResult.Status.OK); 
                    result.setKeepCallback(false);
                    callbackContext.sendPluginResult(result);
                }
            });
            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    PluginResult result = new  PluginResult(PluginResult.Status.ERROR, e.getMessage()); 
                    result.setKeepCallback(false);
                    callbackContext.sendPluginResult(result);
                }
            });
            PluginResult result = new  PluginResult(PluginResult.Status.NO_RESULT); 
            result.setKeepCallback(true);
            callbackContext.sendPluginResult(result);
            return true;
        }

        return false;
    }

    // private PendingIntent createServicePendingIntent() {
    // }

    private PendingIntent getServicePendingIntent() {
        Context context = cordova.getActivity().getApplicationContext();
        Intent intent = new Intent(context, MotionService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 629, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }
    
    static void onMotionActivity(DetectedActivity activity) {
        int activityType = activity.getType();
        switch (activityType) {
            case DetectedActivity.STILL:
            case DetectedActivity.WALKING:
            case DetectedActivity.RUNNING:
            case DetectedActivity.IN_VEHICLE:
                    if (Integer.compare(activityType, lastActivityType) != 0) {
                        lastActivityType = activityType;
                        sendJavascript("plugins.motion.MotionActivity.publish("+ activityType +")");
                    }
                break;
        }
    }

    private static synchronized void sendJavascript(final String js) {
        if (webView == null) {
            return;
        }

        final CordovaWebView view = webView.get();

        ((Activity)(view.getContext())).runOnUiThread(new Runnable() {
            public void run() {
                view.loadUrl("javascript:" + js);
            }
        });
    }
}
