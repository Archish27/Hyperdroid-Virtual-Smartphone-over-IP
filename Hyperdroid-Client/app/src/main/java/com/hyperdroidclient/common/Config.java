package com.hyperdroidclient.common;

/**
 * Created by Archish on 12/20/2016.
 */


import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * ConfigurationFile
 * includes
 * all
 * Utilities.
 */
public class Config {

    public static final String BASE_URL = "http://api.ipify.org/";

    public static void changeFontInViewGroup(ViewGroup viewGroup, int fontType) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (TextView.class.isAssignableFrom(child.getClass())) {
                ((TextView) child).setTypeface(CustomFontLoader.getTypeface(viewGroup.getContext(),
                        fontType));
            } else if (ViewGroup.class.isAssignableFrom(child.getClass())) {
                changeFontInViewGroup((ViewGroup) viewGroup.getChildAt(i), fontType);
            }
        }
    }
}
