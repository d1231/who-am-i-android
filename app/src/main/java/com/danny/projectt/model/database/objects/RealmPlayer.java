package com.danny.projectt.model.database.objects;

import com.danny.projectt.model.objects.Player;
import com.danny.projectt.model.objects.TeamHistory;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class RealmPlayer extends RealmObject {

    public static final String GUESS_FINISH = "finishGuessing";

    public static final String ID = "id";

    @PrimaryKey
    private String id;

    private String name;

    private String position;

    private String placeOfBirth;

    private String dateOfBirth;

    private RealmList<RealmTeamHistory> realmTeamHistories;

    private String currentGuess;

    @Index
    private boolean finishGuessing;

    public RealmPlayer() {

    }

    public RealmPlayer(Player player) {

        this(player.id(), player.name(), player.position(), player.placeOfBirth(), player.dateOfBirth(),
                mapTeamHistory(player.teamHistory()));

    }

    public RealmPlayer(String id, String name, String position, String placeOfBirth, String dateOfBirth, RealmList<RealmTeamHistory> realmTeamHistories) {

        this.id = id;
        this.name = name;
        this.position = position;
        this.placeOfBirth = placeOfBirth;
        this.dateOfBirth = dateOfBirth;
        this.realmTeamHistories = realmTeamHistories;
    }

    private static RealmList<RealmTeamHistory> mapTeamHistory(List<TeamHistory> teamHistories) {


        RealmList<RealmTeamHistory> realmTeamHistories = new RealmList<>();
        for (TeamHistory teamHistory : teamHistories) {
            final RealmTeamHistory realmTeamHistory = new RealmTeamHistory(teamHistory);
            realmTeamHistories.add(realmTeamHistory);
        }

        return realmTeamHistories;
    }

    public String getId() {

        return id;
    }

    public String getName() {

        return name;
    }

    public String getPosition() {

        return position;
    }

    public String getPlaceOfBirth() {

        return placeOfBirth;
    }

    public String getDateOfBirth() {

        return dateOfBirth;
    }

    public RealmList<RealmTeamHistory> getRealmTeamHistories() {

        return realmTeamHistories;
    }

    @Override
    public String toString() {

        return "RealmPlayer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", placeOfBirth='" + placeOfBirth + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", realmTeamHistories=" + realmTeamHistories +
                '}';
    }

    public void markAsFinished() {

        finishGuessing = true;

    }
}
