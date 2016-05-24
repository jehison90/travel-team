package com.team.travel.travelteam.dialogs;

import android.content.Context;

import com.team.travel.travelteam.R;

/**
 * Created by Jehison on 23/05/2016.
 */
public class ProgressDialogUtility {

    private static android.app.ProgressDialog spinner;

    private static Context context;

    public static void showProgressDialog(){
        if(spinner != null) {
            spinner.show();
        }
    }

    public static void dismissProgressDialog(){
        if(spinner != null && spinner.isShowing()) {
            spinner.dismiss();
        }
    }

    public static void setContext(Context contextToAssign){
        context = contextToAssign;
        spinner = null;
        spinner = new android.app.ProgressDialog(context, R.style.spinnerDialog);
        spinner.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        spinner.setMessage("Loading ...");
        spinner.setCancelable(false);
    }

}
