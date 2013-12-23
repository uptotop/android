package com.project.uptotop.utils;

import android.graphics.Bitmap;

public class BitmapWarehouse {
	
	private Bitmap bmp = null;
	private String imgPath = null;
	
	private static BitmapWarehouse instance = null;

	protected BitmapWarehouse() {
	}
	
	public static BitmapWarehouse getInstance(){
		if ( instance == null ){
			instance = new BitmapWarehouse();
		}
		return instance;
	}
	
	public void setBmp(Bitmap bmp){
		this.bmp = bmp;
	}
	
	public Bitmap getBmp(){
		return this.bmp;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

}
