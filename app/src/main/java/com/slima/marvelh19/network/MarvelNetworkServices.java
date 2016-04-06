package com.slima.marvelh19.network;

import com.slima.marvelh19.model.characters.CharacterResult;
import com.slima.marvelh19.model.characters.ComicsResult;
import com.slima.marvelh19.model.characters.ComicsResults;
import com.slima.marvelh19.model.characters.EventsResult;
import com.slima.marvelh19.model.characters.Response;
import com.slima.marvelh19.model.characters.SeriesResult;
import com.slima.marvelh19.model.characters.StoriesResult;
import com.slima.marvelh19.network.services.CharactersEntityService;
import com.slima.marvelh19.network.services.ComicsEntityService;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * The network services to connect to Marvel
 *
 * API method calls specified here
 *
 * Created by sergio.lima on 30/03/2016.
 */
public class MarvelNetworkServices {

    protected final String MAREVEL_BASE_URL = "http://gateway.marvel.com/v1/public/";
    protected final String MARVEL_KEY_PUBLIC = "70a60e84b1442a00a41fcf202baa3bee";
    protected final String MARVEL_KEY_PRIVATE = "160e804d9ae3c260cf3c2916c6d02eff68265999";

    private final CharactersEntityService mCharactersEntityService;
    private final ComicsEntityService mComicsEntityService;

    //FIXME: This might be bette on a sharedpref or something ?!?! and increased after every request
    private Long mTimeStamp = new Long(0);

    private String oldHash = "";

    /**
     * Constructor of the Marvel Srevice
     */
    public MarvelNetworkServices()  {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        String hashed = getNewHash(1L);

                        HttpUrl newurl = original.url().newBuilder()
                                .addEncodedQueryParameter("apikey", MARVEL_KEY_PUBLIC)
                                .addEncodedQueryParameter("ts", mTimeStamp.toString())
                                .addEncodedQueryParameter("hash", hashed)
                                .build();

                        Request request = original.newBuilder()
                                .header("User-Agent", "SLIMA-App")
                                .header("Content-Type", "application/json;charset=utf-8")
                                .header("Accept", "application/json")
                                .method(original.method(), original.body())
                                .url(newurl)
                                .build();

                        return chain.proceed(request);
                    }
                })
                .addInterceptor(interceptor)
                .cache(new Cache(new File("./tmp/"), 1024))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MAREVEL_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)

                .build();

        mCharactersEntityService = retrofit.create(CharactersEntityService.class);
        mComicsEntityService = retrofit.create(ComicsEntityService.class);

    }

    /**
     * get hew hash if required.
     * Avoiding hashing unnecessary because its a cpu intensive task
     *
     * @param newTimestamp  the new timestamp
     * @return  the new hash or old if timestamp equals to previous
     */
    private String getNewHash(Long newTimestamp) {

        if (!newTimestamp.equals(mTimeStamp)) {
            // update timestamp
            mTimeStamp = newTimestamp;

            String toHash = mTimeStamp + MARVEL_KEY_PRIVATE + MARVEL_KEY_PUBLIC;

            System.out.println("Tohash: " + toHash);

            MessageDigest digester = null;
            try {
                digester = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            byte[] digest = digester.digest(toHash.getBytes());

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < digest.length; i++) {
                if ((0xff & digest[i]) < 0x10) {
                    sb.append("0").append(Integer.toHexString((0xFF & digest[i])));
                } else {
                    sb.append(Integer.toHexString(0xFF & digest[i]));
                }
            }

            oldHash = sb.toString();

        }

        // return old hash or updated
        return oldHash;
    }

    /**
     * implementation of the Characters entity fetching mechanism
     *
     * @return
     * @throws IOException
     */
    public List<CharacterResult> getCharacters() throws IOException {
        Call<Response<CharacterResult>> config = mCharactersEntityService.getCharacters(4, 0, "name");
        retrofit2.Response<Response<CharacterResult>> execute = config.execute();

        return execute.body().getData().getResults();
    }

    /**
     *  implementation of the Characters entity fetching mechanism
     *
     * @param total
     * @param offset
     * @return
     * @throws IOException
     */
    public List<CharacterResult> getCharacters(int total, int offset) throws IOException {
        Call<Response<CharacterResult>> config = mCharactersEntityService.getCharacters(total, offset, "name");
        retrofit2.Response<Response<CharacterResult>> execute = config.execute();

        if (execute.code() == HttpURLConnection.HTTP_OK && execute.body() != null) {
            return execute.body().getData().getResults();
        } else {
            return null;
        }
    }

    /**
     * implementation of the Characters entity fetching mechanism
     *
     * @return
     * @throws IOException
     */
    public List<ComicsResult> getComics() throws IOException {
        Call<Response<ComicsResult>> config = mComicsEntityService.getComics(4, 0);
        retrofit2.Response<Response<ComicsResult>> execute = config.execute();

        return execute.body().getData().getResults();
    }

    /**
     * implementation of the Characters entity fetching mechanism
     *
     * @return
     * @throws IOException
     */
    public List<ComicsResults> getComicsForCharacterId(Integer characterId) throws IOException {
        Call<Response<ComicsResults>> config = mCharactersEntityService.getComicsPerCharacterId(characterId);
        retrofit2.Response<Response<ComicsResults>> execute = config.execute();

        return execute.body().getData().getResults();
    }

    /**
     * implementation of the Characters entity fetching mechanism
     *
     * @return
     * @throws IOException
     */
    public List<SeriesResult> getSeriesForCharacterId(Integer characterId) throws IOException {
        Call<Response<SeriesResult>> config = mCharactersEntityService.getSeriesPerCharacterId(characterId);
        retrofit2.Response<Response<SeriesResult>> execute = config.execute();

        return execute.body().getData().getResults();
    }

    /**
     * implementation of fetching the events per character id
     *
     * @param characterId
     * @return
     * @throws IOException
     */
    public List<EventsResult> getEventsForCharacterId(Integer characterId) throws IOException {
        Call<Response<EventsResult>> config = mCharactersEntityService.getEventsPerCharacterId(characterId);
        retrofit2.Response<Response<EventsResult>> execute = config.execute();

        return execute.body().getData().getResults();
    }

    /**
     * implementation of fetching the stories per character id
     * @param characterId
     * @return
     * @throws IOException
     */
    public List<StoriesResult> getStoriesForCharacterId(Integer characterId) throws IOException {
        Call<Response<StoriesResult>> config = mCharactersEntityService.getStoriesPerCharacterId(characterId);
        retrofit2.Response<Response<StoriesResult>> execute = config.execute();

        return execute.body().getData().getResults();
    }

    /**
     * implementation of fetching the stories per character id
     * @param byName    name to search for
     * @return
     * @throws IOException
     */
    public List<CharacterResult> getCharactersByName(String byName) throws IOException {
        Call<Response<CharacterResult>> config = mCharactersEntityService.getCharactersByName(byName);
        retrofit2.Response<Response<CharacterResult>> execute = config.execute();

        return execute.body().getData().getResults();
    }
}
