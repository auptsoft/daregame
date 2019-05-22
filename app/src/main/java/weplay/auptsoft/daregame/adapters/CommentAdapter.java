package weplay.auptsoft.daregame.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import weplay.auptsoft.daregame.BR;
import weplay.auptsoft.daregame.BuildConfig;
import weplay.auptsoft.daregame.R;
import weplay.auptsoft.daregame.databinding.ItemCommentBinding;
import weplay.auptsoft.daregame.models.Comment;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentSimpleViewHolder> {
    private ArrayList<Comment> commentArrayList;
    private Context context;

    public CommentAdapter(ArrayList<Comment> commentArrayList, Context context) {
        this.commentArrayList = commentArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentSimpleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemCommentBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_comment, viewGroup, false);

        return new CommentSimpleViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentSimpleViewHolder commentSimpleViewHolder, int i) {

        commentSimpleViewHolder.binding.commentUser.setText(commentArrayList.get(i).getUser().getUsername());
        commentSimpleViewHolder.binding.commentContent.setText(commentArrayList.get(i).getContent());

        commentSimpleViewHolder.binding.commentUserPicCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BuildConfig.DEBUG) {
                    Toast.makeText(context, "img clicked", Toast.LENGTH_LONG).show();
                }
            }
        });

        commentSimpleViewHolder.binding.commentUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BuildConfig.DEBUG) {
                    Toast.makeText(context, "username clicked", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentArrayList.size();
    }

    class CommentSimpleViewHolder extends RecyclerView.ViewHolder {
        ItemCommentBinding binding;

        public CommentSimpleViewHolder(ItemCommentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
