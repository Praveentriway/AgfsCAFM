package com.daemon.emco_android.ui.adapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.listeners.PpeListener;
import com.daemon.emco_android.repository.db.entity.PPEFetchSaveEntity;
import com.daemon.emco_android.repository.db.entity.PPENameEntity;
import com.daemon.emco_android.utils.Font;

import java.util.ArrayList;
import java.util.List;

public class PM_EquipmentTools_ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static String MODULE = "RCLAdapter";
    public static String TAG = "";

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private List<PPENameEntity> data = new ArrayList<>();
    private List<PPEFetchSaveEntity> fetchdata = new ArrayList<>();
    private AppCompatActivity mActivity;
    private PpeListener mCallBack;
    private Font font;
    private int mSelectedPosition;
    private boolean isFooterEnabled = false;
    private boolean isFetchData = false;
    private boolean isCompleted = false;

    public PM_EquipmentTools_ListAdapter(AppCompatActivity mActivity, List<PPENameEntity> data, List<PPEFetchSaveEntity> fetchdata) {
        TAG = "RC_ListAdapter";
        Log.d(MODULE, TAG);

        this.mActivity = mActivity;
        this.data = data;
        this.fetchdata = fetchdata;
        font = App.getInstance().getFontInstance();
    }

    public void setListener(PpeListener mCallBack) {
        this.mCallBack = mCallBack;
    }

    public void setFetchData(boolean fetchData) {
        isFetchData = fetchData;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TAG = "onCreateViewHolder";
        Log.d(MODULE, TAG);
        RecyclerView.ViewHolder mHolder = null;
        if (viewType == VIEW_ITEM) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_equipmenttools, parent, false);
            mHolder = new PpeViewHolder(layoutView);
        } else if (viewType == VIEW_PROG) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_loading_message_list, parent, false);
            mHolder = new LoadingMessageHolder(layoutView);
        }
        return mHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder mHolder, final int position) {
        TAG = (isFetchData ? fetchdata.size() : data.size()) + " onBindViewHolder " + position;
        Log.d(MODULE, TAG);
        try {
            if (mHolder instanceof PpeViewHolder) {
                PpeViewHolder holder = (PpeViewHolder) mHolder;
                if (isFetchData) {
                    PPEFetchSaveEntity current = fetchdata.get(position);
                    holder.et_remarks.setTag(position);
                    holder.tv_description.setText(current.getComplainNumber());
                    if (current.getCompanyCode() != null && !current.getCompanyCode().equals("null")) {
                        holder.et_remarks.setText(current.getComplainNumber());
                    } else {
                        holder.et_remarks.setText("");
                    }

                } else {
                    PPENameEntity current = data.get(position);
                    holder.et_remarks.setTag(position);
                    holder.tv_description.setText(current.getCType());
                    if (current.getCode() != null && !current.getCode().equals("null")) {
                        holder.et_remarks.setText(current.getCode());
                    } else {
                        holder.et_remarks.setText("");
                    }
                }
                // Last view for list.
                if (!(isFetchData ? fetchdata : data).isEmpty() || (isFetchData ? fetchdata : data) == null) {
                    if (position == (isFetchData ? fetchdata.size() : data.size()) - 1) {
                        holder.btnSave.setVisibility(isCompleted ? View.GONE : View.VISIBLE);
                        holder.btnSave.setTypeface(font.getHelveticaRegular());
                        holder.btnSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mCallBack.onPPESaveClicked(data, fetchdata, isFetchData);

                            }
                        });
                    }
                }
            } else if (mHolder instanceof LoadingMessageHolder) {
                LoadingMessageHolder holder = (LoadingMessageHolder) mHolder;
                holder.layout_loading_message.setVisibility(View.VISIBLE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setcheckedData(int position, int checkedRadioButtonId) {
        switch (checkedRadioButtonId) {
            case R.id.rd_yes:
                if (isFetchData) {
                    fetchdata.get(position).setPpeUsed("Y");
                } else {
                    data.get(position).setPpeUsed("Y");
                }
                break;
            case R.id.rd_no:
                if (isFetchData) {
                    fetchdata.get(position).setPpeUsed("N");
                } else {
                    data.get(position).setPpeUsed("N");
                }
                break;
            case R.id.rd_nr:
                if (isFetchData) {
                    fetchdata.get(position).setPpeUsed("X");
                } else {
                    data.get(position).setPpeUsed("X");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return (isFooterEnabled) ? (isFetchData ? fetchdata.size() + 1 : data.size() + 1) : (isFetchData ? fetchdata.size() : data.size());
    }

    @Override
    public int getItemViewType(int position) {
        return isFetchData ? (isFooterEnabled && position >= fetchdata.size()) ? VIEW_PROG : VIEW_ITEM : (isFooterEnabled && position >= data.size()) ? VIEW_PROG : VIEW_ITEM;
    }

    public void selectPosition(int position) {
        int lastPosition = mSelectedPosition;
        mSelectedPosition = position;
        notifyItemChanged(lastPosition);
        notifyItemChanged(position);
    }

    public class PpeViewHolder extends RecyclerView.ViewHolder {
        TextView tv_description, et_remarks;
        View itemView;
        Button btnSave;

        public PpeViewHolder(View itemView) {
            super(itemView);
            try {
                this.itemView = itemView;
                tv_description = (TextView) itemView.findViewById(R.id.tv_description);
                et_remarks = (TextView) itemView.findViewById(R.id.tv_remarks);
                btnSave = (Button) itemView.findViewById(R.id.btnSave);

                tv_description.setTypeface(font.getHelveticaRegular());
                et_remarks.setTypeface(font.getHelveticaRegular());
                btnSave.setTypeface(font.getHelveticaRegular());
                btnSave.setVisibility(View.GONE);

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
