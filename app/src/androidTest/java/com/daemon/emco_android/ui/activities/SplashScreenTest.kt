package com.daemon.emco_android.ui.activities


import android.graphics.Point
import android.graphics.Rect
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.GeneralLocation
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Tap
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.daemon.emco_android.R
import com.daemon.emco_android.ui.activities.action.GeneralClickAction
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class SplashScreenTest {

    @Rule @JvmField
    val mActivityRule = ActivityTestRule(SplashScreen::class.java)

    val serverURL="cafm.agfacilities.com"



    @Test
    fun splashScreenTest() {
        splashLogic()
    }

    fun splashLogic(){

        try {
            splashLogin()
        } catch (e: NoMatchingViewException) {
            logOut()
            login()
        }
    }

    fun splashLogin() {

        onView(withId(R.id.tv_login)).perform(click())
        onView(withId(R.id.tie_username))
                .perform(typeText("100121"), closeSoftKeyboard()); //type email and hide keyboard

        onView(withId(R.id.tie_password))
                .perform(typeText("100121"), closeSoftKeyboard());//type password and hide keyboard

        onView(withId(R.id.tie_serverurl)).perform(clickDrawables())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`(serverURL))).perform(click())
        onView(withText("OK")).perform(click());
        onView(withId(R.id.btnLogin)).perform(click())

    }

    private fun login() {

        onView(withId(R.id.tie_username))
                .perform(typeText("100121"), closeSoftKeyboard()); //type email and hide keyboard

        onView(withId(R.id.tie_password))
                .perform(typeText("100121"), closeSoftKeyboard());//type password and hide keyboard

        onView(withId(R.id.tie_serverurl)).perform(clickDrawables())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`(serverURL))).perform(click())
        onView(withText("OK")).perform(click());
        onView(withId(R.id.btnLogin)).perform(click())
    }

    fun logOut() {
        onView(withId(R.id.fab_menu)).perform(click());
        onView(withText("Logout")).perform(click());
        onView(withText("YES")).perform(click());
    }


    fun customClick(): ViewAction? {
        return actionWithAssertions(
                GeneralClickAction (Tap.SINGLE, GeneralLocation.VISIBLE_CENTER, Press.FINGER))
    }

    fun clickDrawables(): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> //must be a textview with drawables to do perform
            {
                return allOf(isAssignableFrom(TextView::class.java), object : BoundedMatcher<View?, TextView>(TextView::class.java) {
                    override fun matchesSafely(tv: TextView): Boolean {
                        if (tv.requestFocusFromTouch()) //get fpocus so drawables become visible
                            for (d in tv.compoundDrawables)  //if the textview has drawables then return a match
                                if (d != null) return true
                        return false
                    }

                    override fun describeTo(description: Description) {
                        description.appendText("has drawable")
                    }
                })
            }

            override fun getDescription(): String {
                return "click drawables"
            }

            override fun perform(uiController: UiController, view: View) {
                val tv = view as TextView
                if (tv != null && tv.requestFocusFromTouch()) //get focus so drawables are visible
                {
                    val drawables = tv.compoundDrawables
                    val tvLocation = Rect()
                    tv.getHitRect(tvLocation)
                    val tvBounds = arrayOfNulls<Point>(4) //find textview bound locations
                    tvBounds[0] = Point(tvLocation.left, tvLocation.centerY())
                    tvBounds[1] = Point(tvLocation.centerX(), tvLocation.top)
                    tvBounds[2] = Point(tvLocation.right, tvLocation.centerY())
                    tvBounds[3] = Point(tvLocation.centerX(), tvLocation.bottom)
                    for (location in 0..3) if (drawables[location] != null) {
                        val bounds = drawables[location]!!.bounds
                        tvBounds[location]!!.offset(bounds.width() / 2, bounds.height() / 2) //get drawable click location for left, top, right, bottom
                        if (tv.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, tvBounds[location]!!.x.toFloat(), tvBounds[location]!!.y.toFloat(), 0))) tv.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, tvBounds[location]!!.x.toFloat(), tvBounds[location]!!.y.toFloat(), 0))
                    }
                }
            }
        }
    }


}
