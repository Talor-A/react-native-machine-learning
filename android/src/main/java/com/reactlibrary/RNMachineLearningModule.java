
package com.reactlibrary;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;
import android.util.Log;
import android.net.Uri;
import java.util.List;
import java.util.Map;
import java.io.FileNotFoundException;
import java.util.HashMap;
import org.tensorflow.demo.*;

public class RNMachineLearningModule extends ReactContextBaseJavaModule {

  // Load tensorflow_interface native library
  static {
    System.loadLibrary("tensorflow_inference");
  }

  // Constants
  private static final String MODEL_FILE = "file:///android_asset/tensorflow_inception_graph.pb";
  private static final String LABEL_FILE = "file:///android_asset/imagenet_comp_graph_label_strings.txt";
  private static final int INPUT_SIZE = 224;
  private static final int IMAGE_MEAN = 117;
  private static final float IMAGE_STD = 1;
  private static final String INPUT_NAME = "input";
  private static final String OUTPUT_NAME = "output";

  // Create TensorFlowInferenceInterface
  private Classifier classifier;

  public RNMachineLearningModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public Map<String, Object> getConstants() {
    final Map<String, Object> constants = new HashMap<>();
    return constants;
  }

  @Override
  public String getName() {
    return "RNMachineLearning";
  }

  // Bridged methods

  @ReactMethod
  public void recognizeFile(String uri, Promise promise) {
    try {
      Bitmap bmp = BitmapFactory
          .decodeStream(getReactApplicationContext().getContentResolver().openInputStream(Uri.parse(uri)));
      promise.resolve(recognize(bmp));
    } catch (FileNotFoundException e) {
      promise.reject(e);
    }
  }

  @ReactMethod
  public void recognizeData(String base6data, Promise promise) {
    promise.reject(new Error("not implemented!"));
  }

  public Bitmap cropScaleBitmap(Bitmap bmp, int size) {
    float imageAspect = (float) bmp.getHeight() / bmp.getWidth();
    int scaledHeight, scaledWidth;
    Bitmap scaledBitmap, croppedBitmap;
    Log.d("RNML", "original size: " + bmp.getHeight() + "x" + bmp.getWidth());
    if (imageAspect > 1) { // is portrait
      scaledWidth = size;
      scaledHeight = Math.round(size * imageAspect);
      Log.d("RNML", "scaled size: " + scaledHeight + "x" + scaledWidth);
      scaledBitmap = Bitmap.createScaledBitmap(bmp, scaledHeight, scaledWidth, true);
      croppedBitmap = Bitmap.createBitmap(scaledBitmap, 0, (scaledBitmap.getHeight() - size) / 2, size, size);
    } else { // is landscape
      scaledHeight = size;
      scaledWidth = Math.round(size / imageAspect);
      Log.d("RNML", "scaled size: " + scaledHeight + "x" + scaledWidth);
      scaledBitmap = Bitmap.createScaledBitmap(bmp, scaledHeight, scaledWidth, true);
      croppedBitmap = Bitmap.createBitmap(scaledBitmap, Math.round((scaledBitmap.getWidth() - size) / 2), 0, size,
          size);
    }
    return croppedBitmap;
  }

  public WritableArray recognize(Bitmap bmp) {
    classifier = TensorFlowImageClassifier.create(getCurrentActivity().getAssets(), MODEL_FILE, LABEL_FILE, INPUT_SIZE,
        IMAGE_MEAN, IMAGE_STD, INPUT_NAME, OUTPUT_NAME);
    Bitmap croppedBitmap = cropScaleBitmap(bmp, INPUT_SIZE);
    final List<Classifier.Recognition> results = classifier.recognizeImage(croppedBitmap);

    return getResultsAsWritableArray(results);
  }

  //--------------------------------------------------------------
  private WritableArray getResultsAsWritableArray(List<Classifier.Recognition> results) {
    WritableArray array = new WritableNativeArray();
    for (Classifier.Recognition result : results) {
      WritableMap resultMap = new WritableNativeMap();
      resultMap.putDouble("confidence", result.getConfidence());
      resultMap.putString("title", result.getTitle());
      resultMap.putString("id", result.getId());
      array.pushMap(resultMap);
    }
    return array;
  }

}