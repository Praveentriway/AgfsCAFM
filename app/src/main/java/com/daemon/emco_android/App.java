package com.daemon.emco_android;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.crashlytics.android.Crashlytics;
import com.daemon.emco_android.utils.Font;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.Stack;

import io.fabric.sdk.android.Fabric;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.calligraphy3.CalligraphyConfig;

/**
 * Created by praba on 4/12/19.
 */
public class App extends Application {
    public static final String TAG = App.class.getSimpleName();
    private static App mInstance;
    private Font font;

    public static synchronized App getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        mInstance = this;
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(loadConfig(App.this));
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()

                                .setDefaultFontPath("fonts/HelveticaRegular.otf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());


    }

    public ImageLoaderConfiguration loadConfig(Context cd) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                cd).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        return config;
    }

    public Font getFontInstance() {
        if (this.font == null) {
            this.font = new Font(mInstance);
        }
        return this.font;
    }

}



