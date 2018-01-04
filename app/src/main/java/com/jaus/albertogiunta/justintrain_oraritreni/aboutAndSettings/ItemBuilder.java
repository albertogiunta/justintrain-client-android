package com.jaus.albertogiunta.justintrain_oraritreni.aboutAndSettings;

import com.google.android.flexbox.FlexboxLayout;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaus.albertogiunta.justintrain_oraritreni.R;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.ENUM_CATEGORIES;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers.AnalyticsHelper;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.SettingsPreferences;

import static butterknife.ButterKnife.apply;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.components.ViewsUtils.GONE;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.components.ViewsUtils.VISIBLE;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.SCREEN_SETTINGS;

public class ItemBuilder {

    private final LayoutInflater inflater;
    private final LinearLayout   parent;
    private       View           view;

    public ItemBuilder(Context context, LinearLayout parent) {
        this.inflater = LayoutInflater.from(context);
        this.parent = parent;
    }

    public ItemBuilder addItemGroupHeader(String header) {
        view = inflater.inflate(R.layout.item_element_header, parent, false);
        RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.rl_container);
        TextView       tv = (TextView) rl.findViewById(R.id.tv_header);
        tv.setText(header);
        return this;
    }

    public ItemBuilder addItemWithTitle(String title) {
        view = inflater.inflate(R.layout.item_element_title, parent, false);
        TextView tv = (TextView) view.findViewById(R.id.tv_title);
        tv.setText(title);
        return this;
    }

    public ItemBuilder addItemWithIconTitle(int icon, String title) {
        view = inflater.inflate(R.layout.item_element_icon_title, parent, false);
        ImageView iv = (ImageView) view.findViewById(R.id.iv_icon);
        iv.setImageResource(icon);
        TextView tv = (TextView) view.findViewById(R.id.tv_title);
        tv.setText(title);
        return this;
    }

    public ItemBuilder addItemWithTitleSubtitle(String title, String subtitle) {
        view = inflater.inflate(R.layout.item_element_title_subtitle, parent, false);
        TextView tv = (TextView) view.findViewById(R.id.tv_title);
        tv.setText(title);
        TextView tv2 = (TextView) view.findViewById(R.id.tv_subtitle);
        tv2.setText(subtitle);
        return this;
    }

    public ItemBuilder addItemWithIconTitleSubtitle(int icon, String title, String subtitle) {
        view = inflater.inflate(R.layout.item_element_icon_title_subtitle, parent, false);
        ImageView iv = (ImageView) view.findViewById(R.id.iv_icon);
        iv.setImageResource(icon);
        TextView tv = (TextView) view.findViewById(R.id.tv_title);
        tv.setText(title);
        TextView tv2 = (TextView) view.findViewById(R.id.tv_subtitle);
        tv2.setText(subtitle);
        return this;
    }

    public ItemBuilder addItemPrefWithToggle(Context context, String title, String subtitle, boolean isChecked, boolean useSwitch, boolean isProFeature, boolean userIsPro, String preferenceCONST, String settingsEnabledAction, String settingsDisabledAction) {
        view = inflater.inflate(R.layout.item_element_title_subtitle_checkbox, parent, false);
        TextView tv = (TextView) view.findViewById(R.id.tv_title);
        tv.setText(title);
        TextView tv2 = (TextView) view.findViewById(R.id.tv_subtitle);
        tv2.setText(subtitle);
        CheckBox     checkboxButton = (CheckBox) view.findViewById(R.id.cb_pref);
        SwitchCompat switchButton   = (SwitchCompat) view.findViewById(R.id.sw_pref);
        checkboxButton.setChecked(isChecked);
        switchButton.setChecked(isChecked);
        if (useSwitch) {
            apply(checkboxButton, GONE);
            apply(switchButton, VISIBLE);
        } else {
            apply(checkboxButton, VISIBLE);
            apply(switchButton, GONE);
        }
        View.OnClickListener listener = view1 -> {
            if (SettingsPreferences.isGenericSettingEnabled(context, preferenceCONST, true)) {
                SettingsPreferences.disableGenericSetting(context, preferenceCONST);
                AnalyticsHelper.getInstance(context).logScreenEvent(SCREEN_SETTINGS, settingsDisabledAction);
                checkboxButton.setChecked(false);
                switchButton.setChecked(false);
            } else {
                SettingsPreferences.enableGenericSetting(context, preferenceCONST);
                AnalyticsHelper.getInstance(context).logScreenEvent(SCREEN_SETTINGS, settingsEnabledAction);
                checkboxButton.setChecked(true);
                switchButton.setChecked(true);
            }
        };
        if (!isProFeature || userIsPro) {
            checkboxButton.setEnabled(true);
            switchButton.setEnabled(true);
            checkboxButton.setOnClickListener(listener);
            switchButton.setOnClickListener(listener);
            view.setOnClickListener(listener);
        } else {
            checkboxButton.setChecked(true);
            switchButton.setChecked(true);
            checkboxButton.setEnabled(false);
            switchButton.setEnabled(false);
        }
        return this;
    }

    public ItemBuilder addItemTrainCategories(Context context) {
        view = inflater.inflate(R.layout.item_element_categories_checkboxes, parent, false);
        TextView      tv            = (TextView) view.findViewById(R.id.tv_title);
        TextView      tv2           = (TextView) view.findViewById(R.id.tv_subtitle);
        FlexboxLayout fblCategories = (FlexboxLayout) view.findViewById(R.id.fbl_categories);
        tv.setText("Categorie di treni incluse nella ricerca");
        tv2.setText(SettingsPreferences.getCategoriesAsString(context, " / "));
        for (ENUM_CATEGORIES cat : ENUM_CATEGORIES.values()) {
            CheckBox                   cbCat  = new CheckBox(context);
            FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.flexBasisPercent = 0.5f;
            cbCat.setLayoutParams(params);
            cbCat.setText(cat.getAlias());
            cbCat.setChecked(SettingsPreferences.isCategoryEnabled(context, cat));
            cbCat.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked || (!isChecked && SettingsPreferences.isPossibleToDisableCheckbox(context))) {
                    SettingsPreferences.setCategory(context, cat.name(), isChecked);
                    tv2.setText(SettingsPreferences.getCategoriesAsString(context, " / "));
                } else {
                    cbCat.setChecked(true);
                }
            });
            fblCategories.addView(cbCat);
        }
        return this;
    }

    public ItemBuilder addItemLongText(String text) {
        view = inflater.inflate(R.layout.item_element_text_background, parent, false);
        TextView tv = (TextView) view.findViewById(R.id.tv_text);
        tv.setText(text);
        return this;
    }

    public ItemBuilder addOnClickListener(View.OnClickListener listener) {
        view.setOnClickListener(listener);
        return this;
    }

    public View build() {
        return this.view;
    }
}
