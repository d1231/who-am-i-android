package com.danny.projectt.model.objects;

import com.danny.projectt.model.database.objects.RealmPlayer;
import com.danny.projectt.model.database.objects.RealmTeamHistory;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

public class PlayerUtils {

    public static Player fromRealm(RealmPlayer realmPlayer) {

        final RealmList<RealmTeamHistory> realmTeamHistories = realmPlayer.getRealmTeamHistories();

        List<TeamHistory> teamHistory = new ArrayList<>(realmTeamHistories.size());

        for (RealmTeamHistory realmTeamHistory : realmTeamHistories) {
            teamHistory.add(TeamHistory.create(realmTeamHistory.getName(), realmTeamHistory.getStartYear(),
                    realmTeamHistory.getEndYear(), realmTeamHistory.isLoan(), realmTeamHistory.getApps(),
                    realmTeamHistory.getGoals()));
        }

        return Player.create(realmPlayer.getId(), realmPlayer.getName(), realmPlayer.getPosition(), realmPlayer
                .getPlaceOfBirth(), realmPlayer.getDateOfBirth(), teamHistory);
    }

}
