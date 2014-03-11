package com.hoy.asynctasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import com.hoy.asynctasks.interfaces.GenericSuccessHandleable;
import com.hoy.asynctasks.interfaces.GenericSuccessImgHandleable;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.helpers.ImageHelper;
import com.hoy.services.ImageService;
import com.hoy.utilities.RestClient;
import sun.security.pkcs11.wrapper.Constants;

import java.io.ByteArrayOutputStream;

/**
 * Created by ksairi on 3/5/14.
 */
public class GetMilongaImgAsyncTask extends AsyncTask<String, Void, String> {
    String paramEntity;
    Context context;
    GenericSuccessImgHandleable genericSuccessImgHandleable;
    Bitmap milongaImage = null;

    public GetMilongaImgAsyncTask(Context context, String paramEntity, GenericSuccessImgHandleable genericSuccessImgHandleable) {
        this.context = context;
        this.paramEntity = paramEntity;
        this.genericSuccessImgHandleable = genericSuccessImgHandleable;
    }

    @Override
    protected String doInBackground(String... strings) {
        milongaImage = ImageService.getLocalMilongaImg(context, paramEntity);
        if( milongaImage == null){
            milongaImage = RestClient.getMilongaImageBitMap(MilongaHoyConstants.MILONGA_IMAGE_URL.concat(MilongaHoyConstants.PREFIX_MILONGA_IMAGE).concat(paramEntity).concat(".png"));
            if(milongaImage != null){
                return ImageHelper.getBase64String(milongaImage);
            }
            else{
                return null;
            }
        }
        else{
            return null;
        }

    }

    @Override
    protected void onPostExecute(String imageBase64String) {
        super.onPostExecute(imageBase64String);
        if(imageBase64String != null){
            ImageService.saveMilongaImg(context,paramEntity,imageBase64String);
        }
        if(milongaImage != null){
            genericSuccessImgHandleable.handleSuccessCallBack(milongaImage);
        }
        else{
            genericSuccessImgHandleable.handleErrorResult();
        }

    }
}
