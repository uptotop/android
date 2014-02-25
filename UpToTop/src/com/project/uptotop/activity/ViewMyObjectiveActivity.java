/**
 * 
 */
package com.project.uptotop.activity;

import android.os.Bundle;
import android.view.Window;

import com.project.uptotop.R;
import com.project.uptotop.adapter.ObjectiveAdapter;

/**
 * @author alexey.kvitko
 *
 */
public class ViewMyObjectiveActivity extends BaseListActivity{
	
	@Override
	protected void onCreate( Bundle savedInstanceState ){
		super.onCreate( savedInstanceState );
		requestWindowFeature(Window.FEATURE_ACTION_MODE_OVERLAY);
		setContentView(R.layout.view_my_obj);
		setTitle( R.string.my_objectives );
		setListAdapter(new ObjectiveAdapter( getApplicationContext(), getMyObjectives(), null ) );
	}

}
