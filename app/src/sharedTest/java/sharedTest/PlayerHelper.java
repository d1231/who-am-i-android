package sharedTest;

import com.danny.projectt.model.objects.Player;
import com.danny.projectt.utils.AutoValueTypeAdapterFactory;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PlayerHelper {

    public static final String PLAYER_RAW_JSON = "{\"_id\":\"5782858fd727f8805262e113\",\"id\":\"Neil Maddison\",\"name\":\"Neil Maddison\",\"fullname\":\"Neil Maddison\",\"position\":\"Central midfielder\",\"placeOfBirth\":\"Darlington, England\",\"dateOfBirth\":\"{{birth date and age|df=y|1969|10|2}}\",\"__v\":0,\"teams\":[{\"team\":\"Southampton F.C.\",\"start\":1988,\"end\":1997,\"loan\":false,\"_id\":\"5782858fd727f8805262e118\",\"leagueStats\":{\"apps\":169,\"goals\":19}},{\"team\":\"Middlesbrough F.C.\",\"start\":1997,\"end\":2001,\"loan\":false,\"_id\":\"5782858fd727f8805262e117\",\"leagueStats\":{\"apps\":55,\"goals\":4}},{\"team\":\"Barnsley F.C.\",\"start\":2000,\"end\":2000,\"loan\":true,\"_id\":\"5782858fd727f8805262e116\",\"leagueStats\":{\"apps\":3,\"goals\":0}},{\"team\":\"Bristol City\",\"start\":2001,\"end\":2001,\"loan\":true,\"_id\":\"5782858fd727f8805262e115\",\"leagueStats\":{\"apps\":7,\"goals\":1}},{\"team\":\"Darlington F.C.\",\"start\":2001,\"end\":2007,\"loan\":false,\"_id\":\"5782858fd727f8805262e114\",\"leagueStats\":{\"apps\":115,\"goals\":4}}]}";

    public static Player getDummyPlayer() {

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(AutoValueTypeAdapterFactory.create())
                .create();

        return gson.fromJson(PLAYER_RAW_JSON, Player.class);
    }


    public static Player createPlayer(String name) {

        return createPlayer("50", name);
    }

    public static Player createPlayer(String id, String name) {

        return Player.create(id, name, "Goalkeeper", "Spain", "10.8.94", Lists.newArrayList());
    }

    public static Player createPlayer(long id) {

        return createPlayer(String.valueOf(id), "Def Name");
    }


}
