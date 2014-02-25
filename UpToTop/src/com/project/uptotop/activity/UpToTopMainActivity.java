package com.project.uptotop.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import com.project.uptotop.AppConstants;
import com.project.uptotop.R;
import com.project.uptotop.dao.UpToTopDBHelper;
import com.project.uptotop.utils.BitmapWarehouse;

public class UpToTopMainActivity extends BaseActivity implements SurfaceHolder.Callback, View.OnClickListener, AppConstants{
	
	@SuppressLint("SimpleDateFormat")
	final SimpleDateFormat FORMAT= new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	
	private static final int ACTION_LOGIN = 203;
	
	private static BitmapWarehouse BMP_WRH = BitmapWarehouse.getInstance();
	
	private final String ORIENTATION="orientation";
	private final String LANDSCAPE = "landscape";

	public static final String ABS_PATH ="path";
	public static final String DPI ="densityDPI";
	
	private ImageButton btnPreview;
	private ImageButton btnTake;
	private ImageButton btnClose;
	private ImageButton btnFocus;
	
	
	private SurfaceView srfView;
	private SurfaceHolder srfHolder;
	private AutoFocusCallback autoFocus;
//	private AutoFocusMoveCallback moveFocus;
	
	private Camera camera;
	
	private String imagePath;
	
	private Integer densityDPI;
	
	private SQLiteDatabase dataBase;	
	
	private ShutterCallback shutter;
	private PictureCallback rawPicture;
	private PictureCallback jpgPicture;
	
	private LayoutInflater mainInflanter;
	private LayoutParams mainParams;
	
	public UpToTopMainActivity(){
		super();
		
		this.shutter = new ShutterCallback() {
			@Override
			public void onShutter() {
			}
		};
		
		this.rawPicture = new PictureCallback() {
			@Override
			public void onPictureTaken(byte[] data, Camera camera) {
			}
		};
		
		this.jpgPicture = new PictureCallback() {
			
			@Override
			public void onPictureTaken(byte[] data, Camera camera) {
				btnPreview.setEnabled( false );
		        File imagesFolder = new File(Environment.getExternalStorageDirectory(), IMAGE_DIR);
		        if ( !imagesFolder.exists() ){
		        	imagesFolder.mkdirs();	
		        }
		        Date date = new Date();
		        File image = new File(imagesFolder, "image_"+FORMAT.format( date )+".jpg");
		        imagePath = image.getAbsolutePath(); 
		        try {
		            FileOutputStream fos = new FileOutputStream( image );
		            fos.write(data);
		            fos.close();
		        } catch (FileNotFoundException e) {
		            Log.d("MyCameraApp", R.string.file_not_found + e.getMessage());
		        } catch (IOException e) {
		            Log.d("MyCameraApp", R.string.file_accsess_error + e.getMessage());
		        }
		        data = null;
		        DisplayMetrics metrics = getResources().getDisplayMetrics();
				densityDPI = metrics.densityDpi;
				BMP_WRH.setImgPath( imagePath );
				viewCreateObjActivity();
			}
		};
		
		autoFocus = new AutoFocusCallback() {
			@Override
			public void onAutoFocus(boolean success, Camera camera) {
				if ( success ){
					btnFocus.setImageResource( R.drawable.take_focus );
					btnTake.setImageResource( R.drawable.take);
					btnTake.setEnabled( true );
				} else {
					btnFocus.setImageResource( R.drawable.lost_focus );
					btnTake.setImageResource( R.drawable.take_dis);
					btnTake.setEnabled( false );
					stopCamera();
					startCamera();
				}
			}
		};
	
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		setContentView(R.layout.main);
		createDB();
		if ( userCursor.getCount() > 0 && getUser().isShowStartup() ){
			showLoginForm();
		}
		File tmbDir = new File (TMB_DIR);
		if ( !tmbDir.exists() ){
			tmbDir.mkdir();
		}
		this.srfView = ( SurfaceView ) findViewById( R.id.surfaceViewId );
		this.srfHolder = srfView.getHolder();
		srfHolder.addCallback( this );
		
		mainInflanter = LayoutInflater.from( getBaseContext() );
		mainParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		initButtons();
		
		setFinishOnTouchOutside (false); 
		
	}
	
