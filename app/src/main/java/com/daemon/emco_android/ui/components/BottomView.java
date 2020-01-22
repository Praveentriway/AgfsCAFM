package com.daemon.emco_android.ui.components;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.daemon.emco_android.R;


/**
 * BottomView
 * Material Design guidelines : https://www.google.com/design/spec/components/bottom-navigation.html
 */

public class BottomView extends FrameLayout {

    // Listener
    private OnNavigationPositionListener navigationPositionListener;

    // Variables
    private Context context;
    private Resources resources;
    private BottomViewBehavior<BottomView> bottomViewBehavior;
    private View backgroundColorView;
    private boolean selectedBackgroundVisible = false;
    private boolean translucentNavigationEnabled;
    private boolean isHidden = false;
    private boolean isBehaviorTranslationSet = false;
    ;
    private boolean behaviorTranslationEnabled = true;
    private boolean needHideBottomNavigation = false;
    private boolean hideBottomNavigationWithAnimation = false;
    // Variables (Styles)
    private Typeface titleTypeface;
    private int defaultBackgroundColor = Color.WHITE;
    private int defaultBackgroundResource = 0;
    private boolean forceTint = false;
    private int bottomNavigationHeight, navigationBarHeight = 0;

    /**
     * Constructors
     */
    public BottomView(Context context) {
        super(context);
        init(context, null);
    }

