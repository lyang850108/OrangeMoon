package com.orangemoo.com.beta.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.orangemoo.com.beta.R;
import com.orangemoo.com.beta.activity.RecyclerViewActivity;
import com.orangemoo.com.beta.adapter.PeopleListAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yangli on 15-11-19 tiny lucky.
 */
public class PeopleFragment extends BaseFragment {

    private View mView;

    @Bind(R.id.recycler_view_blog)
    RecyclerView mRecyclerView;

    ImageView mTitleImage;

    private PeopleHandler peopleHandler;

    public PeopleFragment() {

    }

    //private List<MovieInfoBean> mNewPrograms = new ArrayList<MovieInfoBean>();

    private static final int LOAD_MORE = 1;
    private static final int LOAD_NEW= 2;
    private int mLoadIndex = 0;

    //private LoadDataTask mLoadTask;
    private void initData() {
        /*if (!NetworkUtil.isNetworkAvailable(mActivity)) {
            // TODO add Toast
            return;// network unavailable, just return;
        }
        mLoadTask = new LoadDataTask(LOAD_MORE);
        mLoadTask.execute(mLoadIndex);*/
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_people, container, false);
        ButterKnife.bind(this, mView);
        initView();
        peopleHandler = new PeopleHandler();
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        peopleHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        peopleHandler.sendMessageDelayed(peopleHandler.obtainMessage(MSG_REFRESH, 0, 0), 1000);
    }

    private static final int MSG_REFRESH = 0;
    private class PeopleHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (MSG_REFRESH == what) {
                if (msg.arg1 < 3) {
                    msg.arg1++;
                    sendMessageDelayed(peopleHandler.obtainMessage(MSG_REFRESH, msg.arg1 + 1, 0), 1000);
                } else {
                    //setRefreshing(false);
                }
            }
        }
    }

    private LinearLayoutManager mLinearLayoutManager;
    private PeopleListAdapter mPeopleListAdapter;
    private void initView() {

        mLinearLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mPeopleListAdapter = new PeopleListAdapter(this, mActivity.getLayoutInflater());
        mRecyclerView.setAdapter(mPeopleListAdapter);
        mPeopleListAdapter.setOnItemClickListener(new PeopleListAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(View view, int postion) {
                Intent intent = new Intent(getContext(), RecyclerViewActivity.class);
                startActivity(intent);
            }
        });

        //if do nothing recyllerview wil refresh everywhere
        mRecyclerView.addOnScrollListener(mRecylerViewScrollListener);
    }



    public static boolean mFirstLoad = true;
    /*class LoadDataTask extends AsyncTask<Integer, Void, Void> {

        private int mLoadType;
        LoadDataTask(int type) {
            mLoadType = type;
        }

        @Override
        protected void onPreExecute() {
            //if (mLoadType == LOAD_MORE) {
                if (mFirstLoad) {
                    // trigger SwipeRefreshLayout to show or wait onMeasure called.
//                    mSwipeRefreshLayout.setProgressViewOffset(false, -26 * 3, 64 * 3);
                    mSwipeRefreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setRefreshing(true);
                        }
                    }, 100);
                } else {
                    setRefreshing(true);
                }
            //}
        }

        @Override
        protected Void doInBackground(Integer[] params) {
            int index = 0;
            if (params != null && params.length > 0) {
                index = params[0];
            }
            try {
                //mNewPrograms = App.getRetrofitService().getProgramList(index);
                MovieUSBox object = DoubanApiUtils.getMovieApiService().getMoviceUSBox(DoubanApiUtils.API_KEY);
                collectResultsFromResponse(object);
            } catch (Exception e) {
                LogUtil.e("doInBackground, Exception:" + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void o) {
            *//*if (mNewPrograms != null && mNewPrograms.size() > 0) {
                if (mLoadType == LOAD_NEW && mDatas.size() > 0) {
                    int oldFirstId = mDatas.get(0).id;
                    for (ProgramItem item_right : mNewPrograms) {
                        if (item_right.id < oldFirstId) {
                            // add new items at head
                            mDatas.add(0, item_right);
                            mLoadIndex++;
                        }
                    }
                } else {
                    mLoadIndex += mNewPrograms.size();
                    mDatas.addAll(mNewPrograms);
                }
                mNewPrograms.clear();
                mAdapter.notifyDataSetChanged();
            }
            mHandler.sendEmptyMessage(MSG_LOAD_DONE);*//*

            //mNewPrograms.clear();
            mBlogAdapter.notifyDataSetChanged();
            //mHandler.sendEmptyMessage(MSG_LOAD_DONE);
        }
    }

    public void collectResultsFromResponse(Object object) {
        if (object == null) {
            return;
        }

        List<SubjectsEntity> subjects;
        SubjectsEntity subjectsEntity;
        SubjectEntity subject;

        int i;

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

                LogUtil.d(subject.getTitle());

                mNewPrograms.add(new MovieInfoBean(subject));
            }
        }


    }*/
}
