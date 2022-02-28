package com.daemon.emco_android.utils.meterReading.ImageProcessing;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicConvolve3x3;

import androidx.annotation.RequiresApi;

//import android.renderscript.Allocation;
//import android.renderscript.Element;
//import android.renderscript.RenderScript;
//import android.renderscript.ScriptIntrinsicConvolve3x3;
//import android.renderscript.Element;


public class ImageSharpener {

    final static int KERNAL_WIDTH = 3;
    final static int KERNAL_HEIGHT = 3;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap sharpenBitmap(Bitmap bitmap, float[] coefficients, Context context) {


        Bitmap result = Bitmap.createBitmap(
                bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());

        RenderScript renderScript = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap(renderScript, bitmap);
        Allocation output = Allocation.createFromBitmap(renderScript, result);

        ScriptIntrinsicConvolve3x3 convolution = ScriptIntrinsicConvolve3x3
                .create(renderScript, Element.U8_4(renderScript));
        convolution.setInput(input);
        convolution.setCoefficients(coefficients);
        convolution.forEach(output);

        output.copyTo(result);
        renderScript.destroy();
        return result;
    }


}
