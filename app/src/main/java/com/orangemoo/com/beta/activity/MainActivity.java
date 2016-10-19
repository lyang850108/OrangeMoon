package com.orangemoo.com.beta.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.orangemoo.com.beta.R;
import com.orangemoo.com.beta.fragment.ArticleFragment;
import com.orangemoo.com.beta.fragment.BaseFragment;
import com.orangemoo.com.beta.fragment.PeopleFragment;
import com.orangemoo.com.beta.util.LogUtil;
import com.orangemoo.com.beta.util.PreferenceUtils;
import com.orangemoo.com.beta.widget.ListenableListView;
import com.orangemoo.com.beta.widget.ViewPagerTabs;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @Bind(R.id.view_pager)
    ViewPager mViewPager;

    @Bind(R.id.pager_tabs)
    ViewPagerTabs mViewPagerTabs;

    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipRefreshLayout;

    PagerAdapter mPagerAdapter;
    private String[] mTabTitles;
    BaseFragment mCurrentFragment;

    public static final int FRAGMENT_TAG_INTRO = 0;
    public static final int FRAGMENT_TAG_DETAIL = 1;
    public static final int FRAGMENT_TAG_PRICE = 2;
    public static final int FRAGMENT_TAG_FAQ = 3;

    ListenableListView.OnListScrollListener mOnListScrollListener = new ListenableListView.OnListScrollListener() {
        @Override
        public void onYScrolled(int scrollY) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initViewTabs();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        /*ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);*/
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Init mSwipRefreshLayout view
        mSwipRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCurrentFragment.onRefresh();
            }
        });
        mSwipRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.ae_theme_color));
        if (PreferenceUtils.isFirstLaunch(this)) {
            Snackbar.make(mSwipRefreshLayout, R.string.tip_pull_to_refresh, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.tip_ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    }).show();
        }
    }

    private void initViewTabs() {
        ButterKnife.bind(this);

        mTabTitles = new String[] {getString(R.string.tab_title_intro), getString(R.string.tab_title_detail)};
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mViewPagerTabs.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentFragment = (BaseFragment) mPagerAdapter.getItem(position);

                mViewPagerTabs.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                mViewPagerTabs.onPageScrollStateChanged(state);
            }
        });

        BaseFragment fragmentMain1 = new ArticleFragment();
        BaseFragment fragment2 = new PeopleFragment();
        fragment2.setTag(FRAGMENT_TAG_DETAIL);

        mCurrentFragment = fragmentMain1;
        mCurrentFragment.setListScrollListener(mOnListScrollListener);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(),
                fragmentMain1, fragment2);
        mViewPager.setAdapter(mPagerAdapter);

        mViewPagerTabs.setViewPager(mViewPager);
    }

    private class PagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments;

        public PagerAdapter(FragmentManager fm, Fragment... fragments) {
            super(fm);

            mFragments = new ArrayList<Fragment>();
            for (Fragment fragment : fragments) {
                mFragments.add(fragment);
            }
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabTitles[position];
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item_right clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item_right clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            Intent intentForSetting = new Intent(this, SettingActivity.class);
            startActivity(intentForSetting);

        } else if (id == R.id.nav_send) {
            Intent intentForLoginning = new Intent(this, LoginActivity.class);
            startActivity(intentForLoginning);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
