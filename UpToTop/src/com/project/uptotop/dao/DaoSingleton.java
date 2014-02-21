/**
 * 
 */
package com.project.uptotop.dao;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

import com.project.uptotop.CalcConstants;

/**
 * @author alexey.kvitko
 *
 */
public class DaoSingleton {
	
	private static DaoSingleton instance;
	
	private ContentResolver resolver;
	private Cursor userCursor;
	
	private DaoSingleton(Context context ){
		this.resolver = context.getContentResolver();
		this.userCursor = this.resolver.query( CalcConstants.TABLE_USER_URI, CalcConstants.USER_TABLE_FIELDS, null,null,null );
	}
	
	
	public static synchronized DaoSingleton getInstance(Context context){
		if ( instance == null ){
			instance = new DaoSingleton( context );
		}
		return instance;
	}
	
	public ContentResolver getResolver() {
		return resolver;
	}


	public Cursor getUserCursor() {
		return userCursor;
	}

}
