package com.orangemoo.com.beta.fragment;

import android.os.Bundle;

/**
 * Created by 德祥 on 2015/7/13.
 */
public class ContentFragment extends SwipeRefreshFragment {

    public static ContentFragment newInstance(int requestType) {
        ContentFragment fragment = new ContentFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_API_TYPE, requestType);
        fragment.setArguments(args);
        return fragment;
    }

    public ContentFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
