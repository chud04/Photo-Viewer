package com.rosenfeld.workingtitle; 

import android.app.Activity;
import android.content.Intent;
import android.graphics.*;
import android.graphics.drawable.*;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class WorkingTitleSelectMessageActivity extends WorkingTitleActivity {

 int textSizeUnitIndex,size;
 boolean bold,italics,textChoice,picChoice;
 Bitmap picThumb;
 BitmapDrawable picThumbDrawable;
 String message;
 String[] textSizeUnit;
 EditText textMessage,textSize,textColor;
 TextView coloredText;
 ImageView imageThumb;
 Button browseButton,sendPageButton;
 RadioButton textChoiceButton,picChoiceButton;
 Spinner spinner;
 CheckBox italicsCheckBox,boldCheckBox;
 
  /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_message);
        
        //Instantiate the values that will be programmatically placed in the spinner 
        //selector
        textSizeUnit = new String[3];
  textSizeUnit[0] = "mm";
  textSizeUnit[1] = "in";
  textSizeUnit[2] = "px";
  bold = false;
  italics = false;
  textChoice = false;
  picChoice = false;
  picThumb = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
  picThumbDrawable = new BitmapDrawable(getResources(), picThumb);
  textMessage = (EditText) findViewById(R.id.textMessage);
  textSize = (EditText) findViewById(R.id.textSize);
  textColor = (EditText) findViewById(R.id.textColor);
  coloredText = (TextView) findViewById(R.id.coloredText);
  coloredText.setTypeface(Typeface.create(Typeface.SERIF,Typeface.NORMAL));
  //Create the TextView that will show the text color and style the user chose
  imageThumb = (ImageView) findViewById(R.id.imageThumb);
  browseButton = (Button) findViewById(R.id.browseButton);
  sendPageButton = (Button) findViewById(R.id.sendPageButton);
        textChoiceButton = (RadioButton) findViewById(R.id.textChoiceButton);
        picChoiceButton = (RadioButton) findViewById(R.id.picChoiceButton);
  spinner = (Spinner) findViewById(R.id.textSizeUnitSpinner);
  italicsCheckBox = (CheckBox) findViewById(R.id.italicsCheckBox);
  boldCheckBox = (CheckBox) findViewById(R.id.boldCheckBox);
  
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
     switch(requestCode){
      case SELECT_GALLERY_PIC_REQUEST:
       //When the activity is called for a result...
       if(resultCode == Activity.RESULT_CANCELED){
        //request was cancelled
       }else if(resultCode == Activity.RESULT_OK){
        //Get the Uri of the picture that was selected, then use a custom method
        //to turn that Uri into a String file path
        Uri selectedImageUri = data.getData(); 
     String selectedImagePath = getImagePath(selectedImageUri);
     Bitmap test = BitmapFactory.decodeFile(selectedImagePath);
     //Create a bitmap from that path, then shrink it down (maintaining the 
     //same ratio) to be a small thumbnail at the bottom of the screen
     double ratio = ((double)test.getHeight()) / test.getWidth();
     Bitmap galleryPic = Bitmap.createScaledBitmap(test, 150, 150*(int)ratio, true);
        picThumbDrawable = new BitmapDrawable(getResources(), galleryPic);
        imageThumb.setBackgroundDrawable(picThumbDrawable);
       }
       break;
      default:
       break;
     }
    }

 public void onPause(){
  super.onPause();
  //Unregister all the listeners to save memory
  spinner.setOnItemSelectedListener(null);
  browseButton.setOnClickListener(null);
  sendPageButton.setOnClickListener(null);
  textColor.setOnKeyListener(null);
  textMessage.setOnKeyListener(null);
  italicsCheckBox.setOnCheckedChangeListener(null);
  boldCheckBox.setOnCheckedChangeListener(null);
  textChoiceButton.setOnClickListener(null);
  picChoiceButton.setOnClickListener(null);
 }

 public void onResume(){
  super.onResume();
     spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
      
      //Register a listener for the spinner to keep track of which measuring unit is 
      //chosen for the text
   @Override
   public void onItemSelected(AdapterView<?> parent, View itemSelected,
     int selectedItemIndex, long selectedId){
    textSizeUnitIndex = selectedItemIndex;
   }

   @Override
   public void onNothingSelected(AdapterView<?> arg0) {
    // TODO Auto-generated method stub 
   }
  });
     browseButton.setOnClickListener(new OnClickListener(){
      //Launch an intent which allows the user to pick a photo from their photo 
      //gallery
      public void onClick(View v){
       Intent pickPhoto = new Intent(Intent.ACTION_GET_CONTENT);
       pickPhoto.setType("image/*");
       startActivityForResult(pickPhoto, SELECT_GALLERY_PIC_REQUEST);
      }
     });
     sendPageButton.setOnClickListener(new OnClickListener(){
      //Launch an intent which takes the user to the SelectRecipientActivity
      public void onClick(View v){
       Intent intent = new Intent(getApplicationContext(), 
         WorkingTitleSelectRecipientActivity.class);
       startActivity(intent);
      }
     });
     
     textColor.setOnKeyListener(new OnKeyListener(){

      //Set the TextView to pay attention to the italics and bold buttons and the 
      //color EditText, so that it can update itself accordingly when it should
   @Override
   public boolean onKey(View v, int keyCode, KeyEvent event) {
    if((event.getAction()==KeyEvent.ACTION_DOWN) && 
      (keyCode==KeyEvent.KEYCODE_ENTER)){
     boolean darkr = false;
     boolean darkg = false;
     boolean darkb = false;
     //This is just to check if the color of the text is in the darker or 
     //lighter half of the scale. Accordingly, the background changes from 
     //black to white and back to make the text more visible
     String color = textColor.getText().toString();
     for(int i=0; i<8; i++){
      if((color.charAt(0)+"").equals(i+"")) darkr = true;
      if((color.charAt(2)+"").equals(i+"")) darkg = true;
      if((color.charAt(4)+"").equals(i+"")) darkb = true;
     }
     coloredText.setTextColor(Color.parseColor("#"+color));
     if(darkr && darkg && darkb){
      coloredText.setBackgroundColor(Color.WHITE);
     }else{
      coloredText.setBackgroundColor(Color.BLACK);
     }
     return true;
    }
    return false;
   }
      
     });

     textMessage.setOnKeyListener(new OnKeyListener(){
      
      //This simply waits for the user to click ENTER, and then stores the message, to
      //be converted into a .png file
      //and sent later

   @Override
   public boolean onKey(View v, int keyCode, KeyEvent event) {
    if((event.getAction()==KeyEvent.ACTION_DOWN) && 
      (keyCode==KeyEvent.KEYCODE_ENTER)){
     message = textMessage.getText().toString();
     return true;
    }
    return false;
   }
      
     });

     italicsCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
      
      //This is just a listener to see if italics has been selected, to update the 
      //coloredText TextView. The same 
      //is done for the boldCheckBox below

   @Override
   public void onCheckedChanged(CompoundButton button, boolean isChecked) {
    if(isChecked){
     italics = true;
     if(bold){
      coloredText.setTypeface(Typeface.create(Typeface.SERIF,
        Typeface.BOLD_ITALIC));
     }else{
      coloredText.setTypeface(Typeface.create(Typeface.SERIF,
        Typeface.ITALIC));
     }
    }else{
     italics = false;
     if(bold){
      coloredText.setTypeface(Typeface.create(Typeface.SERIF,
        Typeface.BOLD));
     }else{
      coloredText.setTypeface(Typeface.create(Typeface.SERIF,
        Typeface.NORMAL));
     }
    }
   }
     });
     
     boldCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){

   @Override
   public void onCheckedChanged(CompoundButton button, boolean isChecked) {
    if(isChecked){
     bold = true;
     if(italics){
      coloredText.setTypeface(Typeface.create(Typeface.SERIF,
        Typeface.BOLD_ITALIC));
     }else{
      coloredText.setTypeface(Typeface.create(Typeface.SERIF,
        Typeface.BOLD));
     }
    }else{
     bold = false;
     if(italics){
      coloredText.setTypeface(Typeface.create(Typeface.SERIF,
        Typeface.ITALIC));
     }else{
      coloredText.setTypeface(Typeface.create(Typeface.SERIF,
        Typeface.NORMAL));
     }
    }
   }
   
     });
     
     //The two buttons below are RadioButtons, so when one is clicked, it is set to true,
     //and the other is set to false and unselected. Whichever is selected will determine
     //how exactly the user sends their message to a recipient
     textChoiceButton.setOnClickListener(new OnClickListener(){

   @Override
   public void onClick(View v) {
    textChoice = true;
    picChoice = false;
    textChoiceButton.setChecked(true);
    picChoiceButton.setChecked(false);
   }
      
     });
     
     picChoiceButton.setOnClickListener(new OnClickListener(){

   @Override
   public void onClick(View v) {
    picChoice = true;
    textChoice = false;
    picChoiceButton.setChecked(true);
    textChoiceButton.setChecked(false);
   }
      
     });
 }
}
