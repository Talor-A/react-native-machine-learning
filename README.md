# react-native-machine-learning

allows the use of common machine learning frameworks in native mobile environments, bridged to React Native JS engine

## Overview
this module allows react native apps on android to use tensorflow mobile (iOS coming soon). import a compiled model for image classification, regression, etc. and use the methods documented below to interface with it.


## Getting started

`$ npm install react-native-machine-learning --save`

### automatic installation

`$ react-native link react-native-machine-learning`

### Manual installation


#### iOS

iOS coming soon!
<!--
1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-machine-learning` and add `RNMachineLearning.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNMachineLearning.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<
-->

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNMachineLearningPackage;` to the imports at the top of the file
  - Add `new RNMachineLearningPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-machine-learning'
  	project(':react-native-machine-learning').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-machine-learning/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-machine-learning')
		```


## Usage

*note: example requires react-native-image-picker module to be installed.*

```javascript
import React, { Component } from 'react'
import { AppRegistry, Text, Button, ScrollView, Image } from 'react-native'
import RNML from 'react-native-machine-learning'
import ImagePicker from 'react-native-image-picker'

const clamp = (num, min, max) => Math.min(Math.max(num, min), max)

const getImage = (options = {}) =>
  new Promise((resolve, reject) => {
    ImagePicker.launchImageLibrary(options, response => {
      console.log('Response = ', response)

      if (response.didCancel) {
        reject(new Error('cancelled'))
      } else if (response.error) {
        reject(response.error)
      } else {
        // You can also display the image using data:
        // let source = { uri: 'data:image/jpeg;base64,' + response.data };
        resolve(response.uri)
      }
    })
  })
const getFromCamera = (options = {}) =>
  new Promise((resolve, reject) => {
    ImagePicker.launchCamera(options, response => {
      console.log('Response = ', response)

      if (response.didCancel) {
        reject(new Error('cancelled'))
      } else if (response.error) {
        reject(response.error)
      } else {
        // You can also display the image using data:
        // let source = { uri: 'data:image/jpeg;base64,' + response.data };
        resolve(response.uri)
      }
    })
  })
export default class MLtest extends Component {
  constructor (props) {
    super(props)
    this.runImage = this.runImage.bind(this)
    this.runCamera = this.runCamera.bind(this)
    this.state = { results: [] }
  }
  runImage () {
    getImage()
      .then(uri => {
        this.setState({ photo: uri })
        return RNML.recognizeFile(uri)
      })
      .then(results => this.setState({ results }))
      .catch(e => console.warn(e))
  }
  runCamera () {
    getFromCamera()
      .then(uri => {
        this.setState({ photo: uri })
        return RNML.recognizeFile(uri)
      })
      .then(results => this.setState({ results }))
      .catch(e => console.warn(e))
  }
  render () {
    return (
      <ScrollView style={{ flex: 1 }}>
        <Image
          source={{ uri: this.state.photo }}
          style={{ width: '100%', aspectRatio: 1 }}
        />
        {this.state.results.map(result => (
          <Text
            key={result.id + result.confidence}
            style={{
              fontSize: clamp(Math.round(result.confidence * 100), 16, 100),
              fontWeight: 'bold'
            }}>
            {result.title} ({Math.round(result.confidence * 100)}%)
          </Text>
        ))}
        <Button onPress={this.runImage} title={'pick from gallery'} />
        <Button onPress={this.runCamera} title={'take photo'} />
      </ScrollView>
    )
  }
}

AppRegistry.registerComponent('MLtest', () => MLtest)


```
