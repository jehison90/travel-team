package com.team.travel.travelteam.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.team.travel.travelteam.R;

/**
 * Created by Jehison on 23/05/2016.
 */
public class JoinRouteDialog extends Dialog {

    public JoinRouteDialog(Context context) {
        super(context);
    }

    public JoinRouteDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.join_route_dialog);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);
    }
}
