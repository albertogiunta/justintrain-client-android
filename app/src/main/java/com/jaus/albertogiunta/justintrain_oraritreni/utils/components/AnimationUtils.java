package com.jaus.albertogiunta.justintrain_oraritreni.utils.components;

import android.view.View;
import android.view.animation.OvershootInterpolator;

public class AnimationUtils {

    public enum ANIM_TYPE {
        LIGHT(1.05f), MEDIUM(1.2f), HEAVY(1.2f);

        private float scalingOffset;

        ANIM_TYPE(float scalingOffset) {
            this.scalingOffset = scalingOffset;
        }

        public float getScalingOffset() {
            return scalingOffset;
        }
    }

    private final static float SCALING_OFFSET = 1.2f;

    public static void animOnPress(View v, ANIM_TYPE animType) {
        v.setScaleX(animType.getScalingOffset());
        v.setScaleY(animType.getScalingOffset());
        v.animate().setInterpolator(new OvershootInterpolator()).scaleX(1).scaleY(1).setDuration(250);
    }

    public static void onCompare(View v) {
        v.setScaleX(0f);
        v.setScaleY(0f);
        v.animate().setInterpolator(new OvershootInterpolator()).scaleX(1).scaleY(1).setDuration(150);
    }

}
