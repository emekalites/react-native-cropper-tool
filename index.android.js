'use strict'

import { NativeModules } from 'react-native';
const { ImageCropperTool } = NativeModules;

const DEFAULT_OPTIONS = {
    guideLines: "on",
    cropShape: "rectangle",
    externalDirectoryName: "CropImage",
    autoZoomEnabled: false,
    fixAspectRatio: false,
    showCropOverlay: true,
    showProgressBar: false
};

export default {
    setImage: setImage = (options) => {
        if (typeof options === 'function') {
            options = {};
        }
        return new Promise((resolve, reject) => {
            ImageCropperTool.setImage({...DEFAULT_OPTIONS, ...options});
        });
    },
    cropImage: cropImage = () => {
        return new Promise((resolve, reject) => {
            ImageCropperTool.cropImage();
        });
    }
};
