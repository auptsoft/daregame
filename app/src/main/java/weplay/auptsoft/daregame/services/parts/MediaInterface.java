package weplay.auptsoft.daregame.services.parts;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import weplay.auptsoft.daregame.services.response.GeneralResponse;
import weplay.auptsoft.daregame.services.response.ResultObject;

/**
 * Created by Andrew on 1.3.19.
 */

public interface MediaInterface {
    @Headers({"Accept:application/json"})
    @Multipart
    @POST("{urlPath}")
    Call<GeneralResponse<String>> uploadMediaToServer(@Path(value="urlPath", encoded = true) String urlPath, @Part MultipartBody.Part file, @Part MultipartBody.Part data, @Header("Authorization") String access_token );
}
