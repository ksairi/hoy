package com.hoy.timer_task;

import android.content.Context;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.helpers.ImageHelper;
import com.hoy.helpers.SharedPreferencesHelper;
import com.hoy.model.PromoImg;
import com.hoy.services.ImageService;
import com.hoy.utilities.RestClient;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 6/5/13
 * Time: 11:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class RetrievePromoImgRunnable extends AbstractRunnable {
    private PromoImg promoImg;


    public RetrievePromoImgRunnable(Context context, PromoImg promoImg) {
        this.context = context;
        this.promoImg = promoImg;
    }

    @Override
    public void run() {

        String result = RestClient.executeHttpGetRequest(ImageHelper.buildUrl(promoImg));
        List<PromoImg> promoImgs = ImageHelper.parseResponse(result, promoImg);
        if (promoImgs != null) {
            ImageService.savePromoImgs(context, promoImgs);

            SharedPreferencesHelper.setValueSharedPreferences(context, MilongaHoyConstants.PROMO_IMG_UPDATED, "true");

        }

    }


    @Override
    protected String getParams() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected String getUrl() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


}
