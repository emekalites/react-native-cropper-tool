package com.emekalites.rn.cropper.tool;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import android.widget.LinearLayout;

import com.emekalites.rn.cropper.library.CropImage;
import com.emekalites.rn.cropper.library.CropImageView;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import java.io.File;

import static com.emekalites.rn.cropper.utils.MediaUtils.getImageUrl;
import static com.emekalites.rn.cropper.utils.MediaUtils.saveImageFromBitmap;

/**
 * Created by emnity on 3/10/18.
 */

class CropperView extends LinearLayout implements CropImageView.OnSetImageUriCompleteListener, CropImageView.OnCropImageCompleteListener {

    private static final String TAG = CropperView.class.getSimpleName();
    private CropImageView mCropImageView;
    private Context mContext;
    private String imageUrl = "";
    private String folderName = "CropTool";

    public CropperView(Context context) {
        super(context);
        mContext = context;
        inflate(getContext(), R.layout.activity_main, this);

        int SDK_INT = Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        mCropImageView = (CropImageView) findViewById(R.id.cropImageView);
        mCropImageView.setOnSetImageUriCompleteListener(this);
        mCropImageView.setOnCropImageCompleteListener(this);
        setCurrentCropViewOptions();
        Uri uri = getImageUrl(context, imageUrl, folderName);
        mCropImageView.setImageUriAsync(uri);
    }

    public void setCurrentCropViewOptions() {
        mCropImageView.setCropShape(CropImageView.CropShape.RECTANGLE);
        mCropImageView.setGuidelines(CropImageView.Guidelines.ON);
        mCropImageView.setFixedAspectRatio(false);
        mCropImageView.setMultiTouchEnabled(false);
        mCropImageView.setShowCropOverlay(true);
        mCropImageView.setShowProgressBar(false);
        mCropImageView.setAutoZoomEnabled(false);
    }

    @Override
    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
        if (error == null) {
            Log.e(TAG, "image loaded");
        }
        else {
            Log.e(TAG, "Failed to load image by URI: ", error);
        }
    }

    @Override
    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
        if (result.getError() == null) {
            if (result.getUri() != null) {
                sendEmmiterEvent(result.getUri());

            } else {
                Bitmap mImage = mCropImageView.getCropShape() == CropImageView.CropShape.OVAL
                                ? CropImage.toOvalBitmap(result.getBitmap())
                                : result.getBitmap();
                File file = null;
                try {
                    file = saveImageFromBitmap(mContext, mImage, folderName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(file != null){
                    sendEmmiterEvent(Uri.fromFile(file));
                }
            }

        } else {
            Log.e(TAG, "Failed to crop image", result.getError());
        }
    }

    private void sendEmmiterEvent(Uri uri) {
        WritableMap event = Arguments.createMap();
        event.putString("uri", uri.toString());
        ReactContext reactContext = (ReactContext) getContext();
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(),"topChange",event);
    }

    public void cropImage() {
        mCropImageView.getCroppedImageAsync();
    }

    public void setImageUrl(String url) {
        imageUrl = url;
    }

    public void setFolderName(String folder) {
        folderName = folder;
    }
}
