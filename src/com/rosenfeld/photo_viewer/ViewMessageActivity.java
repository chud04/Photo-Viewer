package com.rosenfeld.photo_viewer;

import android.graphics.*;
import android.graphics.drawable.*;
import android.hardware.*;
import android.os.Bundle;
import android.widget.*;

public class ViewMessageActivity extends MainActivity implements 
SensorEventListener{
 
 //Declare all the Views, etc. that can be instantiated with the ids from the .xml file
 SensorManager sm;
 Sensor mags,accels;
 ImageViewerActivity pictureView;
 Bitmap bitmapsrc,bitmapview;
 BitmapDrawable bitmapdrawable;
 Rect rect;
 TextView scrollXText,scrollYText,phoneW,phoneH,magX,magY;
 float[] rotate,rotX,rotY,inc,gData,mgData,orientX,orientY;
 int picWidth,picHeight;
 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.view_message); (This is only used when testing, the 
        //actual code uses setContentView(pictureView)
        
        //Set the content view FIRST, so the other instantiations don't return null and 
        //throw errors when you call methods on them
        
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        mags = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        accels = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //Set the pictureView, which shows bitmapdrawable of the sent message, to get its 
        //data from bitmapdrawable
        picWidth = 1500;
        picHeight = 1500;
        //This is just a temporary width and height; in reality, these values would be the 
        //saved values from the received bitmap file
        rect = new Rect(0,0,phonePixW,phonePixH);
        bitmapsrc = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
          R.drawable.ic_launcher),picWidth,picHeight,false);
        pictureView = new ImageViewerActivity(this, bitmapsrc);
        /*scrollXText = (TextView) findViewById(R.id.scrollXText);
        scrollYText = (TextView) findViewById(R.id.scrollYText);
        phoneW = (TextView) findViewById(R.id.phoneW);
        phoneH = (TextView) findViewById(R.id.phoneH);
        magX = (TextView) findViewById(R.id.magX);
        magY = (TextView) findViewById(R.id.magY);*/
        //Again, this is all just for testing purposes. The actual view contains no TextViews
        
        //Create a bitmap from the saved picture, the rectangle merely defines where in the
        //picture the android's screen is located, to render the correct image
        rotate = new float[9];
        rotX = new float[9];
        rotY = new float[9];
        orientX = new float[3];
        orientY = new float[3];
        setContentView(pictureView);
    }
 public void onAccuracyChanged(Sensor arg0, int arg1) {
  // TODO Auto-generated method stub
  
 }
 public void onSensorChanged(SensorEvent event) {
   switch(event.sensor.getType()){
   case Sensor.TYPE_ACCELEROMETER:
    //In this case, get the acceleration data, subtract the negate X and Y 
    //values, and then add the remaining acceleration (multiplied by the time)
    //to the velocity, as a form of continuous integration
    gData = event.values;
    break;
   case Sensor.TYPE_MAGNETIC_FIELD:
    //This takes in the magnetic field data and uses it to find the phone's 
    //rotation matrix from the Earth's identity matrix
    mgData = event.values;
    break;
   default:
    break;
  }
  if(mgData != null && gData != null){
   SensorManager.getRotationMatrix(rotate,inc,gData,mgData);
   SensorManager.remapCoordinateSystem(rotate, SensorManager.AXIS_Y, SensorManager.AXIS_Z, rotX);
   SensorManager.remapCoordinateSystem(rotate, SensorManager.AXIS_X, SensorManager.AXIS_Z, rotY);
   //Remap the coordinate system twice, because I want different values from each one
   SensorManager.getOrientation(rotX, orientX);
   SensorManager.getOrientation(rotY, orientY);
   //Use the values to get the phone's orientation with regards to the Earth's coordinate system
   pictureView.redraw(orientX,orientY);
   //Call the custom method, which updates the pictureView ImageView, and then calls invalidate()
   //so that the onDraw() method is called and the view is drawn anew
   
   /*scrollXText.setText(pictureView.getX() + "");
   scrollYText.setText(pictureView.getY() + "");
   phoneW.setText(phonePixW + "");
   phoneH.setText(phonePixH + "");
   magX.setText(pictureView.getMagX() + "");
   magY.setText(pictureView.getMagY() + "");*/
   //More testing code, I am only keeping it in because I want to keep workiing on this project
  }
 }
 
 public void onPause(){
  super.onPause();
  //Unregister the View listeners, so they don't use too much memory in the background
  sm.unregisterListener(this);
 }
 
 public void onResume(){
  super.onResume();
  //Using a fast update on the sensors is imperative, as otherwise the phone might not be able to
  //keep up with the user's movements, and this will make it go out of sync
  if(accels != null) sm.registerListener(this, accels, SensorManager.SENSOR_DELAY_FASTEST);
  if(mags != null) sm.registerListener(this, mags, SensorManager.SENSOR_DELAY_FASTEST);
 }
}