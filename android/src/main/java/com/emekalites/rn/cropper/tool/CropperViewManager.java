package com.emekalites.rn.cropper.tool;

import android.util.Log;
import android.view.View;

import com.facebook.infer.annotation.Assertions;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

import javax.annotation.Nullable;

/**
 * Created by emnity on 3/10/18.
 */

class CropperViewManager extends SimpleViewManager<CropperView> {

    public static final int COMMAND_CROP_IMAGE = 1;

    @Override
    public String getName() {
        return "ImageCropperView";
    }

    @Override
    protected CropperView createViewInstance(ThemedReactContext context) {
        return new CropperView(context);
    }

    @ReactProp(name = "url")
    public void setUrl(CropperView view, String url) {
        view.setImageUrl(url);
    }

    @ReactProp(name = "folderName")
    public void setFolderName(CropperView view, String folderName) {
        view.setFolderName(folderName);
    }

    @Override
    public Map<String,Integer> getCommandsMap() {
        Log.d("React"," View manager getCommandsMap:");
        return MapBuilder.of("cropImage", COMMAND_CROP_IMAGE);
    }

    @Override
    public void receiveCommand(CropperView view, int commandType, @Nullable ReadableArray args) {
        super.receiveCommand(view, commandType, args);
        Assertions.assertNotNull(view);
        Assertions.assertNotNull(args);
        switch (commandType) {
            case COMMAND_CROP_IMAGE: {
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
