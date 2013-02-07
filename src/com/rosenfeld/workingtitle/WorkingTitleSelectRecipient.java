package com.rosenfeld.workingtitle;

import android.app.*;
import android.content.*;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

public class WorkingTitleSelectRecipientActivity extends WorkingTitleActivity {

 EditText phoneNumber;
 Button chooseContact,sendMessageButton;
 ImageView contactImage;
 String number;
 int numColumn,idColumn;
 
  /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.select_recipient);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        chooseContact = (Button) findViewById(R.id.chooseContact);
        sendMessageButton = (Button) findViewById(R.id.sendMessageButton);
        contactImage = (ImageView) findViewById(R.id.contactImage);
    }
    
    
    //This method is used to tell the phone what to do after an Intent that was called for a
    //result finishes (what to do with the data it returned, that is)
 protected void onActivityResult(int requestCode, int resultCode, Intent data){
     switch(requestCode){
      case SELECT_CONTACT_REQUEST:
       if(resultCode == Activity.RESULT_CANCELED){
        //request was cancelled
       }else if(resultCode == Activity.RESULT_OK){
        //set contact text field to show name, and imageview to show the 
        //contact's corresponding thumbnail
        Cursor c = getContentResolver().query(Phone.CONTENT_URI, new String[] {Phone._ID,Phone.NUMBER}, null,
          null, null);
        //Use the cursor to get the phone number stored at the given contact's location URI,
        //then set the TextView to show that number (it is still editable, however)
        number = getColumnData(c,data.getData().getLastPathSegment());
        phoneNumber.setText(number);
     /*if(id.equals(data.getData().getLastPathSegment())){
      number = c.getString(c.getColumnIndex(ContactsContract.PhoneLookup.NUMBER));
      if(number != null) phoneNumber.setText(number);
     }*/
        
       }
       break;
      default:
       break;
     }
    }
 
 //This is a custom method that takes a cursor and uses it efficiently to search
 //through the values returned for a location containing a certain ID, then returns
 //the desired data at that location
 private String getColumnData(Cursor c, String reqID){
  numColumn = c.getColumnIndex(Phone.NUMBER);
  idColumn = c.getColumnIndex(Phone._ID);
  String id;
  c.moveToFirst();
  id = c.getString(idColumn);
  while(!(id.equals(reqID))){
   c.moveToNext();
   id = c.getString(idColumn);
  }
  return c.getString(numColumn);
 }
    public void onPause(){
     super.onPause();
     //Unregister all the listeners, to save memory
     chooseContact.setOnClickListener(null);
     sendMessageButton.setOnClickListener(null);
     phoneNumber.setOnClickListener(null);
    }
    
    public void onResume(){
     super.onResume();
     chooseContact.setOnClickListener(new OnClickListener(){
      
      //When this button is clicked, show the custom dialog to choose a contact

   @Override
   public void onClick(View v) {
    Intent pickContact = new Intent(Intent.ACTION_GET_CONTENT);
       pickContact.setType("vnd.android.cursor.item/phone");
       startActivityForResult(pickContact, SELECT_CONTACT_REQUEST);
   };
     });
     
     sendMessageButton.setOnClickListener(new OnClickListener(){

      //This is just a toast for now, but it should send the message to the contact 
      //that was chosen, provide a status message (successful or unsuccessful), then 
      //return to the main menu
      
   @Override
   public void onClick(View v) {
    Toast.makeText(WorkingTitleSelectRecipientActivity.this,
         "TODO: Send Message and Create Progress Dialog, then" +
         "return to Main Menu", 
         Toast.LENGTH_LONG).show();
   }

     });
     //More time is needed to complete this, as a method for sending encoded bitmaps must
     //be created
     
     phoneNumber.setOnKeyListener(new OnKeyListener(){
      
      //This EditText holds the phone number of the contact to send the message to. 
      //When the ENTER key is pressed, it stores the number input in a String variable
      //to use later

   @Override
   public boolean onKey(View v, int keyCode, KeyEvent event) {
    if((event.getAction()==KeyEvent.ACTION_DOWN) && 
      (keyCode==KeyEvent.KEYCODE_ENTER)){
     number = phoneNumber.getText().toString();
     return true;
    }
    return false;
   }
      
     });
    }
}