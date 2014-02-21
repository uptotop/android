package com.project.uptotop.activity;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.project.uptotop.AppConstants;
import com.project.uptotop.CalcConstants;
import com.project.uptotop.R;
import com.project.uptotop.dao.DAOSqls;
import com.project.uptotop.model.UserModel;

public class LoginActivity extends BaseActivity  implements DAOSqls,AppConstants,
View.OnClickListener{
	
	private EditText userLogin;
	private EditText userPassword;
	
	private Button submitBtn;
	private Button cancelBtn;
	private CheckBox checkBox;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_activity);
		
		userLogin = (EditText) findViewById( R.id.enterLoginId );
		userPassword = (EditText) findViewById( R.id.enterPasswordId );
		
		submitBtn = ( Button ) findViewById( R.id.btnLoginId );
		cancelBtn = ( Button ) findViewById (R.id.btnCancelLoginId );
		checkBox = ( CheckBox ) findViewById( R.id.showCheckBoxId );
		
		submitBtn.setOnClickListener( this );
		cancelBtn.setOnClickListener( this );
		
		setUserLogin();
	}
	
	private void setUserLogin(){
		userLogin.setText( getUser().getUserLogin() );
	}

	@Override
	public void onClick(View btn) {
		UserModel user = getUser();
		switch (btn.getId()) {
		case R.id.btnLoginId:
			if ( user.getUserPassword().equals( userPassword.getText().toString().trim() ) ){
				if ( !checkBox.isChecked() ){
					ContentValues values = new ContentValues(4);
					values.put( USERS_FIELD_LOGIN, user.getUserLogin() );
					values.put( USERS_FIELD_PASSWORD, user.getUserPassword() );
					values.put( USERS_FIELD_AVATAR, user.getAvatar() );
					values.put( USERS_FIELD_LOCATION, user.getLocation() );
					values.put( USER_FIELD_SHOW_STARTUP, 0);
					resolver.update(CalcConstants.TABLE_USER_URI, values, ID+"="+ user.getUserId(), null);
				}
				setResult( RESULT_OK );
				finish();
			} else {
				Toast.makeText(getApplicationContext(),R.string.wrongPassword, Toast.LENGTH_LONG).show();
				userPassword.requestFocus();
			}
			break;
		case R.id.btnCancelLoginId:
			setResult( RESULT_CANCELED );
			finish();
			break;
				default:
			break;
		}
	}

}
