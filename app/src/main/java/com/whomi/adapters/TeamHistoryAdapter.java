package com.whomi.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whomi.R;
import com.whomi.model.objects.TeamHistory;

import java.util.List;

public class TeamHistoryAdapter extends RecyclerView.Adapter<TeamHistoryViewHolder> {

    private final LayoutInflater layoutInflater;

    private final List<TeamHistory> teamHistory;

    public TeamHistoryAdapter(Context context, List<TeamHistory> teamHistory) {

        this.layoutInflater = LayoutInflater.from(context);
        this.teamHistory = teamHistory;
    }

    @Override
    public TeamHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final View view = layoutInflater.inflate(R.layout.item_teamhistory, parent, false);

        return new TeamHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TeamHistoryViewHolder holder, int position) {

        holder.bind(teamHistory.get(position));

    }

    @Override
    public int getItemCount() {

        return teamHistory.size();
    }
}
