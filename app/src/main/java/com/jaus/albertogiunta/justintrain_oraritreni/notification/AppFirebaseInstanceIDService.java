package com.jaus.albertogiunta.justintrain_oraritreni.notification;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import trikita.log.Log;

public class AppFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private final static String TAG = "FCM Token";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        // Implement this method to send any registration to app's server.
    }
}