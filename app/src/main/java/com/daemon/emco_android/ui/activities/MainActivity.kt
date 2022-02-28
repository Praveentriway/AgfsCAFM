package com.daemon.emco_android.ui.activities

import android.Manifest.permission
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.StackingBehavior
import com.crashlytics.android.Crashlytics
import com.daemon.emco_android.App
import com.daemon.emco_android.BuildConfig
import com.daemon.emco_android.R
import com.daemon.emco_android.listeners.AppUpdateListener
import com.daemon.emco_android.listeners.IOnBackPressed
import com.daemon.emco_android.listeners.UserListener
import com.daemon.emco_android.model.common.Login
import com.daemon.emco_android.model.response.CommonResponse
import com.daemon.emco_android.repository.db.dbhelper.ReceiveComplaintItemDbInitializer
import com.daemon.emco_android.repository.db.entity.UserToken
import com.daemon.emco_android.repository.remote.UserService
import com.daemon.emco_android.repository.remote.restapi.ApiClient
import com.daemon.emco_android.repository.remote.restapi.ApiInterface
import com.daemon.emco_android.service.EmployeeTrackingService
import com.daemon.emco_android.ui.fragments.common.MainDashboard
import com.daemon.emco_android.ui.fragments.reactive.FragmentRxSubmenu
import com.daemon.emco_android.ui.fragments.user.ChangePassword
import com.daemon.emco_android.ui.fragments.user.UserProfile
import com.daemon.emco_android.utils.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.layout_drawer_menu.*
import kotlinx.android.synthetic.main.nav_header_main2.*
import kotlinx.android.synthetic.main.toolbar.*
// I commented update feild on 377 line
class MainActivity : AppCompatActivity(), UserListener, View.OnClickListener, AppUpdateListener {
    private val TAG = MainActivity::class.java.simpleName
    private val PERMISSION_REQUEST_CODE = 1
    private var mInterface: ApiInterface? = null
    private var mArgs: Bundle? = null
    var updateShow = false
    var mServiceIntent: Intent? = null
    private var mAppBarConfiguration: AppBarConfiguration? = null
    private var mHandler: Handler? = null
    private lateinit var mPreferences: SharedPreferences
    private lateinit var mEditor: SharedPreferences.Editor
    private var mActivity: AppCompatActivity? = null
    private val font = App.getInstance().getFontInstance()
    private var mManager: FragmentManager? = null
    private var mContext: Context? = null
    private lateinit var drawableLogout: Drawable
    private var mLoginData: String? = null
    private var mStrEmpId: String? = null
    private var trackingFlag: String? = null
    private var username: String? = null
    private var email: String? = null
    private var mobile: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_main2)
            mActivity = this
            mContext = applicationContext
            mPreferences = getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE)
            mEditor = mPreferences.edit()
            mHandler = Handler()
            mManager = supportFragmentManager
            mArgs = intent.extras
            drawableLogout = resources.getDrawable(R.drawable.ic_logout)
            drawableLogout.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
            val builder = VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())
            mInterface = ApiClient.getClientLongTime(15, SessionManager.getSession("baseurl", mActivity))!!
                    .create(ApiInterface::class.java)
           // setupActionbar()
            loadFragment()
            logUser()
            setupDrawer()
            if (!checkLocationPermission()) {
                ActivityCompat.requestPermissions(this, arrayOf(permission.ACCESS_COARSE_LOCATION, permission.ACCESS_FINE_LOCATION, permission.READ_PHONE_STATE), PERMISSION_REQUEST_CODE)
            }
            run { statusCheck() }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun logUser() {
        try {
            mLoginData = mPreferences!!.getString(AppUtils.SHARED_LOGIN, null)
            if (mLoginData == null) return
            val gson = Gson()
            val login = gson.fromJson(mLoginData, Login::class.java)
            mStrEmpId = login.employeeId
            trackingFlag = login.trackingFlag
            username = login.firstName.replace("\n", "").replace("\r", "")

            email = if(login.emailId!=null){
                login.emailId
            } else{""}

            mobile = if(login.mobileNumber!=null){
                login.mobileNumber
            } else{""}

            Crashlytics.setUserIdentifier(mStrEmpId)
            Crashlytics.setUserEmail(login.emailId)
            Crashlytics.setUserName(login.userType + login.userName)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        return (NavigationUI.navigateUp(navController, mAppBarConfiguration!!)
                || super.onSupportNavigateUp())
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        mServiceIntent = Intent(this, EmployeeTrackingService::class.java)
        if (AppUtils(applicationContext).isMyServiceRunning(EmployeeTrackingService::class.java, this)) {
            stopService(mServiceIntent)
        }
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }

    private fun setupActionbar() {

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        if (SessionManager.getSessionForURL(LoginActivity.IP_ADDRESS, this) != null && !SessionManager.getSessionForURL(LoginActivity.IP_ADDRESS, this).trim { it <= ' ' }.isEmpty()
                && SessionManager.getSessionForURL(LoginActivity.IP_ADDRESS, this).contains(LoginActivity.MBM)) {
            img_toolbar.setImageDrawable(ContextCompat.getDrawable(this@MainActivity, R.drawable.logo_mbm_png))
        } else {
            img_toolbar.setImageDrawable(ContextCompat.getDrawable(this@MainActivity, R.drawable.ag_logo_white))
        }
        var titleTextView: TextView? = null
        try {
            val f = toolbar.javaClass.getDeclaredField("mTitleTextView")
            f.isAccessible = true
            titleTextView = f[toolbar] as TextView
            titleTextView = f[toolbar] as TextView
            titleTextView.typeface = font!!.helveticaRegular
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }

    fun loadFragment() {
        setupActionbar()
        // update the main content by replacing fragments
        val fragment: Fragment = MainDashboard()
        //val fragment: Fragment = FragmentRxSubmenu()
        fragment.arguments = mArgs
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
        fragmentTransaction.replace(R.id.frame_container, fragment, AppUtils.TAG_FRAGMNENT_MAIN)
        fragmentTransaction.commit()
    }

    override fun onClick(view: View) {
        drawer_layout.closeDrawer(GravityCompat.START)
        when (view.id) {
            R.id.txt_profile -> loadFragment(UserProfile(), Utils.TAG_VIEW_PROFILE)
            R.id.txt_home -> {
                val fm = supportFragmentManager
                var i = 0
                while (i < fm.backStackEntryCount) {
                    fm.popBackStack()
                    ++i
                }
                val _fragment: Fragment = MainDashboard()
                val _transaction = mManager!!.beginTransaction()
                _transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                _transaction.replace(R.id.frame_container, _fragment)
            }
            R.id.txt_change_password -> loadFragment(ChangePassword(), Utils.TAG_CHANGE_PASS)
            R.id.txt_logout -> logout()
            else -> {
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean { // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        menu.findItem(R.id.action_search).isVisible = false
        menu.findItem(R.id.action_refresh).isVisible = false
        menu.findItem(R.id.action_home).isVisible = false
        menu.findItem(R.id.action_logout).isVisible = false
        menu.findItem(R.id.action_logout).icon = drawableLogout
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {

                try {
                    val builder = MaterialDialog.Builder(mActivity!!)
                            .content("Are you sure you want to logout?")
                            .title("Logout")
                            .positiveText(R.string.lbl_yes)
                            .negativeText(R.string.lbl_cancel)
                            .stackingBehavior(StackingBehavior.ADAPTIVE)
                            .onPositive { dialog, which ->
                                dialog.dismiss()
                                clearPreferences()
                                SessionManager.clearSession(mActivity)
                                clearToken()
                                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                    val dialog = builder.build()
                    dialog.show()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            R.id.action_refresh -> {
            }
            R.id.action_home -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    var token: String? = null
    fun clearToken() {
        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w(TAG, "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }
                    token = task.result!!.token
                    deleteToken()
                })
    }

   private fun deleteToken() {
        val user = UserToken()
        user.userId = mStrEmpId
        user.token = token
        UserService(this@MainActivity, this).deleteToken(user)
    }

    private fun clearPreferences() {
        Log.d(TAG, "clearPreferences")
        try {
            mEditor = mPreferences!!.edit()
            mEditor
                    .putString(
                            AppUtils.SHARED_LOGIN_OFFLINE, mPreferences!!.getString(AppUtils.SHARED_LOGIN, null))
                    .commit()
            mEditor.putString("day", null).commit()
            mEditor.putString(AppUtils.SHARED_LOGIN, null).commit()
            mEditor.putString(AppUtils.SHARED_CUSTOMER_LC, null).commit()
            ReceiveComplaintItemDbInitializer(mActivity, null, null).execute(AppUtils.MODE_DELETE)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragments = mManager!!.fragments
        if (fragments != null) {
            for (fragment in fragments) {
                fragment.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    override fun onBackPressed() {
        val main = mManager!!.findFragmentByTag(AppUtils.TAG_FRAGMNENT_MAIN)
        val reactive_main = mManager!!.findFragmentByTag(Utils.TAG_REACTIVE_MAINTENANCE)
        val receive_complaintlist = mManager!!.findFragmentByTag(Utils.TAG_RECEIVED_COMPALINTS)
        if (main != null) {
            if (main.isVisible) { // exit your application
                finish()
                return
            }
        }
        if (reactive_main != null) {
            if (reactive_main.isVisible) {
                super.onBackPressed()
                return
            }
        }
        if (receive_complaintlist != null) {
            if (receive_complaintlist.isVisible) {
                loadFragment(FragmentRxSubmenu(), Utils.TAG_REACTIVE_MAINTENANCE)
                return
            }
        }
        if (fragmentManager.backStackEntryCount > 1) {
            Log.d(
                    TAG, "getFragmentManager().getBackStackEntryCount() "
                    + fragmentManager.backStackEntryCount)
            fragmentManager.popBackStack()
        } else {
            Log.d(TAG, "super.onBackPressed")
            super.onBackPressed()
        }



    }

    fun loadFragment(fragment: Fragment?, tag: String?) {
        Log.d(TAG, "loadFragment")
        try {
            val mPendingRunnable = Runnable {
                // update the main content by replacing fragments
                val fragmentTransaction = mActivity!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.frame_container, fragment!!, tag)
                fragmentTransaction.addToBackStack(tag)
                fragmentTransaction.commit()
            }
            if (mPendingRunnable != null) {
                mHandler!!.post(mPendingRunnable)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d(TAG, " onConfigurationChanged :")
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }
// I commented update feild
    override fun onResume() {
        if (!GpsUtils.isNetworkConnected(mContext)) {
            buildAlertMessageNoGps(resources.getString(R.string.no_internet_msg))
        }
        if (!updateShow) {
            //below line
            AppUpdaterUtils().showDialog(this,this)
        }
        updateShow = true
        super.onResume()
    }

    override fun onLoginDataReceivedSuccess(login: Login?, totalNumberOfRows: String?, token: String?) {}
    override fun onUserDataReceivedSuccess(response: CommonResponse) {}
    override fun onUserDataReceivedFailure(strErr: String) {}
    fun onDrawerOpen() {
        drawer_layout.openDrawer(GravityCompat.START)
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

    var GPS_MSG = "Your GPS seems to be disabled, Please enable it."
    fun statusCheck() {
        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps(GPS_MSG)
        }
    }

    private fun buildAlertMessageNoGps(msg: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Turn on") { dialog, id ->
                    dialog.cancel()
                    if (msg === GPS_MSG) {
                        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    } else {
                        startActivity(Intent(Settings.ACTION_SETTINGS))
                    }
                }
                .setNegativeButton("Dismiss") { dialog, id -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }

    fun logout() {
        try {
            val builder = androidx.appcompat.app.AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to logout?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id ->
                        dialog.dismiss()
                        clearPreferences()
                        SessionManager.clearSession(mActivity)
                        clearToken()
                        val intent = Intent(this@MainActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .setNegativeButton("No") { dialog, id -> dialog.cancel() }
            val alert = builder.create()
            alert.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setupDrawer() {
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        // Passing each menu ID as a set of Ids because each
// menu should be considered as top level destinations.
        mAppBarConfiguration = AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer_layout)
                .build()



        txt_home.setOnClickListener(this)
        txt_profile.setOnClickListener(this)
        txt_logout.setOnClickListener(this)
        txt_change_password.setOnClickListener(this)
        txt_appversion.text = getString(R.string.app_name) + " V " + BuildConfig.VERSION_NAME
        txtusername.text = "$username | $mobile"
        txt_email.text = email
    }


    override fun onUpdateClicked(update: Boolean) {
        clearPreferences()
        SessionManager.clearSession(mActivity)
        clearToken()
        mServiceIntent = Intent(this@MainActivity, EmployeeTrackingService::class.java)
        if (AppUtils(applicationContext).isMyServiceRunning(EmployeeTrackingService::class.java, this@MainActivity)) {
            stopService(mServiceIntent)
        }
        updateShow = update
        val appPackageName = packageName // getPackageName() from Context or Activity object
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
        } catch (anfe: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
        }

    }




}