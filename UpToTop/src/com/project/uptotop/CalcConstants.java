package com.project.uptotop;

import android.net.Uri;

import com.project.uptotop.dao.DAOSqls;

public class CalcConstants implements DAOSqls,AppConstants{

	public static final String[] USER_TABLE_FIELDS = new String[] { ID,USERS_FIELD_LOGIN,USERS_FIELD_PASSWORD,
																   USERS_FIELD_AVATAR,USERS_FIELD_LOCATION,
																   USER_FIELD_SHOW_STARTUP };
	
	public static final String[] OBJ_TABLE_FIELDS = new String[] { ID,OBJ_IMAGE_PATH,OBJ_NAME,OBJ_DESCRIPTION,OBJ_PRICE };
	
	public static final Uri TABLE_USER_URI=Uri.parse( PROVIDER_CONTENT+PROVIDER_URI+TABLE_USER_NAME );
		   
	public static final Uri TABLE_OBJ_URI=Uri.parse( PROVIDER_CONTENT+PROVIDER_URI+TABLE_OBJECTIVE  );
	
}
