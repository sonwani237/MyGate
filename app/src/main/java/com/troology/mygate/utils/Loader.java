package com.troology.mygate.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.troology.mygate.R;

/**
 * Created by Divyanshu
 */

public class Loader extends Dialog{

     public Loader(Context context) {
        super(context);
    }

    public Loader(Context context, int theme) {
        super(context, theme);
        setContentView(R.layout.custom_progress_view);
    }

    public Loader(Context context, boolean cancelable,
                  OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

}
