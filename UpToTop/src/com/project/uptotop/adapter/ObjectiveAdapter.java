/**
 * 
 */
package com.project.uptotop.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.uptotop.R;
import com.project.uptotop.model.ObjectiveModel;
import com.project.uptotop.utils.AppUtils;

/**
 * @author alexey.kvitko
 *
 */
public class ObjectiveAdapter extends BaseAdapter{
	
	private LayoutInflater inflater;
	private List<ObjectiveModel> objectives;
	
	public ObjectiveAdapter(Context context, List<ObjectiveModel> objectives, WindowManager manager){
		super();
		this.inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		this.objectives = objectives;
	}

	@Override
	public int getCount() {
		return objectives.size();
	}

	@Override
	public Object getItem( int idx ) {
		return objectives.get( idx );
	}

	@Override
	public long getItemId( int idx ) {
		Long id = Long.getLong( objectives.get( idx ).getObjId().toString() );
		return id;
	}

	@Override
	public View getView(int idx, View convertView, ViewGroup parent) {
		View view = inflater.inflate(R.layout.obj_row, null);
		ImageView image = (ImageView) view.findViewById(R.id.listIconId);
		Bitmap decodedBmp = AppUtils.decodeScaledBitmapFromSdCard(objectives.get( idx ).getImagePath(),
				80, 90, 320);
		Bitmap scaledBmp = AppUtils.rotateBitmap(decodedBmp, 90);
		Bitmap rotBmp = AppUtils.roundCornerImage(scaledBmp, 5);
		image.setImageBitmap(rotBmp);
		
		TextView txtName = (TextView) view.findViewById(R.id.objectiveNameId);
		txtName.setText( objectives.get( idx ).getName() );
		
		TextView txtDesc = (TextView) view.findViewById(R.id.objectiveDescId);
		txtDesc.setText( objectives.get( idx ).getDescription() );
		
		TextView txtPrice = (TextView) view.findViewById(R.id.objectivePriceId);
		txtPrice.setText( objectives.get( idx ).getPrice().toString() );
		
		TextView txtStatus = (TextView) view.findViewById(R.id.objectiveStatusId);
		txtStatus.setText( R.string.objective_status );
		
		return view;
	}
	

}
