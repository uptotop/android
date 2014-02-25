/**
 * 
 */
package com.project.uptotop.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;

import com.project.uptotop.AppConstants;
import com.project.uptotop.CalcConstants;
import com.project.uptotop.dao.DAOSqls;
import com.project.uptotop.model.ObjectiveModel;

/**
 * @author alexey.kvitko
 *
 */
public class BaseListActivity extends ListActivity implements DAOSqls,AppConstants{
	
	protected ContentResolver resolver;
	protected Cursor objectiveCursor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		resolver = getContentResolver();
	}
	
	protected List<ObjectiveModel> getMyObjectives(){
		List<ObjectiveModel> objectives = new ArrayList<ObjectiveModel>();
		objectiveCursor = resolver.query( CalcConstants.TABLE_OBJ_URI, CalcConstants.OBJ_TABLE_FIELDS, null,null,null );
		while( objectiveCursor.moveToNext() ){
			ObjectiveModel  model = new ObjectiveModel();
			model.setObjId( objectiveCursor.getInt( 0 ) );
			model.setImagePath( objectiveCursor.getString( 1 ) );
			model.setName( objectiveCursor.getString( 2 ) );
			model.setDescription( objectiveCursor.getString( 3 ) );
			model.setPrice( objectiveCursor.getDouble( 4 ) );
			objectives.add( model );
		}
		return objectives;
	}
	

}
