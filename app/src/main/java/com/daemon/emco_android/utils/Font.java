package com.daemon.emco_android.utils;

import android.content.Context;
import android.graphics.Typeface;

public class Font {

    private Typeface type;

    private Context context;

    public Font(Context context) {
        this.context = context;
    }

    public Typeface getHelveticaRegular() {
        type = Typeface.createFromAsset(context.getAssets(),
                "fonts/HelveticaRegular.otf");

        return type;
    }


    public Typeface getHelveticaBold() {
        type = Typeface.createFromAsset(context.getAssets(),
                "fonts/HelveticaBold.otf");


        return type;
    }
}
