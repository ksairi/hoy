package com.hoy.services;

import android.content.Context;
import android.database.SQLException;
import android.view.Display;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.datasources.PromoImgDataSource;
import com.hoy.helpers.DeviceHelper;
import com.hoy.model.PromoImg;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 6/6/13
 * Time: 2:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageService {

	private static PromoImgDataSource  promoImgDataSource;
	private static PromoImg promoImg;



	private static PromoImgDataSource getPromoImgDataSource(Context uiContext){
			if(promoImgDataSource == null){
				promoImgDataSource = new PromoImgDataSource(uiContext);
			}
			return promoImgDataSource;


		}

	public static String savePromoImgs(Context context,List<PromoImg> promoImgs){
		try{
			getPromoImgDataSource(context).open();
				getPromoImgDataSource(context).truncateImgPromoTable();

			for(PromoImg promoImg : promoImgs){
				getPromoImgDataSource(context).createData(promoImg.getBase64p(),promoImg.getWidth(),promoImg.getHeight());
				//getPromoImgDataSource(context).createData(promoImg.getBase64l(),promoImg.getHeight(),promoImg.getWidth());
			}
			getPromoImgDataSource(context).close();
		}catch (SQLException e){
			return null;
		}

		return MilongaHoyConstants.SAVE_PROMO_SUCCESS;

	}


	public static PromoImg getPromoImg(Context context){


		if(promoImg == null){
			promoImg = new PromoImg();
		}

		Display display = DeviceHelper.getDisplay(context);
		final int width = display.getWidth();  // deprecated
		final int height = display.getHeight();  // deprecated
		if(width > height){
			//landscape
			promoImg.setWidth(height);
			promoImg.setHeight(width);
		}
		else{
			//portrait
			promoImg.setWidth(width);
			promoImg.setHeight(height);
		}
		return promoImg;

	}

	public static String getPromoImgBase64ByIndex(Context context, Integer index){

		getPromoImgDataSource(context).open();
					String promoImgBase64 = promoImgDataSource.getImgPromoBase64ByIndex(context,index);
		getPromoImgDataSource(context).close();
		return promoImgBase64;


	}

}
