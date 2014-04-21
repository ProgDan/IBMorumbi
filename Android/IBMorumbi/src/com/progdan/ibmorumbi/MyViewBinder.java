package com.progdan.ibmorumbi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.progdan.ibmorumbi.imageloader.ImageLoader;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleAdapter.ViewBinder;

public class MyViewBinder implements ViewBinder {

	@Override
	public boolean setViewValue(View view, Object data,
			String textRepresentation) {
		ImageView image;
		
		if ((view instanceof ImageView) && (data instanceof Bitmap)) {
			image = (ImageView) view;
			Bitmap bm = (Bitmap) data;
			image.setImageBitmap(bm);
			return true;
		}
		if ((view instanceof ImageView) && (data instanceof String)) {
			// Loader image - will be shown before loading image
			int loader = R.drawable.icon_loader;
			
			// Imageview to show
			image = (ImageView) view;
			
			// Image url
			String image_url = (String) data;
			
			// Get the file name
	        File f = new File(image_url);
	        String parent = "pastores";
	        String fileName = f.getName();	

			if(assetExists(view.getContext().getAssets(),fileName, parent)){
				InputStream bitmap=null;
				try{
				    bitmap=view.getContext().getAssets().open(parent+"/"+fileName);
				    Bitmap bit=BitmapFactory.decodeStream(bitmap);
				    image.setImageBitmap(bit);	
				} catch (IOException e) {
				    e.printStackTrace();
				} finally {
					try {
					    if(bitmap!=null)
					    	bitmap.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			else {			
				// ImageLoader class instance
				ImageLoader imgLoader = new ImageLoader(view.getContext()
						.getApplicationContext());
				
				imgLoader.DisplayImage(image_url, loader, image);
			}
			return true;
		}
		return false;
	}
	
	private static boolean assetExists(AssetManager assets, String fileName, String parent) {
	    try {
	        // now use path to list all files
	        String[] assetList = assets.list(parent);
	        if (assetList != null && assetList.length > 0) {
	            for (String item : assetList) {
	                if (fileName.equals(item))
	                    return true;
	            }
	        }
	    } catch (IOException e) {
	        // Log.w(TAG, e); // enable to log errors
	    }
	    return false;
	}

}
