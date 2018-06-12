package com.emekalites.rcropper.tool;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import android.widget.LinearLayout;

import com.emekalites.rcropper.library.CropImageView;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import java.io.File;

import static com.emekalites.rcropper.utils.MediaUtils.getImageUrl;
import static com.emekalites.rcropper.utils.MediaUtils.saveImageFromBitmap;

/**
 * Created by emnity on 6/12/18.
 */

public class CropperMainView extends LinearLayout implements CropImageView.OnSetImageUriCompleteListener, CropImageView.OnCropImageCompleteListener {

    private static final String TAG = CropperMainView.class.getSimpleName();

    private CropImageView mCropImageView;

    Activity mActivity;
    Context mContext;
    Uri uri;
    String folderName = "";

    private CropperMainView(Context context){
        super(context);
    }

    public CropperMainView(Context context, Activity activity) {
        super(context);
        mActivity = activity;
        mContext = context;
        Log.e(TAG, "Load CropperMainView");

        inflate(context, R.layout.crop_image_activity, this);

        int SDK_INT = Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        mCropImageView = (CropImageView) findViewById(R.id.cropImageMainView);
        mCropImageView.setOnSetImageUriCompleteListener(this);
        mCropImageView.setOnCropImageCompleteListener(this);
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public void setImageURI(String uri) {
        Uri source = getImageUrl(mContext, uri, this.folderName);
        Log.e(TAG, ""+source);
        if(source != null){
            this.uri = source;
            mCropImageView.setImageUriAsync(source);

            mCropImageView.setCropShape(CropImageView.CropShape.RECTANGLE);
            mCropImageView.setGuidelines(CropImageView.Guidelines.ON);
            mCropImageView.setFixedAspectRatio(false);
            mCropImageView.setMultiTouchEnabled(false);
            mCropImageView.setShowCropOverlay(true);
            mCropImageView.setShowProgressBar(false);
            mCropImageView.setAutoZoomEnabled(false);
        }
        else {
            sendEmiterEvent("", "could not load image");
        }
    }

    @Override
    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
        if (error == null) {
            Log.e(TAG, "image loaded: " + uri);
        }
        else {
            Log.e(TAG, "Failed to load image: " + error);
            sendEmiterEvent("", error.getMessage());
        }
    }

    public void cropImage() {
        Log.e(TAG, "Crop image");
        mCropImageView.getCroppedImageAsync();
    }

    @Override
    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
        if (result.getError() == null && result.isSuccessful()) {
            try {
                File image = saveImageFromBitmap(mContext, result.getBitmap(), this.folderName);
                Log.e(TAG, "image cropped: " + "file://" + image.getPath());
                sendEmiterEvent("file://" + image.getPath(), "");
            } catch (Exception e) {
                e.printStackTrace();
                sendEmiterEvent("", e.getMessage());
            }
            Log.e(TAG, "image cropped: " + result.getCropRect().toShortString());

        } else {
            Log.e(TAG, "Failed to crop image: " + result.getError());
            sendEmiterEvent("", result.getError().getMessage());
        }
    }

    private void sendEmiterEvent(String imageUri, String error) {
        WritableMap event = Arguments.createMap();
        event.putString("uri", imageUri);
        event.putString("error", error);
        ReactContext reactContext = (ReactContext) getContext();
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(),"topChange", event);
    }
}
