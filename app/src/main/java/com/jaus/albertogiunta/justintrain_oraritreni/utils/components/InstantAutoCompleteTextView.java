package com.jaus.albertogiunta.justintrain_oraritreni.utils.components;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.AutoCompleteTextView;

/**
 * This class was overridden in order to have an AutocompleteTextView that could perform a query even when it only got focused
 * (hence before any input from the user)
 */
public class InstantAutoCompleteTextView extends AutoCompleteTextView {
    public InstantAutoCompleteTextView(Context context) {
        super(context);
    }

    public InstantAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InstantAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK &&
                event.getAction() == KeyEvent.ACTION_UP) {
            this.dismissDropDown();
            return false;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        // performFiltering is called also when the text view just got focused
        if (focused) {
            performFiltering("", 0);
        }
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

}
