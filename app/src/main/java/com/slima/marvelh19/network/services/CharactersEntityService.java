package com.slima.marvelh19.network.services;

import com.slima.marvelh19.model.characters.CharacterResult;
import com.slima.marvelh19.model.characters.ComicsResults;
import com.slima.marvelh19.model.characters.EventsResult;
import com.slima.marvelh19.model.characters.Response;
import com.slima.marvelh19.model.characters.SeriesResult;
import com.slima.marvelh19.model.characters.StoriesResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * REST calls rellated to the fetching of characters
 * <p/>
 * Created by sergio.lima on 31/03/2016.
 */
public interface CharactersEntityService {

    /**
     * fecth the list of characters after an offset and with a limit
     *
     * @param limit
     * @param offset
     * @return the list of characters
     */
    @GET("characters")
    Call<Response<CharacterResult>> getCharacters(@Query("limit") Integer limit, @Query("offset") Integer offset, @Query("orderBy") String orderBy);

    /**
     * fetch the characters with the default options
     *
     * @return the list of characters
     */
    @GET("characters")
    Call<Response<CharacterResult>> getCharacters(@Query("orderBy") String orderBy);

    /**
     * fetch the characters with the default options searching by name
     *
     * @return the list of characters
     */
    @GET("characters")
    Call<Response<CharacterResult>> getCharactersByName(@Query("nameStartsWith") String name);

    /**
     * Get the character by id
     *
     * @param charaterId the character id
     * @return the Charatcer
     */
    @GET("characters/{characterid}")
    Call<Response<CharacterResult>> getCharacterById(@Path("characterid") String charaterId);

    /**
     * Get the character by id
     *
     * @param charaterId the character id
     * @return the Charatcer
     */
    @GET("characters/{characterid}/comics")
    Call<Response<ComicsResults>> getComicsPerCharacterId(@Path("characterid") Integer charaterId);

    /**
     * Get the character by id
     *
     * @param charaterId the character id
     * @return the Charatcer
     */
    @GET("characters/{characterid}/series")
    Call<Response<SeriesResult>> getSeriesPerCharacterId(@Path("characterid") Integer charaterId);

    /**
     * Get the character by id
     *
     * @param charaterId the character id
     * @return the Charatcer
     */
    @GET("characters/{characterid}/events")
    Call<Response<EventsResult>> getEventsPerCharacterId(@Path("characterid") Integer charaterId);

    /**
     * Get the character by id
     *
     * @param charaterId the character id
     * @return the Charatcer
     */
    @GET("characters/{characterid}/stories")
    Call<Response<StoriesResult>> getStoriesPerCharacterId(@Path("characterid") Integer charaterId);
}
