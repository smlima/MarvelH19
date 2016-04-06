package com.slima.marvelh19.network.services;

import com.slima.marvelh19.model.characters.ComicsResult;
import com.slima.marvelh19.model.characters.ComicsResults;
import com.slima.marvelh19.model.characters.Response;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 *
 * Created by sergio.lima on 31/03/2016.
 */
public interface ComicsEntityService {


    /**
     * fecth the list of comics after an offset and with a limit
     *
     * @param limit
     * @param offset
     * @return the list of comics
     */
    @GET("comics")
    Call<Response<ComicsResult>> getComics(@Query("limit") Integer limit, @Query("offset") Integer offset);

    /**
     * fetch the comics with the default options
     *
     * @return the list of comic
     */
    @GET("comics")
    Call<Response<ComicsResult>> getComics();

    /**
     * Get the comic by id
     *
     * @param comicId the comic id
     * @return the comic
     */
    @GET("comics/{comicId}")
    Call<Response<ComicsResults>> getComicById(@Path("comicId") String comicId);
}
