package com.orangemoo.com.beta.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.orangemoo.com.beta.R;
import com.orangemoo.com.beta.adapter.ContentItemAdapter;
import com.orangemoo.com.beta.app.App;
import com.orangemoo.com.beta.beans.MovieInfoBean;
import com.orangemoo.com.beta.beans.MovieTops;
import com.orangemoo.com.beta.beans.MovieUSBox;
import com.orangemoo.com.beta.beans.entities.SearchResultEntity;
import com.orangemoo.com.beta.beans.entities.SubjectEntity;
import com.orangemoo.com.beta.beans.entities.SubjectsEntity;
import com.orangemoo.com.beta.interfaces.MovieInfoActionsListener;
import com.orangemoo.com.beta.network.DoubanApiUtils;
import com.orangemoo.com.beta.network.RetrofitCallback;
import com.orangemoo.com.beta.util.PreferenceUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by 德祥 on 2015/7/13.
 * The base fragment
 */
public class SwipeRefreshFragment extends SwipeBaseFragment {

    protected static final String ARG_API_TYPE = "api_type";

    private static final int EVENT_UPDATE_INIT = 0;
    private static final int EVENT_UPDATE_START = 1;
    private static final int EVENT_UPDATE_FAILED = 2;
    private static final int EVENT_UPDATE_DONE = 3;
    private static final int EVENT_UPDATE_US_BOX_DONE = 4;
    private static final int EVENT_UPDATE_MOVIE_TOPS_DONE = 5;
    private static final int EVENT_UPDATE_SEARCH_DONE = 6;

