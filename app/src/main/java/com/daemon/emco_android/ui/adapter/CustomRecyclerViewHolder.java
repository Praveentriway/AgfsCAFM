package com.daemon.emco_android.ui.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daemon.emco_android.R;


public class CustomRecyclerViewHolder extends RecyclerView.ViewHolder {

    private TextView textView = null;
    private ImageView imageView=null;
    private ProgressBar progressBar=null;


    public CustomRecyclerViewHolder(View itemView) {
        super(itemView);

        if(itemView != null)
        {
            textView = (TextView)itemView.findViewById(R.id.custom_refresh_recycler_view_text_view);
            imageView=  (ImageView) itemView.findViewById(R.id.custom_refresh_recycler_view_image_view);
            progressBar=  (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }

    public TextView getTextView() {
        return textView;
    }

    public  ImageView getImageView(){
      return  imageView;
    }

    public ProgressBar getProgressBar(){
        return  progressBar;
    }

}