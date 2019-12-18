package com.ar.widget.customtabview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;

final public class CustomTabPagerAdapter extends FragmentPagerAdapter implements TabLayout.OnTabSelectedListener {

    private static final String TAG = CustomTabPagerAdapter.class.getSimpleName();

    private Context context;
    private TabLayout tabLayout;
    private ArrayList<Fragment> pages;

    private int[] titles;
    private boolean showArrow;
    private int selectedColor;
    private int completedColor;
    private int unSelectedColor;
    private int[] selectedIcons;
    private int[] completedIcons;
    private int[] unselectedIcons;
    private boolean showStepCount;
    private Builder.TabBehaviour tabBehaviour;

    private CustomTabPagerAdapter(Builder builder) {
        super(builder.fragmentManager, builder.fragmentPagerAdapterBehaviour);

        this.pages = builder.pages;
        this.context = builder.context;
        this.tabLayout = builder.tabLayout;

        this.titles = builder.titles;
        this.showArrow = builder.showArrow;
        this.selectedColor = builder.selectedColor;
        this.completedColor = builder.completedColor;
        this.unSelectedColor = builder.unSelectedColor;
        this.selectedIcons = builder.selectedIcons;
        this.completedIcons = builder.completedIcons;
        this.unselectedIcons = builder.unselectedIcons;
        this.showStepCount = builder.showStepCount;
        this.tabBehaviour = builder.tabBehaviour;
    }

    @Override
    public int getCount() {
        return null != pages ? pages.size() : 0;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return pages.get(position);
    }

    @SuppressLint("InflateParams")
    public View getTabView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        // 1. arrow
        AppCompatImageView arrow = view.findViewById(R.id.arrow);
        if (showArrow) {
            arrow.setVisibility((position == 0) ? View.VISIBLE : View.INVISIBLE);
            if (selectedColor != 0)
                DrawableCompat.setTint(arrow.getDrawable(), selectedColor);
        }
        // 2. icon
        AppCompatImageView icon = view.findViewById(R.id.icon);
        if (null != selectedIcons && null != unselectedIcons) {
            icon.setVisibility(View.VISIBLE);
            if (position == 0)
                icon.setImageResource(selectedIcons[position]);
            else
                icon.setImageResource(unselectedIcons[position]);
        }
        // 3. title
        AppCompatTextView title = view.findViewById(R.id.title);
        if (null != titles) {
            title.setVisibility(View.VISIBLE);
            title.setText(titles[position]);
            if (position == 0) {
                if (selectedColor != 0)
                    title.setTextColor(selectedColor);
                title.setTypeface(null, Typeface.BOLD);
            } else if (unSelectedColor != 0)
                title.setTextColor(unSelectedColor);
        }
        // 4. step count
        AppCompatTextView number = view.findViewById(R.id.number);
        View leftLine = view.findViewById(R.id.left_line);
        View rightLine = view.findViewById(R.id.right_line);
        if (showStepCount) {
            number.setVisibility(View.VISIBLE);
            leftLine.setVisibility(View.VISIBLE);
            rightLine.setVisibility(View.VISIBLE);
            if (position == 0) {
                leftLine.setVisibility(View.INVISIBLE);
                if (selectedColor != 0)
                    DrawableCompat.setTint(number.getBackground(), selectedColor);
            } else if (position == (getCount() - 1)) {
                rightLine.setVisibility(View.INVISIBLE);
            } else {
                if (unSelectedColor != 0)
                    DrawableCompat.setTint(number.getBackground(), unSelectedColor);
            }
            number.setText(String.valueOf(position + 1));
        }
        return view;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        decorateTab(tab, true);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        decorateTab(tab, false);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void decorateTab(TabLayout.Tab tab, boolean tabSelected) {
        View tabView = tab.getCustomView();
        if (null != tabView) {
            AppCompatImageView arrow = tabView.findViewById(R.id.arrow);
            AppCompatImageView icon = tabView.findViewById(R.id.icon);
            AppCompatTextView title = tabView.findViewById(R.id.title);
            AppCompatTextView number = tabView.findViewById(R.id.number);
            if (tabSelected) {
                // 1. arrow
                arrow.setVisibility(showArrow ? View.VISIBLE : View.GONE);
                // 2. icon
                if (icon.getVisibility() == View.VISIBLE)
                    icon.setImageResource(selectedIcons[tab.getPosition()]);
                // 3. title
                if (title.getVisibility() == View.VISIBLE) {
                    if (selectedColor != 0)
                        title.setTextColor(selectedColor);
                    title.setTypeface(null, Typeface.BOLD);
                }
                // 4. step count
                if (number.getVisibility() == View.VISIBLE) {
                    if (selectedColor != 0)
                        DrawableCompat.setTint(number.getBackground(), selectedColor);
                }
                // 5. completed tabs
                if (completedColor != 0) {
                    highlightCompletedTabs(tab.getPosition());
                }
            } else {
                // 1. arrow
                arrow.setVisibility(showArrow ? View.INVISIBLE : View.GONE);
                // 2. icon
                if (icon.getVisibility() == View.VISIBLE)
                    icon.setImageResource(unselectedIcons[tab.getPosition()]);
                // 3. title
                if (title.getVisibility() == View.VISIBLE) {
                    if (unSelectedColor != 0) {
                        title.setTextColor(unSelectedColor);
                    }
                    title.setTypeface(null, Typeface.NORMAL);
                }
                // 4. step count
                if (number.getVisibility() == View.VISIBLE) {
                    if (unSelectedColor != 0)
                        DrawableCompat.setTint(number.getBackground(), unSelectedColor);
                }
            }
        }
        if (tabBehaviour == Builder.TabBehaviour.NOT_MOVABLE_TO_NEXT_TAB) {
            toggleTabsClickable(0, tab.getPosition(), true);
            toggleTabsClickable(tab.getPosition(), getCount(), false);
        }
    }

