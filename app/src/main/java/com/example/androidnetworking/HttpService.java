package com.example.androidnetworking;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class HttpService {
    private OnCustomEventListener listener;

    public HttpService(OnCustomEventListener listener) {
        this.listener = listener;
    }

    public JSONArray sendHttpRequest(
            HttpMethod httpMethod,
            String url,
            boolean isRelativeUrl,
            Map<String, String> params,
            boolean isAuth,
            Map<String, String> customHeaders,
            Map<String, String> body
    ) throws ANError {


        final Map<String, JSONArray> responseMap = new HashMap<>();
        final Map<String, ANError> errorMap = new HashMap<>();
        switch (httpMethod) {
            case GET:
                AndroidNetworking.get(url)
                        .addQueryParameter(params)
                        .addHeaders(customHeaders)
                        .setPriority(Priority.LOW)
                        .build()
                        .getAsJSONArray(new JSONArrayRequestListener() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Log.d("ddddddd", "" + response.toString());
                                // responseMap.put("response",response);
                                listener.onEvent(response);
                                EventBus.getDefault().post(new MessageEvent("Hey event subscriber!"));
                            }

                            @Override
                            public void onError(ANError error) {
                                //   errorMap.put("error", error);
                                listener.onError(error);
                            }
                        });
                break;

        }
        if (!errorMap.isEmpty()) {
            throw errorMap.get("error");
        }

        return responseMap.get("response");
    }

    public interface OnCustomEventListener {
        void onEvent(JSONArray jsonArray);

        void onError(ANError error);
    }
}
