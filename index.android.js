'use strict'

import { NativeModules } from 'react-native';
const { ImageCropperTool } = NativeModules;

const DEFAULT_OPTIONS = {};

module.exports = {
    ...ImageCropperTool,
    selectImage:function selectImage(options, callback){
        if (typeof options === 'function') {
            callback = options;
            options = {};
        }
        return ImageCropperTool.selectImage({...DEFAULT_OPTIONS, ...options}, callback);
    }
};
