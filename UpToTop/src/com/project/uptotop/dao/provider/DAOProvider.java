/**
 * 
 */
package com.project.uptotop.dao.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.project.uptotop.dao.DAOSqls;
import com.project.uptotop.dao.UpToTopDBHelper;
import com.project.uptotop.utils.AppUtils;

/**
 * @author alexey.kvitko
 *
 */
public class DAOProvider extends ContentProvider implements DAOSqls{
	
	private String table= null;
	private SQLiteDatabase db;
	private Uri contentUri;
	
	public DAOProvider(){
	}
	
	@Override
	public boolean onCreate() {
		this.db = new UpToTopDBHelper( getContext() ).getWritableDatabase();
		return ( db== null ) ? false : true;
	}

	@Override
	public int delete(Uri url, String selection, String[] selectionArgs) {
		table = AppUtils.getTableNameFromUrl( url.toString() );
		int retVal = db.delete(table, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(url, null);
		return retVal;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cursor query(Uri uri, String[] columns, String selection,
			String[] selectionArgs, String sortOrder) {
		String orderBy = sortOrder;
		
		if ( TextUtils.isEmpty( sortOrder ) ){
			orderBy = UpToTopDBHelper.ID;
		} 
		table = AppUtils.getTableNameFromUrl( uri.toString() );
		Cursor cur = db.query(table, columns, selection, selectionArgs, null, null, orderBy);
		cur.setNotificationUri( getContext().getContentResolver(), uri);
		
		return cur;
	}
	
	@Override
	public Uri insert(Uri url, ContentValues values) {
		ContentValues cntValues = new ContentValues( values );
		Uri uri = url;
		table = AppUtils.getTableNameFromUrl( url.toString() );
		long rowId = db.insert(table,UpToTopDBHelper.USERS_FIELD_AVATAR,cntValues);
		if ( rowId > 0 ){
			uri = ContentUris.withAppendedId(getContentUri(table), rowId);
			try{
				getContext().getContentResolver().notifyChange(uri, null);
			}catch (Exception e){
				Log.i("INSERT", e.getMessage());
			}
			
		} else {
			return null;
		}
		return uri;
		
	}
	
	@Override
	public int update(Uri url, ContentValues values, String selection, String[] selectionArgs) {
		table = AppUtils.getTableNameFromUrl( url.toString() );
		int retVal = db.update(table, values, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(url, null);
		return retVal;
	}

	public String getTable() {
		return table;
	}

	public SQLiteDatabase getDb() {
		return db;
	}

	public Uri getContentUri(String table) {
		this.contentUri = Uri.parse( PROVIDER_CONTENT+PROVIDER_URI+this.table );
		return contentUri;
	}

}
