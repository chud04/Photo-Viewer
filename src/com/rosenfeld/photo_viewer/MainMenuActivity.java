package com.rosenfeld.photo_viewer;

import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainMenuActivity extends MainActivity {

  /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        sendMessageButton = (Button) findViewById(R.id.sendMessageButton);
        viewMessageButton = (Button) findViewById(R.id.viewMessageButton);
        //The two buttons on the main menu
    }
    
    public void onPause(){
     super.onPause();
     sendMessageButton.setOnClickListener(null);
     viewMessageButton.setOnClickListener(null);
    }
    
    public void onResume(){
     super.onResume();
     //These two statements just reregister the two buttons with their listeners
     sendMessageButton.setOnClickListener(new OnClickListener(){

   @Override
   public void onClick(View v) {
    Intent intent = new Intent(getApplicationContext(), SelectMessageActivity.class);
       startActivity(intent);
    
   }
      
     });
     
     viewMessageButton.setOnClickListener(new OnClickListener(){

   @Override
   public void onClick(View v) {
    Intent intent = new Intent(getApplicationContext(), ViewMessageActivity.class);
       startActivity(intent);
    
   }
      
     });
    }
    
    //Takes in the Uri (a form of memory location for android) and produces the
    //String path for that Uri
    public String getImagePath(Uri uri) { 
        String[] projection = { MediaStore.Images.Media.DATA }; 
        Cursor cursor = new CursorLoader(null, uri, projection, null, null, null).loadInBackground(); 
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA); 
        cursor.moveToFirst(); 
        return cursor.getString(column_index); 
    } 
}