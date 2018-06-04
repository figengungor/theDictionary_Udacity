package com.figengungor.thedictionary.utils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by figengungor on 6/2/2018.
 */

public class KeyboardUtils {

    public static void hideSoftKeyboard(Activity activity){
        InputMethodManager imm =(InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(activity.getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

}
