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

import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.repository.remote.HardSoftService;
import com.daemon.emco_android.model.common.PpmScheduleDetails;
import com.daemon.emco_android.utils.Font;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class IM_PPMListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static String MODULE = "RCLAdapter";
    public static String TAG = "";

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private List<PpmScheduleDetails> data = new ArrayList<>();
    private AppCompatActivity mActivity;
    private HardSoftService.Listener mCallBack;
    private Font font;
    private int mSelectedPosition;
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    private boolean isFooterEnabled = false;

    public IM_PPMListAdapter(AppCompatActivity mActivity, List<PpmScheduleDetails> data) {
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
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_im_ppmdetails, parent, false);
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
                final PpmScheduleDetails current = data.get(position);

                holder.itemView.setTag(position);
                holder.tv_s_no.setText(String.valueOf(position + 1));

                if (current.getBuilding() != null)
                    holder.tv_building.setText(current.getBuilding());
                if (current.getLocationName() != null)
                    holder.tv_location_name.setText(current.getLocationName());
                if (current.getAssetBarCode() != null)
                    holder.tv_location_barcode.setText(current.getAssetBarCode());
                if (current.getStartDate() != null)
                    holder.tv_start_date.setText(dateFormat.format(formatter.parse(current.getStartDate())));
                if (current.getEndDate() != null)
                    holder.tv_end_date.setText(dateFormat.format(formatter.parse(current.getEndDate())));
                if (current.getPpmStatus() != null)
                    holder.tv_total_ppm.setText(current.getPpmStatus());
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

    public void updateList(List<PpmScheduleDetails> itemEntities) {
        data = itemEntities;
        notifyDataSetChanged();
    }

    public void setListener(HardSoftService.Listener mCallBack) {
        this.mCallBack = mCallBack;
    }

    public class ComplaintListViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_s_no, tv_building, tv_location_floor_flat, tv_total_ppm,tv_location_name,tv_location_barcode, tv_start_date, tv_end_date, tv_ppm_details;
        CheckBox cb_ccc;

        public ComplaintListViewHolder(final View itemView) {
            super(itemView);
            try {
                cb_ccc = (CheckBox) itemView.findViewById(R.id.cb_ccc);

                tv_s_no = (TextView) itemView.findViewById(R.id.tv_s_no);
                tv_building = (TextView) itemView.findViewById(R.id.tv_building);
                tv_total_ppm=(TextView)itemView.findViewById(R.id.tv_total_ppm);
                tv_location_name=(TextView)itemView.findViewById(R.id.tv_location_name);
                tv_location_floor_flat = (TextView) itemView.findViewById(R.id.tv_location_floor_flat);
                tv_location_barcode = (TextView) itemView.findViewById(R.id.tv_location_barcode);
                tv_start_date = (TextView) itemView.findViewById(R.id.tv_start_date);
                tv_end_date = (TextView) itemView.findViewById(R.id.tv_end_date);
                tv_ppm_details = (TextView) itemView.findViewById(R.id.tv_ppm_details);


                tv_s_no.setTypeface(font.getHelveticaRegular());
                tv_building.setTypeface(font.getHelveticaRegular());
                tv_location_name.setTypeface(font.getHelveticaRegular());
                tv_location_floor_flat.setTypeface(font.getHelveticaRegular());
                tv_location_barcode.setTypeface(font.getHelveticaRegular());
                tv_start_date.setTypeface(font.getHelveticaRegular());
                tv_end_date.setTypeface(font.getHelveticaRegular());
                tv_ppm_details.setTypeface(font.getHelveticaRegular());

                cb_ccc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    }
                });

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            mCallBack.onppmListItemClicked(data, (int) itemView.getTag());
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
