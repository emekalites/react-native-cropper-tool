package com.emekalites.rcropper.tool;

import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.infer.annotation.Assertions;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

/**
 * Created by emnity on 6/12/18.
 */

public class CropperViewManager extends ViewGroupManager<CropperMainView> {

    private static final String TAG = CropperMainView.class.getSimpleName();
    public static final String PROPS_IMAGE_URI="source";
    public static final String PROPS_FOLDER_NAME="folderName";

    public static final int COMMAND_CROP_IMAGE = 1;

    private CropperToolModule mContextModule;

    public CropperViewManager(ReactApplicationContext context) {
        mContextModule = new CropperToolModule(context);
    }

    @Override
    public String getName() {
        return "ImageCropperView";
    }

    @Override
    public CropperMainView createViewInstance(ThemedReactContext context) {
        Log.d(TAG, "React View manager createViewInstance:");
        return new CropperMainView(context, mContextModule.getActivity());
    }

    @ReactProp(name = PROPS_IMAGE_URI)
    public void setUrl(CropperMainView view, String imageURI) {
        Log.e(TAG, "setImageUri: " + imageURI);
        if(view != null) {
            view.setImageURI(imageURI);
        }
    }

    @ReactProp(name = PROPS_FOLDER_NAME)
    public void setFolderName(CropperMainView view, String folderName) {
        Log.e(TAG, "setFolderName: " + folderName);
        if(view != null) {
            view.setFolderName(folderName);
        }
    }

    @Override
    public Map<String,Integer> getCommandsMap() {
        Log.e(TAG,"View manager getCommandsMap:");
        return MapBuilder.of("cropImage", COMMAND_CROP_IMAGE);
    }

    @Override
    public void receiveCommand(CropperMainView view, int commandType, @Nullable ReadableArray args) {
        Assertions.assertNotNull(view);
        Assertions.assertNotNull(args);
        switch (commandType) {
            case COMMAND_CROP_IMAGE: {
                Log.e(TAG,"do crop image");
                view.cropImage();
                return;
            }

            default:
                throw new IllegalArgumentException(String.format(
                        "Unsupported command %d received by %s.",
                        commandType,
                        getClass().getSimpleName()));
        }
    }
}
