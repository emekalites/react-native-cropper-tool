package com.emekalites.rn.cropper.tool;

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
        return "ImageCropperView";
    }
}
