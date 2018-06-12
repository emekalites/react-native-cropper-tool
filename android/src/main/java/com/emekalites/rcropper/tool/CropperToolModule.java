package com.emekalites.rcropper.tool;

import android.app.Activity;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

/**
 * Created by emnity on 3/10/18.
 */

class CropperToolModule extends ReactContextBaseJavaModule {

    public CropperToolModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "ImageCropperModule";
    }

    public Activity getActivity() {
        return this.getCurrentActivity();
    }
}