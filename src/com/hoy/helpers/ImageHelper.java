package com.hoy.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.model.PromoImg;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 6/6/13
 * Time: 12:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageHelper {
    private static final String IMG_PROMO_WIDTH_PARAMETER = "w";
    private static final String IMG_PROMO_HEIGHT_PARAMETER = "h";
    private static final String IMG_PROMO_APP_NUMBER_PARAMETER = "i";
    /// JEFF! change this to customize the banner
    //0 = get all banners
    // 1 = flordemilonga
    // 2 = teachingtango
    private static final String IMG_PROMO_APP_NUMBER_VALUE = "0";

    public static String buildUrl(PromoImg promoImg) {

        String url = MilongaHoyConstants.PROMO_IMG_URL;
        return url.concat("?").concat(IMG_PROMO_WIDTH_PARAMETER).concat("=").concat(promoImg.getWidth().toString()).concat("&").concat(IMG_PROMO_HEIGHT_PARAMETER).concat("=").concat(promoImg.getHeight().toString()).concat("&").concat(IMG_PROMO_APP_NUMBER_PARAMETER).concat("=").concat(IMG_PROMO_APP_NUMBER_VALUE);
    }

    public static List<PromoImg> parseResponse(String jsonString, PromoImg promoImg) {
        List<PromoImg> promoImgs = new ArrayList<PromoImg>();

        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (Integer i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                String base64Portrait = jsonObject.getString("base64p");
                //String base64Landscape = jsonObject.getString("base64l");
                String urlDestination = jsonObject.getString("url");
                PromoImg newPromoImg = new PromoImg();
                newPromoImg.setBase64p(base64Portrait);
                newPromoImg.setHeight(promoImg.getHeight());
                newPromoImg.setWidth(promoImg.getWidth());
                //newPromoImg.setWidth(promoImg.getWidth());
                newPromoImg.setUrlDestination(urlDestination);
                promoImgs.add(newPromoImg);

            }

        } catch (Exception e) {
            return null;
        }
        return promoImgs;
    }

    public static Bitmap getBitMap(String base64String) {

        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return bitmap;
    }

    public static String getBase64String(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
