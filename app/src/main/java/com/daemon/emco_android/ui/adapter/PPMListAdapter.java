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
import com.daemon.emco_android.repository.remote.PPMDetailsService;
import com.daemon.emco_android.model.common.PPMDetails;
import com.daemon.emco_android.utils.Font;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PPMListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  public static String TAG = PPMListAdapter.class.getSimpleName();

  private final int VIEW_ITEM = 1;
  private final int VIEW_PROG = 0;
  // 2017-12-02 00:00:00.0
  private DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
  private DateFormat formatter = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
  private List<PPMDetails> data = new ArrayList<>();
  private AppCompatActivity mActivity;
  private PPMDetailsService.Listener mCallBack;
  private Font font;
  private int mSelectedPosition;
  private Date dateStart = new Date();
  private boolean isFooterEnabled = false;

  public PPMListAdapter(AppCompatActivity mActivity, List<PPMDetails> data) {
    Log.d(TAG, "PPMListAdapter");

    this.mActivity = mActivity;
    this.data = data;
    font = App.getInstance().getFontInstance();
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    Log.d(TAG, "onCreateViewHolder");
    RecyclerView.ViewHolder mHolder = null;
    if (viewType == VIEW_ITEM) {
      View layoutView =
          LayoutInflater.from(parent.getContext())
              .inflate(R.layout.view_item_ppmdetails, parent, false);
      mHolder = new ComplaintListViewHolder(layoutView);
    } else if (viewType == VIEW_PROG) {
      View layoutView =
          LayoutInflater.from(parent.getContext())
              .inflate(R.layout.view_loading_message_list, parent, false);
      mHolder = new LoadingMessageHolder(layoutView);
    }
    return mHolder;
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder mHolder, final int position) {
    Log.d(TAG, "onBindViewHolder"+position);




    try {
      if (mHolder instanceof ComplaintListViewHolder) {
        final ComplaintListViewHolder holder = (ComplaintListViewHolder) mHolder;
        final PPMDetails current = data.get(position);

        holder.itemView.setTag(position);
        holder.tv_sno.setText((position + 1) + "");
        holder.tv_location_name.setVisibility(View.GONE);
        if (current.getZoneBuilding() != null)
          holder.tv_complaint_no.setText(current.getZoneBuilding());
        if (current.getEquipmentName() != null) holder.tv_time.setText(current.getEquipmentName());
        if (current.getStartDate() != null) {
          Date date = dateFormatter.parse(current.getStartDate());
          holder.tv_site_location.setText(formatter.format(date));
        }
        if (current.getEndDate() != null) {
          Date date = dateFormatter.parse(current.getEndDate());
          holder.tv_complaint_details.setText(formatter.format(date));
        }

        if (current.getTotalEquipments() != null)
          holder.tv_status.setText(current.getTotalEquipments());
        if (current.getCompleted() != null) holder.tv_work_type.setText(current.getCompleted());
        if (current.getPending() != null) holder.tv_priority.setText(current.getPending());

        holder.cb_ccc.setChecked(current.isChecked());

        if ((position+1)%2 == 0){
          ((ComplaintListViewHolder) mHolder).linear_main.setBackgroundColor(mActivity.getResources().getColor(R.color.colorWhite));
        }

        else{
          ((ComplaintListViewHolder) mHolder).linear_main.setBackgroundColor(mActivity.getResources().getColor(R.color.list_bg  ));
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

  public void updateList(List<PPMDetails> itemEntities) {
    data = itemEntities;
    notifyDataSetChanged();
  }

  public void checkAll(boolean b) {
    if (data != null) {
      for (int i = 0; i < data.size(); i++) {
        data.get(i).setChecked(b);
      }
      notifyDataSetChanged();
    }
  }

  public void deleteItem(int position) {
    data.remove(position);
    notifyItemRemoved(position);
  }

  public void setListener(PPMDetailsService.Listener mCallBack) {
    this.mCallBack = mCallBack;
  }

  public class ComplaintListViewHolder extends RecyclerView.ViewHolder {
    TextView tv_sno,
        tv_complaint_no,
        tv_site_location,
        tv_complaint_details,
        tv_time,
        tv_status,
        tv_work_type,
        tv_priority,tv_location_name;
    CheckBox cb_ccc;
    LinearLayout linear_main;

    public ComplaintListViewHolder(final View itemView) {
      super(itemView);
      try {
        cb_ccc = (CheckBox) itemView.findViewById(R.id.cb_ccc);
        linear_main= (LinearLayout) itemView.findViewById(R.id.linear_main);
        tv_complaint_no = (TextView) itemView.findViewById(R.id.tv_building);
        tv_sno = (TextView) itemView.findViewById(R.id.tv_sno);
        tv_time = (TextView) itemView.findViewById(R.id.tv_asset_name);
        tv_work_type = (TextView) itemView.findViewById(R.id.tv_completed);
        tv_priority = (TextView) itemView.findViewById(R.id.tv_pending);
        tv_site_location = (TextView) itemView.findViewById(R.id.tv_start_date);
        tv_complaint_details = (TextView) itemView.findViewById(R.id.tv_end_date);
        tv_status = (TextView) itemView.findViewById(R.id.tv_total_ppm);
        tv_location_name=(TextView)itemView.findViewById(R.id.tv_location_name);
        tv_time.setTypeface(font.getHelveticaRegular());
        tv_sno.setTypeface(font.getHelveticaRegular());
        tv_work_type.setTypeface(font.getHelveticaRegular());
        tv_priority.setTypeface(font.getHelveticaRegular());
        tv_site_location.setTypeface(font.getHelveticaRegular());
        tv_complaint_details.setTypeface(font.getHelveticaRegular());
        tv_status.setTypeface(font.getHelveticaRegular());

        cb_ccc.setOnCheckedChangeListener(
            new CompoundButton.OnCheckedChangeListener() {
              @Override
              public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                try {
                  PPMDetails current = data.get((int) itemView.getTag());
                  data.get((int) itemView.getTag()).setChecked(b);
                  mCallBack.onppmListItemChecked(data);
                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
            });
        itemView.setOnClickListener(
            new View.OnClickListener() {
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
