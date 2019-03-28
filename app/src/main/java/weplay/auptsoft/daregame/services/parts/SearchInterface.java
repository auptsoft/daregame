package weplay.auptsoft.daregame.services.parts;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Andrew on 12.3.19.
 */

public interface SearchInterface {
    @Headers({"Accept:application/json"})
    @Multipart
    @POST("{urlPath}")
    Call<ResponseBody> post(@Path(value = "urlPath", encoded = true) String path, @Part MultipartBody.Part data, @Header("Authorization") String access_token);
}
