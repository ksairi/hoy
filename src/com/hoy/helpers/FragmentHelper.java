package com.hoy.helpers;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.hoy.fragments.ProgressDialogFragment;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 5/30/13
 * Time: 11:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class FragmentHelper {

	protected static ProgressDialogFragment progressDialogFragment;
	public static void changeProgressDialogState(FragmentManager fragmentManager, Boolean show){


			progressDialogFragment = getProgressDialogFragment();

			if(show){
				progressDialogFragment.show(fragmentManager,"");

			}
			else{
				progressDialogFragment.dismiss();

			}

		}

	protected static ProgressDialogFragment getProgressDialogFragment(){

			if(progressDialogFragment == null){

				progressDialogFragment = new ProgressDialogFragment();
			}

			return progressDialogFragment;
		}
}
