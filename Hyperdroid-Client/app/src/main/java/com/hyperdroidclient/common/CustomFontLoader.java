package com.hyperdroidclient.common;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Archish on 10/06/2016.
 */

public class CustomFontLoader {

    // Font for icons
    public static final int MONTSERRAT = 0;
    public static final int MONTSERRAT_BOLD = 1;

    private static final int NUM_OF_CUSTOM_FONTS = 2;

    private static boolean fontsLoaded = false;

    private static Typeface[] fonts = new Typeface[NUM_OF_CUSTOM_FONTS];

    private static String[] fontPath = {
            "fonts/MONTSERRAT-REGULAR.TTF",
            "fonts/MONTSERRAT-BOLD.TTF",
    };


    /**
     * Returns a loaded custom font based on it's identifier.
     *
     * @param context        - the current context
     * @param fontIdentifier = the identifier of the requested font
     * @return Typeface object of the requested font.
     */
    public static Typeface getTypeface(Context context, int fontIdentifier) {
        if (!fontsLoaded) {
            loadFonts(context);
        }
        return fonts[fontIdentifier];
    }


    private static void loadFonts(Context context) {
        for (int i = 0; i < NUM_OF_CUSTOM_FONTS; i++) {
            fonts[i] = Typeface.createFromAsset(context.getAssets(), fontPath[i]);
        }
        fontsLoaded = true;

    }
}
