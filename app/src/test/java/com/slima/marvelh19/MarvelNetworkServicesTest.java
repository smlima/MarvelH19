package com.slima.marvelh19;

import com.slima.marvelh19.model.characters.CharacterResult;
import com.slima.marvelh19.model.characters.ComicsResults;
import com.slima.marvelh19.network.MarvelNetworkServices;
import com.slima.marvelh19.services.MyExecutor;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

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
            List<ComicsResults> comics = sMarvelNetworkServices.getComics();

            for (ComicsResults comic : comics) {
                System.out.println(comic.getTitle());
            }

            comics = sMarvelNetworkServices.getComics();

            for (ComicsResults comic : comics) {
                System.out.println(comic.getTitle());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    @Test
    public void testExecutor() {


        MyExecutor myExecutor = new MyExecutor();

        FutureTask task1 = new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                Thread.sleep(300);

                System.out.println("Task1 ..... done");

                return null;
            }
        });

        FutureTask task2 = new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                Thread.sleep(400);

                System.out.println("Task2 ..... done");

                return null;
            }
        });
        FutureTask task3 = new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                Thread.sleep(200);

                System.out.println("Task3 ..... done");

                return null;
            }
        });

        FutureTask task4 = new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {

                Thread.sleep(250);
                System.out.println("Task4 ..... done");

                return null;
            }
        });

        myExecutor.execute(task1);
        System.out.println(myExecutor.getQueue().remainingCapacity());
        myExecutor.execute(task2);
        System.out.println(myExecutor.getQueue().remainingCapacity());

        if (myExecutor.getQueue().remainingCapacity()==0) {
            // wait
            try {
                myExecutor.awaitTermination(100, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                try {
                    myExecutor.getQueue().take();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }

                e.printStackTrace();
            }
        }

        myExecutor.execute(task3);

        System.out.println(myExecutor.getQueue().remainingCapacity());

        if (myExecutor.getQueue().remainingCapacity()==0) {
            // wait
            try {
                myExecutor.awaitTermination(100, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                try {
                    myExecutor.getQueue().take();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        }
        myExecutor.execute(task4);
        System.out.println(myExecutor.getQueue().remainingCapacity());

        task1.cancel(true);


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Finishde test");


    }


}