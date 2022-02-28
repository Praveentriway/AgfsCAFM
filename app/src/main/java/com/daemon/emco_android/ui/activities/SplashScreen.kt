package com.daemon.emco_android.ui.activities

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.daemon.emco_android.R
import com.daemon.emco_android.listeners.AppUpdateListener
import com.daemon.emco_android.ui.adapter.MyAdapter
import com.daemon.emco_android.utils.AnimateUtils
import com.daemon.emco_android.utils.AppUpdaterUtils
import com.daemon.emco_android.utils.AppUtils
import kotlinx.android.synthetic.main.activity_splash_screen.*
import me.relex.circleindicator.CircleIndicator
import java.util.*
import kotlin.collections.ArrayList

// I AM COMMENT UPDATE CODE
/**
 * Created by praba on 7/12/19.
 */
class SplashScreen : AppCompatActivity(), AppUpdateListener {

    private lateinit var mPreferences: SharedPreferences
    var updateShow = false
    private var mPager: ViewPager? = null
    private var currentPage = 0
    private var img = arrayOf<Int>()
    private val ImgArray = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        img = arrayOf<Int>(R.drawable.splash_bg1, R.drawable.splash_bg, R.drawable.splash_bg2, R.drawable.splash_bg4, R.drawable.splash_bg3)

        if(resources.getString(R.string.app_name).equals("EMCO CAFM")){
            img = arrayOf<Int>(R.drawable.splash_bg1, R.drawable.splash_bg)
        }

        initView()
        init()
        checkDirectLogin()
        AnimateUtils().splashAnimate(tv_login, tv_signup)


        if(resources.getString(R.string.app_name).equals("EMCO CAFM")){
            logo_line.visibility=View.GONE
            logo_image.visibility=View.GONE
        }

    }
// I AM COMMENT UPDATE CODE
    override fun onResume() {
        if (!updateShow) {
          //  AppUpdaterUtils().showDialog(this,this)
        }
      //  updateShow = true
        super.onResume()
    }

    fun initView() {
        tv_login!!.setOnClickListener {
            startActivity(Intent(this@SplashScreen, LoginActivity::class.java))
            finish()
        }
        tv_signup!!.setOnClickListener { startActivity(Intent(this@SplashScreen, RegisterActivity::class.java)) }
    }

    fun checkDirectLogin() {
        mPreferences = getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE)
        val loginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null)
        if (loginData != null && loginData.length > 0) {
            val mainActivity = Intent(this, MainActivity::class.java)
            startActivity(mainActivity)
            finish()
            return
        }
    }

    override fun onUpdateClicked(update: Boolean) {
        updateShow=update
        val appPackageName: String = getPackageName() // getPackageName() from Context or Activity object
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
        } catch (anfe: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
        }
    }


    private fun init() {
        for (element in img) ImgArray.add(element)
        mPager = findViewById<ViewPager>(R.id.pager)
        mPager!!.setAdapter(MyAdapter(this, ImgArray))
        val indicator = findViewById<CircleIndicator>(R.id.indicator)
        indicator.setViewPager(mPager)
        val handler = Handler()
        val Update = Runnable {
            if (currentPage === img.size) {
                currentPage = 0
            }
            mPager!!.setCurrentItem(currentPage++, true)
        }
        //Auto start
        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 0, 2500)
    }

}