package com.jaus.albertogiunta.justintrain_oraritreni.journeyFavourites;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaus.albertogiunta.justintrain_oraritreni.R;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.ENUM_HOME_HEADER;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.SettingsPreferences;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static butterknife.ButterKnife.apply;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.components.ViewsUtils.GONE;

public class FavouriteHeaderItem extends AbstractFlexibleItem<FavouriteHeaderItem.SimpleViewHolder> {

    private String  titleText;
    private boolean isFooter;
    private Context context;

    public FavouriteHeaderItem(Context context, ENUM_HOME_HEADER headerType, boolean isFooter) {
        super();
        this.titleText = headerType.getTitle();
        this.isFooter = isFooter;
        setDraggable(false);
        setSwipeable(false);
        if (isFooter) this.context = context;
    }

    @Override
    public int getLayoutRes() {
        if (isFooter) {
            return R.layout.item_home_element_footer;
        } else {
            return R.layout.item_home_element_header;
        }
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public SimpleViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new SimpleViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public void bindViewHolder(final FlexibleAdapter adapter, SimpleViewHolder holder, int position, List payloads) {
        holder.bind(titleText, isFooter);
    }

    public class SimpleViewHolder extends FlexibleViewHolder {

        @BindView(R.id.tv_header)
        TextView  title;
        @BindView(R.id.iv_close_hint)
        ImageView ivHint;

        SimpleViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            try {
                ButterKnife.bind(this, itemView);
            } catch (Exception e) {
//                Log.d("SimpleViewHolder: ", e.getMessage());
            }
        }

        void bind(String text, boolean isFooter) {
            if (!isFooter) title.setText(text);
            if (isFooter) ivHint.setOnClickListener(v -> {
                apply(ivHint, GONE);
                apply(title, GONE);
                SettingsPreferences.disableRecentHint(context);
            });
        }

    }
}