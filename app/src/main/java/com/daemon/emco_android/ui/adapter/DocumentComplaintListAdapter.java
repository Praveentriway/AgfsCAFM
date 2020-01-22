package com.daemon.emco_android.ui.adapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.model.common.ReactiveSupportDoc;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DocumentComplaintListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static String MODULE = "RCLAdapter";
    public static String TAG = "";

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private List<ReactiveSupportDoc> data = new ArrayList<>();
    private AppCompatActivity mActivity;
    private DocumentListOnClick mCallBack;
    private Font font;
    private int mSelectedPosition;
    private Date dateStart = new Date();
    private boolean isFooterEnabled = false;
    private String mUnSignedPage = AppUtils.ARGS_RECEIVECOMPLAINT_PAGE;
    private String type;

    public DocumentComplaintListAdapter(AppCompatActivity mActivity, List<ReactiveSupportDoc> data,String type) {
        TAG = "RC_ListAdapter";
        Log.d(MODULE, TAG);

        this.mActivity = mActivity;
        this.data = data;
        this.type=type;
        font = App.getInstance().getFontInstance();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TAG = "onCreateViewHolder";
        Log.d(MODULE, TAG);
        RecyclerView.ViewHolder mHolder = null;
        if (viewType == VIEW_ITEM) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_document_complaint, parent, false);
            mHolder = new DocumentComplaintListAdapter.ComplaintListViewHolder(layoutView);
        } else if (viewType == VIEW_PROG) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_loading_message_list, parent, false);
            mHolder = new DocumentComplaintListAdapter.LoadingMessageHolder(layoutView);
        }
        return mHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder mHolder, final int position) {
        TAG = "onBindViewHolder";
        Log.d(MODULE, TAG);

        try {
            // DateFormat formatter = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss aa", Locale.ENGLISH);
            if (mHolder instanceof DocumentComplaintListAdapter.ComplaintListViewHolder) {
                final DocumentComplaintListAdapter.ComplaintListViewHolder holder = (DocumentComplaintListAdapter.ComplaintListViewHolder) mHolder;
                final ReactiveSupportDoc current = data.get(position);



                if ((position+1)%2 == 0){
                    ((DocumentComplaintListAdapter.ComplaintListViewHolder) mHolder).linear_main.setBackgroundColor(mActivity.getResources().getColor(R.color.colorWhite));
                }
                else{
                    ((DocumentComplaintListAdapter.ComplaintListViewHolder) mHolder).linear_main.setBackgroundColor(mActivity.getResources().getColor(R.color.list_bg  ));
                }

                holder.itemView.setTag(position);
                int pos=position+1;

                 holder.tv_sno.setText(""+pos);
                if (current.getComplaintNo() != null)
                    holder.tv_complaint_no.setText(current.getComplaintNo());
                if (current.getComplaintDate() != null)
                    holder.tv_time.setText(current.getComplaintDate());

                if (current.getComplaintsDetails() != null)
                    holder.tv_complaint_details.setText(current.getComplaintsDetails());
                if (current.getStatusDesc() != null)
                    holder.tv_status.setText(current.getStatusDesc());
                if (current.getWorkNatureDesc() != null)
                    holder.tv_work_type.setText(current.getWorkNatureDesc());


                // changing value dynamically based on Nature of complaint (R or P)
               if(type.equalsIgnoreCase("R")){
                   if (current.getSiteCode() != null)
                       holder.tv_site_location.setText(current.getSiteCode() + "/" + current.getLocation());
                   if (current.getPriority() != null)
                       holder.tv_priority.setText(current.getPriority());
                   if (current.getCustomerRefno() != null)
                       if(current.getCustomerRefno().equalsIgnoreCase("-")){
                           holder.tv_customer_ref.setText(current.getCustomerRefno());
                       }else{
                           holder.tv_customer_ref.setText(current.getCustomerRefno());
                       }
               }
               else{


                   if (current.getSiteCode() != null)
                       holder.tv_site_location.setText(current.getBuildingName() + "/" + current.getLocation());
                   if (current.getCompletedDate() != null)
                       holder.tv_priority.setText(current.getCompletedDate());
                   if (current.getBarcode() != null)
                       holder.tv_customer_ref.setText(current.getBarcode());

               }

            } else if (mHolder instanceof DocumentComplaintListAdapter.LoadingMessageHolder) {
                DocumentComplaintListAdapter.LoadingMessageHolder holder = (DocumentComplaintListAdapter.LoadingMessageHolder) mHolder;
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

    public void updateList(List<ReactiveSupportDoc> itemEntities) {
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

    public void deleteItem(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    public void setmUnSignedPage(String mUnSignedPage) {
        this.mUnSignedPage = mUnSignedPage;
    }

    public void setListener(DocumentListOnClick mCallBack) {
        this.mCallBack = mCallBack;
    }

    public class ComplaintListViewHolder extends RecyclerView.ViewHolder {
        TextView tv_complaint_no, tv_site_location, tv_complaint_details, tv_time, tv_status,
                tv_work_type, tv_priority, tv_customer_ref;

        LinearLayout linear_main;

        TextView tv_sno;

        public ComplaintListViewHolder(final View itemView) {
            super(itemView);
            try {
                tv_sno = (TextView) itemView.findViewById(R.id.tv_sno);
                tv_complaint_no = (TextView) itemView.findViewById(R.id.tv_complaint_no);
                tv_time = (TextView) itemView.findViewById(R.id.tv_date);
                tv_work_type = (TextView) itemView.findViewById(R.id.tv_work_type);
                tv_priority = (TextView) itemView.findViewById(R.id.tv_priority);
                tv_site_location = (TextView) itemView.findViewById(R.id.tv_site_location);
                tv_complaint_details = (TextView) itemView.findViewById(R.id.tv_complaint_details);
                tv_status = (TextView) itemView.findViewById(R.id.tv_status);
                tv_customer_ref = (TextView) itemView.findViewById(R.id.tv_customer_ref);
                linear_main= (LinearLayout) itemView.findViewById(R.id.linear_main);

                tv_complaint_no.setTypeface(font.getHelveticaRegular());
                tv_time.setTypeface(font.getHelveticaRegular());
                tv_work_type.setTypeface(font.getHelveticaRegular());
                tv_priority.setTypeface(font.getHelveticaRegular());
                tv_site_location.setTypeface(font.getHelveticaRegular());
                tv_complaint_details.setTypeface(font.getHelveticaRegular());
                tv_status.setTypeface(font.getHelveticaRegular());
                tv_customer_ref.setTypeface(font.getHelveticaRegular());

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            mCallBack.onDocumentListItemClicked(data, (int) itemView.getTag());
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


    public interface DocumentListOnClick{

        void onDocumentListItemClicked(List<ReactiveSupportDoc> temp, int position);

    }







}
