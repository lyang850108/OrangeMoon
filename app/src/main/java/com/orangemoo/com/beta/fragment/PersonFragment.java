package com.orangemoo.com.beta.fragment;

import android.os.Bundle;

/**
 * Created by 德祥 on 2015/7/13.
 */
public class PersonFragment extends SwipeRefreshFragment {

    public static PersonFragment newInstance(int requestType) {
        PersonFragment fragment = new PersonFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_API_TYPE, requestType);
        fragment.setArguments(args);
        return fragment;
    }

    public PersonFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
