package weplay.auptsoft.daregame.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andremion.louvre.Louvre;
import com.andremion.louvre.home.GalleryActivity;
import com.google.gson.Gson;
import com.yovenny.videocompress.MediaController;

import java.io.File;
import java.io.FileOutputStream;

import es.dmoral.toasty.Toasty;
import pub.devrel.easypermissions.EasyPermissions;
import weplay.auptsoft.daregame.BuildConfig;
import weplay.auptsoft.daregame.R;
import weplay.auptsoft.daregame.activities.VideoActivity;
import weplay.auptsoft.daregame.databinding.FragmentMediaUploadBinding;
import weplay.auptsoft.daregame.models.Media;
import weplay.auptsoft.daregame.presenters.MediaUploadFragmentPresenter;

public class MediaUploadFragment extends DialogFragment implements View.OnClickListener {
    private static final int CAPTURE_VIDEO_REQUEST_CODE = 15;
    private static final int READ_PERMISSION_REQUEST_CODE = 10;
    private static final int PICK_VIDEO_FROM_GALLERY = 16;
    private static final int CAPTURE_IMAGE_REQUEST_CODE = 17;
    private static final int RECORD_AUDIO_FROM_MIC = 18;
    private static final int PICK_AUDIO_FROM_FILE = 19;
    private static final String MEDIA_KEY = "media_key";
    private static final String NEW_CHALLENGE = "new_challenge";
    private static String INTRO_TEXT = "intro_text";
    int IMAGE_SELECTION_CODE = 12;

    private boolean newChallenge = true;

    enum ChooseType {LIVE_VIDEO, LIVE_IMAGE, LIVE_AUDIO, GALLERY_VIDEO, GALLERY_IMAGE, GALLERY_AUDIO};

    public ChooseType chooseType = ChooseType.LIVE_VIDEO;

    Media media;
    String introText;

    String imagePath;
    String videoFilePath;
    String audioFilePath;

    String activeFilePath;

