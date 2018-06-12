# React Native Cropper Tool
[![npm version](https://badge.fury.io/js/react-native-cropper-tool.svg)](https://badge.fury.io/js/react-native-cropper-tool)
[![npm downloads](https://img.shields.io/npm/dt/react-native-cropper-tool.svg)](https://badge.fury.io/js/react-native-cropper-tool)

React Native Cropper Tool for Android

**NOTE: The iOS side of this module will be included when i have figured it out.**

## Installation

First you need to install react-native-cropper-tool:

```sh
npm install react-native-cropper-tool --save
or 
yarn add react-native-cropper-tool --save
```

Second you need to link react-native-cropper-tool:

```sh
react-native link react-native-cropper-tool
```

Use above `react-native link` command to automatically complete the installation, or link manually like so:

### Android

Add these lines in your file: android/settings.gradle

```gradle
...

include ':react-native-cropper-tool'
project(':react-native-cropper-tool').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-cropper-tool/android')
```

Add line in your file: android/app/build.gradle

```gradle
...

dependencies {
    ...
    compile project(':react-native-cropper-tool') // <-- add this line
}
```

Add import and line in your file: android/app/src/main/java/<...>/MainApplication.java

```java
...

import com.emekalites.rcropper.tool.CropperToolPackage; // <-- add this import

public class MainApplication extends Application implements ReactApplication {

    private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {

        @Override
        protected List<ReactPackage> getPackages() {
            return Arrays.<ReactPackage>asList(
                new MainReactPackage(),
                new CropperToolPackage() // <-- add this line
            );
        }
  }

...
}
```

## Usage

Then you can use CropperToool component in your react-native's App, like this:
```javascript
...
import React, { Component } from 'react';
import ImageCropperTool from 'react-native-cropper-tool';

class CustomComponent extends Component {

  ...
  render() {
    return (
      <ImageCropperTool
        {...someProps}
      />
    );
  }
}
```

### Properties

+ **source** : image url

+ **folderName** : Name of folder where images will be stored could be the name of your application.

### Methods

+ **cropImage()** : when called it will crop the image, save the image and return the file path of the cropped image on onCrop() callback

### Callback Props
+ **onCrop** : Triggered when cropImage() is called and also if there is any exception.

### Callback Response
+ **Object** : returns an object after the onCrop callback. { uri: 'cropped image path or empty string', error: 'error if available or empty string' }

### Example

```javascript

import React, { Component } from 'react';
import { View, TouchableHighlight, Text, StyleSheet, PermissionsAndroid, Dimensions, Image } from 'react-native';
import ImageCropperTool from 'react-native-cropper-tool';

const APP_NAME = "Image Cropper";
const { width } = Dimensions.get('window');
const uri = "http://www.hippieshop.com/mas_assets/adrotator/599.jpg";

class CropperToolExample extends Component {
	constructor(props, context) {
		super(props, context);
		this.state = {
			permitted: false,
			h: 0,
			w: 0,
		};
	}
	
	requestStoragePermission = async () => {
		let p1, p2 = false;

        try {
            const grantedRead = await PermissionsAndroid.request(
                PermissionsAndroid.PERMISSIONS.READ_EXTERNAL_STORAGE,
                {
                    'title': `${APP_NAME} Read Storage Permission`,
                    'message': `${APP_NAME} needs access to read your storage.`
                }
            )
            p1 = grantedRead === PermissionsAndroid.RESULTS.GRANTED;
        } catch (err) {
            console.warn(err);
        }

        try {
            const grantedWrite = await PermissionsAndroid.request(
                PermissionsAndroid.PERMISSIONS.WRITE_EXTERNAL_STORAGE,
                {
                    'title': `${APP_NAME} Write Storage Permission`,
                    'message': `${APP_NAME} needs access to write to your storage.`
                }
            )
            p2 = grantedWrite === PermissionsAndroid.RESULTS.GRANTED;
        } catch (err) {
            console.warn(err);
		}
		
		if(p1 && p2){
			this.setState({ permitted: true });
		}
	}
	
	componentWillMount() {
		this.requestStoragePermission();
		Image.getSize(uri, (wt, ht) => {
			const aspectRatio = width / wt;
			const h = aspectRatio * ht;
			this.setState({ w: width, h });
		});
	}
	
    cropImage = () => this.crop.cropImage();

    _onCrop = (result) => {
		//result.uri - for the file path uri
        //result.error - for the eror message
		alert(JSON.stringify(result));
		this.setState({ cropImage: result.uri });
    }
	
	render() {
		const { permitted, h, w, cropImage } = this.state;
		return (
			permitted ? 
				<View style={{ flex: 1 }}>
					<View style={{width: w, height: h}}>
						<ImageCropperTool
							ref={ref => {
								this.crop = ref;
							}}
							source={uri}
							folderName="Cropper"
							onCrop={this._onCrop}
						/>
					</View>
					<View style={{padding: 8}}>
						<TouchableHighlight style={styles.buttonStyle} onPress={() => this.cropImage()}>
							<Text>Crop Image</Text>
						</TouchableHighlight>
					</View>
					<View>
						<Image
							resizeMode={'contain'}
							source={{uri: cropImage}}
							style={{width: 200, height: 200}}
						/>
					</View>
				</View> : 
				<View style={{ flex: 1 }}>
					<Text>Waiting for permission</Text>
				</View>
		);
	}
}

const styles = StyleSheet.create({
    buttonStyle: {
        alignItems: "center", height: 50,
        backgroundColor: "#eeeeee",
        margin: 10
    }
});

AppRegistry.registerComponent('CropperToolExample', () => CropperToolExample);
```

-------------

Library used:

https://github.com/ArthurHub/Android-Image-Cropper