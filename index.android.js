import React, { Component } from 'react';
import ReactNative, { View, requireNativeComponent, UIManager } from 'react-native';
import PropTypes from 'prop-types';

class ImageCropperTool extends Component {
	constructor(props, context) {
		super(props, context);
		this._onChange = this._onChange.bind(this);
	}

	_onChange(e) {
		if (!this.props.onCrop) {
		  return;
		}
		this.props.onCrop({
			uri: e.nativeEvent.uri,
			error: e.nativeEvent.error,
		});
	}

	cropImage = () => {
        UIManager.dispatchViewManagerCommand(
            ReactNative.findNodeHandle(this),
            UIManager.ImageCropperView.Commands.cropImage,
            [],
        );
    };
	
	render() {
		return (
			<ImageCropperView {...this.props} onChange={this._onChange} />
		);
	}
}

ImageCropperTool.propTypes = {
	...View.propTypes,
	source: PropTypes.string,
	folderName: PropTypes.string,
	onCrop: PropTypes.func,
};

var ImageCropperView = requireNativeComponent('ImageCropperView', ImageCropperTool, {
    nativeOnly: { onChange: true }
});

module.exports = ImageCropperTool;
