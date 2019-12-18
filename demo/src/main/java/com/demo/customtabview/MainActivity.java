package com.demo.customtabview;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ar.widget.customtabview.CustomTabPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private NonSwipeAbleViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabs = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.pages);
        AppCompatButton nextTab = findViewById(R.id.next_tab);
        AppCompatButton previousTab = findViewById(R.id.previous_tab);

        nextTab.setOnClickListener(this);
        previousTab.setOnClickListener(this);

        Bundle bundle = new Bundle();
        bundle.putString("name", getString(R.string.first));
        PageFragment page1 = new PageFragment();
        page1.setArguments(bundle);

        bundle = new Bundle();
        bundle.putString("name", getString(R.string.second));
        PageFragment page2 = new PageFragment();
        page2.setArguments(bundle);

        bundle = new Bundle();
        bundle.putString("name", getString(R.string.third));
        PageFragment page3 = new PageFragment();
        page3.setArguments(bundle);

        bundle = new Bundle();
        bundle.putString("name", getString(R.string.fourth));
        PageFragment page4 = new PageFragment();
        page4.setArguments(bundle);

        bundle = new Bundle();
        bundle.putString("name", getString(R.string.fifth));
        PageFragment page5 = new PageFragment();
        page5.setArguments(bundle);

        ArrayList<Fragment> pages = new ArrayList<>();
        pages.add(page1);
        pages.add(page2);
        pages.add(page3);
        pages.add(page4);
        pages.add(page5);

        int[] titles = {R.string.first, R.string.second, R.string.third, R.string.fourth, R.string.fifth};
        int[] selectedIcons = {R.drawable.ic_profile_selected, R.drawable.ic_profile_selected,
                R.drawable.ic_profile_selected, R.drawable.ic_profile_selected,
                R.drawable.ic_profile_selected};
        int[] completedIcons = {R.drawable.ic_profile_completed, R.drawable.ic_profile_completed,
                R.drawable.ic_profile_completed, R.drawable.ic_profile_completed,
                R.drawable.ic_profile_completed};
        int[] unSelectedIcons = {R.drawable.ic_profile_unselected, R.drawable.ic_profile_unselected,
                R.drawable.ic_profile_unselected, R.drawable.ic_profile_unselected,
                R.drawable.ic_profile_unselected};

        CustomTabPagerAdapter pagerAdapter = new CustomTabPagerAdapter.Builder(this, tabs, pages, getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
                .setShowArrow(true)
                .setTitles(titles)
                .setSelectedColor(ContextCompat.getColor(this, R.color.colorSelected))
                .setCompletedColor(ContextCompat.getColor(this, R.color.colorCompleted))
                .setUnSelectedColor(ContextCompat.getColor(this, R.color.colorUnSelected))
                .setSelectedIcons(selectedIcons)
                .setCompletedIcons(completedIcons)
                .setUnselectedIcons(unSelectedIcons)
                .setShowStepCount(true)
                .setTabBehaviour(CustomTabPagerAdapter.Builder.TabBehaviour.NOT_MOVABLE_TO_NEXT_TAB)
                .build();

        viewPager.setPagingEnabled(false);
        viewPager.setAdapter(pagerAdapter);

        tabs.addOnTabSelectedListener(pagerAdapter);
        tabs.setupWithViewPager(viewPager);

        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            TabLayout.Tab tab = tabs.getTabAt(i);
            if (null != tab)
                tab.setCustomView(pagerAdapter.getTabView(i));
        }
    }

    @Override
    public void onClick(View v) {
        int currentPage = viewPager.getCurrentItem();
        int totalPages = viewPager.getAdapter().getCount();
        if (v.getId() == R.id.previous_tab) {
            if (currentPage > 0) {
                viewPager.setCurrentItem(currentPage - 1);
            }
        } else if (v.getId() == R.id.next_tab) {
            if (currentPage < (totalPages - 1)) {
                viewPager.setCurrentItem(currentPage + 1);
            }
        }
    }
}