    FragmentMediaUploadBinding binding;
    MediaUploadFragmentPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null) {
            String mediaJson = bundle.getString(MEDIA_KEY, null);
            introText = bundle.getString(INTRO_TEXT, "Upload your media");
            newChallenge = bundle.getBoolean(NEW_CHALLENGE, true);
            if (mediaJson != null) {
                media = new Gson().fromJson(mediaJson, Media.class);
            } else {
                media = new Media();
            }
        } else {
            media = new Media();
            introText = "Upload your media";
        }

        chooseType = getChooseType(media);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_media_upload, container, false);
        View view = binding.getRoot();

        presenter = new MediaUploadFragmentPresenter(this, media);

        binding.setPresenter(presenter);

        //handle();

        presenter.setIntroText(introText);
        presenter.setNewChallenge(newChallenge);

        binding.selectFile.setOnClickListener(this);

        //presenter.setSelectBtnVisible(!media.getMedia_source().equals("live"));

        if (media.getMedia_source().equals("live")) handle();

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.equals(binding.selectFile)) {
            handle();
        }
    }

    /*public static MediaUploadFragment newInstance(ChooseType chooseType) {
        String mediaTypeString = chooseType.toString();

        return newInstance(mediaTypeString);
    }

    public static MediaUploadFragment newInstance(String mediaType) {
        Bundle bundle = new Bundle();
        bundle.putString(MEDIA_KEY, mediaType);

        MediaUploadFragment mediaUploadFragment = new MediaUploadFragment();
        mediaUploadFragment.setArguments(bundle);

        return mediaUploadFragment;
    } */

    public static MediaUploadFragment newInstance(Media media, String introText, boolean newChallenge) {
        Bundle bundle = new Bundle();
        String mediaJson = new Gson().toJson(media, Media.class);
        bundle.putString(MEDIA_KEY, mediaJson);
        bundle.putString(INTRO_TEXT, introText);
        bundle.putBoolean(NEW_CHALLENGE, newChallenge);

        MediaUploadFragment mediaUploadFragment = new MediaUploadFragment();
        mediaUploadFragment.setArguments(bundle);

        return mediaUploadFragment;
    }

    private ChooseType getChooseType(Media media) {
        String chooseTypeString = media.getMedia_source().toUpperCase()+"_"+media.getType().toUpperCase();
        return ChooseType.valueOf(chooseTypeString);
    }

    public void handle() {
        switch (chooseType) {
            case LIVE_VIDEO:
                getVideoFromCamera();
                break;

            case LIVE_IMAGE:
                getImageFromCamera();
                break;

            case LIVE_AUDIO:
                getAudioFromMic();
                break;

            case GALLERY_VIDEO:
                getVideoFromGallery();
                break;

            case GALLERY_IMAGE:
                getImageFromGallery();
                break;

            case GALLERY_AUDIO:
                getAudioFromFile();
                break;

                default:
                    getImageFromGallery();
        }
    }

    public void getImageFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_REQUEST_CODE);
        }
    }

    public void getImageFromGallery() {
        Louvre.init(this)
                .setMaxSelection(1)
                .setRequestCode(IMAGE_SELECTION_CODE)
                .open();
    }

    public void getVideoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if(intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_VIDEO_REQUEST_CODE);
        }
    }

    public void getVideoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_VIDEO_FROM_GALLERY);
    }

    public void getAudioFromMic() {
        Intent intent= new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        startActivityForResult(intent, RECORD_AUDIO_FROM_MIC);
    }

    public void getAudioFromFile() {
        Intent intent= new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_AUDIO_FROM_FILE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_SELECTION_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if(EasyPermissions.hasPermissions(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                imagePath = getRealPathFromURIPath(GalleryActivity.getSelection(data).get(0), getActivity());

                activeFilePath = imagePath;
                presenter.setIntroText(activeFilePath);
                presenter.setMediaPath(activeFilePath);

            } else {
                EasyPermissions.requestPermissions(this, "", READ_PERMISSION_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE );
            }
        } else if(requestCode == CAPTURE_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if(EasyPermissions.hasPermissions(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                /*imagePath = getRealPathFromURIPath(uri, getActivity());
                activeFilePath = imagePath;
                presenter.setIntroText(activeFilePath);
                presenter.setMediaPath(activeFilePath); */
                Bitmap photo = (Bitmap)data.getExtras().get("data");
                File sd = Environment.getExternalStorageDirectory();
                File dest = new File(sd, "pic");

                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(dest);
                    photo.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    activeFilePath = dest.getPath();
                    presenter.setMediaPath(activeFilePath);
                    presenter.setIntroText(activeFilePath);

                } catch (Exception e) {
                    e.printStackTrace();
                    if(BuildConfig.DEBUG) {
                        Toasty.error(getContext(), e.getMessage()).show();
                    } else {
                        Toasty.error(getContext(), "Error occurred while storing picture").show();
                    }
                }
            } else {
                EasyPermissions.requestPermissions(this, "", READ_PERMISSION_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE );
            }
        } else if (requestCode == CAPTURE_VIDEO_REQUEST_CODE && resultCode == Activity.RESULT_OK ) {
            Uri uri = data.getData();
            if(EasyPermissions.hasPermissions(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                videoFilePath = getRealPathFromURIPath(uri, getActivity());
                activeFilePath = videoFilePath;
                presenter.setIntroText(activeFilePath);
                presenter.setMediaPath(activeFilePath);
            } else {
                EasyPermissions.requestPermissions(this, "", READ_PERMISSION_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE );
            }
        }  else if (requestCode == PICK_VIDEO_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if(EasyPermissions.hasPermissions(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                videoFilePath = getRealPathFromURIPath(uri, getActivity());
                activeFilePath = videoFilePath;
                presenter.setIntroText(activeFilePath);
                presenter.setMediaPath(activeFilePath);
            } else {
                EasyPermissions.requestPermissions(this, "", READ_PERMISSION_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE );
            }
        } else if (requestCode == RECORD_AUDIO_FROM_MIC && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if(EasyPermissions.hasPermissions(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                audioFilePath = getRealPathFromURIPath(uri, getActivity());
                activeFilePath = audioFilePath;
                presenter.setIntroText(activeFilePath);
                presenter.setMediaPath(activeFilePath);
            } else {
                EasyPermissions.requestPermissions(this, "", READ_PERMISSION_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE );
            }
        } else if (requestCode == PICK_AUDIO_FROM_FILE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if(EasyPermissions.hasPermissions(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                audioFilePath = getRealPathFromURIPath(uri, getActivity());
                activeFilePath = audioFilePath;
                presenter.setIntroText(activeFilePath);
                presenter.setMediaPath(activeFilePath);
            } else {
                EasyPermissions.requestPermissions(this, "", READ_PERMISSION_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE );
            }
        } else {
            dismiss();
        }
    }


    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String out = cursor.getString(idx);
            cursor.close();
            return out;
        }

    }

}
