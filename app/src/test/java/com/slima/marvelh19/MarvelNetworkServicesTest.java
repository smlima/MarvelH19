package com.slima.marvelh19;

import com.slima.marvelh19.model.characters.CharacterResult;
import com.slima.marvelh19.model.characters.ComicsResult;
import com.slima.marvelh19.network.MarvelNetworkServices;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class MarvelNetworkServicesTest {

    static MarvelNetworkServices sMarvelNetworkServices;

    @BeforeClass
    public static void setup(){
        sMarvelNetworkServices = new MarvelNetworkServices();
    }

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void getCharecters() {

        try {
            List<CharacterResult> charecters = sMarvelNetworkServices.getCharacters();

            for (CharacterResult comics : charecters) {
                System.out.println(comics.getName());
            }

            charecters = sMarvelNetworkServices.getCharacters();

            for (CharacterResult comics : charecters) {
                System.out.println(comics.getName());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void getComics() {

        try {
            List<ComicsResult> comics = sMarvelNetworkServices.getComics();

            for (ComicsResult comic : comics) {
                System.out.println(comic.getTitle());
            }

            comics = sMarvelNetworkServices.getComics();

            for (ComicsResult comic : comics) {
                System.out.println(comic.getTitle());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}