    private void highlightCompletedTabs(int currentTab) {
        for (int i = 0; i < currentTab; i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (null != tab) {
                View tabView = tab.getCustomView();
                if (null != tabView) {
                    AppCompatImageView icon = tabView.findViewById(R.id.icon);
                    AppCompatTextView title = tabView.findViewById(R.id.title);
                    AppCompatTextView number = tabView.findViewById(R.id.number);
                    if (icon.getVisibility() == View.VISIBLE && null != completedIcons)
                        icon.setImageResource(completedIcons[i]);
                    if (title.getVisibility() == View.VISIBLE)
                        title.setTextColor(completedColor);
                    if (number.getVisibility() == View.VISIBLE)
                        DrawableCompat.setTint(number.getBackground(), completedColor);
                }
            }
        }
    }

    private void toggleTabsClickable(int fromTab, int toTab, boolean clickable) {
        LinearLayout tabStrip = ((LinearLayout) tabLayout.getChildAt(0));
        tabStrip.setEnabled(false);
        for (int i = fromTab; i < toTab; i++) {
            tabStrip.getChildAt(i).setClickable(clickable);
        }
    }

    final static public class Builder {

        private Context context;
        private TabLayout tabLayout;
        private ArrayList<Fragment> pages;
        private FragmentManager fragmentManager;
        private int fragmentPagerAdapterBehaviour;

        public enum TabBehaviour {
            MOVABLE_TO_NEXT_TAB,
            NOT_MOVABLE_TO_NEXT_TAB
        }

        private int[] titles;
        private boolean showArrow;
        private int selectedColor;
        private int completedColor;
        private int unSelectedColor;
        private int[] selectedIcons;
        private int[] completedIcons;
        private int[] unselectedIcons;
        private boolean showStepCount;
        private TabBehaviour tabBehaviour;

        public Builder(@NonNull Context context, @NonNull TabLayout tabLayout, @NonNull ArrayList<Fragment> pages,
                       FragmentManager fragmentManager, int fragmentPagerAdapterBehaviour) {
            this.pages = pages;
            this.context = context;
            this.tabLayout = tabLayout;
            this.fragmentManager = fragmentManager;
            this.fragmentPagerAdapterBehaviour = fragmentPagerAdapterBehaviour;
        }

        public Builder setTitles(@Nullable int[] titles) {
            this.titles = titles;
            return this;
        }

        public Builder setShowArrow(boolean showArrow) {
            this.showArrow = showArrow;
            return this;
        }

        public Builder setSelectedColor(int selectedColor) {
            this.selectedColor = selectedColor;
            return this;
        }

        public Builder setCompletedColor(int completedColor) {
            this.completedColor = completedColor;
            return this;
        }

        public Builder setUnSelectedColor(int unSelectedColor) {
            this.unSelectedColor = unSelectedColor;
            return this;
        }

        public Builder setSelectedIcons(@Nullable int[] selectedIcons) {
            this.selectedIcons = selectedIcons;
            return this;
        }

        public Builder setCompletedIcons(@Nullable int[] completedIcons) {
            this.completedIcons = completedIcons;
            return this;
        }

        public Builder setUnselectedIcons(@Nullable int[] unselectedIcons) {
            this.unselectedIcons = unselectedIcons;
            return this;
        }

        public Builder setShowStepCount(boolean showStepCount) {
            this.showStepCount = showStepCount;
            return this;
        }

        public Builder setTabBehaviour(TabBehaviour tabBehaviour) {
            this.tabBehaviour = tabBehaviour;
            return this;
        }

        public CustomTabPagerAdapter build() {
            return new CustomTabPagerAdapter(this);
        }
    }
}