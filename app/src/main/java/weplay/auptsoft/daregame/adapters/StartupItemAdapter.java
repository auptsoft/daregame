package weplay.auptsoft.daregame.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import weplay.auptsoft.daregame.R;
import weplay.auptsoft.daregame.models.StartupItem;

/**
 * Created by Andrew on 10.2.19.
 */

public class StartupItemAdapter extends RecyclerView.Adapter<StartupItemAdapter.SimpleItemAdapterViewHolder> {

    ArrayList<StartupItem> startupItems;
    Context context;

    public StartupItemAdapter(ArrayList<StartupItem> startupItems, Context context) {
        this.startupItems = startupItems;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return startupItems.size();
    }

    @Override
    public SimpleItemAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_start_up_view, parent, false);
        return new SimpleItemAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleItemAdapterViewHolder holder, int position) {
        holder.textOne.setText(startupItems.get(position).getTitle()+"");

        holder.textTwo.setText(startupItems.get(position).getDescription()+"");
        //holder.textTwo.setPaintFlags(holder.textTwo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        //holder.textTwo.setVisibility(startupItems.get(position).getPrice() == startupItems.get(position).getDiscountedPrice()?View.INVISIBLE :View.VISIBLE);

        holder.itemImage.setImageResource(startupItems.get(position).getImageResId());

        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(context, SimpleUpdateActivity.class);
                context.startActivity(intent);
            }
        }); */
    }

    public class SimpleItemAdapterViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        ImageView itemImage;
        TextView textOne, textTwo;
        public SimpleItemAdapterViewHolder(View view) {
            super(view);
            itemView = view.findViewById(R.id.start_up_item_view);
            itemImage = (ImageView)view.findViewById(R.id.start_up_item_image);
            textOne = (TextView)view.findViewById(R.id.start_up_item_title);
            textTwo = (TextView)view.findViewById(R.id.start_up_item_description);
        }
    }
}