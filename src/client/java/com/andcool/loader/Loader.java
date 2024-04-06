package com.andcool.loader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.andcool.Main;
import org.json.JSONObject;

public class Loader {
    public static void download_file(String fileURL, String saveDir) throws IOException {
        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();

        String saveFilePath = saveDir + File.separator + "pepeland.zip";

        if (responseCode == HttpURLConnection.HTTP_OK) {

            InputStream inputStream = httpConn.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);

            int bytesRead;
            byte[] buffer = new byte[4096];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();

            Main.LOGGER.info("Resourcepack downloaded successfully.");
        } else {
            Main.LOGGER.error("No file to download. Server replied HTTP code: " + responseCode);
        }
        httpConn.disconnect();
    }

    public static JSONObject fetch() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://0.0.0.0:8088"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new JSONObject(response.body());
    }
}