    private int mApiType = DoubanApiUtils.API_TYPE_UNKOWN;
    private String mSearchKey;

    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.recycler_view_layout)
    RecyclerView mRecyclerView;

    private ArrayList<MovieInfoBean> mMoviesList = new ArrayList<MovieInfoBean>();

    private ContentItemAdapter mArrayAdapter;
    private UpdateHandler mUpdateHandler;

    @Override
    protected int getLayout() {
        return R.layout.swipe_refresh_fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mApiType = getArguments().getInt(ARG_API_TYPE);
        }

        mMoviesList.clear();
        mUpdateHandler = new UpdateHandler();

        if (mApiType != DoubanApiUtils.API_TYPE_SEARCH) {
            startRequestDelay(100);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
    }

    protected void startRequestDelay(int delay) {
        mUpdateHandler.sendEmptyMessageDelayed(EVENT_UPDATE_INIT, delay);
    }

    private void setupRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        if (mMoviesList != null && mMoviesList.size() > 0) {
            mArrayAdapter = new ContentItemAdapter(getActionsListener(), mMoviesList, mApiType);
            mRecyclerView.setAdapter(mArrayAdapter);
        }

        mSwipeRefreshLayout.setColorSchemeResources(R.color.ae_theme_color);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doUpdateWork(mApiType);
            }
        });
        if (PreferenceUtils.isFirstLaunch(App.getsContext())) {
            Snackbar.make(mSwipeRefreshLayout, R.string.tip_pull_to_refresh, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.tip_ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    }).show();
        }
        if (DoubanApiUtils.API_TYPE_SEARCH == mApiType) {
            setRefreshEnable(false);
        }
    }

    /**
     * 设置refresh控件是否可用
     * @param enable
     */
    public void setRefreshEnable(boolean enable) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setEnabled(enable);
        }
    }

    /**
     * 按评分从高到低对列表进行排序
     */
    private void sortListByScore() {
        if (mMoviesList == null || mMoviesList.size() <= 1) {
            return;
        }

        Collections.sort(mMoviesList, new Comparator<MovieInfoBean>() {
            @Override
            public int compare(MovieInfoBean lhs, MovieInfoBean rhs) {
                return (Float.valueOf(rhs.getAverage())).compareTo(Float.valueOf(lhs.getAverage()));
            }
        });
    }


    public void startNewSearch(String key) {
        if (TextUtils.isEmpty(key)) {
            return;
        }

        if (mApiType == DoubanApiUtils.API_TYPE_SEARCH) {
            mSearchKey = key;
            setRefreshEnable(true);
            fetchSearchContents();
        }
    }

    /**
     * 进行搜索
     */
    private void fetchSearchContents() {
        mSwipeRefreshLayout.setRefreshing(true);
        doUpdateWork(mApiType);
    }

    /**
     * UI update handler
     */
    private class UpdateHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case EVENT_UPDATE_INIT:
                    mSwipeRefreshLayout.setRefreshing(true);
                    doUpdateWork(mApiType);
                    break;
                case EVENT_UPDATE_START:
                    mMoviesList.clear();
                    mRecyclerView.setAdapter(null);
                    mRecyclerView.invalidate();
                    break;
                case EVENT_UPDATE_FAILED:
                    mSwipeRefreshLayout.setRefreshing(false);
                    if (mApiType == DoubanApiUtils.API_TYPE_SEARCH) {
                        setRefreshEnable(false);
                    }
                    break;
                case EVENT_UPDATE_US_BOX_DONE:
                case EVENT_UPDATE_MOVIE_TOPS_DONE:
                case EVENT_UPDATE_SEARCH_DONE:
                    collectResultsFromResponse(mApiType, msg.obj);
                    sendEmptyMessage(EVENT_UPDATE_DONE);
                    break;
                case EVENT_UPDATE_DONE:
                    // search的结果就不排序了
                    if(mApiType != DoubanApiUtils.API_TYPE_SEARCH) {
                        sortListByScore();
                    }
                    mArrayAdapter = new ContentItemAdapter(getActionsListener(), mMoviesList, mApiType);
                    mRecyclerView.setAdapter(mArrayAdapter);
                    mSwipeRefreshLayout.setRefreshing(false);
                    if (mApiType == DoubanApiUtils.API_TYPE_SEARCH) {
                        setRefreshEnable(false);
                    }
                    break;
            }

            super.handleMessage(msg);
        }
    }

    /**
     * 异步请求数据
     */
    private void doUpdateWork(int apiType) {
        if(apiType < 0) {
            return;
        }
        mUpdateHandler.sendEmptyMessage(EVENT_UPDATE_START);
        int successCode;
        int failCode = EVENT_UPDATE_FAILED;
        switch (apiType) {
            case DoubanApiUtils.API_TYPE_US_BOX:
                successCode = EVENT_UPDATE_US_BOX_DONE;
                DoubanApiUtils.getMovieApiService().getMoviceUSBox(DoubanApiUtils.API_KEY,
                        new RetrofitCallback<>(mUpdateHandler, successCode, failCode, MovieUSBox.class));
                break;
            case DoubanApiUtils.API_TYPE_TOPS:
                successCode = EVENT_UPDATE_MOVIE_TOPS_DONE;
                DoubanApiUtils.getMovieApiService().getTop250(DoubanApiUtils.API_KEY,
                        new RetrofitCallback<>(mUpdateHandler, successCode, failCode, MovieTops.class));
                break;
            case DoubanApiUtils.API_TYPE_SEARCH:
                successCode = EVENT_UPDATE_SEARCH_DONE;
                if (TextUtils.isEmpty(mSearchKey)) {
                    break;
                }
                String encodeKey;
                try {
                    encodeKey = URLEncoder.encode(mSearchKey, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    encodeKey = "";
                }
                if (TextUtils.isEmpty(encodeKey)) {
                    break;
                }
                DoubanApiUtils.getMovieApiService().doSearch(encodeKey, DoubanApiUtils.API_KEY,
                        new RetrofitCallback<>(mUpdateHandler, successCode, failCode, SearchResultEntity.class));
                break;
            default:
                successCode = EVENT_UPDATE_US_BOX_DONE;
                DoubanApiUtils.getMovieApiService().getMoviceUSBox(DoubanApiUtils.API_KEY,
                        new RetrofitCallback<>(mUpdateHandler, successCode, failCode, MovieUSBox.class));
                break;
        }
    }

    public void collectResultsFromResponse(int apiType, Object object) {
        if (object == null) {
            return;
        }

        List<SubjectsEntity> subjects;
        SubjectsEntity subjectsEntity;
        SubjectEntity subject;

        int i;

        switch (apiType) {
            case DoubanApiUtils.API_TYPE_US_BOX:
                MovieUSBox usBox = (MovieUSBox)object;
                subjects = usBox.getSubjects();
                if (subjects != null) {
                    for (i = 0; i < subjects.size(); i++) {
                        subjectsEntity = subjects.get(i);
                        if (subjectsEntity == null) {
                            continue;
                        }
                        subject = subjectsEntity.getSubject();
                        if (subject == null) {
                            continue;
                        }

                        mMoviesList.add(new MovieInfoBean(subject));
                    }
                }
                break;
            case DoubanApiUtils.API_TYPE_TOPS:
                MovieTops movieTops = (MovieTops)object;
                List<SubjectEntity> subjectEntities;
                subjectEntities = movieTops.getSubjects();
                if (subjectEntities != null) {
                    for (i = 0; i < subjectEntities.size(); i++) {
                        subject = subjectEntities.get(i);
                        if (subject == null) {
                            continue;
                        }
                        mMoviesList.add(new MovieInfoBean(subject));
                    }
                }
                break;
            case DoubanApiUtils.API_TYPE_SEARCH:
                List<SearchResultEntity.SubjectsEntity> searResSubjectsList;
                SearchResultEntity searchResult = (SearchResultEntity)object;
                searResSubjectsList = searchResult.getSubjects();
                if (searResSubjectsList != null) {
                    for (i = 0; i < searResSubjectsList.size(); i++) {
                        SearchResultEntity.SubjectsEntity subsEntity = searResSubjectsList.get(i);
                        if (subsEntity == null) {
                            continue;
                        }
                        subject = SubjectEntity.cloneFromSearchSubjectsEntity(subsEntity);
                        if (subject == null) {
                            continue;
                        }

                        mMoviesList.add(new MovieInfoBean(subject));
                    }
                }
                break;
            default:

                break;
        }
    }

    /**
     * Created by 德祥 on 2015/7/13.
     *
     */
    public abstract static class BaseFragment extends Fragment {

        /**
         * this can let our all fragments call the activity`s methods
         */
        protected MovieInfoActionsListener mMovieInfoActionsListener;

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            try {
                mMovieInfoActionsListener = (MovieInfoActionsListener) activity;
            } catch (ClassCastException e) {
                Log.d(activity.getClass().getSimpleName(), " make sure that dont need to implent MovieInfoActionsListener!");
            }
        }

        protected MovieInfoActionsListener getActionsListener() {
            return mMovieInfoActionsListener;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(getLayout(), container, false);
            ButterKnife.bind(this, view);
            return view;
        }

        protected abstract int getLayout();
    }
}
