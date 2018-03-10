package com.emekalites.rn.cropper.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by emnity on 3/9/18.
 */

public class MediaUtils {
    private static final String TAG = MediaUtils.class.getSimpleName();

    public static File saveImageFromBitmap(Context context, Bitmap bitmap, String folderName) throws Exception{

        FileOutputStream outStream = null;
        File dir = null;
        if (folderName != null && !folderName.isEmpty()) {
            File sdCard = Environment.getExternalStorageDirectory();
            dir = new File(sdCard.getAbsolutePath() + "/" + folderName);
        } else {
            dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "");
        }
        dir.mkdirs();
        String fileName = String.format("%d.jpg", System.currentTimeMillis());
        File outFile = new File(dir, fileName);
        outStream = new FileOutputStream(outFile);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
        outStream.flush();
        outStream.close();

        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(outFile));
        context.sendBroadcast(intent);

        return outFile;
    }

    public static File saveImageFromUri(Context context, Uri uri, String folderName) throws Exception{

        File originalFile = new File(uri.getPath());
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);

        File outFile = saveImageFromBitmap(context, bitmap, folderName);

        //Delete original file
        if (outFile.exists() && originalFile.exists()) {
            originalFile.delete();
        }

        return outFile;
    }

    public static Uri getImageUrl(Context context, String url, String folderName) {
        try {
            URL mUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) mUrl.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bmp = BitmapFactory.decodeStream(input);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

            Uri uri = null;
            try {
                File file = saveImageFromBitmap(context, bmp, folderName);
                if(file != null){
                    uri = Uri.fromFile(file);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return uri;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
