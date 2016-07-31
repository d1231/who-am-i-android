package sharedTest;

import com.danny.projectt.utils.AutoValueTypeAdapterFactory;
import com.danny.projectt.model.objects.Player;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PlayerHelper {

    private static final String PLAYER = "{\"_id\":\"5782858fd727f8805262e113\",\"id\":\"Neil Maddison\",\"name\":\"Neil Maddison\",\"fullname\":\"Neil Maddison\",\"position\":\"Central midfielder\",\"placeOfBirth\":\"Darlington, England\",\"dateOfBirth\":\"{{birth date and age|df=y|1969|10|2}}\",\"__v\":0,\"teams\":[{\"team\":\"Southampton F.C.\",\"start\":1988,\"end\":1997,\"loan\":false,\"_id\":\"5782858fd727f8805262e118\",\"leagueStats\":{\"apps\":169,\"goals\":19}},{\"team\":\"Middlesbrough F.C.\",\"start\":1997,\"end\":2001,\"loan\":false,\"_id\":\"5782858fd727f8805262e117\",\"leagueStats\":{\"apps\":55,\"goals\":4}},{\"team\":\"Barnsley F.C.\",\"start\":2000,\"end\":2000,\"loan\":true,\"_id\":\"5782858fd727f8805262e116\",\"leagueStats\":{\"apps\":3,\"goals\":0}},{\"team\":\"Bristol City\",\"start\":2001,\"end\":2001,\"loan\":true,\"_id\":\"5782858fd727f8805262e115\",\"leagueStats\":{\"apps\":7,\"goals\":1}},{\"team\":\"Darlington F.C.\",\"start\":2001,\"end\":2007,\"loan\":false,\"_id\":\"5782858fd727f8805262e114\",\"leagueStats\":{\"apps\":115,\"goals\":4}}]}";

    public static Player getDummyPlayer() {

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new AutoValueTypeAdapterFactory())
                .create();

        return gson.fromJson(PLAYER, Player.class);
    }

}
