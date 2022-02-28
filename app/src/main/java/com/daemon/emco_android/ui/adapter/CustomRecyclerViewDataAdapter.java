package com.daemon.emco_android.ui.adapter;


import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.daemon.emco_android.R;

import java.util.List;


public class CustomRecyclerViewDataAdapter extends RecyclerView.Adapter<CustomRecyclerViewHolder> {

    private List<CustomRecyclerViewItem> viewItemList;
    private int imgCount;
    private ImageListner listner;
    private ImageListnerR listnerR;
    private Context _con;
    private boolean loading;
    private String type=null;

    public CustomRecyclerViewDataAdapter(List<CustomRecyclerViewItem> viewItemList, int imgCount, ImageListner lstn, Context con,boolean loading) {


        if(viewItemList.size()<imgCount){
            viewItemList.add(0,null);
        }
        this.viewItemList = viewItemList;
        this.imgCount=imgCount;
        this.listner=lstn;
        this._con=con;
        this.loading=loading;
    }

    public CustomRecyclerViewDataAdapter(List<CustomRecyclerViewItem> viewItemList, int imgCount, ImageListnerR lstn, Context con,boolean loading,String type) {

        if(viewItemList.size()<imgCount){
            viewItemList.add(0,null);
        }
        this.viewItemList = viewItemList;
        this.imgCount=imgCount;
        this.listnerR=lstn;
        this._con=con;
        this.loading=loading;
        this.type=type;
    }

    @Override
    public CustomRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get LayoutInflater object.
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // Inflate the RecyclerView item layout xml.
        View itemView = layoutInflater.inflate(R.layout.activity_custom_refresh_recycler_view_item, parent, false);

        // Create and return our customRecycler View Holder object.
        CustomRecyclerViewHolder ret = new CustomRecyclerViewHolder(itemView);
        return ret;
    }

    @Override
    public void onBindViewHolder(final CustomRecyclerViewHolder holder, final int position) {


        if(loading){
            holder.getTextView().setVisibility(View.VISIBLE);
            holder.getProgressBar().setVisibility(View.VISIBLE);
            holder.getImageView().setVisibility(View.GONE);
        }
        else {
            holder.getTextView().setVisibility(View.GONE);
            holder.getImageView().setVisibility(View.VISIBLE);
            holder.getProgressBar().setVisibility(View.GONE);

            if (viewItemList != null) {
                // Get car item dto in list.
                CustomRecyclerViewItem viewItem = viewItemList.get(position);

                //.. check view item null to set add image icon

                if (viewItem != null) {

                    //.. check for no image
                    if (viewItem.getDet().getBase64Image() != null) {
                        holder.getImageView().setImageBitmap(viewItem.getThums());
                    } else {
                        holder.getImageView().setImageResource(R.drawable.noimage);
                    }
                } else {
                    holder.getImageView().setVisibility(View.VISIBLE);
                }
            }


            holder.getImageView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //.. position count logic
                    //.. if list item has "image add symbol" it will return list count as position
                    //.. and when add image is not available position will send in reverse order to match the image name from server

                    if (viewItemList == null || viewItemList.get(position) == null) {

                        if(type==null){
                            listner.onImageClick(viewItemList.size() - 1, null);
                        }
                        else{
                            listnerR.onImageClick(viewItemList.size() - 1, null,type);
                        }




                    } else {
                        if (viewItemList.get(position).getDet().getBase64Image() == null) {


                            if(type==null){
                                listner.onImageClick(viewItemList.size() - (position + 1), "noImage");
                            }
                            else{
                                listnerR.onImageClick(viewItemList.size() - (position + 1), "noImage",type);
                            }


                        } else {

                            if(type==null){
                                listner.onImageClick(viewItemList.size() - (position + 1), viewItemList.get(position).getDet().getBase64Image());
                            }
                            else{
                                listnerR.onImageClick(viewItemList.size() - (position + 1), viewItemList.get(position).getDet().getBase64Image(),type);
                            }
                        } } }
            });
        } }

    @Override
    public int getItemCount() {
        int ret = 0;
        if(viewItemList!=null)
        {
            ret = viewItemList.size();
        }
        return ret;
    }

    public interface ImageListner{
         void onImageClick(int pos,String base64);
    }

    public interface ImageListnerR{
        void onImageClick(int pos,String base64,String type);
    }

}

