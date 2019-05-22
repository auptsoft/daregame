package weplay.auptsoft.daregame.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import weplay.auptsoft.daregame.AppState;
import weplay.auptsoft.daregame.BuildConfig;
import weplay.auptsoft.daregame.R;
import weplay.auptsoft.daregame.adapters.CommentAdapter;
import weplay.auptsoft.daregame.databinding.FragmentChallengeDetailsBinding;
import weplay.auptsoft.daregame.models.Category;
import weplay.auptsoft.daregame.models.Challenge;
import weplay.auptsoft.daregame.models.Comment;
import weplay.auptsoft.daregame.models.Media;
import weplay.auptsoft.daregame.models.User;
import weplay.auptsoft.daregame.presenters.ChallengeDetailsPresenter;
import weplay.auptsoft.daregame.services.RESTUtil;
import weplay.auptsoft.daregame.services.response.GeneralResponse;
import weplay.auptsoft.daregame.view_models.ChallengeViewModel;

/**
 * Created by Andrew on 25.3.19.
 */

public class ChallengeDetailsFragment extends Fragment implements View.OnClickListener{

    FragmentChallengeDetailsBinding binding;

    ArrayList<Comment> commentArrayList;

    ChallengeDetailsPresenter presenter;

    CommentAdapter commentAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_challenge_details, container, false);
        View view = binding.getRoot();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(AppState.currentChallenge.getTitle());

        presenter = new ChallengeDetailsPresenter(getContext());

        binding.setPresenter(presenter);

        Challenge challenge = AppState.currentChallenge; //new Challenge("Title2", "description of Challenge2", 2.3, 2, 1);

        binding.setItem(new ChallengeViewModel(challenge, getContext()));

        if(challenge.getChallenger_media() != null && challenge.getChallenger_media().size() > 0) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.challenger_media_frame, MediaItemFragment.newInstance(challenge.getChallenger_media().get(0)))
                    .commit();
        } else {
            View v = getLayoutInflater().inflate(R.layout.no_media_layout, null, false);
            //binding.challengeMediaFrame.addView(v);
        }

        initializeData();

        commentAdapter = new CommentAdapter(commentArrayList, getContext());
        binding.challengeCommentsRecyclerView.setAdapter(commentAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.challengeCommentsRecyclerView.setLayoutManager(layoutManager);
        binding.challengeCommentsRecyclerView.setNestedScrollingEnabled(true);

        binding.postCommentAction.setOnClickListener(this);
        binding.likeIcon.setOnClickListener(this);

        presenter.setLiked(containsUser(AppState.currentChallenge.getLikes(), AppState.user));

        return view;
    }

    @Override
    public void onResume() {
        if(AppState.isDirty()) {
            initializeData();
            AppState.handleDirty();
        }
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        if(view.equals(binding.postCommentAction)) {
            Comment comment = new Comment("by "+AppState.user.getUsername(),
                    binding.newCommentInputEdit.getText().toString(),
                    AppState.user.getId());
            comment.setCommentable_id(AppState.currentChallenge.getId());
            comment.setCommentable_type("challenge");
            comment.setUser_id(AppState.user.getId());

            presenter.setCommentPostLoading(true);
            RESTUtil.send(AppState.BASE_URL, AppState.INITIAL_PATH + "/comment", RESTUtil.Method.POST, comment,
                    new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            presenter.setCommentPostLoading(false);
                            if(response.message().equals("OK")) {
                                try {
                                    String responseString = response.body().string();
                                    Type type = new TypeToken<GeneralResponse<Comment>>() {}.getType();

                                    GeneralResponse<Comment> commentGeneralResponse = new Gson().fromJson(responseString, type);
                                    if (commentGeneralResponse.getMessage().equals("success")) {
                                        commentArrayList.add(commentGeneralResponse.getData());
                                        commentAdapter.notifyDataSetChanged();

                                        Toasty.success(getContext(), "Comment Posted").show();
                                        AppState.setDirty(1);
                                    }else {
                                        if (BuildConfig.DEBUG) {
                                            Toasty.warning(getContext(), responseString).show();
                                        } else {
                                            Toasty.warning(getContext(), "Unexpected error occurred").show();
                                        }
                                    }
                                } catch (Exception e) {
                                    if(BuildConfig.DEBUG) {
                                        Toasty.warning(getContext(), e.getMessage()).show();
                                    } else {
                                        Toasty.warning(getContext(), "Unexpected error occured").show();
                                    }
                                }
                            } else {
                                if(BuildConfig.DEBUG) {
                                    Toasty.warning(getContext(), response.message()).show();
                                } else {
                                    Toasty.warning(getContext(), "Unexpected error occured").show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            presenter.setCommentPostLoading(false);
                            if(BuildConfig.DEBUG) {
                                Toasty.error(getContext(), t.getMessage()).show();
                            } else {
                                Toasty.error(getContext(), "Error occurred while connecting to the internet").show();
                            }
                        }
                    });
        } else if (view.equals(binding.likeIcon)) {

            presenter.setLikeActionLoading(true);
            RESTUtil.send(AppState.BASE_URL, AppState.INITIAL_PATH + "/toggleLike/challenge/" + AppState.currentChallenge.getId(),
                    RESTUtil.Method.POST, "", new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            presenter.setLikeActionLoading(false);

                            if (response.message().equals("OK")) {
                                try {
                                    String jsonString = response.body().string();
                                    Type type = new TypeToken<GeneralResponse<String>>() {
                                    }.getType();
                                    GeneralResponse<String> generalResponse = new Gson().fromJson(jsonString, type);

                                    if (generalResponse.getMessage().equals("liked")) {
                                        Challenge challenge = AppState.currentChallenge;
                                        challenge.getLikes().add(AppState.user);
                                        ChallengeViewModel viewModel = binding.getItem();
                                        viewModel.setChallenge(challenge);
                                        binding.setItem(viewModel);
                                        presenter.setLiked(true);
                                        AppState.setDirty(2);
                                    } else if (generalResponse.getMessage().equals("unliked")) {
                                        Challenge challenge = AppState.currentChallenge;
                                        challenge.setLikes(removeUser((ArrayList<User>) challenge.getLikes(), AppState.user));
                                        ChallengeViewModel viewModel = binding.getItem();
                                        viewModel.setChallenge(challenge);
                                        binding.setItem(viewModel);
                                        presenter.setLiked(false);
                                        AppState.setDirty(2);
                                    }
                                    Toasty.success(getContext(), generalResponse.getData()).show();
                                } catch (IOException ioe) {
                                    if (BuildConfig.DEBUG) {
                                        Toasty.error(getContext(), "An unexpected error occurred").show();
                                    }
                                }
                            } else {
                                Toasty.error(getContext(), response.message()).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            presenter.setLikeActionLoading(false);
                            if(BuildConfig.DEBUG) {
                                Toasty.error(getContext(), t.getMessage());
                            } else {
                                Toasty.error(getContext(), "Error occurred while connecting to the internet").show();
                            }
                        }
                    });
            //http://192.168.43.32/daregame/public/api/challenge/like;
        }
    }

    void initializeData() {
        List<Comment> comments = AppState.currentChallenge.getComments();
        if(comments != null ) {
            commentArrayList = (ArrayList<Comment>)comments;
        }
    }

//    void getComments() {
//        presenter.setCommentPostLoading(true);
//        RESTUtil.send(AppState.BASE_URL, AppState.INITIAL_PATH + "/comment", RESTUtil.Method.POST, comment,
//                new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        presenter.setCommentPostLoading(false);
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        presenter.setCommentPostLoading(false);
//                    }
//                });
//    }

//    void initializeTestData() {
//        commentArrayList = new ArrayList<>();
//        commentArrayList.add(new Comment("title", "content", 1));
//        commentArrayList.add(new Comment("title", "content", 1));
//        commentArrayList.add(new Comment("title", "content", 1));
//        commentArrayList.add(new Comment("title", "content", 1));
//        commentArrayList.add(new Comment("title", "content", 1));
//    }

    ArrayList<User> removeUser(ArrayList<User> users, User user) {
        //ArrayList<User> userArrayList = new ArrayList<>();
        for (int i=0; i<users.size(); i++) {
            if(users.get(i).getId() == user.getId()) {
                users.remove(i);
            }
        }
        return users;
    }

    boolean containsUser(List<User> users, User user) {
        for(User us : users) {
            if (us.getId() == user.getId()) {
                return true;
            }
        }
        return false;
    }

}
