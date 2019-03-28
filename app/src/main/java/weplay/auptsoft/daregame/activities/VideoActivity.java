package weplay.auptsoft.daregame.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wang.avi.AVLoadingIndicatorView;
import com.yovenny.videocompress.MediaController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import weplay.auptsoft.daregame.R;
import weplay.auptsoft.daregame.databinding.ActivityVideoBinding;
import weplay.auptsoft.daregame.models.Media;
import weplay.auptsoft.daregame.models.Tag;
import weplay.auptsoft.daregame.services.RESTUtil;
import weplay.auptsoft.daregame.services.response.GeneralResponse;
import weplay.auptsoft.daregame.services.parts.MediaInterface;

/**
 * Created by Andrew on 1.3.19.
 */

public class VideoActivity extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks{
    ActivityVideoBinding activityVideoBinding;
    public static int CAPTURE_VIDEO_REQUEST_CODE = 5;
    public static int READ_PERMISSION_REQUEST_CODE = 6;
    String videoFilePath;
    static final String SERVER_PATH = "http://192.168.43.187/daregame/public/api/";

    Handler handler = new Handler();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityVideoBinding = DataBindingUtil.setContentView(this, R.layout.activity_video);

        activityVideoBinding.record.setOnClickListener(this);
        activityVideoBinding.upload.setOnClickListener(this);
        activityVideoBinding.uploadTag.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.equals(activityVideoBinding.record)) {
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            if(intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, CAPTURE_VIDEO_REQUEST_CODE);
            }
        } else if (view.equals(activityVideoBinding.upload)) {
            if (videoFilePath != null) {
                Toast.makeText(getBaseContext(), "compressing", Toast.LENGTH_LONG).show();
                final String compressedOutputPath = getFileDestinationPath();
                compressVideo(videoFilePath, compressedOutputPath, new OnCompressResult() {
                    @Override
                    public void onResult(String path) {
                        Toast.makeText(getBaseContext(), videoFilePath+"....... "+getFileDestinationPath(), Toast.LENGTH_LONG).show();
                        activityVideoBinding.record.setText(videoFilePath);
                        activityVideoBinding.upload.setText(getFileDestinationPath());
                        //uploadVideoToServer(compressedOutputPath);
                        uploadMediaToServer(compressedOutputPath);
                    }

                    @Override
                    public void onError(String errorString) {
                        Toast.makeText(getBaseContext(), errorString, Toast.LENGTH_LONG).show();
                    }
                });

            } else {
                Toast.makeText(getBaseContext(), "Capture video first", Toast.LENGTH_LONG).show();
            }
            //uploadTag();
        } else if (view.equals(activityVideoBinding.uploadTag)) {
            uploadTag();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == CAPTURE_VIDEO_REQUEST_CODE) {
            Uri uri = data.getData();
            if(EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                activityVideoBinding.videoView.setVideoURI(uri);
                activityVideoBinding.videoView.start();
                videoFilePath = getRealPathFromURIPath(uri, this);
            } else {
                EasyPermissions.requestPermissions(this, "", READ_PERMISSION_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE );
            }
        }
    }

    private String getFileDestinationPath() {
        String generatedFilename = String.valueOf(System.currentTimeMillis());
        String filePathEnvironment = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        File directoryFolder = new File(filePathEnvironment+"/video/");
        if(!directoryFolder.exists()) {
            directoryFolder.mkdir();
        }
        return filePathEnvironment+"/video/"+generatedFilename+".mp4";
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return  cursor.getString(idx);
        }
    }

    private void uploadTag() {
        Tag tag = new Tag("General", "This tag is attached to all models");
        Toast.makeText(this, "uploading tag", Toast.LENGTH_SHORT).show();
        RESTUtil.send(SERVER_PATH, "tag", RESTUtil.Method.POST, tag, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //Toast.makeText(getBaseContext(), "Data: "+response.body(), Toast.LENGTH_LONG).show();
                try {
                    String result = response.body().string();
                    Log.i("OUTPUT", result);
                    Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();

                    JSONObject jsonObject = new JSONObject(result);

                    /*String status = jsonObject.getString("status");
                    String tagString = jsonObject.getJSONObject("data").toString();
                    Tag t = new GsonBuilder().create().fromJson(tagString, Tag.class);

                    Toast.makeText(getBaseContext(), t.getDescription(), Toast.LENGTH_LONG).show(); */

                    //com.wang.avi.indicators.BallPulseRiseIndicator

                    Type generalResponseType = new TypeToken<GeneralResponse<List<Tag>>>(){}.getType();
                    Gson gson = new Gson();
                    GeneralResponse<List<Tag>> tagGeneralResponse = gson.fromJson(result, generalResponseType);
                    Toast.makeText(getBaseContext(), tagGeneralResponse.getData().size()+"", Toast.LENGTH_LONG).show();

                } catch (IOException ioe) {
                    Toast.makeText(getBaseContext(),  ioe.getMessage(), Toast.LENGTH_LONG).show();
                } catch (JSONException je) {
                    Toast.makeText(getBaseContext(), je.getMessage(), Toast.LENGTH_LONG).show();
                    je.printStackTrace();
                }
                //Gson gson = new GsonBuilder().create();
                //Tag t = gson.fromJson(response.body().getData().toString(), Tag.class);
                //Toast.makeText(getBaseContext(), t.getDescription(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Errors: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                /*Object obj = new Object();
                List<String> strings = (List<String>)obj; */
            }
        });
    }

    private void uploadMediaToServer(String pathToMediaFile) {
        File mediaFile = new File(pathToMediaFile);
        Media media = new Media(pathToMediaFile, "Image From Android",
                "video", "Description of video", mediaFile.getName(), 1,  "challenge");
        RESTUtil.uploadMedia(SERVER_PATH, pathToMediaFile, media, new Callback<GeneralResponse<String>>() {
            @Override
            public void onResponse(Call<GeneralResponse<String>> call, Response<GeneralResponse<String>> response) {
                //Toast.makeText(getApplication(), response.message(), Toast.LENGTH_LONG).show();
                GeneralResponse<String> generalResponse = response.body();
                if(!TextUtils.isEmpty(generalResponse.getMessage())) {
                    Toast.makeText(getBaseContext(), generalResponse.getMessage()+" =>"+generalResponse.getData(), Toast.LENGTH_LONG ).show();
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse<String>> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Errors: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadVideoToServer(String pathToVideoFile) {
        File videoFile = new File(pathToVideoFile);
        RequestBody videoBody = RequestBody.create(MediaType.parse("video/*"), videoFile);
        MultipartBody.Part vFile = MultipartBody.Part.createFormData("media_file", videoFile.getName(), videoBody);
        MultipartBody.Part data = MultipartBody.Part.createFormData("data", "Hello");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_PATH)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MediaInterface mediaInterface = retrofit.create(MediaInterface.class);
        Call<GeneralResponse<String>> serverCom = mediaInterface.uploadVideoToServer("upload", vFile, data, "");

        serverCom.enqueue(new Callback<GeneralResponse<String>>() {
            @Override
            public void onResponse(Call<GeneralResponse<String>> call, Response<GeneralResponse<String>> response) {
                GeneralResponse<String> resultObject = response.body();
                if(!TextUtils.isEmpty(resultObject.getMessage())) {
                    Toast.makeText(getBaseContext(), resultObject.getMessage()+" =>"+resultObject.getData(), Toast.LENGTH_LONG ).show();
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse<String>> call, Throwable t) {
                Toast.makeText(getBaseContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    /*private void uploadVideoToServer(String pathToVideoFile) {
        File videoFile = new File(pathToVideoFile);
        RequestBody videoBody = RequestBody.create(MediaType.parse("video/*"), videoFile);
        MultipartBody.Part vFile = MultipartBody.Part.createFormData("media_file", videoFile.getName(), videoBody);
        MultipartBody.Part data = MultipartBody.Part.createFormData("data", "");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_PATH)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MediaInterface mediaInterface = retrofit.create(MediaInterface.class);
        Call<ResultObject> serverCom = mediaInterface.uploadVideoToServer(vFile, data);

        serverCom.enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                ResultObject resultObject = response.body();
                if(!TextUtils.isEmpty(resultObject.getMessage())) {
                    Toast.makeText(getBaseContext(), resultObject.getMessage()+" =>"+resultObject.getData(), Toast.LENGTH_LONG ).show();
                }
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                Toast.makeText(getBaseContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    } */

    private void compressVideo(final String inPath, final String outPath, final OnCompressResult onCompressResult) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (MediaController.getInstance().convertVideo(inPath, outPath)) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onCompressResult.onResult(outPath);
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onCompressResult.onError("Could not compress");
                        }
                    });
                }
            }
        }).start();
    }

    interface OnCompressResult {
        void onResult(String path);
        void onError(String errorString);
    }
}
