package com.danny.projectt.model.database.objects;

import com.danny.projectt.model.objects.TeamHistory;

import io.realm.RealmObject;

public class RealmTeamHistory extends RealmObject {

    private String name;

    private int startYear;

    private int endYear;

    private boolean loan;

    private int apps;

    private int goals;

    public RealmTeamHistory() {

    }

    public RealmTeamHistory(TeamHistory teamHistory) {

        this(teamHistory.teamName(), teamHistory.startYear(), teamHistory.endYear(), teamHistory.aLoan(),
                teamHistory.leagueStats().apps(), teamHistory.leagueStats().goals());

    }

    public RealmTeamHistory(String name, int startYear, int endYear, boolean loan, int apps, int goals) {


        this.name = name;
        this.startYear = startYear;
        this.endYear = endYear;
        this.loan = loan;
        this.apps = apps;
        this.goals = goals;
    }

    public String getName() {

        return name;
    }

    public int getStartYear() {

        return startYear;
    }

    public int getEndYear() {

        return endYear;
    }

    public boolean isLoan() {

        return loan;
    }

    public int getApps() {

        return apps;
    }

    public int getGoals() {

        return goals;
    }

    @Override
    public String toString() {

        return "RealmTeamHistory{" +
                "name='" + name + '\'' +
                ", startYear=" + startYear +
                ", endYear=" + endYear +
                ", loan=" + loan +
                ", apps=" + apps +
                ", goals=" + goals +
                '}';
    }
}
