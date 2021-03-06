package com.hoy.activities;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import com.hoy.R;
import com.hoy.asynctasks.GetInitialContentAsyncTask;
import com.hoy.asynctasks.SyncLocalEventsAsyncTask;
import com.hoy.asynctasks.interfaces.GenericSuccessListHandleable;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.dto.EventDTO;
import com.hoy.helpers.SharedPreferencesHelper;
import com.hoy.model.FilterParams;
import com.hoy.model.PromoImg;
import com.hoy.schedulers.EventsScheduler;
import com.hoy.services.ImageService;
import com.hoy.utilities.DateUtils;


import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 5/29/13
 * Time: 5:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class InitialActivity extends GenericActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.init_layout);
        prepareContent();
    }

    private void prepareContent() {
        final PromoImg promoImg = ImageService.getPromoImg(getContext());
        if (SharedPreferencesHelper.getValueInSharedPreferences(getContext(), MilongaHoyConstants.HAS_CLEANED_VALUES).equals(MilongaHoyConstants.EMPTY_STRING)) {
            SharedPreferencesHelper.removeValueSharedPreferences(getContext(), MilongaHoyConstants.LAST_FULL_UPDATE_DATE);
            SharedPreferencesHelper.setValueSharedPreferences(getContext(), MilongaHoyConstants.HAS_CLEANED_VALUES, MilongaHoyConstants.HAS_CLEANED_VALUES);
        }
        PackageInfo packageInfo = null;
        try{
            packageInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);

            if(packageInfo != null){
                Integer versionCode = packageInfo.versionCode;
                try{
                    Integer lastAppVersionCodeSaved = Integer.decode(SharedPreferencesHelper.getValueInSharedPreferences(getContext(), MilongaHoyConstants.APP_VERSION_CODE));
                        if(lastAppVersionCodeSaved == null || versionCode > lastAppVersionCodeSaved){
                            prepareContentForNewAppVersion(versionCode);
                        }

                }catch (NumberFormatException e){
                    prepareContentForNewAppVersion(versionCode);
                }
            }
        }catch (PackageManager.NameNotFoundException e){

        }



        new GetInitialContentAsyncTask(getContext(), promoImg, getSupportFragmentManager(), new GenericSuccessListHandleable<EventDTO>() {
            public void handleSuccessCallBack(List<EventDTO> eventDTOs) {
                SharedPreferencesHelper.setValueSharedPreferences(getContext(), MilongaHoyConstants.LAST_FULL_UPDATE_DATE, DateUtils.getTodayAndTimeString());
                EventsScheduler.startRetrievePromoImgTask(getContext(), promoImg);
                genericStartActivity(HomeActivity.class, MilongaHoyConstants.EVENT_DTOS, (ArrayList) eventDTOs, true);
            }

            public void handleErrorResult() {

            }

            public void handleErrorCallBack(final List<EventDTO> eventDTOs) {

                EventsScheduler.startRetrievePromoImgTask(getContext(), promoImg);
                new SyncLocalEventsAsyncTask(getContext(), new FilterParams(DateUtils.getTodayString()), getSupportFragmentManager(), new GenericSuccessListHandleable<EventDTO>() {
                    public void handleSuccessCallBack(List<EventDTO> localEventDTOs) {
                        genericStartActivity(HomeActivity.class, MilongaHoyConstants.EVENT_DTOS, (ArrayList) localEventDTOs, true);
                        Toast.makeText(getContext(), R.string.connection_errors, Toast.LENGTH_SHORT).show();
                    }

                    public void handleErrorResult() {
                        genericStartActivity(HomeActivity.class, MilongaHoyConstants.EVENT_DTOS, (ArrayList) eventDTOs, true);
                        Toast.makeText(getContext(), R.string.connection_errors, Toast.LENGTH_SHORT).show();
                    }

                    public void handleErrorCallBack(List<EventDTO> localEventDTOs) {

                    }
                }).execute();
            }
        }).execute();

    }

    protected Context getContext() {
        return this;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ImageView imageView = (ImageView) findViewById(R.id.init_image);
        imageView.setBackgroundResource(R.drawable.init_image);
    }

    private void prepareContentForNewAppVersion(Integer versionCode){

        SharedPreferencesHelper.removeValueSharedPreferences(getContext(), MilongaHoyConstants.SERVER_LAST_UPDATE_TIME);
        SharedPreferencesHelper.setValueSharedPreferences(getContext(), MilongaHoyConstants.APP_VERSION_CODE, String.valueOf(versionCode));
    }
}
