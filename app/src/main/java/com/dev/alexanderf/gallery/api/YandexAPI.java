package com.dev.alexanderf.gallery.api;

import android.support.annotation.NonNull;

import com.dev.alexanderf.gallery.GalleryApplication;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by AF.
 */
public class YandexAPI {
    private static final String API_BASE_URL = "https://cloud-api.yandex.net/";
    private static final int HTTP_REQUEST_TIMEOUT = 30;
    public static final String PUBLIC_KEY_URL = "https://yadi.sk/d/mXxwzgCH3UVw57";

    public interface API {

    @GET("/v1/disk/public/resources")
    Call<YandexResponse<PublicListResponse>> getItemList(@Query("public_key") String public_key, @Query("offset") Integer offset, @Query("limit") Integer limit);

    }

    private static Cache cache = null;
    private static API api;

    static class ApiCacheInterceptor implements Interceptor {

        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {

            Request request = chain.request();
            Request newRequest = request.newBuilder().cacheControl(new CacheControl.Builder()
                    .maxAge(7, TimeUnit.DAYS)
                    .maxStale(7, TimeUnit.DAYS)
                    .build()).build();
            return chain.proceed(newRequest);
        }
    }

    private static void initLogging(OkHttpClient.Builder builder) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(logging);
    }


    public synchronized static API getInstanceWithCache(){
        if(api == null){
            if(cache == null){
                cache = new Cache(GalleryApplication.getAppContext().getCacheDir(), 30L * 1024 * 1024);
            }
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .connectTimeout(HTTP_REQUEST_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(HTTP_REQUEST_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(HTTP_REQUEST_TIMEOUT, TimeUnit.SECONDS)
                    .cache (cache)
                    .addInterceptor(new ApiCacheInterceptor());

            initLogging(builder);

            OkHttpClient httpClient = builder.build();
            api = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(httpClient)
                    .build()
                    .create(API.class);
        }
        return api;
    }

}
