package com.daemon.emco_android.ui.adapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.listeners.SearchComplaintListener;
import com.daemon.emco_android.repository.db.entity.MultiSearchComplaintEntity;
import com.daemon.emco_android.utils.Font;

import java.util.ArrayList;

public class ViewComplaintListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = ViewComplaintListAdapter.class.getSimpleName();

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private ArrayList<MultiSearchComplaintEntity> searchComplaintEntities = new ArrayList<>();
    private AppCompatActivity mActivity;
    private SearchComplaintListener mCallBack;
    private Font font;
    private int mSelectedPosition;
    private boolean isFooterEnabled = false;

    public ViewComplaintListAdapter(AppCompatActivity mActivity, ArrayList<MultiSearchComplaintEntity> searchComplaintEntities) {
        Log.d(TAG, "ViewComplaintListAdapter");

        this.mActivity = mActivity;
        this.searchComplaintEntities = searchComplaintEntities;
        font = App.getInstance().getFontInstance();
    }

    public void enableFooter(boolean footerEnabled) {
        isFooterEnabled = footerEnabled;
    }

    public void setListener(SearchComplaintListener mCallBack) {
        this.mCallBack = mCallBack;
    }

    public void delete(int position) {
        searchComplaintEntities.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        RecyclerView.ViewHolder mHolder = null;
        if (viewType == VIEW_ITEM) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_search_complaint_item, parent, false);
            mHolder = new ComplaintListViewHolder(layoutView);
        } else if (viewType == VIEW_PROG) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_loading_message_list, parent, false);
            mHolder = new LoadingMessageHolder(layoutView);
        }
        return mHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder mHolder, final int position) {
        Log.d(TAG, "onBindViewHolder");

        try {
            if (mHolder instanceof ComplaintListViewHolder) {
                ComplaintListViewHolder holder = (ComplaintListViewHolder) mHolder;
                MultiSearchComplaintEntity item = searchComplaintEntities.get(position);
                holder.tv_zone_area.setText(item.getZoneName());
                holder.tv_complaint_no.setText(item.getTicketNo());
                holder.tv_date.setText(item.getTaskDate());
                holder.tv_site_location.setText(item.getLocation());
                holder.tv_nature.setText(item.getWorkNatureDesc());
                holder.tv_request_details.setText(item.getTaskDetails());
                holder.tv_status.setText(item.getStatusDesc());
                holder.tv_coutumer_ref.setText(item.getCustomerRefNo());
                holder.tv_property.setText(item.getBuildingName());

            } else if (mHolder instanceof LoadingMessageHolder) {
                LoadingMessageHolder holder = (LoadingMessageHolder) mHolder;
                holder.layout_loading_message.setVisibility(View.VISIBLE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return (isFooterEnabled) ? searchComplaintEntities.size() + 1 : searchComplaintEntities.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (isFooterEnabled && position >= searchComplaintEntities.size()) ? VIEW_PROG : VIEW_ITEM;
    }


    public void selectPosition(int position) {
        int lastPosition = mSelectedPosition;
        mSelectedPosition = position;
        notifyItemChanged(lastPosition);
        notifyItemChanged(position);
    }

    public class ComplaintListViewHolder extends RecyclerView.ViewHolder {
        TextView tv_zone_area, tv_property, tv_site_location, tv_complaint_no, tv_request_details, tv_nature, tv_date,
                tv_status, tv_coutumer_ref;
        View itemView;

        public ComplaintListViewHolder(View itemView) {
            super(itemView);
            try {
                this.itemView = itemView;
                tv_complaint_no = (TextView) itemView.findViewById(R.id.tv_complaint_no);
                tv_date = (TextView) itemView.findViewById(R.id.tv_date);
                tv_zone_area = (TextView) itemView.findViewById(R.id.tv_zone_area);
                tv_site_location = (TextView) itemView.findViewById(R.id.tv_site_location);
                tv_request_details = (TextView) itemView.findViewById(R.id.tv_request_details);
                tv_status = (TextView) itemView.findViewById(R.id.tv_status);
                tv_coutumer_ref = (TextView) itemView.findViewById(R.id.tv_coutumer_ref);
                tv_nature = (TextView) itemView.findViewById(R.id.tv_nature);
                tv_property = (TextView) itemView.findViewById(R.id.tv_property);

                tv_complaint_no.setTypeface(font.getHelveticaRegular());
                tv_date.setTypeface(font.getHelveticaRegular());
                tv_site_location.setTypeface(font.getHelveticaRegular());
                tv_request_details.setTypeface(font.getHelveticaRegular());
                tv_nature.setTypeface(font.getHelveticaRegular());
                tv_status.setTypeface(font.getHelveticaRegular());
                tv_coutumer_ref.setTypeface(font.getHelveticaRegular());
                tv_zone_area.setTypeface(font.getHelveticaRegular());
                tv_property.setTypeface(font.getHelveticaRegular());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public class LoadingMessageHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView text_view_loading_message;
        LinearLayout layout_loading_message;

        public LoadingMessageHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            layout_loading_message = (LinearLayout) itemView.findViewById(R.id.layout_loading);
            text_view_loading_message = (TextView) itemView.findViewById(R.id.text_view_message);
            text_view_loading_message.setTypeface(font.getHelveticaRegular());
        }
    }

}
