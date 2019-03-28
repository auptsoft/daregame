package weplay.auptsoft.daregame.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import weplay.auptsoft.daregame.services.parts.FormInterface;
import weplay.auptsoft.daregame.services.parts.MediaInterface;
import weplay.auptsoft.daregame.services.parts.SearchInterface;
import weplay.auptsoft.daregame.services.response.GeneralResponse;

/**
 * Created by Andrew on 6.3.19.
 */

public class RESTUtil {
    public static class SearchQueryItem {
        private String column;
        private String operator;
        private String value;

        public SearchQueryItem(String column, String operator, String value) {
            this.column = column;
            this.operator = operator;
            this.value = value;
        }

        public String getColumn() {
            return column;
        }

        public void setColumn(String column) {
            this.column = column;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }


    public enum Method{GET, POST, PUT, DELETE};
    private static String accessKey;

    static {
        accessKey = "";
    }
    public static String getAccessKey() {
        return accessKey;
    }

    public static void setAccessKey(String accessKey) {
        RESTUtil.accessKey = accessKey;
    }

    public interface RestResponse<T> {
        void onResponse(Call<String> call, Response<String> response, T data);
        void onFailure(Call<String> call, Throwable throwable);
    }

    //@SuppressWarnings("unchecked")
    public static <T, E> void send(String baseUrl, String path, Method method, T data, Callback<ResponseBody> responseCallback) {
        Gson gson = new GsonBuilder().create();
        String jsonString = gson.toJson(data);
        MultipartBody.Part dataPart = MultipartBody.Part.createFormData("data", jsonString);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FormInterface formInterface = retrofit.create(FormInterface.class);
        Call<ResponseBody> generalResponseCall;
        switch (method) {
            case GET:
                generalResponseCall = formInterface.get(path, getAccessKey());
                break;

            case POST:
                generalResponseCall = formInterface.post(path, dataPart, getAccessKey());
                break;

            case PUT:
                generalResponseCall = formInterface.put(path, dataPart, getAccessKey());
                break;

            case DELETE:
                generalResponseCall = formInterface.delete(path, getAccessKey());
                break;

            default:
                generalResponseCall = formInterface.get(path, getAccessKey());
        }
        generalResponseCall.enqueue(responseCallback);
    }


    public static <T> void uploadMedia(String url, String pathToFile, T data,  Callback<GeneralResponse<String>> responseCallback) {
        File mediaFile = new File(pathToFile);
        RequestBody mediaBody = RequestBody.create(MediaType.parse("video/*"), mediaFile);
        MultipartBody.Part mediaPart = MultipartBody.Part.createFormData("media_file", mediaFile.getName(), mediaBody);

        Gson gson = new GsonBuilder().create();
        String jsonString = gson.toJson(data);
        MultipartBody.Part dataString = MultipartBody.Part.createFormData("data", jsonString);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MediaInterface mediaInterface = retrofit.create(MediaInterface.class);
        Call<GeneralResponse<String>> serverCom = mediaInterface.uploadVideoToServer("upload", mediaPart, dataString, getAccessKey());

        serverCom.enqueue(responseCallback);
    }

    public static void search(String baseUrl, String path, List<SearchQueryItem> data, Callback<ResponseBody> responseCallback){
        Gson gson = new GsonBuilder().create();
        String jsonString = gson.toJson(data);
        MultipartBody.Part dataPart = MultipartBody.Part.createFormData("data", jsonString);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SearchInterface searchInterface = retrofit.create(SearchInterface.class);
        Call<ResponseBody> generalResponseCall = searchInterface.post(path, dataPart, getAccessKey());
        generalResponseCall.enqueue(responseCallback);
    }
}