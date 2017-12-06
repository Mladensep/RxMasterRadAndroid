package com.example.mladen.masterradandroid.retrofit;


import com.example.mladen.masterradandroid.model.CommentModel;
import com.example.mladen.masterradandroid.model.RecensionModel;
import com.example.mladen.masterradandroid.model.SchoolModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RestApi {

    //@GET("/get.php?dataset=skole&lang=sr&term=json")
    @GET("/Api/Schools")
    Observable<List<SchoolModel>> getAllSchoolData();

    @POST("/Api/Comment")
    Observable<String> postOrder(@Body CommentModel sendComment);

    @GET("/Api/Comment/")
    Observable<List<CommentModel>> getAllComment(@Query("id") int id);

    @POST("/Api/Recension")
    Observable<String> postRecension(@Body RecensionModel recensionModel);

    @GET("/Api/Recension/")
    Observable<List<RecensionModel>> getAllRecension(@Query("schoolId") int school_id);
}
