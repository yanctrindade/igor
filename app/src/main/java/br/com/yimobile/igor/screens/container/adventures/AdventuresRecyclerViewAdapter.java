package br.com.yimobile.igor.screens.container.adventures;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import br.com.yimobile.igor.R;
import database.Adventure;

public class AdventuresRecyclerViewAdapter extends RecyclerView.Adapter {

    private ArrayList<Adventure> adventuresArrayList;
    private Context context;


    AdventuresRecyclerViewAdapter(ArrayList<Adventure> adventures, Context context) {
        this.adventuresArrayList = adventures;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.adventures_list_item, parent, false);

        return new AdventuresViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        AdventuresViewHolder adventureHolder = (AdventuresViewHolder) holder;

        String title  = adventuresArrayList.get(position).getNome();
        String descr  = adventuresArrayList.get(position).getDescricao();
        adventureHolder.adventure = adventuresArrayList.get(position);
        adventureHolder.titleTextView.setText(title);
        adventureHolder.subtitleTextView.setText(descr);
    }

    @Override
    public int getItemCount() {
        return adventuresArrayList.size();
    }

    void swap() {
        notifyDataSetChanged();
    }
}
