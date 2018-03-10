import React, { Component } from 'react';
import PropTypes from 'prop-types';
import ReactNative from 'react-native';
import { NativeModules, requireNativeComponent, View, DeviceEventEmitter, UIManager } from 'react-native';

// const ImageCropperToolNM = NativeModules.ImageCropperView;

class ImageCropperTool extends Component {
    constructor(props, context) {
        super(props, context);
        this.onChange = this.onChange.bind(this);
        this.subscriptions = [];
    }

    onChange(event) {
        if(event.nativeEvent.pathName){
            if (!this.props.onSaveEvent) {
                return;
            }
            this.props.onSaveEvent(event.nativeEvent);
        }
    }
    
    componentDidMount() {
        if (this.props.onSaveEvent) {
            let sub = DeviceEventEmitter.addListener(
                'onSaveEvent',
                this.props.onSaveEvent
            );
            this.subscriptions.push(sub);
        }
    }
    componentWillUnmount() {
        this.subscriptions.forEach(sub => sub.remove());
        this.subscriptions = [];
    }

    cropImage() {
        UIManager.dispatchViewManagerCommand(
            ReactNative.findNodeHandle(this),
            UIManager.RSSignatureView.Commands.cropImage,
            [],
        );
    }
    
    render() {
        if (!props.url) {
            throw new Error(
                'The <ImageCropperTool> component needs an image url.',
            )
        }
        return (
            <ImageCropperView {...props} onChange={this.onChange} />
        );
    }
}

ImageCropperTool.propTypes = {
    ...View.propTypes,
    url: PropTypes.string,
    folderName: PropTypes.string,
};

const ImageCropperView = requireNativeComponent('ImageCropperView', ImageCropperTool, {
	nativeOnly: { 
		onChange: true,
	},
});

// ImageCropperTool.cropImage = () => {
//     return new Promise((resolve, reject) => {
//         ImageCropperToolNM.cropImage(resolve, reject);
//     });
// }

export default ImageCropperTool;