/**
 * 
 */
package com.project.uptotop.activity;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.project.uptotop.AppConstants;
import com.project.uptotop.CalcConstants;
import com.project.uptotop.R;
import com.project.uptotop.utils.AppUtils;

/**
 * @author alexey.kvitko
 *
 */
public class CreateObjectiveActivity extends BaseActivity  implements View.OnClickListener,AppConstants{
	
	protected static final String TAG = CreateObjectiveActivity.class.getSimpleName();
	
	
	private static final int ALL_COMPONENT_HEIGHT = 280;
	
	private static final int MDPI = 160;
	private static final int HDPI = 240;
	private static final int XHDPI = 320;

	
	private String  imagePath;
	private ImageView previewImage;
	
	
	private EditText objName;
	private EditText objDescription;
	private EditText objPrice;
	
	private Button cancelBtn;
	private Button sendBtn;
	

	@SuppressWarnings("deprecation")
	@Override
	 public void onCreate(Bundle savedInstanceState){
		 super.onCreate( savedInstanceState );
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		 setContentView( R.layout.preview);
		 overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
		 previewImage = (ImageView) findViewById(R.id.previewImageId);
		 Bundle extras = getIntent().getExtras();
		 WindowManager manager = (WindowManager) getSystemService( Context.WINDOW_SERVICE );
		 
		 Display display = manager.getDefaultDisplay();
		 DisplayMetrics metrics = getResources().getDisplayMetrics();
		 Integer density = metrics.densityDpi;
		 
		 int calcHeight = (ALL_COMPONENT_HEIGHT*density)/MDPI;
		 Integer displayWidth = display.getWidth();
		 Integer displayHeight = display.getHeight() - calcHeight;
		 double scale = ( double ) display.getWidth()/display.getHeight(); 
		
		 
		 Integer height =  (int) ( displayHeight );
		 Integer width =  (int) ( height*scale );
		 LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,height);
		 previewImage.setLayoutParams(params);
		 
		 objName = ( EditText ) findViewById( R.id.objNameId );
		 objDescription = ( EditText ) findViewById( R.id.objDescriptionId );
		 objPrice = ( EditText ) findViewById( R.id.objPriceId );
		 
		 objName.setOnClickListener(this);
		 objDescription.setOnClickListener(this);
		 objPrice.setOnClickListener(this);
		 
	
		 imagePath = extras.getString( UpToTopMainActivity.ABS_PATH );
		 Bitmap decodedBmp = AppUtils.decodeScaledBitmapFromSdCard( imagePath,width,height,density );
		 Bitmap scaledBmp = AppUtils.rotateBitmap( decodedBmp,90 );
		 Bitmap highLight =AppUtils.highlightImage( scaledBmp, 10);
		 Bitmap rotBmp = AppUtils.roundCornerImage( highLight,40 );
		 		 
		 previewImage.setImageBitmap( rotBmp );
		 
		 cancelBtn =  ( Button ) findViewById( R.id.cancelId );
		 cancelBtn.setOnClickListener( this );
		 
		 sendBtn =  ( Button ) findViewById( R.id.sendId );
		 sendBtn.setOnClickListener( this );
	 }
	
	@Override
	protected void onResume() {
		super.onResume();
	}


	@Override
	public void onClick(View btn) {
		switch ( btn.getId() ){
		
		case R.id.cancelId : {
			this.finish();
			break;
		}
		case R.id.sendId : {
			sendObjective();
			break;
		}
		}
	}
	
	private void sendObjective(){
		String emptyString = null;
		EditText  editText = null;
		if ( objName.getText().toString().trim().length() == 0 ){
			emptyString = this.getString(R.string.msg_req)+this.getString(R.string.name)+this.getString(R.string.msg_empty);
			editText = objName;
		} else if ( objDescription.getText().toString().trim().length() == 0 ){
			emptyString = this.getString(R.string.msg_req)+this.getString(R.string.description)+this.getString(R.string.msg_empty);
			editText = objDescription;
		} else if ( objPrice.getText().toString().trim().length() == 0 ){
			emptyString = this.getString(R.string.msg_req)+this.getString(R.string.price)+this.getString(R.string.msg_empty);
			editText = objPrice;
		} 
		if ( emptyString != null ){
			Toast toast = Toast.makeText(getApplicationContext(), emptyString, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
			toast.show();
			editText.requestFocus();
		//	getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		}
		saveObjective();
	}
	
	@Override
    protected void onPause() {
        overridePendingTransition(R.anim.hold, R.anim.pull_out_to_left);
        super.onPause();
    }
	
	private void saveObjective(){
		
		ContentValues values = new ContentValues(4);
		
		String objNameTxt = objName.getText().toString();
		String objDescTxt = objDescription.getText().toString();
		Double objPriceDbl = 0.0;
		if ( objPrice.getText().toString().trim().length() > 0 ){
			objPriceDbl = Double.valueOf( objPrice.getText().toString() );	
		}
		values.put( OBJ_IMAGE_PATH, imagePath );
		values.put( OBJ_NAME, objNameTxt );
		values.put( OBJ_DESCRIPTION, objDescTxt );
		values.put( OBJ_PRICE, objPriceDbl );
		
		resolver.insert( CalcConstants.TABLE_OBJ_URI, values);	
		
		Toast.makeText(getApplicationContext(),R.string.success, Toast.LENGTH_LONG).show();
		this.finish();
	}


}
