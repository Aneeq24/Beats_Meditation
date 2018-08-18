package com.las.atmosherebinauraltherapy.helper;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.support.constraint.ConstraintLayout;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.las.atmosherebinauraltherapy.R;

/**
 * Created by RanaTalal on 6/16/2017.
 */

public class AnimationHelper {
    public static AnimationHelper animationHelper;
    Resources r = AppConstant.CONTEXT.getResources();

    public static AnimationHelper getInstance() {
        if (animationHelper == null) {
            animationHelper = new AnimationHelper();
        }
        return animationHelper;
    }


    public void layoutTopToBottom(int height, ConstraintLayout view) {
        float value = height / 2;
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, r.getDisplayMetrics());
        value = value + px;
        ObjectAnimator animX = ObjectAnimator.ofFloat(view, "x", 0f);
        ObjectAnimator animY = ObjectAnimator.ofFloat(view, "y", value);
        animY.setDuration(1000);
        AnimatorSet animSetXY = new AnimatorSet();
        animSetXY.playTogether(animX, animY);
        animSetXY.start();
    }

    public void layoutBottomToTop(int height, ConstraintLayout view, final ConstraintLayout settingView, final ImageView settingBtn) {

        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56, r.getDisplayMetrics());
        float value = height;

        ObjectAnimator animX = ObjectAnimator.ofFloat(view, "x", 0f);
        ObjectAnimator animY = ObjectAnimator.ofFloat(view, "y", px);
        animY.setDuration(1000);
        AnimatorSet animSetXY = new AnimatorSet();
        animSetXY.playTogether(animX, animY);
        animSetXY.start();
        animSetXY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                settingView.setVisibility(View.GONE);
                settingBtn.setClickable(true);
            }
        });
    }

    public void buttonPullDown(int height, ConstraintLayout view) {
        float value = height / 2;

        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, r.getDisplayMetrics());
        value = value + px;

        ObjectAnimator animX = ObjectAnimator.ofFloat(view, "x", 0f);
        ObjectAnimator animY = ObjectAnimator.ofFloat(view, "y", value);
        animY.setDuration(1000);
        AnimatorSet animSetXY = new AnimatorSet();
        animSetXY.playTogether(animX, animY);
        animSetXY.start();
    }

    public void buttonPullUp(int height, ConstraintLayout view) {
        float value = height / 2;

        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, r.getDisplayMetrics());
        value = value - px;

        ObjectAnimator animX = ObjectAnimator.ofFloat(view, "x", 0f);
        ObjectAnimator animY = ObjectAnimator.ofFloat(view, "y", value);
        animY.setDuration(1000);
        AnimatorSet animSetXY = new AnimatorSet();
        animSetXY.playTogether(animX, animY);
        animSetXY.start();
    }

    public void rotationAnimation(Context context, ImageView imageButton) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.rotation_animation);
        animation.setDuration(1000);
        animation.setRepeatMode(Animation.RESTART);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setInterpolator(new LinearInterpolator());
        imageButton.startAnimation(animation);

    }
}
