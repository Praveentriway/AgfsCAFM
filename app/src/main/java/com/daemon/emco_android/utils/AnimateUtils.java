package com.daemon.emco_android.utils;

import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.github.florent37.expectanim.ExpectAnim;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.github.florent37.expectanim.core.Expectations.atItsOriginalPosition;
import static com.github.florent37.expectanim.core.Expectations.invisible;
import static com.github.florent37.expectanim.core.Expectations.outOfScreen;
import static com.github.florent37.expectanim.core.Expectations.visible;

public class AnimateUtils {


    public void fabAnimate(FloatingActionButton fab){

        new ExpectAnim()
                .expect(fab)
                .toBe(
                        outOfScreen(Gravity.BOTTOM),
                        invisible()
                )
                .toAnimation()
                .setNow();


        new ExpectAnim()
                .expect(fab)
                .toBe(
                        atItsOriginalPosition(),
                        visible()
                )
                .toAnimation()
                .setDuration(800)
                .start();

    }

    public void filterLayoutAnimate(LinearLayout linear){

        new ExpectAnim()
                .expect(linear)
                .toBe(
                        outOfScreen(Gravity.TOP),
                        invisible()
                )
                .toAnimation()
                .setNow();


        new ExpectAnim()
                .expect(linear)
                .toBe(
                        atItsOriginalPosition(),
                        visible()
                )
                .toAnimation()
                .setDuration(800)
                .start();

    }

    public void splashAnimate(View tv_login ,View tv_signup){

              new ExpectAnim()

                .expect(tv_login)
                .toBe(
                        outOfScreen(Gravity.TOP),
                        invisible()
                )
                .toAnimation()
                .setNow();


              new ExpectAnim()

                .expect(tv_signup)
                .toBe(
                        outOfScreen(Gravity.BOTTOM),
                        invisible()
                )
                .toAnimation()
                .setNow();


                 new ExpectAnim()
                .expect(tv_login)
                .toBe(
                        atItsOriginalPosition(),
                        visible()
                ).expect(tv_signup)
                .toBe(
                        atItsOriginalPosition(),
                        visible()
                )
                .toAnimation()
                .setDuration(600)
                .start();

    }



}
