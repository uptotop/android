package com.project.uptotop.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;

import com.project.uptotop.AppConstants;
import com.project.uptotop.CalcConstants;
import com.project.uptotop.dao.DAOSqls;
import com.project.uptotop.model.UserModel;

public class BaseActivity extends Activity implements DAOSqls,AppConstants{
	
	protected ContentResolver resolver;
	protected Cursor userCursor;
	protected Cursor objectiveCursor;
	private UserModel user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		resolver = getContentResolver();
		userCursor = resolver.query( CalcConstants.TABLE_USER_URI, CalcConstants.USER_TABLE_FIELDS, null,null,null );
	}
	
	
	public UserModel getUser() {
		user = null;
		userCursor = resolver.query( CalcConstants.TABLE_USER_URI, CalcConstants.USER_TABLE_FIELDS, null,null,null );
		if ( userCursor.getCount() != 0 ){
            user = new UserModel();			
			while ( userCursor.moveToNext() ){
				user.setUserId( userCursor.getInt( 0 ) );
				user.setUserLogin( userCursor.getString( 1 ) );
				user.setUserPassword( userCursor.getString( 2 ) );
				user.setAvatar( userCursor.getString( 3 ) );
				user.setLocation( userCursor.getString( 4 ) );
				user.setShowStartup( userCursor.getInt( 5 ) );
			}
		}	
		return user;
	}

}
