package com.hoy.services;

import android.content.Context;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.view.Display;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.datasources.MilongaImgDataSource;
import com.hoy.datasources.PromoImgDataSource;
import com.hoy.helpers.DeviceHelper;
import com.hoy.helpers.ImageHelper;
import com.hoy.model.PromoImg;
import com.hoy.utilities.RestClient;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 6/6/13
 * Time: 2:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageService {

    private static PromoImgDataSource promoImgDataSource;
    private static MilongaImgDataSource milongaImgDataSource;
    private static PromoImg promoImg;


    private static PromoImgDataSource getPromoImgDataSource(Context uiContext) {
        if (promoImgDataSource == null) {
            promoImgDataSource = new PromoImgDataSource(uiContext);
        }
        return promoImgDataSource;


    }

    private static MilongaImgDataSource getMilongaImgDataSource(Context uiContext) {
        if (milongaImgDataSource == null) {
            milongaImgDataSource = new MilongaImgDataSource(uiContext);
        }
        return milongaImgDataSource;


    }

    public static String savePromoImgs(Context context, List<PromoImg> promoImgs) {
        try {
            getPromoImgDataSource(context).open();
            getPromoImgDataSource(context).truncateImgPromoTable();

            for (PromoImg promoImg : promoImgs) {
                getPromoImgDataSource(context).createData(promoImg.getBase64p(), promoImg.getUrlDestination(), promoImg.getWidth(), promoImg.getHeight());
                //getPromoImgDataSource(context).createData(promoImg.getBase64l(),promoImg.getHeight(),promoImg.getWidth());
            }
            getPromoImgDataSource(context).close();
        } catch (SQLException e) {
            return null;
        }

        return MilongaHoyConstants.SAVE_PROMO_SUCCESS;

    }


    public static PromoImg getPromoImg(Context context) {


        if (promoImg == null) {
            promoImg = new PromoImg();
        }

        Display display = DeviceHelper.getDisplay(context);
        final int width = display.getWidth();  // deprecated
        final int height = display.getHeight();  // deprecated
        if (width > height) {
            //landscape
            promoImg.setWidth(height);
            promoImg.setHeight(width);
        } else {
            //portrait
            promoImg.setWidth(width);
            promoImg.setHeight(height);
        }
        return promoImg;

    }

    public static String[] getNextPromoImgDataByIndex(Context context, Integer index) {

        getPromoImgDataSource(context).open();
        String[] result = promoImgDataSource.getNextPromoImgDataByIndex(context, index);
        getPromoImgDataSource(context).close();
        return result;


    }

    public static String[] getPromoImgDataByIndex(Context context, Integer index) {

        getPromoImgDataSource(context).open();
        String[] result = promoImgDataSource.getPromoImgDataByIndex(index);
        getPromoImgDataSource(context).close();
        return result;


    }

    public static synchronized String syncAndSavePromoImgs(PromoImg promoImg, Context context) {

        String url = ImageHelper.buildUrl(promoImg);
        String jsonString = RestClient.executeHttpGetRequest(url);
        List<PromoImg> promoImgs = ImageHelper.parseResponse(jsonString, promoImg);
        if (promoImgs != null && ImageService.savePromoImgs(context, promoImgs) != null) {
            return MilongaHoyConstants.SAVE_PROMO_SUCCESS;
        }
        return null;
    }


    public static void saveMilongaImg(Context context, String paramEntity, String imageBase64String) {
        getMilongaImgDataSource(context).createData(paramEntity,imageBase64String);

    }

    public static Bitmap getLocalMilongaImg(Context context, String paramEntity) {
        return getMilongaImgDataSource(context).getMilongaImgById(paramEntity,true);
    }
}
