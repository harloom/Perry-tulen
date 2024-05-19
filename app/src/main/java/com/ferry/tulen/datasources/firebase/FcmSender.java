package com.ferry.tulen.datasources.firebase;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FcmSender {

    private static final String SERVER_KEY = "YOUR_SERVER_KEY";
    private static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";

    public static void sendNotification(String registrationToken, String title, String body) throws IOException {
        URL url = new URL(FCM_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "key=" + SERVER_KEY);

        String payload = String.format("{\"to\": \"%s\", \"notification\": {\"title\": \"%s\", \"body\": \"%s\"}}",
                registrationToken, title, body);

        connection.setDoOutput(true);
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(payload.getBytes());
        outputStream.flush();
        outputStream.close();

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            System.out.println("Notification sent successfully.");
        } else {
            System.out.println("Error sending notification: " + connection.getResponseMessage());
        }
    }
}