    public BottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BottomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        createItems();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!isBehaviorTranslationSet) {
            //The translation behavior has to be set up after the super.onMeasure has been called.
            setBehaviorTranslationEnabled(behaviorTranslationEnabled);
            isBehaviorTranslationSet = true;
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            state = bundle.getParcelable("superState");
        }
        super.onRestoreInstanceState(state);
    }

    /////////////
    // PRIVATE //
    /////////////

    /**
     * Init
     *
     * @param context
     */
    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        resources = this.context.getResources();

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BottomViewBehavior_Params, 0, 0);
            try {
                selectedBackgroundVisible = ta.getBoolean(R.styleable.BottomViewBehavior_Params_selectedBackgroundVisible, false);
                translucentNavigationEnabled = ta.getBoolean(R.styleable.BottomViewBehavior_Params_translucentNavigationEnabled, false);
            } finally {
                ta.recycle();
            }
        }


        ViewCompat.setElevation(this, resources.getDimension(R.dimen.bottom_view_elevation));
        setClipToPadding(false);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, bottomNavigationHeight);
        setLayoutParams(params);
    }

    /**
     * Create the items in the bottom navigation
     */
    private void createItems() {

        int layoutHeight = (int) resources.getDimension(R.dimen.bottom_view_height);

        backgroundColorView = new View(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            LayoutParams backgroundLayoutParams = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, calculateHeight(layoutHeight));
            addView(backgroundColorView, backgroundLayoutParams);
            bottomNavigationHeight = layoutHeight;
        }
        // Force a request layout after all the items have been created
        post(new Runnable() {
            @Override
            public void run() {
                requestLayout();
            }
        });
    }

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private int calculateHeight(int layoutHeight) {
        if (!translucentNavigationEnabled) return layoutHeight;

        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            navigationBarHeight = resources.getDimensionPixelSize(resourceId);
        }

        int[] attrs = {android.R.attr.fitsSystemWindows, android.R.attr.windowTranslucentNavigation};
        TypedArray typedValue = getContext().getTheme().obtainStyledAttributes(attrs);

        @SuppressWarnings("ResourceType")
        boolean fitWindow = typedValue.getBoolean(0, false);

        @SuppressWarnings("ResourceType")
        boolean translucentNavigation = typedValue.getBoolean(1, true);

        if (hasImmersive() /*&& !fitWindow*/ && translucentNavigation) {
            layoutHeight += navigationBarHeight;
        }

        typedValue.recycle();

        return layoutHeight;
    }

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public boolean hasImmersive() {
        Display d = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        d.getRealMetrics(realDisplayMetrics);

        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;

        return (realWidth > displayWidth) || (realHeight > displayHeight);
    }

    /**
     * Return if the behavior translation is enabled
     *
     * @return a boolean value
     */
    public boolean isBehaviorTranslationEnabled() {
        return behaviorTranslationEnabled;
    }

    /**
     * Set the behavior translation value
     *
     * @param behaviorTranslationEnabled boolean for the state
     */
    public void setBehaviorTranslationEnabled(boolean behaviorTranslationEnabled) {
        this.behaviorTranslationEnabled = behaviorTranslationEnabled;
        if (getParent() instanceof CoordinatorLayout) {
            ViewGroup.LayoutParams params = getLayoutParams();
            if (bottomViewBehavior == null) {
                bottomViewBehavior = new BottomViewBehavior<>(behaviorTranslationEnabled, navigationBarHeight);
            } else {
                bottomViewBehavior.setBehaviorTranslationEnabled(behaviorTranslationEnabled, navigationBarHeight);
            }
            if (navigationPositionListener != null) {
                bottomViewBehavior.setOnNavigationPositionListener(navigationPositionListener);
            }
            ((CoordinatorLayout.LayoutParams) params).setBehavior(bottomViewBehavior);
            if (needHideBottomNavigation) {
                needHideBottomNavigation = false;
                bottomViewBehavior.hideView(this, bottomNavigationHeight, hideBottomNavigationWithAnimation);
                isHidden = true;
            }
        }
    }

    /**
     * Hide Bottom Navigation with animation
     */
    public void hideBottomNavigation() {
        hideBottomNavigation(true);
    }

    /**
     * Hide Bottom Navigation with animation
     */
    public void showBottomNavigation() {
        isHidden = false;
        if (bottomViewBehavior != null) {
            bottomViewBehavior.resetOffset(this, true);
        } else if (getParent() instanceof CoordinatorLayout) {
            needHideBottomNavigation = false;
            hideBottomNavigationWithAnimation = true;
        } else {
            // Hide bottom navigation
            ViewCompat.animate(this)
                    .translationY(bottomNavigationHeight)
                    .setInterpolator(new LinearOutSlowInInterpolator())
                    .setDuration(300)
                    .start();
        }
    }

    /**
     * Hide Bottom Navigation with or without animation
     *
     * @param withAnimation Boolean
     */
    public void hideBottomNavigation(boolean withAnimation) {
        isHidden = true;
        if (bottomViewBehavior != null) {
            bottomViewBehavior.hideView(this, bottomNavigationHeight, withAnimation);
        } else if (getParent() instanceof CoordinatorLayout) {
            needHideBottomNavigation = true;
            hideBottomNavigationWithAnimation = withAnimation;
        } else {
            // Hide bottom navigation
            ViewCompat.animate(this)
                    .translationY(bottomNavigationHeight)
                    .setInterpolator(new LinearOutSlowInInterpolator())
                    .setDuration(withAnimation ? 300 : 0)
                    .start();
        }
    }

    /**
     * Restore Bottom Navigation with animation
     */
    public void restoreBottomNavigation() {
        restoreBottomNavigation(true);
    }

    /**
     * Restore Bottom Navigation with or without animation
     *
     * @param withAnimation Boolean
     */
    public void restoreBottomNavigation(boolean withAnimation) {
        isHidden = false;
        if (bottomViewBehavior != null) {
            bottomViewBehavior.resetOffset(this, withAnimation);
        } else {
            // Show bottom navigation
            ViewCompat.animate(this)
                    .translationY(0)
                    .setInterpolator(new LinearOutSlowInInterpolator())
                    .setDuration(withAnimation ? 300 : 0)
                    .start();
        }
    }

    /**
     * Return if the translucent navigation is enabled
     */
    public boolean isTranslucentNavigationEnabled() {
        return translucentNavigationEnabled;
    }

    /**
     * Set the translucent navigation value
     */
    public void setTranslucentNavigationEnabled(boolean translucentNavigationEnabled) {
        this.translucentNavigationEnabled = translucentNavigationEnabled;
    }

    /**
     * Return if the tint should be forced (with setColorFilter)
     *
     * @return Boolean
     */
    public boolean isForceTint() {
        return forceTint;
    }


    /**
     * Activate or not the elevation
     *
     * @param useElevation boolean
     */
    public void setUseElevation(boolean useElevation) {
        ViewCompat.setElevation(this, useElevation ?
                resources.getDimension(R.dimen.bottom_view_elevation) : 0);
        setClipToPadding(false);
    }

    /**
     * Activate or not the elevation, and set the value
     *
     * @param useElevation boolean
     * @param elevation    float
     */
    public void setUseElevation(boolean useElevation, float elevation) {
        ViewCompat.setElevation(this, useElevation ? elevation : 0);
        setClipToPadding(false);
    }

    /**
     * Return if the Bottom Navigation is hidden or not
     */
    public boolean isHidden() {
        return isHidden;
    }

    public interface OnNavigationPositionListener {
        /**
         * Called when the bottom navigation position is changed
         *
         * @param y int: y translation of bottom navigation
         */
        void onPositionChange(int y);
    }
}