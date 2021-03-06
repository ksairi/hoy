package com.hoy.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.hoy.R;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.helpers.ImageHelper;
import com.hoy.schedulers.EventsScheduler;

import java.util.concurrent.ScheduledFuture;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 6/21/13
 * Time: 11:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class PromoImgFragment extends Fragment {
    ScheduledFuture changePromoImgtask;
    PromoImgFragmentInterface promoImgFragmentInterface;
    String promoImgUrlDestination;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.include_img_promo, container, false);
        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        startPromoImgTimerTask();
    }

    @Override
    public void onPause() {
        super.onPause();
        cancelPromoImgTimerTask();
    }

    protected Handler changePromoImgHandler = new Handler() {

        ImageView promoImageView;
        Bundle bundle;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            bundle = msg.getData();
            promoImageView = (ImageView) view.findViewById(R.id.promo_img);
            String[] promoImgData = bundle.getStringArray(MilongaHoyConstants.PROMO_IMG_DATA);
            if (getActivity() != null) {
                Animation myFadeInAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                promoImageView.startAnimation(myFadeInAnimation); //Set animation to your ImageView

                if (promoImgData != null) {
                    promoImageView.setImageBitmap(ImageHelper.getBitMap(promoImgData[MilongaHoyConstants.PROMO_IMG_BASE_64_INDEX_POSITION]));
                    promoImgUrlDestination = promoImgData[(MilongaHoyConstants.PROMO_IMG_URL_DESTINATION_INDEX_POSITION)];
                }

                promoImageView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        if (promoImgUrlDestination != null && !promoImgUrlDestination.equals(MilongaHoyConstants.EMPTY_STRING)) {
                            intent.setData(Uri.parse(promoImgUrlDestination));
                            startActivity(intent);
                        }
                    }
                });
            }

        }
    };

    public interface PromoImgFragmentInterface {

        public Boolean getNextPromoImg();

    }

    @Override
    public void onDetach() {
        super.onDestroyView();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof PromoImgFragmentInterface) {
            promoImgFragmentInterface = (PromoImgFragmentInterface) activity;

        } else {
            throw new ClassCastException(activity.toString()
                    + " must implemenet EventListFragment.OnItemSelectedListener");
        }
    }

    public void cancelPromoImgTimerTask() {
        if (changePromoImgtask != null) {
            changePromoImgtask.cancel(true);
        }

    }

    public void startPromoImgTimerTask() {
        changePromoImgtask = EventsScheduler.startChangePromoImgTask(getActivity(), changePromoImgHandler, promoImgFragmentInterface.getNextPromoImg());
    }
}
