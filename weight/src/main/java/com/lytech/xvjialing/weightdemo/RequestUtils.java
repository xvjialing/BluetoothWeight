package com.lytech.xvjialing.weightdemo;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;

public class RequestUtils {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String TAG = RequestUtils.class.getSimpleName();
    private String jsonStr;

    private OkHttpClient client;
    private Map<String, String> params;
    private String request_url;
    private Context mContext;

    private String session;
    private long size;

    public RequestUtils() {
        client = new OkHttpClient();
    }


    public Observable<String> requestWithoutCookie(Map<String, String> map, String requestUrl) {
        params = map;
        request_url = requestUrl;
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    FormBody.Builder builder = new FormBody.Builder();
                    if (params != null && !params.isEmpty()) {
                        for (Map.Entry<String, String> entry : params.entrySet()) {
                            Log.d(TAG, entry.getKey() + entry.getValue() + "");
                            builder.add(entry.getKey(), entry.getValue());
                        }
                    }
                    RequestBody requestBody = builder.build();
                    Request request = new Request.Builder()
                            .url(request_url)
                            .post(requestBody)
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            subscriber.onError(e);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                Headers headers=response.headers();
                                Log.d(TAG+"headers",headers.toString());
//                                List<String> cookies=headers.values("Set-Cookie");
//                                String session = cookies.get(0);
//                                Log.d(TAG + "session", session);

                                subscriber.onNext(UnicodeDecode.decode(response.body().string()));
                            }
                            subscriber.onCompleted();
                        }
                    });
                }
            }

        });
    }

}
