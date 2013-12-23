package com.project.uptotop.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import com.project.uptotop.AppConstants;
import com.project.uptotop.R;
import com.project.uptotop.utils.AppUtils;
import com.project.uptotop.utils.BitmapWarehouse;

public class UpToTopMainActivity extends Activity implements SurfaceHolder.Callback, View.OnClickListener, AppConstants{
	
	@SuppressLint("SimpleDateFormat")
	final SimpleDateFormat FORMAT= new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	
	private static BitmapWarehouse BMP_WRH = BitmapWarehouse.getInstance();
	
	private final String ORIENTATION="orientation";
	private final String LANDSCAPE = "landscape";

	public static final String ABS_PATH ="path";
	
	private static final Integer ACTION_TRANSFER=101;
	
	private ImageButton btnPreview;
	private ImageButton btnTake;
	private ImageButton btnClose;
	private ImageButton btnFocus;
	
	
	private SurfaceView srfView;
	private SurfaceHolder srfHolder;
	private AutoFocusCallback autoFocus;
//	private AutoFocusMoveCallback moveFocus;
	
	private Camera camera;
	
	private byte[] imageInByte;
	private String imagePath;
	
	
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
		        
				Bitmap scaledBitmap = AppUtils.decodeScaledBitmapFromSdCard( imagePath,70,50 );
				
				BMP_WRH.setBmp( scaledBitmap );
				BMP_WRH.setImgPath( imagePath );
				setThumbnail( scaledBitmap ) ;
				btnPreview.setEnabled( true );
				stopCamera();
				startCamera();
			}
		};
		
		autoFocus = new AutoFocusCallback() {
			@Override
			public void onAutoFocus(boolean success, Camera camera) {
				if ( success ){
					btnFocus.setImageResource( R.drawable.take_focus );
				} else {
					btnFocus.setImageResource( R.drawable.lost_focus );
				}
			}
		};
	
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		
		this.srfView = ( SurfaceView ) findViewById( R.id.surfaceViewId );
		this.srfHolder = srfView.getHolder();
		srfHolder.addCallback( this );
		srfHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS );
		
		mainInflanter = LayoutInflater.from( getBaseContext() );
		mainParams = new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
		
		initButtons();

		if ( BMP_WRH.getBmp() != null ){
			setThumbnail( BMP_WRH.getBmp() );
			imagePath = BMP_WRH.getImgPath();
			btnPreview.setEnabled( true );
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
		getMenuInflater().inflate(R.menu.preview_main, menu);
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
	
	private void setThumbnail(Bitmap bmp ){
		Bitmap borderedBmp = AppUtils.addWhiteBorder( bmp , 2);
		btnPreview.setImageBitmap( AppUtils.rotateBitmap(borderedBmp, 90) );
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

	@Override
	public void onClick(View btn) {
		switch ( btn.getId() ){
		case R.id.btnTakeId :{
			System.gc();
			camera.takePicture(shutter, rawPicture, jpgPicture);
			//Toast.makeText(this, R.string.image_capture, Toast.LENGTH_LONG).show();
			break;
		}
		case R.id.btnPreviewId :{
			stopCamera();
			Intent intent = new Intent();
			intent.putExtra(ABS_PATH, imagePath);
		//	intent.setClass(this, RealPreviewActivity.class );
			startActivityForResult(intent, ACTION_TRANSFER);
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
