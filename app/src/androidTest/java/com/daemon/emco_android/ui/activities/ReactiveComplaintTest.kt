package com.daemon.emco_android.ui.activities

import android.view.View
import android.widget.DatePicker
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.*
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.daemon.emco_android.R
import com.daemon.emco_android.ui.adapter.ViewComplaintPPEListAdapter
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.anything
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


@LargeTest
@RunWith(AndroidJUnit4::class)
class ReactiveComplaintTest{

    @Rule
    @JvmField
    val mActivityRule = ActivityTestRule(MainActivity::class.java)


    val complaintNo="19042235"
    val defect="A/C Less Cooling"
    val defectComment="Test Defect"
    val workStatus="Pending"
    val workDoneComment="Test Comment"
    val reason="Others"


    @Test
    fun reactiveComplaint(){
        onView(withId(R.id.btn_reactive_maintenance)).perform(click())
        onView(withId(R.id.btn_receive_complaint)).perform(click())
        sleep(1000)
        onView(withId(android.R.id.button1)).perform(click())
        onView(CoreMatchers.allOf(withId(R.id.tv_complaint_no), withText(complaintNo))).perform(click())
        onView(withId(R.id.rx_complaint_view_nested_scroll)).perform(swipeUp())
        onView(withId(R.id.btn_next)).perform(click())
        // After responding screen


        // filling defect found

        onView(withId(R.id.tv_select_defectsfound)).perform(click())
        onView(withText(defect)).perform(ViewActions.click())
        onView(withId(R.id.tie_defectsfound))
                .perform(clearText(),typeText(defectComment), ViewActions.closeSoftKeyboard())

        // filling work status
        onView(withId(R.id.tv_select_workstatus)).perform(ViewActions.click())
        onView(withText(workStatus)).perform(ViewActions.click())
        onView(withText("CHOOSE")).perform(ViewActions.click())

        // fill work done comment
        onView(withId(R.id.tie_workdone))
                .perform(clearText(),typeText(workDoneComment), ViewActions.closeSoftKeyboard())

        // scroll up
        onView(withId(R.id.nested_scroll_rx_respond)).perform(swipeUp())

        try{
            // filling reason
            onView(withId(R.id.tv_select_reason)).perform( click())
            onView(withText(reason)).perform(click())

      } catch (e: Exception) {
          e.printStackTrace()
      }

        // Tenative date picker
        onView(withId(R.id.tv_select_tentative_date)).perform(click())
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DATE]
        onView(withClassName(Matchers.equalTo(DatePicker::class.java.name))).perform(PickerActions.setDate(year, month, day))
        onView(withId(android.R.id.button1)).perform(click())

       // Filling PPE Details

        onView(withId(R.id.btn_ppe)).perform(click())

        // to select checkbox inside a recyclerview item
        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition<RecyclerView.ViewHolder>(0,clickItemWithId(R.id.rd_yes)))
        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition<RecyclerView.ViewHolder>(0,clickItemWithId(R.id.rd_no)))

        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition<RecyclerView.ViewHolder>(0,clickItemWithId(R.id.rd_nr)))

        onView(withId(R.id.btn_save)).perform(click())
        sleep(1000)
        try{
            onView(withText("OKAY")).perform(click())
        }
        catch (e:Exception){
         Espresso.pressBack()
        }


// save the complaint and navigating to home screen
        onView(withId(R.id.btn_respond_save)).perform(click())

        sleep(1000)

        onView(withText("OKAY")).perform(click())
        onView(withId(R.id.action_home)).perform(click())

        sleep(1000)

    }

    fun clickItemWithId(id: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View>? {
                return null
            }

            override fun getDescription(): String {
                return "Click on a child view with specified id."
            }

            override fun perform(uiController: UiController, view: View) {
                val v = view.findViewById<View>(id) as View
                v.performClick()
            }
        }
    }


    @Test
    @Ignore
    fun contactUs(){

        onView(withId(R.id.btn_help_support)).perform(ViewActions.click())

    }

}