package com.jaus.albertogiunta.justintrain_oraritreni.networking;

//public class ConfigsNetworkingFactory {
//
//    public static <T> T createRetrofitService(final Class<T> clazz, final String endPoint) {
//
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
//        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(interceptor)
//                .build();
//
//        Gson gson = new GsonBuilder()
//                .registerTypeAdapter(DateTime.class, new DateTimeAdapter())
//                .registerTypeAdapterFactory(new PostProcessingEnabler())
//                .create();
//
//        final Retrofit restAdapter = new Retrofit.Builder()
//                .baseUrl(endPoint)
//                .client(client)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .build();
//
//        return restAdapter.create(clazz);
//    }
//}
