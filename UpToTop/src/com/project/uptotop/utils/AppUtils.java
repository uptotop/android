/**
 * 
 */
package com.project.uptotop.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.util.support.Base64;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;

import com.project.uptotop.AppConstants;

/**
 * @author alexey.kvitko
 *
 */
public class AppUtils implements AppConstants{
	
	protected static final String TAG = AppUtils.class.getSimpleName();
	
	public static Bitmap decodeScaledBitmapFromSdCard(String filePath,
	        int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(filePath, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeFile(filePath, options);
	}

	public static int calculateInSampleSize(
	        BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;

	    if (height > reqHeight || width > reqWidth) {

	        // Calculate ratios of height and width to requested height and width
	        final int heightRatio = Math.round((float) height / (float) reqHeight);
	        final int widthRatio = Math.round((float) width / (float) reqWidth);

	        // Choose the smallest ratio as inSampleSize value, this will guarantee
	        // a final image with both dimensions larger than or equal to the
	        // requested height and width.
	        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	    }

	    return inSampleSize;
	}
	
	public static Bitmap addWhiteBorder(Bitmap bmp, int borderSize) {
	    Bitmap bmpWithBorder = Bitmap.createBitmap(bmp.getWidth() + borderSize * 2, bmp.getHeight() + borderSize * 2, bmp.getConfig());
	    Canvas canvas = new Canvas(bmpWithBorder);
	    canvas.drawColor(Color.WHITE);
	    canvas.drawBitmap(bmp, borderSize, borderSize, null);
	    return bmpWithBorder;
	}
	

	public static Bitmap rotateBitmap(Bitmap bitMap, float degrees) {
	    Matrix m = new Matrix();
	    if (degrees != 0) {
	        // clockwise
	        m.postRotate(degrees, (float) bitMap.getWidth() / 2,
	                (float) bitMap.getHeight() / 2);
	    }
	    Bitmap newBitMap = null;
	    try {
	        newBitMap = Bitmap.createBitmap(bitMap, 0, 0, bitMap.getWidth(),bitMap.getHeight(), m, true);
	        if ( !bitMap.equals(newBitMap) ) {
	        	bitMap.recycle();
	        	bitMap = newBitMap;
	        }
	    } catch (OutOfMemoryError ex) {
	        // We have no memory to rotate. Return the original bitmap.
	    }
	    return bitMap;
	}
	
	/**
	 *  This method parse full name from file system to represent on display
	 */
	public static String parseToDisplayStr( String fullName ){
		String nameToParse = fullName.substring( fullName.length()-23,fullName.length()-4);
		StringBuilder displayName = new StringBuilder();
		String separator = "/";
		String[] split = nameToParse.split( UNDERSCORE );
		for(int i = 0; i < split.length ;i++){
			if ( i == 2) {
				displayName.append(split[i]+" ");	
				separator = ":";
			} else {
				displayName.append(split[i]+separator);
			}
		}
		displayName.delete( displayName.length()-1 , displayName.length() );
		return displayName.toString();
				
	}
	
	public static String convertToBase64( String fileName ){
		String base64Str = null;
		Bitmap scaledBitmap = decodeScaledBitmapFromSdCard( fileName , 1280, 1024 );
		Bitmap rotateBitmap = rotateBitmap(scaledBitmap, 90);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		rotateBitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);
		byte[] byteArray = baos.toByteArray();
		base64Str = Base64.encodeBytes(byteArray);
		return base64Str;
	}
	public static byte[] getBytesFromFile( String fileName )  {
		File file = new File(fileName);
		InputStream is;
		byte[] bytes = null;
		try {
			is = new FileInputStream(file);
			long length = file.length();

			if (length > Integer.MAX_VALUE) {
				return null;
			}

			bytes = new byte[(int) length];
			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length
					&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += numRead;
			}

			// Ensure all the bytes have been read in
			if (offset < bytes.length) {
				throw new IOException("Could not completely read file "
						+ file.getName());
			}
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bytes;
    }


}
