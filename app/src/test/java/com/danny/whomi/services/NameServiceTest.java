package com.danny.whomi.services;

import com.danny.whomi.services.NameService;
import com.google.common.base.Objects;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by danny on 05-Mar-17.
 */
@RunWith(MockitoJUnitRunner.class)
public class NameServiceTest {

    @Test
    public void testUpperCase() throws Exception {

        NameService nameService = new NameService();

        assertThat(nameService.normalizeName("hello World"), guessMatcher("HELLO WORLD"));
        assertThat(nameService.normalizeName("heLlo World"), guessMatcher("HELLO WORLD"));
        assertThat(nameService.normalizeName("HELLO WORLD"), guessMatcher("HELLO WORLD"));

    }

    @Test
    public void testSpaceBehavior() {

        NameService nameService = new NameService();

        assertThat(nameService.normalizeName(" hello h    ello "), guessMatcher("hello h ello"));
    }

    @Test
    public void testAccentsStriping() {

        NameService nameService = new NameService();

        assertThat(nameService.normalizeName("Luîz Canâô"), guessMatcher("Luiz Canao"));
        assertThat(nameService.normalizeName("Luïz çanâe"), guessMatcher("Luiz Canae"));
        assertThat(nameService.normalizeName("Lüÿz çanâê"), guessMatcher("Luyz Canae"));
    }

    @Test
    public void testRemovingNonEnglishName() {

        NameService nameService = new NameService();

        assertThat(nameService.normalizeName("Beni Dahan בני דהן"), guessMatcher("beni dahan"));
        assertThat(nameService.normalizeName("בני בני Beni Dahan בני דהן"), guessMatcher("beni dahan"));

    }

    private Matcher<String> guessMatcher(String s) {

        return is(s.toUpperCase());

    }


}
