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
                "fonts/HelveticaRegular.ttf");

//        type = Typeface.createFromAsset(context.getAssets(),
//                "fonts/ptsans/PTSans-Regular.ttf");

        return type;
    }

    public Typeface getTrajanMedium()
    {
        type = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaMedium.ttf");
//        type = Typeface.createFromAsset(context.getAssets(),"fonts/ptsans/PTSans-Regular.ttf");
        return type;
    }

    public Typeface getHelveticaBold() {
        type = Typeface.createFromAsset(context.getAssets(),
                "fonts/HelveticaBold.ttf");

//        type = Typeface.createFromAsset(context.getAssets(),
//                "fonts/ptsans/PTSans-Bold.ttf");

        return type;
    }
}
