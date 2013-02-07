package com.rosenfeld.workingtitle;



import android.app.Activity;

import android.database.Cursor;

import android.net.Uri;

import android.os.Bundle;

import android.provider.MediaStore;

import android.util.DisplayMetrics;



public class WorkingTitleActivity extends Activity {
	

	//Main Activity that all the other activities extend; contains 
	
	//all the common static variables and methods

	

	static final int SELECT_GALLERY_PIC_REQUEST = 1;
	
	static final int SELECT_CONTACT_REQUEST = 2;
	
	static int phonePixW;
	
	static int phonePixH;
	
	static double phoneDensX,phoneDensY;
	
	 

	/** Called when the activity is first created. */
    
	@Override
    
	public void onCreate(Bundle savedInstanceState) {
        
		super.onCreate(savedInstanceState);
        
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
        
		phoneDensX = metrics.xdpi;
        
		phoneDensY = metrics.ydpi;
        
		phonePixW = metrics.widthPixels;
        
		phonePixH = metrics.heightPixels;
    
	}

    

	//Takes in the Uri (a form of memory location for android) and produces the
    
	//String path for that Uri
    

	public String getImagePath(Uri uri) { 
        
		String[] projection = { MediaStore.Images.Media.DATA }; 
        
		Cursor cursor = managedQuery(uri, projection, null, null, null); 
        
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA); 
        
		cursor.moveToFirst(); 
        
		return cursor.getString(column_index); 
    
	} 

}