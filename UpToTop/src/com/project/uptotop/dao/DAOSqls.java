/**
 * 
 */
package com.project.uptotop.dao;


/**
 * @author alexey.kvitko
 *
 */
public interface DAOSqls {
	
	String DATABASE_NAME="uptotop.db";
	
	// TABLE USER PROFILE
	String TABLE_USER_NAME="userprofile";
	String ID = "_id";
	String USERS_FIELD_LOGIN="login";
	String USERS_FIELD_PASSWORD="password";
	String USERS_FIELD_AVATAR="avatar";
	String USERS_FIELD_LOCATION="location";
	String USER_FIELD_SHOW_STARTUP="show_startup";
	
	//TABLE OBJECTIVE
	String TABLE_OBJECTIVE="objective";
	String OBJ_ID = "_id";
	String OBJ_IMAGE_PATH="image_path";
	String OBJ_NAME ="obj_name";
	String OBJ_DESCRIPTION ="obj_desc";
	String OBJ_PRICE ="obj_price";
	
	 
	
	// CONTENT PROVIDER
    String PROVIDER_CONTENT="content://";
	String PROVIDER_URI="com.project.uptotop.dao.provider/uptotop/";
		
	String CREATE_TABLE_USER_PROFILE = " CREATE TABLE "+TABLE_USER_NAME+" ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "+
								USERS_FIELD_LOGIN+" TEXT, "+ USERS_FIELD_PASSWORD+" TEXT, "+USERS_FIELD_AVATAR+" TEXT, "
										+ USERS_FIELD_LOCATION+" TEXT, "+USER_FIELD_SHOW_STARTUP+" INT);";
	String CREATE_TABLE_OBJECTIVE = " CREATE TABLE "+TABLE_OBJECTIVE+" ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "+
			OBJ_IMAGE_PATH+" TEXT, "+ OBJ_NAME+" TEXT, "+OBJ_DESCRIPTION+" TEXT, "+OBJ_PRICE+" DECIMAL(10,2) );";
	
}

