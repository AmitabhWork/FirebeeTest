package ahh.com.app;

import android.content.Context;
import android.view.View;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ahh.com.app.beans.CoreMember;

class MyRecyclerViewAdapter extends RecyclerView
        .Adapter<MyRecyclerViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private final Context mContext;
    private ArrayList<DataObject> mDataset;
    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView label;
//        TextView dateTime;
        ImageView imageView;

        public DataObjectHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.memberName);
//            dateTime = (TextView) itemView.findViewById(R.id.memberDescript);
            imageView = (ImageView) itemView.findViewById(R.id.member_image);
//            Log.i(LOG_TAG, "Adding Listener");
//            itemView.setOnClickListener(this);
        }

//        @Override
//        public void onClick(View v) {
//            myClickListener.onItemClick(getAdapterPosition(), v);
//        }
    }

//    public void setOnItemClickListener(MyClickListener myClickListener) {
//        this.myClickListener = myClickListener;
//    }

    public MyRecyclerViewAdapter(ArrayList<DataObject> myDataset, Context context) {
        mDataset = myDataset;
        mContext = context;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item_one, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        StorageReference imgpath = mDataset.get(position).getImageUrl();
        Log.e("Adapter > onBindViewHolder", imgpath.toString());
        holder.label.setText(mDataset.get(position).getName());
//        holder.dateTime.setText(mDataset.get(position).getmText2());
//        holder.imageView.setImageResource();
//        Picasso.with(mContext).load(imgpath).
//                transform(new CropSquareTransformation()).into(holder.imageView);

//        Glide.with(mContext)
//                .load(mDataset.get(position).getMbgimage())
//                .centerCrop()
////                .placeholder(R.drawable.loading_spinner)
////                .crossFade()
//                .into(holder.imageView);
        Glide.with(mContext)
                .using(new FirebaseImageLoader())
                .load(imgpath).fitCenter().crossFade().placeholder(R.drawable.placeholder_md)
                .into(holder.imageView);
    }

    public void addItem(DataObject dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

}

