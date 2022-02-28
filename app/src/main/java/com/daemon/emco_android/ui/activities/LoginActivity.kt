package com.daemon.emco_android.ui.activities

import android.Manifest.permission
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.daemon.emco_android.R
import com.daemon.emco_android.ui.fragments.user.Login
import com.daemon.emco_android.utils.AppUtils
import com.daemon.emco_android.utils.SessionManager
import io.github.inflationx.viewpump.ViewPumpContextWrapper


class LoginActivity : AppCompatActivity() {
    private lateinit var mPreferences: SharedPreferences
    private val PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!checkLocationPermission()) {
            ActivityCompat.requestPermissions(this, arrayOf(permission.ACCESS_COARSE_LOCATION, permission.ACCESS_FINE_LOCATION, permission.READ_PHONE_STATE), PERMISSION_REQUEST_CODE)
        } else {
            statusCheck()
        }
        try {
            mPreferences = getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE)
            val loginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null)
            if (loginData != null && loginData.isNotEmpty()) {
                loadFragment()
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
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.visibility= View.GONE
        val imageInToolbar = toolbar.findViewById<View>(R.id.img_toolbar) as ImageView
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        if (SessionManager.getSessionForURL(IP_ADDRESS, this) != null && !SessionManager.getSessionForURL(IP_ADDRESS, this).trim { it <= ' ' }.isEmpty()
                && SessionManager.getSessionForURL(IP_ADDRESS, this).contains(MBM)) {
            imageInToolbar.setImageDrawable(ContextCompat.getDrawable(this@LoginActivity, R.drawable.logo_mbm_png_9))
        } else {
            imageInToolbar.setImageDrawable(ContextCompat.getDrawable(this@LoginActivity, R.drawable.ag_logo_white))
        }
    }

    fun loadFragment() {
        val fragment: Fragment = Login()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
        fragmentTransaction.replace(R.id.frame_container, fragment)
        fragmentTransaction.commit()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    /**
     * All about permission
     */

    private fun checkLocationPermission(): Boolean {
        val result3 = ContextCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION)
        val result4 = ContextCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION)
        val result5 = ContextCompat.checkSelfPermission(this, permission.READ_PHONE_STATE)
        return result3 == PackageManager.PERMISSION_GRANTED && result4 == PackageManager.PERMISSION_GRANTED && result5 == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.size > 0) {
                val coarseLocation = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val fineLocation = grantResults[1] == PackageManager.PERMISSION_GRANTED
                val phonestate = grantResults[2] == PackageManager.PERMISSION_GRANTED
                if (coarseLocation && fineLocation && phonestate) {
                    statusCheck()
                } else {
                    AppUtils.showDialog(this, resources.getString(R.string.permission_msg))
                }
            }
        }
    }

    fun statusCheck() {
        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }
    }

    private fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Your GPS seems to be disabled, Please enable it.")
                .setCancelable(false)
                .setPositiveButton("Ok") { dialog, id -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
                .setNegativeButton("No") { dialog, id -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }

    companion object {
        private val TAG = LoginActivity::class.java.simpleName
        @JvmField
        var IP_ADDRESS = "ip_address"
        @JvmField
        var MBM = "mbm"
    }
}