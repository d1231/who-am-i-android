package com.danny.projectt.utils;


import com.danny.projectt.model.objects.Player;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;

import sharedTest.PlayerHelper;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class GsonConverterTest {

    private Gson gson;

    @Before
    public void setUp() throws Exception {

        gson = new GsonBuilder().registerTypeAdapterFactory(AutoValueTypeAdapterFactory.create())
                                .create();

    }

    @Test
    public void testFrom() throws Exception {

        final GsonConverter<Player> gsonConverter = new GsonConverter<>(gson, Player.class);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        final Player player = PlayerHelper.getDummyPlayer();
        gsonConverter.toStream(player, outputStream);

        final Player o = gson.fromJson(new InputStreamReader(new ByteArrayInputStream(outputStream.toByteArray())), Player.class);

        assertThat(o, is(player));
    }

    @Test
    public void testTo() throws Exception {


        final GsonConverter<Player> gsonConverter = new GsonConverter<>(gson, Player.class);

        final Player player = PlayerHelper.getDummyPlayer();

        final Player resPlayer = gsonConverter.from(PlayerHelper.PLAYER_RAW_JSON.getBytes());

        assertThat(resPlayer, is(player));

    }
}
