package com.danny.whomi.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.danny.whomi.R;
import com.danny.whomi.model.objects.Stats;
import com.danny.whomi.model.objects.TeamHistory;

import butterknife.BindView;
import butterknife.ButterKnife;

class TeamHistoryViewHolder extends RecyclerView.ViewHolder {


    @BindView(R.id.team_history_years)
    TextView teamHistoryYears;

    @BindView(R.id.team_history_name)
    TextView teamHistoryName;

    @BindView(R.id.team_history_apps)
    TextView teamHistoryApps;

    @BindView(R.id.team_history_goals)
    TextView teamHistoryGoals;

    TeamHistoryViewHolder(View itemView) {

        super(itemView);
        ButterKnife.bind(this, itemView);

    }

    public void bind(TeamHistory teamHistory) {

        teamHistoryYears.setText(String.format("%d-%d", teamHistory.startYear(), teamHistory.endYear()));

        String s;
        if (teamHistory.aLoan()) {
            s = String.format("%s (Loan)", teamHistory.teamName());
        } else {
            s = teamHistory.teamName();
        }
        teamHistoryName.setText(s);

        final Stats leagueStats = teamHistory.leagueStats();
        teamHistoryApps.setText(String.format("%d", leagueStats.apps()));

        teamHistoryGoals.setText(String.format("%d", leagueStats.goals()));
    }
}
