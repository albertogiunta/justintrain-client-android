package com.jaus.albertogiunta.justintrain_oraritreni.networking;

import com.jaus.albertogiunta.justintrain_oraritreni.data.Message;
import com.jaus.albertogiunta.justintrain_oraritreni.data.ServerConfig;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

public interface ConfigsService {

    String SERVICE_ENDPOINT = "https://gist.githubusercontent.com";

    @GET("/albertogiunta/0edfd0717e63e3f13f0d528c33eaab1d/raw/messages_config.json")
    Observable<List<Message>> getAllMessages();

    @GET("/albertogiunta/ab6460dfd72aa460e224c6a09fbd07e1/raw/server_config.json")
    Observable<List<ServerConfig>> getAllServerConfigs();

}
