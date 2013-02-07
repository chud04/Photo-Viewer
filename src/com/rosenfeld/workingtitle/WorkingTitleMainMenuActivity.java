package com.rosenfeld.workingtitle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WorkingTitleMainMenuActivity extends WorkingTitleActivity {
 
 Button sendMessageButton,viewMessageButton;

  /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        
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
    Intent intent = new Intent(getApplicationContext(), 
      WorkingTitleSelectMessageActivity.class);
       startActivity(intent);
    
   }
      
     });
     
     viewMessageButton.setOnClickListener(new OnClickListener(){

   @Override
   public void onClick(View v) {
    Intent intent = new Intent(getApplicationContext(), 
      WorkingTitleViewMessageActivity.class);
       startActivity(intent);
    
   }
      
     });
    }
}