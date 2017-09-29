package br.com.yimobile.igor.screens.container.adventures;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import br.com.yimobile.igor.R;

public class AdventuresRecyclerViewAdapter extends RecyclerView.Adapter {

    private ArrayList<String> adventuresArrayList;
    private Context context;


    public AdventuresRecyclerViewAdapter(ArrayList<String> adventures, Context context) {
        this.adventuresArrayList = adventures;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.adventures_list_item, parent, false);

        AdventuresViewHolder holder = new AdventuresViewHolder(view, context);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        AdventuresViewHolder adventureHolder = (AdventuresViewHolder) holder;

        String title  = adventuresArrayList.get(position);

        adventureHolder.titleTextView.setText(title);
    }

    @Override
    public int getItemCount() {
        return adventuresArrayList.size();
    }
}
