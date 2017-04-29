package com.jaus.albertogiunta.justintrain_oraritreni.networking;

import com.jaus.albertogiunta.justintrain_oraritreni.data.News;
import com.jaus.albertogiunta.justintrain_oraritreni.data.Train;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface TrainService {

    String version = "v1";

    @GET(version + "/train/{trainId}")
    Observable<Train> getTrainDetails(@Path("trainId") String trainId);

    @GET(version + "/train/{trainId}/news")
    Observable<News> getTrainNews(@Path("trainId") String trainId);
}
