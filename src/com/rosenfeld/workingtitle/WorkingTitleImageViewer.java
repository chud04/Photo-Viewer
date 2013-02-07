package com.rosenfeld.workingtitle;

import android.content.Context;
import android.graphics.*;
import android.view.*;

public class WorkingTitleImageViewer extends View {

 Bitmap toDisplay;
 Rect viewRect,scrollRect;
 int phonePixW,phonePixH,scrollX,scrollY;
 double ratio;
 float prevMagX,prevMagY;
 
 public WorkingTitleImageViewer(Context context, Bitmap src) {
  super(context);
  phonePixH = WorkingTitleActivity.phonePixH;
  phonePixW = WorkingTitleActivity.phonePixW;
  toDisplay = src;
  viewRect = new Rect(0,0,phonePixW,phonePixH);
  scrollRect = new Rect(viewRect);
  scrollX = (toDisplay.getWidth() - phonePixW) / 2;
  scrollY = (toDisplay.getHeight() - phonePixH) / 2;
  //scrollX and scrollY start in the middle of the image so that there is an even distance to
  //travel to both the left and right
  ratio = toDisplay.getWidth() / Math.PI;
  prevMagX = 0;
  prevMagY = 0;
 }
 
 public void onDraw(Canvas canvas){
  if(scrollX < 0) scrollX = 0;
  if(scrollY < 0) scrollY = 0;
  if(scrollX + phonePixW > toDisplay.getWidth()) scrollX = toDisplay.getWidth() - phonePixW;
  if(scrollY + phonePixH > toDisplay.getHeight()) scrollY = toDisplay.getHeight() - phonePixH;
  scrollRect.set(scrollX, scrollY, scrollX+phonePixW, scrollY+phonePixH);
  //Relocate the rectangle that contains the image to be drawn/shown
  canvas.drawBitmap(toDisplay, scrollRect, viewRect, new Paint());
  //Draw on the canvas whatever is contained by the newly relocated rectangle
 }
 
 public void redraw(float[] orientX, float[] orientY){
  /*double epsilon1 = 0.15;
  if(Math.abs(prevMagX - orientX[0]) < epsilon1) prevMagX = orientX[0];
  if(Math.abs(prevMagY - orientY[1]) < epsilon1) prevMagY = orientY[1];*/
  prevMagX = orientX[0];
  prevMagY = orientY[1];
  scrollX = (int) (toDisplay.getWidth() / 2 + prevMagX * ratio);
  scrollY = (int) (toDisplay.getHeight() / 2 * (prevMagY + 1));
  //Set the X and Y values for correct reorientation of the rectangle
  invalidate();
  //This is just called to make sure that the android redraws the image, and calls onDraw()
 }
 
 public float getX(){
  return scrollX;
 }
 
 public float getY(){
  return scrollY;
 }
 
 public float getMagX(){
  return prevMagX;
 }
 
 public float getMagY(){
  return prevMagY;
 }
 //These are just some return methods for the sole purpose of testing/debugging
 
}