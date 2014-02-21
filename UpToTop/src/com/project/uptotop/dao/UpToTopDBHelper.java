/**
 * 
 */
package com.project.uptotop.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**`
 * @author alexey.kvitko
 *
 */
public class UpToTopDBHelper extends SQLiteOpenHelper implements DAOSqls{
	

	public UpToTopDBHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL( CREATE_TABLE_USER_PROFILE );
		db.execSQL( CREATE_TABLE_OBJECTIVE );
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
