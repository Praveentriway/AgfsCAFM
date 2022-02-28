package com.daemon.emco_android.ui.adapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.listeners.ReceivecomplaintList_Listener;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintItemEntity;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class IM_HardSoftListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static String MODULE = "RCLAdapter";
    public static String TAG = "";

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private List<ReceiveComplaintItemEntity> data = new ArrayList<>();
    private AppCompatActivity mActivity;
    private ReceivecomplaintList_Listener mCallBack;
    private Font font;
    private int mSelectedPosition;
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    private boolean isFooterEnabled = false;

    public IM_HardSoftListAdapter(AppCompatActivity mActivity, List<ReceiveComplaintItemEntity> data) {
        TAG = "RC_ListAdapter";
        Log.d(MODULE, TAG);
        this.mActivity = mActivity;
        this.data = data;
        font = App.getInstance().getFontInstance();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TAG = "onCreateViewHolder";
        Log.d(MODULE, TAG);
        RecyclerView.ViewHolder mHolder = null;
        if (viewType == VIEW_ITEM) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_im_hardsoftlist, parent, false);
            mHolder = new ComplaintListViewHolder(layoutView);
        } else if (viewType == VIEW_PROG) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_loading_message_list, parent, false);
            mHolder = new LoadingMessageHolder(layoutView);
        }
        return mHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder mHolder, final int position) {
        TAG = "onBindViewHolder";
        Log.d(MODULE, TAG);

        try {
            DateFormat formatter = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss aa", Locale.ENGLISH);
            if (mHolder instanceof ComplaintListViewHolder) {
                final ComplaintListViewHolder holder = (ComplaintListViewHolder) mHolder;
                final ReceiveComplaintItemEntity current = data.get(position);

                holder.itemView.setTag(position);
                holder.tv_s_no.setText(String.valueOf(position + 1));

                if (current.getTicketNo() != null)
                    holder.tv_request_no.setText(current.getTicketNo());
                if (current.getComplainDate() != null)
                    holder.tv_time.setText( current.getComplainDate());
                if (current.getSiteCode() != null)
                    holder.tv_request_location.setText(current.getSiteCode() + "/" + current.getLocation());
                if (current.getComplainDetails() != null)
                    holder.tv_complaint_details.setText(current.getComplainDetails());
                if (current.getStatusDesription() != null)
                    holder.tv_status.setText(current.getStatusDesription());
                if(current.getCustomerRefrenceNumber()!=null)
                    holder.tv_customer_ref_no.setText(current.getCustomerRefrenceNumber());
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
        return (isFooterEnabled) ? data.size() + 1 : data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (isFooterEnabled && position >= data.size()) ? VIEW_PROG : VIEW_ITEM;
    }


    public void enableFooter(boolean footerEnabled) {
        isFooterEnabled = footerEnabled;
    }

    public void selectPosition(int position) {
        int lastPosition = mSelectedPosition;
        mSelectedPosition = position;
        notifyItemChanged(lastPosition);
        notifyItemChanged(position);
    }

    public void updateList(List<ReceiveComplaintItemEntity> itemEntities) {
        data = itemEntities;
        notifyDataSetChanged();
    }

    public void checkAll(boolean b) {
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                data.get(i).setChecked(b ? data.get(i).getStatus().equals(AppUtils.FORWARDED) : b);
            }
            notifyDataSetChanged();
        }
    }

    public void refreshItemtoResponded(int position) {
        ReceiveComplaintItemEntity itemToUpdate = data.get(position);
        if (itemToUpdate.getStatus().equals(AppUtils.FORWARDED)) {
            itemToUpdate.setStatus(AppUtils.RESPONDED);
            itemToUpdate.setStatusDesription(AppUtils.mRESPONDED);
            data.remove(position);
            data.add(position, itemToUpdate);
            this.notifyItemRemoved(position);
            this.notifyItemInserted(position);
        }
    }

    public void deleteItem(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    public void setListener(ReceivecomplaintList_Listener mCallBack) {
        this.mCallBack = mCallBack;
    }

    public class ComplaintListViewHolder extends RecyclerView.ViewHolder {
        TextView tv_s_no, tv_request_no, tv_request_location, tv_complaint_details, tv_time, tv_status,tv_customer_ref_no;
        CheckBox cb_ccc;

        public ComplaintListViewHolder(final View itemView) {
            super(itemView);
            try {
                cb_ccc = (CheckBox) itemView.findViewById(R.id.cb_ccc);

                tv_s_no = (TextView) itemView.findViewById(R.id.tv_s_no);
                tv_request_no = (TextView) itemView.findViewById(R.id.tv_request_no);
                tv_time = (TextView) itemView.findViewById(R.id.tv_date);
                tv_request_location = (TextView) itemView.findViewById(R.id.tv_request_location);
                tv_complaint_details = (TextView) itemView.findViewById(R.id.tv_complaint_details);
                tv_status = (TextView) itemView.findViewById(R.id.tv_status);
                tv_customer_ref_no=(TextView)itemView.findViewById(R.id.tv_customer_ref_no);

                tv_s_no.setTypeface(font.getHelveticaRegular());
                tv_time.setTypeface(font.getHelveticaRegular());
                tv_request_no.setTypeface(font.getHelveticaRegular());
                tv_request_location.setTypeface(font.getHelveticaRegular());
                tv_complaint_details.setTypeface(font.getHelveticaRegular());
                tv_status.setTypeface(font.getHelveticaRegular());
                tv_customer_ref_no.setTypeface(font.getHelveticaRegular());

                cb_ccc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    }
                });

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            //int pos = getAdapterPosition();
                            //ReceiveComplaintItemEntity it = data.get(pos);
                            //Toast.makeText(v.getContext(), "You clicked " + it.getComplaintNumber(), Toast.LENGTH_SHORT).show();
                            mCallBack.onReceiveComplaintListItemClicked(data, (int) itemView.getTag());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
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
