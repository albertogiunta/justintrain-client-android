package com.jaus.albertogiunta.justintrain_oraritreni.utils.components;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.jaus.albertogiunta.justintrain_oraritreni.R;

import butterknife.ButterKnife;

public class ViewsUtils {

    public static final ButterKnife.Action<View> VISIBLE = (view, index) -> view.setVisibility(View.VISIBLE);

    public static final ButterKnife.Action<View> GONE = (view, index) -> view.setVisibility(View.GONE);

    public static final ButterKnife.Action<View> INVISIBLE = (view, index) -> view.setVisibility(View.INVISIBLE);



    public enum COLORS {
        WHITE, YELLOW, ORANGE, RED, GREEN, BLACK, GREY_LIGHTER, BLUE
    }

    public static int getTimeDifferenceColor(Context context, COLORS color) {
        switch (color) {
            case WHITE:
                return getColor(context, R.color.txt_white);
            case YELLOW:
                return getColor(context, R.color.txt_yellow);
            case ORANGE:
                return getColor(context, R.color.txt_orange);
            case RED:
                return getColor(context, R.color.txt_red);
            case GREEN:
                return getColor(context, R.color.txt_green);
            case BLACK:
                return getColor(context, R.color.txt_dark);
            case GREY_LIGHTER:
                return getColor(context, R.color.txt_grey_lighter);
            case BLUE:
                return getColor(context, R.color.btn_dark_cyan);
            default:
                return getColor(context, R.color.txt_dark);
        }
    }

    public static int getTimeDifferenceColor(Context context, int timeDifference) {
        int color = getColor(context, R.color.txt_dark);
        if (timeDifference == 0) {
            color = getColor(context, R.color.ontime);
        } else if (timeDifference > 0) {
            color = getColor(context, R.color.late1);
        } else if (timeDifference < 0) {
            color = getColor(context, R.color.early1);
        }
        return color;
    }

    public static int getColor(Context context, int resource) {
        return ContextCompat.getColor(context, resource);
    }

    public static int convertDPtoPX(Context context, float dp) {
        float d = context.getResources().getDisplayMetrics().density;
        return (int) (dp * d); // margin in pixels
    }

}
