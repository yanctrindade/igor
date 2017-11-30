package br.com.yimobile.igor.screens.container.adventures.progressAdventure.sessionsFragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import br.com.yimobile.igor.R;
import database.Session;

public class SessionRecyclerViewAdapter extends RecyclerView.Adapter {

    private ArrayList<Session> sessionsArrayList;
    private Context context;


    public SessionRecyclerViewAdapter(ArrayList<Session> sessions, Context context) {
        this.sessionsArrayList = sessions;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.resume_list_item, parent, false);

        SessionViewHolder holder = new SessionViewHolder(view, context);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        SessionViewHolder sessionsHolder = (SessionViewHolder) holder;

        Session session = sessionsArrayList.get(position);
        String title  = session.getData() + " " + session.getTitulo();

        sessionsHolder.titleTextView.setText(title);
    }

    @Override
    public int getItemCount() {
        return sessionsArrayList.size();
    }

    public void swap() {
        notifyDataSetChanged();
    }
}
