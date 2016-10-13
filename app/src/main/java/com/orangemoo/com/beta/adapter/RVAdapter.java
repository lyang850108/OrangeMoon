package com.orangemoo.com.beta.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.orangemoo.com.beta.R;
import com.orangemoo.com.beta.model.Person;

import java.util.List;
import java.util.Random;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder> {
    private static final int ITEM_TYPE_NORMAL = 0;
    private static final int ITME_TYPE_HEADER = 1;
    private RecyclerView.ViewHolder mHeaderViewHolder;

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView personName;
        //TextView personAge;
        ImageView personPhoto;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            personName = (TextView)itemView.findViewById(R.id.person_name);
            //personAge = (TextView)itemView.findViewById(R.id.person_age);
            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
        }
    }

    List<Person> persons;

    public void setmHeaderViewHolder(RecyclerView.ViewHolder holder) {
        mHeaderViewHolder = holder;
    }

    public RVAdapter(List<Person> persons){
        this.persons = persons;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    private static Random rand = new Random(47);

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        PersonViewHolder pvh;
        int pesudo = rand.nextInt();

        if (viewType == ITME_TYPE_HEADER) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_item_view, viewGroup, false);
            pvh = new PersonViewHolder(v);
        } else {
            if (pesudo % 2 != 0) {
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_right, viewGroup, false);
            } else {
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_left, viewGroup, false);
            }
            pvh = new PersonViewHolder(v);

        }
        return pvh;

    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int position) {
        if (position > 0) {
            personViewHolder.personName.setText(persons.get(position).name);
            //personViewHolder.personAge.setText(persons.get(position).age);
            personViewHolder.personPhoto.setImageResource(persons.get(position).photoId);
        }

    }

    @Override
    public int getItemCount() {
        if (mHeaderViewHolder != null) {
            return persons.size() + 1;
        }
        return persons.size();

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITME_TYPE_HEADER;
        } else {
            return ITEM_TYPE_NORMAL;
        }
    }
}
