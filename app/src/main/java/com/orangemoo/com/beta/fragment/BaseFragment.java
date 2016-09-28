package com.orangemoo.com.beta.fragment;

import android.app.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.orangemoo.com.beta.R;
import com.orangemoo.com.beta.widget.ListenableListView;


/**
 * Created by zengjinlong on 15-11-2.
 */
public class BaseFragment extends Fragment {

    protected Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    protected ListenableListView.OnListScrollListener mListScrollListener;
    public void setListScrollListener(ListenableListView.OnListScrollListener l) {
        mListScrollListener = l;
    }

    int mFragmentTag;
    public void setTag(int tag) {
        mFragmentTag = tag;
    }

    public boolean handleBackKey() {
        return false;
    }

    RecyclerView.OnScrollListener mRecylerViewScrollListener = new RecyclerView.OnScrollListener() {
        private int mScrolledY = 0;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            mScrolledY += dy;
            if (mScrolledY > 0) {
                //setSwipeEnable(false);
            } else {
                //setSwipeEnable(true);
            }
        }
    };

    public void onFabClicked() {

    }

    public void onRefresh() {
        // implement by child who wants handle refresh.
    }

}
