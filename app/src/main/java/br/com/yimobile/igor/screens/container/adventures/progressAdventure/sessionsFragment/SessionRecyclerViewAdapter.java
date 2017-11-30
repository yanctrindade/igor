package br.com.yimobile.igor.screens.container.adventures.progressAdventure.sessionsFragment;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import br.com.yimobile.igor.R;
import database.Adventure;
import database.Session;

public class SessionRecyclerViewAdapter extends RecyclerView.Adapter {

    private ArrayList<Session> sessionsArrayList;
    private Activity activity;
    private Adventure adventure;

    SessionRecyclerViewAdapter(ArrayList<Session> sessions, Adventure adventure, Activity activity) {
        this.sessionsArrayList = sessions;
        this.adventure = adventure;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity)
                .inflate(R.layout.resume_list_item, parent, false);

        return new SessionViewHolder(view, activity);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        SessionViewHolder sessionsHolder = (SessionViewHolder) holder;

        Session session = sessionsArrayList.get(position);
        String title  = session.getData() + " " + session.getTitulo();

        sessionsHolder.session = session;
        sessionsHolder.adventure = adventure;
        sessionsHolder.titleTextView.setText(title);
    }

    @Override
    public int getItemCount() {
        return sessionsArrayList.size();
    }

    void swap() {
        notifyDataSetChanged();
    }
}
