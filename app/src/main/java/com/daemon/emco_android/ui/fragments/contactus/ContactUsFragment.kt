package com.daemon.emco_android.ui.fragments.contactus

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.util.Linkify
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.daemon.emco_android.R
import com.daemon.emco_android.ui.activities.LoginActivity
import com.daemon.emco_android.utils.SessionManager
import com.github.florent37.expectanim.ExpectAnim
import com.github.florent37.expectanim.core.Expectations
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter
import kotlinx.android.synthetic.main.help_support_fragment.*
import kotlinx.android.synthetic.main.help_support_fragment.view.*
import kotlinx.android.synthetic.main.toolbar.*


class ContactUsFragment : Fragment() {

    private var mActivity: AppCompatActivity? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity as AppCompatActivity?
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var view=inflater.inflate(R.layout.help_support_fragment, container, false)

        setupActionBar()
        setContent(view)
        animateCardview(view)
        Linkify.addLinks(view.text_weblink1, Linkify.ALL);
        Linkify.addLinks(view.text_weblink2, Linkify.ALL);

        view.fabSpeedDial.setMenuListener(object : SimpleMenuListenerAdapter() {
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                //TODO: Start some activity
                when (menuItem.itemId) {
                    R.id.action_email -> {
                        emailSupport()
                    }
                    R.id.action_call -> {
                        checkTelPermission()
                    }
                }
                return false
            }
        })

        return view
    }



    fun emailSupport() {
        val email = Intent(Intent.ACTION_SEND)
        email.putExtra(Intent.EXTRA_EMAIL, arrayOf(resources.getString(R.string.mail)))
        email.putExtra(Intent.EXTRA_SUBJECT, "subject")
        email.putExtra(Intent.EXTRA_TEXT, "message")
        email.type = "message/rfc822"

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

    fun setupActionBar() {
       val mToolbar = mActivity!!.findViewById<View>(R.id.toolbar) as Toolbar
       val tv_toolbar_title = mToolbar.findViewById(R.id.tv_toolbar_title) as TextView
        tv_toolbar_title.setText("Contact Us")
        mActivity!!.setSupportActionBar(mToolbar)
        mActivity!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mActivity!!.supportActionBar!!.setDisplayShowTitleEnabled(false)
        mToolbar.setNavigationOnClickListener(
                View.OnClickListener { mActivity!!.onBackPressed() })
    }

    fun checkTelPermission() {
        if (ContextCompat.checkSelfPermission(context!!,
                        Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity,
                            Manifest.permission.CALL_PHONE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(context as Activity,
                        arrayOf(Manifest.permission.CALL_PHONE),
                        42)
            }
        } else {
            // Permission has already been granted
            callPhone()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 42) {
            // If request is cancelled, the result arrays are empty.
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // permission was granted, yay!
                callPhone()
            } else {
                // permission denied, boo! Disable the
                // functionality
            }
            return
        }
    }

    fun callPhone(){
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:"+resources.getString(R.string.tel))
        startActivity(callIntent)
    }

    private var expectAnimMove: ExpectAnim? = null

    fun animateCardview(view:View){

        ExpectAnim()
                .expect(view.contact_card_view1)
                .toBe(
                        Expectations.outOfScreen(Gravity.RIGHT),
                        Expectations.invisible()
                )
                .toAnimation()
                .setNow()

        ExpectAnim()
                .expect(view.contact_card_view2)
                .toBe(
                        Expectations.outOfScreen(Gravity.RIGHT),
                        Expectations.invisible()
                )
                .toAnimation()
                .setNow()

        this.expectAnimMove = ExpectAnim()
                .expect(view.contact_card_view1)
                .toBe(
                        Expectations.atItsOriginalPosition(),
                        Expectations.visible()
                ).expect(view.contact_card_view2)
                .toBe(
                        Expectations.atItsOriginalPosition(),
                        Expectations.visible()
                )
                .toAnimation()
                .setDuration(800)
                .start()

    }

    fun setContent(view:View){

        var title1AG="AG Facilities Solutions LLC"
        var title2AG="AG Facilities Solutions for Contracting and General Maintenance LLC"

        var po1AG="P.O. Box 283307, Dubai, UAE"
        var po2AG="P.O. Box 27245, Abu Dhabi, UAE"

        var tel1AG="Tel : 04 605 2501"
        var tel2AG="Tel : 04 605 2501"

        var email1AG="Email : agfs.dxb@agfacilities.com"
        var email2AG="Email : agfs.auh@agfacilities.com"

        var website1AG="Website : www.agfacilitiesme.com"
        var website2AG="Website : www.agfacilitiesme.com"

        if (SessionManager.getSessionForURL(LoginActivity.IP_ADDRESS, activity) != null && !SessionManager.getSessionForURL(LoginActivity.IP_ADDRESS, activity).trim { it <= ' ' }.isEmpty()
                && SessionManager.getSessionForURL(LoginActivity.IP_ADDRESS, activity).contains(LoginActivity.MBM)) {

             title1AG="MODERN BUILDING MAINTENANCE LLC"
             title2AG="MODEL BUILDING MAINTENANCE L.L.C."

             po1AG="P.O. Box 46500, Dubai, UAE"
             po2AG="P.O. Box 39703, Abu Dhabi, UAE"

             tel1AG="Tel: 04-605 2500"
             tel2AG="Tel: 02-632 0097"

             email1AG="Email : info@mbm.ae"
             email2AG="Email : abudhabi@mbm.ae"

             website1AG="Website : www.mbm.ae"
             website2AG="Website : www.mbm.ae"

        }

        if(resources.getString(R.string.app_name).equals("EMCO CAFM")) {

            title1AG="EMCO - QATAR"

            po1AG="P.O. BOX 24125 , Doha - Qatar"

            tel1AG="Tel: +974 4435 9188"

            email1AG="Email : mail@emcoqatar.net"

        }

        view.tv_title1.text=title1AG
        view.tv_title2.text=title2AG

        view.tv_po1.text=po1AG
        view.tv_po2.text=po2AG

        view.tv_tel1.text=tel1AG
        view.tv_tel2.text=tel2AG

        view.tv_email1.text=email1AG
        view. tv_email2.text=email2AG

        view. text_weblink1.text=website1AG
        view. text_weblink2.text=website2AG

    }


}