	private void createDB(){
		dataBase = new UpToTopDBHelper( getApplicationContext() ).getWritableDatabase() ;
		if ( dataBase != null ){
			//Toast.makeText(getApplicationContext(),"DataBase created", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(getApplicationContext(),"DataBase created error", Toast.LENGTH_LONG).show();
		}
	}
	
	private void initButtons(){
		View overlay = mainInflanter.inflate(R.layout.overlay, null);
		addContentView( overlay, mainParams );
		View overlayFcsBtn = mainInflanter.inflate(R.layout.focus_overlay, null);
		addContentView(overlayFcsBtn, mainParams);
		btnPreview = ( ImageButton ) overlay.findViewById( R.id.btnPreviewId );
		btnTake = ( ImageButton ) overlay.findViewById( R.id.btnTakeId );
		btnClose = ( ImageButton ) overlay.findViewById( R.id.btnCloseId );
		btnFocus  = (ImageButton ) overlayFcsBtn.findViewById( R.id.btnFocusId );
		btnTake.setEnabled( false );
		
		btnPreview.setOnClickListener( this );
		btnTake.setOnClickListener( this );
		btnClose.setOnClickListener( this );
	}
	
	@Override
	protected void onDestroy(){
		if ( camera != null ){
			camera.release();	
		}
		super.onDestroy();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity_actions, menu);
	    return super.onCreateOptionsMenu(menu);

	}
	
	@Override
	public boolean onOptionsItemSelected( MenuItem item){
		switch ( item.getItemId() ){
			case R.id.action_config :
				viewUserConfigActivity();
				break;
			case R.id.action_view_objectives :
				viewMyObjectivesActivity();
				break;
			default : 
				return false;
		}
		return true;
	}
	

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		startCamera();		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		
	}
	
	private void stopCamera(){
		camera.stopPreview();
		camera.release();
		camera = null;
	}
	
	private void startCamera(){
		try {
			camera =Camera.open();
			camera.setPreviewDisplay( srfHolder );
			Camera.Parameters params = camera.getParameters();
			params.set(ORIENTATION, LANDSCAPE);
			params.setFocusMode( Camera.Parameters.FOCUS_MODE_AUTO );

			camera.setDisplayOrientation(90);
			camera.setParameters( params );
			camera.startPreview();
			camera.autoFocus( autoFocus );
			
			btnFocus.setImageResource( R.drawable.lost_focus );
			btnClose.setEnabled( true );
			
		} catch (Exception e) {
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
		}
	}
	
	private void viewCreateObjActivity(){
		stopCamera();
		
		final Intent intent = new Intent();
		intent.putExtra(ABS_PATH, imagePath);
		intent.setClass(this, CreateObjectiveActivity.class );
		btnTake.setImageResource( R.drawable.take_dis);
		btnTake.setEnabled( false );
		startActivity(intent);
	}
	
	private void showLoginForm(){
		final Intent intent = new Intent();
		intent.setClass(this, LoginActivity.class );
		startActivityForResult(intent, ACTION_LOGIN );
	}
	
	protected void onActivityResult(int request, int result, Intent data){
		super.onActivityResult( request, result, data);
		if ( result == RESULT_OK ){
			return;
		} else if ( result == RESULT_CANCELED ){
			System.exit( 0 );
		}
	}
	
	private void viewUserConfigActivity(){
		stopCamera();
		btnTake.setImageResource( R.drawable.take_dis);
		btnTake.setEnabled( false );
		final Intent intent = new Intent();
		intent.putExtra( DPI, densityDPI );
		intent.setClass(this, UserProfileActivity.class );
		startActivity(intent);
	}
	
	private void viewMyObjectivesActivity(){
		stopCamera();
		btnTake.setImageResource( R.drawable.take_dis);
		btnTake.setEnabled( false );
		final Intent intent = new Intent();
		intent.setClass(this, ViewMyObjectiveActivity.class );
		startActivity(intent);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View btn) {
		switch ( btn.getId() ){
		case R.id.btnTakeId :{
			System.gc();
			camera.takePicture(shutter, rawPicture, jpgPicture);
			//Toast.makeText(this, R.string.image_capture, Toast.LENGTH_LONG).show();
			break;
		}
		case R.id.btnCloseId : {
			stopCamera();
			this.finish();
			break;
		}
		}
	}
	
	
}
