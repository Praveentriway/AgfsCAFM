package com.daemon.emco_android.ui.adapter;

import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintItemEntity;
import com.daemon.emco_android.listeners.ReceivecomplaintList_Listener;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;

import java.util.List;

public class ReceivecomplaintAdapter extends PagedListAdapter<ReceiveComplaintItemEntity, RecyclerView.ViewHolder> {
    public static String MODULE = "RCLAdapter";
    public static String TAG = "";

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private ReceivecomplaintList_Listener mCallBack;
    private Font font;
    private int mSelectedPosition;
    private boolean isFooterEnabled = false;
    private String mUnSignedPage = AppUtils.ARGS_RECEIVECOMPLAINT_PAGE;

    public ReceivecomplaintAdapter() {
        super(ReceiveComplaintItemEntity.DIFF_CALLBACK);
        TAG = "RC_ListAdapter";
        Log.d(MODULE, TAG);
        font = App.getInstance().getFontInstance();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TAG = "onCreateViewHolder";
        Log.d(MODULE, TAG);
        RecyclerView.ViewHolder mHolder = null;
        if (viewType == VIEW_ITEM) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_receivecomplaint, parent, false);
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
            // DateFormat formatter = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss aa", Locale.ENGLISH);
            if (mHolder instanceof ComplaintListViewHolder) {
                final ComplaintListViewHolder holder = (ComplaintListViewHolder) mHolder;
                final ReceiveComplaintItemEntity current = getItem(position);

                holder.itemView.setTag(position);

                if (current.getComplainRefrenceNumber() != null)
                    holder.tv_complaint_no.setText(current.getComplainRefrenceNumber());
                if (current.getComplainDate() != null)
                    holder.tv_time.setText(current.getComplainDate());
                if (current.getSiteCode() != null)
                    holder.tv_site_location.setText(current.getSiteCode() + "/" + current.getLocation());
                if (current.getComplainDetails() != null)
                    holder.tv_complaint_details.setText(current.getComplainDetails());
                if (current.getStatusDesription() != null)
                    holder.tv_status.setText(current.getStatusDesription());
                if (current.getWorkTypeDescription() != null)
                    holder.tv_work_type.setText(current.getWorkTypeDescription());
                if (current.getPriority() != null)
                    holder.tv_priority.setText(current.getPriority());
                if (current.getCustomerRefrenceNumber() != null)
                    holder.tv_customer_ref.setText(current.getCustomerRefrenceNumber());

                if (mUnSignedPage == AppUtils.ARGS_RECEIVECOMPLAINT_PAGE) {
                    holder.cb_ccc.setChecked(current.isChecked());
                } else {
                    holder.cb_ccc.setVisibility(View.GONE);
                }
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
        return (isFooterEnabled) ? getCurrentList().size() + 1 : getCurrentList().size();
    }

    @Override
    public int getItemViewType(int position) {
        return (isFooterEnabled && position >= getCurrentList().size()) ? VIEW_PROG : VIEW_ITEM;
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
        itemEntities = itemEntities;
        notifyDataSetChanged();
    }

    public void checkAll(boolean b) {
        if (getCurrentList() != null) {
            for (int i = 0; i < getCurrentList().size(); i++) {
                getCurrentList().get(i).setChecked(b ? getCurrentList().get(i).getStatus().equals(AppUtils.FORWARDED) : b);
            }
            notifyDataSetChanged();
        }
    }

    public void refreshItemtoResponded(int position) {
        ReceiveComplaintItemEntity itemToUpdate = getCurrentList().get(position);
        if (itemToUpdate.getStatus().equals(AppUtils.FORWARDED)) {
            itemToUpdate.setStatus(AppUtils.RESPONDED);
            itemToUpdate.setStatusDesription(AppUtils.mRESPONDED);
            getCurrentList().remove(position);
            getCurrentList().add(position, itemToUpdate);
            this.notifyItemRemoved(position);
            this.notifyItemInserted(position);
        }
    }

    public void deleteItem(int position) {
        getCurrentList().remove(position);
        notifyItemRemoved(position);
    }

    public void setmUnSignedPage(String mUnSignedPage) {
        this.mUnSignedPage = mUnSignedPage;
    }

    public void setListener(ReceivecomplaintList_Listener mCallBack) {
        this.mCallBack = mCallBack;
    }

    public class ComplaintListViewHolder extends RecyclerView.ViewHolder {
        TextView tv_complaint_no, tv_site_location, tv_complaint_details, tv_time, tv_status,
                tv_work_type, tv_priority, tv_customer_ref;

        CheckBox cb_ccc;

        public ComplaintListViewHolder(final View itemView) {
            super(itemView);
            try {
                cb_ccc = (CheckBox) itemView.findViewById(R.id.cb_ccc);
                tv_complaint_no = (TextView) itemView.findViewById(R.id.tv_complaint_no);
                tv_time = (TextView) itemView.findViewById(R.id.tv_date);
                tv_work_type = (TextView) itemView.findViewById(R.id.tv_work_type);
                tv_priority = (TextView) itemView.findViewById(R.id.tv_priority);
                tv_site_location = (TextView) itemView.findViewById(R.id.tv_site_location);
                tv_complaint_details = (TextView) itemView.findViewById(R.id.tv_complaint_details);
                tv_status = (TextView) itemView.findViewById(R.id.tv_status);
                tv_customer_ref = (TextView) itemView.findViewById(R.id.tv_customer_ref);

                tv_complaint_no.setTypeface(font.getHelveticaRegular());
                tv_time.setTypeface(font.getHelveticaRegular());
                tv_work_type.setTypeface(font.getHelveticaRegular());
                tv_priority.setTypeface(font.getHelveticaRegular());
                tv_site_location.setTypeface(font.getHelveticaRegular());
                tv_complaint_details.setTypeface(font.getHelveticaRegular());
                tv_status.setTypeface(font.getHelveticaRegular());
                tv_customer_ref.setTypeface(font.getHelveticaRegular());

                cb_ccc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        try {
                            if (mUnSignedPage == AppUtils.ARGS_RECEIVECOMPLAINT_PAGE) {
                                ReceiveComplaintItemEntity current = getCurrentList().get((int) itemView.getTag());

                                if (current.getStatus().equals(AppUtils.FORWARDED)) {
                                    getCurrentList().get((int) itemView.getTag()).setChecked(b);
                                    mCallBack.onReceiveComplaintListItemChecked(getCurrentList());
                                } else {
                                    current.setChecked(false);
                                    cb_ccc.setChecked(false);
                                    // AppUtils.showDialog(mActivity, "Responded complaints can't be reassigned");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            mCallBack.onReceiveComplaintListItemClicked(getCurrentList(), (int) itemView.getTag());
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
