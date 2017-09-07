
# react-native-machine-learning

## Getting started

`$ npm install react-native-machine-learning --save`

### Mostly automatic installation

`$ react-native link react-native-machine-learning`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-machine-learning` and add `RNMachineLearning.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNMachineLearning.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

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
```javascript
import RNMachineLearning from 'react-native-machine-learning';

// TODO: What to do with the module?
RNMachineLearning;
```
  # react-native-machine-learning
