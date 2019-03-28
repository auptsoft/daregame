package weplay.auptsoft.daregame.services.parts;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Andrew on 6.3.19.
 */

public interface FormInterface {
    @Headers("Accept: application/json")
    @Multipart
    @POST("{urlPath}")
    Call<ResponseBody> post(@Path(value = "urlPath", encoded = true) String path, @Part MultipartBody.Part data, @Header("Authorization") String access_token);

    @Headers("Accept: application/json")
    @GET("{urlPath}")
    Call<ResponseBody> get(@Path(value = "urlPath", encoded = true) String path, @Header("Authorization") String access_token);

    @Headers("Accept: application/json")
    @Multipart
    @PUT("{urlPath}")
    Call<ResponseBody> put(@Path(value = "urlPath", encoded = true) String path, @Part MultipartBody.Part data, @Header("Authorization") String access_token);

    @Headers("Accept: application/json")
   // @Multipart
    @DELETE("{urlPath}")
    Call<ResponseBody> delete(@Path(value = "urlPath", encoded = true) String path,  @Header("Authorization") String access_token);
}
