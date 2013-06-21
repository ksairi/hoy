package com.hoy.helpers;

import android.support.v4.app.FragmentManager;
import com.hoy.fragments.ProgressDialogFragment;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 5/30/13
 * Time: 11:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class FragmentHelper {

	public static ProgressDialogFragment showProgressDialog(FragmentManager fragmentManager){

		ProgressDialogFragment progressDialogFragment = new ProgressDialogFragment();

		progressDialogFragment.show(fragmentManager,"progress");

		return progressDialogFragment;

	}

	public static void hideProgressDialog(ProgressDialogFragment progressDialogFragment){

		if(progressDialogFragment != null){
			progressDialogFragment.dismiss();

		}
	}

}
