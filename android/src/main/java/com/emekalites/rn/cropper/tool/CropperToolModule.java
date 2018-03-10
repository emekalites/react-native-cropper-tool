package com.emekalites.rn.cropper.tool;

public class CropperToolModule extends ReactContextBaseJavaModule {

    private final static String TAG = ANModule.class.getCanonicalName();

    public CropperToolModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "ImageCropperTool";
    }

    @ReactMethod
    public void setImage(final ReadableMap options, final Callback successCb, final Callback failureCb) {
    }

    @ReactMethod
    public void cropImage(final Callback successCb, final Callback failureCb) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }
}
