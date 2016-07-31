package com.danny.projectt.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.danny.projectt.R;
import com.danny.projectt.model.objects.Stats;
import com.danny.projectt.model.objects.TeamHistory;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeamHistoryViewHolder extends RecyclerView.ViewHolder {


    @BindView(R.id.team_history_years)
    TextView teamHistoryYears;

    @BindView(R.id.team_history_name)
    TextView teamHistoryName;

    @BindView(R.id.team_history_apps)
    TextView teamHistoryApps;

    @BindView(R.id.team_history_goals)
    TextView teamHistoryGoals;

    public TeamHistoryViewHolder(View itemView) {

        super(itemView);
        ButterKnife.bind(this, itemView);

    }

    public void bind(TeamHistory teamHistory) {

        teamHistoryYears.setText(String.format("%d-%d", teamHistory.startYear(), teamHistory.endYear()));

        teamHistoryName.setText(teamHistory.teamName());

        final Stats leagueStats = teamHistory.leagueStats();
        teamHistoryApps.setText(String.format("%d", leagueStats.apps()));

        teamHistoryGoals.setText(String.format("%d", leagueStats.goals()));
    }
}
