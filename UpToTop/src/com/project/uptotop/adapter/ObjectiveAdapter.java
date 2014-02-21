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
		Long id = Long.getLong( objectives.get( idx ).getObjId() );
		return id;
	}

	@Override
	public View getView(int idx, View convertView, ViewGroup parent) {
		View view = inflater.inflate(R.layout.obj_row, null);
		ImageView image = (ImageView) view.findViewById(R.id.listIconId);
		Bitmap decodedBmp = AppUtils.decodeScaledBitmapFromSdCard(objectives.get( idx ).getImagePath(),
				90, 100, 300);
		Bitmap scaledBmp = AppUtils.rotateBitmap(decodedBmp, 90);
		Bitmap highLight = AppUtils.highlightImage(scaledBmp, 10);
		Bitmap rotBmp = AppUtils.roundCornerImage(highLight, 40);
		image.setImageBitmap(rotBmp);
		
		TextView txtView = (TextView) view.findViewById(R.id.objectiveTextId);
		txtView.setText( objectives.get( idx ).getDescription() );
		return view;
	}
	

}
