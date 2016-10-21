package com.orangemoo.com.beta.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.orangemoo.com.beta.R;
import com.orangemoo.com.beta.beans.MovieInfoBean;
import com.orangemoo.com.beta.beans.MovieMajorInfos;
import com.orangemoo.com.beta.interfaces.MovieInfoActionsListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by dxjia on 15-6-23.
 */
public class ContentItemAdapter extends RecyclerView.Adapter<ContentItemAdapter.ViewHolder> {

    final private List<MovieInfoBean> mDatas;
    final private MovieInfoActionsListener mActionListener;
    private int itemType;

    public ContentItemAdapter(MovieInfoActionsListener l, List<MovieInfoBean> moviesList, int type) {
        mActionListener = l;
        mDatas = moviesList;
        itemType = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (0 == itemType) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.article_item_view, parent,false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.people_item_view, parent,false);
        }

        return new ViewHolder(view, itemType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MovieInfoBean bean = mDatas.get(position);
        holder.updateViews(bean);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        /*public ImageView mImageView;
        public MovieView mMovieView;
        public Button mDetailButton;*/
        public MovieInfoBean mBean;
        private TextView mTitleTx;
        public ImageView mImage;

        public void updateViews(MovieInfoBean details) {
            mBean = details;
            mTitleTx.setText(mBean.getTitle());
            /*mMovieView.setDescription(mBean.getFormatedGenres());
            mMovieView.setAverage(mBean.getAverage() + "'");
            mDetailButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MovieMajorInfos movieMajorInfos = new MovieMajorInfos();
                    movieMajorInfos.fillDatas(mBean.getId(), mBean.getTitle(), mBean.getImageUri(),
                            mBean.getCastsCount(), mBean.getCastsIds(), mBean.getCastsAvatorUris(),
                            mBean.getDirectorId(), mBean.getDirectorImageUri(), mBean.getAverage());
                    mActionListener.showDetails(movieMajorInfos);
                }
            });*/
            Context context = mImage.getContext();
            Picasso.with(context)
                    .load(mBean.getImageUri())
                    .placeholder(R.mipmap.ic_loading)
                    .error(R.mipmap.ic_unkown_image)
                    .fit()
                    .into(mImage);
        }

        public ViewHolder(View itemView, int itemType) {
            super(itemView);
            if (0 == itemType) {
                mTitleTx = (TextView) itemView.findViewById(R.id.tx_title_blog);
                mImage = (ImageView) itemView.findViewById(R.id.image_blog_item);
            } else {
                mTitleTx = (TextView) itemView.findViewById(R.id.tx_title_blog);
                mImage = (ImageView) itemView.findViewById(R.id.image_blog_item);
            }

        }
    }
}
