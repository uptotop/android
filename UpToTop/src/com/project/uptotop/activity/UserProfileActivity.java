/**
 * 
 */
package com.project.uptotop.activity;

import java.io.IOException;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.uptotop.CalcConstants;
import com.project.uptotop.R;
import com.project.uptotop.utils.AppUtils;

/**
 * @author alexey.kvitko
 * 
 */
public class UserProfileActivity extends BaseActivity implements 
		View.OnClickListener {

	private static final int PICK_IMAGE = 1;
	
	private static final int AVATAR_WIDTH = 128;
	private static final int AVATAR_HEIGHT = 128;
	
	private Integer densityDPI;

	private ImageView avatar;
	private String avatarPath;
	
	private EditText userLogin;
	private EditText userOldPassword;
	private EditText userPassword;
	private EditText userConfirmPassword;
	private EditText userLocation;
	private TextView phoneTextView;
	
	private String oldPassword;

	private Button cancelBtn;
	private Button saveBtn;

	private Bitmap bitmap;
	
	private Integer userId = null;
	
	private LocationListener locationListener = new LocationListener() {
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
		@Override
		public void onProviderEnabled(String provider) {
		}
		@Override
		public void onProviderDisabled(String provider) {
		}
		@Override
		public void onLocationChanged(Location location) {
		}
	}; 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.user_config);
		
		overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
		
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		densityDPI = metrics.densityDpi;
		
		LocationManager locationManager = ( LocationManager ) getSystemService( Context.LOCATION_SERVICE );
		//locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, locationListener);
		Location loc = locationManager.getLastKnownLocation( LocationManager.GPS_PROVIDER );
		
		Geocoder geocoder = new Geocoder( getApplicationContext() );

		avatar = (ImageView) findViewById(R.id.avatarImageId);
		cancelBtn = (Button) findViewById(R.id.userProfileCancelId);
		saveBtn = (Button) findViewById(R.id.userProfileSaveId);
		
		userLogin = (EditText) findViewById( R.id.userLoginId );
		userOldPassword = (EditText) findViewById( R.id.oldPasswordId );
		userPassword = (EditText) findViewById( R.id.newPasswordId );
		userConfirmPassword = (EditText) findViewById( R.id.confirmPasswordId );
		userPassword = (EditText) findViewById( R.id.newPasswordId );
		userLocation = (EditText) findViewById( R.id.locationId);
		
		phoneTextView = (TextView) findViewById ( R.id.phoneTextId );
		
		setOwnPhoneNumber();
		
		isUserExist();
		if ( loc == null ){
			Toast.makeText(getApplicationContext(), R.string.gps_disable, Toast.LENGTH_LONG ).show();
		}
		if ( loc != null && userLocation.getText().toString().trim().length() == 0 ){
			List<Address> addresses = null;
			try {
				addresses = geocoder.getFromLocation(loc.getLatitude(),loc.getLongitude(), 1);
				userLocation.setText(addresses.get(0).getCountryName()
									+", "+addresses.get(0).getAdminArea()+", "
									+addresses.get(0).getLocality());
			} catch (IOException e) {
				Toast.makeText(getApplicationContext(), R.string.gps_disable, Toast.LENGTH_LONG ).show();
			}
			
		}
		
		cancelBtn.setOnClickListener(this);
		saveBtn.setOnClickListener(this);
		avatar.setOnClickListener(this);
	}
	
	private void setOwnPhoneNumber(){
		TelephonyManager mTelephonyMgr  = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE); 
		phoneTextView.setText( mTelephonyMgr.getSimSerialNumber() );
	}
	
	private void isUserExist(){
		if ( getUser() != null ){
				userId = getUser().getUserId();
				userLogin.setText( getUser().getUserLogin() );
				oldPassword = getUser().getUserPassword();
				avatarPath = getUser().getAvatar();
				userLocation.setText( getUser().getLocation() );
				setAvatar();
			
		} else {
			userOldPassword.setEnabled( false );
			userOldPassword.setFocusable( false );
			userId = null;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		overridePendingTransition(R.anim.hold, R.anim.pull_out_to_left);
		super.onPause();
	}

	@Override
	public void onClick(View btn) {
		switch (btn.getId()) {
		
		case R.id.userProfileSaveId:
			this.saveUserProfile();
			break;
		case R.id.userProfileCancelId:
			this.finish();
			break;
		case R.id.avatarImageId:
			startPickupActivivty();
			break;
		default:
			break;
		}
	}
	
	private void saveUserProfile(){
		ContentValues values = new ContentValues(5);
		String login = userLogin.getText().toString();
		String oldPswd = userOldPassword.getText().toString();
		String newPswd = userPassword.getText().toString();
		String confirmPswd = userConfirmPassword.getText().toString();
		if(  login.trim().length()  < 4 ){
			Toast.makeText(getApplicationContext(),R.string.loginLenShort, Toast.LENGTH_LONG).show();
			userLogin.requestFocus();
			return;
		}
		if ( userId != null  && !oldPassword.equals(oldPswd) ){
			Toast.makeText(getApplicationContext(),R.string.wrongPassword, Toast.LENGTH_LONG).show();
			userOldPassword.requestFocus();
			return;
		}  
		if ( newPswd.length() < PASSWORD_LENGTH ){
			Toast.makeText(getApplicationContext(),R.string.passwordLenShort, Toast.LENGTH_LONG).show();
			userPassword.requestFocus();
			return;
		}
		if ( !newPswd.equals( confirmPswd ) ){
			Toast.makeText(getApplicationContext(),R.string.passwordNotEqual, Toast.LENGTH_LONG).show();
			userConfirmPassword.requestFocus();
			return;
		}
		values.put( USERS_FIELD_LOGIN, login );
		values.put( USERS_FIELD_PASSWORD, newPswd );
		values.put( USERS_FIELD_AVATAR, avatarPath );
		values.put( USERS_FIELD_LOCATION, userLocation.getText().toString() );
		values.put( USER_FIELD_SHOW_STARTUP, 1);
		if ( userId == null ){
			resolver.insert( CalcConstants.TABLE_USER_URI, values);	
		} else {
			resolver.update(CalcConstants.TABLE_USER_URI, values, ID+"="+ userId, null);
		}
		
		userCursor.requery();
		Toast.makeText(getApplicationContext(),R.string.success, Toast.LENGTH_LONG).show();
		this.finish();
	}

	private void startPickupActivivty() {
		try {
			Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent, PICK_IMAGE);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					getString(R.string.pick_exctp), Toast.LENGTH_LONG).show();
			Log.e(e.getClass().getName(), e.getMessage(), e);
		}
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
 
            Cursor imageCursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            imageCursor.moveToFirst();
 
            int columnIndex = imageCursor.getColumnIndex(filePathColumn[0]);
            avatarPath = imageCursor.getString(columnIndex);
            imageCursor.close();
            setAvatar();
         
        }
	}
	
	private void setAvatar(){
		if ( avatarPath == null ){
			return;
		}
		Bitmap decodedBmp = AppUtils.decodeScaledBitmapFromSdCard( avatarPath,AVATAR_WIDTH,AVATAR_HEIGHT,densityDPI ); 
        Bitmap scaledBmp = decodedBmp;
        if ( decodedBmp.getWidth() >=  decodedBmp.getHeight() ){
        	scaledBmp = AppUtils.rotateBitmap( decodedBmp,90 );
        }
		    Bitmap highLight =AppUtils.highlightImage( scaledBmp,5 );
		    bitmap = AppUtils.roundCornerImage( highLight,10 );
        avatar.setImageBitmap( bitmap );
	}
	
	


}
