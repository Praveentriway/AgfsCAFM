package com.daemon.emco_android.ui.components;

import android.content.Context;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.EditText;

import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.utils.Font;

import java.lang.reflect.Field;

/**
 * Created by daemonsoft on 24/5/16.
 */
public class CustomTextInputLayout extends TextInputLayout {

    Font font = App.getInstance().getFontInstance();
    float mTextSize;
    AppCompatActivity mActivity;

    public CustomTextInputLayout(Context context) {
        super(context);
        initFont(context);
    }

    public CustomTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mActivity = (AppCompatActivity) context;
        initFont(context);
    }

    private void initFont(Context context) {


        EditText editText = getEditText();
        if (editText != null) {
            editText.setPadding(3,3,3,3);
            editText.setTextSize(14);
            editText.setTypeface(font.getHelveticaRegular());
            editText.setSingleLine(true);
        }
        try {
            // Retrieve the CollapsingTextHelper Field
            final Field cthf = TextInputLayout.class.getDeclaredField("mCollapsingTextHelper");
            cthf.setAccessible(true);

            // Retrieve an instance of CollapsingTextHelper and its TextPaint
            final Object cth = cthf.get(this);
            final Field tpf = cth.getClass().getDeclaredField("mTextPaint");
            tpf.setAccessible(true);

            // Apply your Typeface to the CollapsingTextHelper TextPaint
            // ((TextPaint) tpf.get(cth)).setTypeface(font.getHelveticaRegular());
            mTextSize = (float) mActivity.getResources().getDimension(R.dimen.text_size_medium);
            ((TextPaint) tpf.get(cth)).setTextSize(mTextSize);
            ((TextPaint) tpf.get(cth)).setTypeface(font.getHelveticaRegular());
        } catch (Exception ignored) {
            // Nothing to do
        }
    }

}
