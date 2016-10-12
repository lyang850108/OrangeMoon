package com.orangemoo.com.beta.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.orangemoo.com.beta.R;
import com.orangemoo.com.beta.adapter.RVAdapter;
import com.orangemoo.com.beta.model.Person;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends Activity {

    private List<Person> persons;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recyclerview_activity);

        rv=(RecyclerView)findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();
        initializeAdapter();
    }

    private void initializeData(){
        persons = new ArrayList<>();
        persons.add(new Person("Emma Wilson", "23 years old", R.mipmap.test22));
        persons.add(new Person("Lavery Maiss", "25 years old", R.mipmap.test23));
        persons.add(new Person("Lillie Watts", "35 years old", R.mipmap.test24));
        persons.add(new Person("Emma Wilson", "23 years old", R.mipmap.test25));
        persons.add(new Person("Lavery Maiss", "25 years old", R.mipmap.test26));
        persons.add(new Person("Lillie Watts", "35 years old", R.mipmap.test27));
        persons.add(new Person("Lavery Maiss", "25 years old", R.mipmap.test28));
        persons.add(new Person("Lillie Watts", "35 years old", R.mipmap.test22));
    }

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(persons);
        rv.setAdapter(adapter);
    }
}
