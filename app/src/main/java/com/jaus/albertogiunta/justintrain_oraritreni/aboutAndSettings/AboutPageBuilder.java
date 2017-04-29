package com.jaus.albertogiunta.justintrain_oraritreni.aboutAndSettings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.jaus.albertogiunta.justintrain_oraritreni.R;

public class AboutPageBuilder {

    private final LayoutInflater inflater;
    private final View           view;
    private final LinearLayout   linearLayout;

    public AboutPageBuilder(Context context) {
        inflater = LayoutInflater.from(context);
        this.view = inflater.inflate(R.layout.activity_about_settings, null);
        this.linearLayout = (LinearLayout) view.findViewById(R.id.ll_container);
    }

    public AboutPageBuilder addItem(View v) {
        linearLayout.addView(v);
        return this;
    }

    public AboutPageBuilder addSeparator() {
        View v = inflater.inflate(R.layout.item_element_separator, linearLayout, false);
        linearLayout.addView(v);
        return this;
    }

    public View build() {
        return view;
    }
}
