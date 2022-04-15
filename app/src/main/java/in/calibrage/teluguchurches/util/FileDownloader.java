package in.calibrage.teluguchurches.util;

import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static in.calibrage.teluguchurches.util.CommonUtil.showProgressNotification;

/*
* class is created to download file's
* */

public class FileDownloader {
    private static final int MEGABYTE = 1024 * 1024;
    public static int percentageValue;


    public static void downloadFile(String fileUrl, File directory, int fileSize, Context mContext, String name) {
        try {

            URL url = new URL(fileUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(directory);
            int totalSize = urlConnection.getContentLength();

            fileSize = totalSize;

            long total = 0;

            byte[] buffer = new byte[MEGABYTE];
            int bufferLength = 0;


            while ((bufferLength = inputStream.read(buffer)) > 0) {
                total += bufferLength;
                percentageValue = (int) ((total * 100) / totalSize);
                fileOutputStream.write(buffer, 0, bufferLength);
                CommonUtil.isShowNotifiction = false;
                showProgressNotification(mContext, 1, name, "File downloading");
            }

            fileOutputStream.close();
            CommonUtil.removeProgressNotification(mContext, 1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
