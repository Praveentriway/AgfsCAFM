package com.daemon.emco_android.ui.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.daemon.emco_android.App
import com.daemon.emco_android.R
import com.daemon.emco_android.ui.fragments.user.UserRegisteration
import com.daemon.emco_android.utils.AppUtils
import com.daemon.emco_android.utils.SessionManager
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.toolbar.*


class RegisterActivity : AppCompatActivity() {
    private lateinit var mPreferences: SharedPreferences
    private lateinit var mEditor: SharedPreferences.Editor
    private val font = App.getInstance().getFontInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
            mPreferences = getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE)
            mEditor = mPreferences.edit()
            val loginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null)
            if (loginData != null && loginData.length > 0) {
                Log.d(TAG, "getLoginData $loginData")
                val mainActivity = Intent(this, MainActivity::class.java)
                startActivity(mainActivity)
                finish()
                return
            } else loadFragment()
            setupActionbar()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    private fun setupActionbar() {

        toolbar.setTitle(R.string.app_name)

        setSupportActionBar(toolbar)
        if (SessionManager.getSessionForURL(LoginActivity.IP_ADDRESS, this) != null && !SessionManager.getSessionForURL(LoginActivity.IP_ADDRESS, this).trim { it <= ' ' }.isEmpty()
                && SessionManager.getSessionForURL(LoginActivity.IP_ADDRESS, this).contains(LoginActivity.MBM)) {
            img_toolbar.setImageDrawable(ContextCompat.getDrawable(this@RegisterActivity, R.drawable.logo_mbm_png))
        } else {
            img_toolbar.setImageDrawable(ContextCompat.getDrawable(this@RegisterActivity, R.drawable.ag_logo_white))
        }
        var titleTextView: TextView? = null
        try {
            val f = toolbar.javaClass.getDeclaredField("mTitleTextView")
            f.isAccessible = true
            titleTextView = f[toolbar] as TextView
            titleTextView.typeface = font!!.helveticaRegular
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }

    fun loadFragment() { // update the main content by replacing fragments
        val fragment: Fragment = UserRegisteration()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
        fragmentTransaction.replace(R.id.frame_container, fragment)
        fragmentTransaction.commit()


    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    companion object {
        private val TAG = RegisterActivity::class.java.simpleName
    }
}