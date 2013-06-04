package com.hoy.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 6/4/13
 * Time: 6:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImgRestClient {

	public static Bitmap getImage(String fileUrl){
	      URL myFileUrl =null;
	      try {
	           myFileUrl= new URL(fileUrl);
	      } catch (MalformedURLException e) {
	           return null;
	      }
	      try {
				   HttpURLConnection conn= (HttpURLConnection)myFileUrl.openConnection();
				   conn.setDoInput(true);
				   conn.connect();
				   InputStream is = conn.getInputStream();

				   return BitmapFactory.decodeStream(is);

	      } catch (IOException e) {

	           return null;
	      }
	 }
}
