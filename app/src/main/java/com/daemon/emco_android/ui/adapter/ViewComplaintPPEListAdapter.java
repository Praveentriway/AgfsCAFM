package com.daemon.emco_android.ui.adapter;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.listeners.PpeListener;
import com.daemon.emco_android.repository.db.entity.PPEFetchSaveEntity;
import com.daemon.emco_android.repository.db.entity.PPENameEntity;
import com.daemon.emco_android.utils.Font;

import java.util.ArrayList;
import java.util.List;

public class ViewComplaintPPEListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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

    public ViewComplaintPPEListAdapter(AppCompatActivity mActivity, List<PPENameEntity> data, List<PPEFetchSaveEntity> fetchdata) {
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
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_receive_complaint_ppe, parent, false);
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
                    if (current.getPpeName() != null)
                        holder.tv_ppe_name.setText(current.getPpeName());
                    if (current.getPpeUsed() != null) {
                        switch (current.getPpeUsed()) {
                            case "Y":
                                holder.rd_yes.setChecked(true);
                                break;
                            case "N":
                                holder.rd_no.setChecked(true);
                                break;
                            case "X":
                                holder.rd_nr.setChecked(true);
                                break;
                            default:
                                holder.rd_yes.setChecked(true);
                                break;
                        }
                    }

                } else {
                    PPENameEntity current = data.get(position);
                    if (current.getName() != null)
                        holder.tv_ppe_name.setText(current.getName());
                }

                setcheckedData(position, holder.radiogroup_ppe.getCheckedRadioButtonId());
                holder.radiogroup_ppe.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                        setcheckedData(position, radioGroup.getCheckedRadioButtonId());
                    }
                });


//                if (position ==  data.size() - 1) {
//
//                    holder.btnSavePpe.setVisibility(View.VISIBLE);
//                    holder.btnSavePpe.setTypeface(font.getHelveticaRegular());
//                    holder.btnSavePpe.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            mCallBack.onPPESaveClicked(data, fetchdata, isFetchData);
//
//                        }
//                    });
//                }
//
//                if (position ==  fetchdata.size() - 1) {
//
//                    holder.btnSavePpe.setVisibility(View.VISIBLE);
//                    holder.btnSavePpe.setTypeface(font.getHelveticaRegular());
//                    holder.btnSavePpe.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            mCallBack.onPPESaveClicked(data, fetchdata, isFetchData);
//
//                        }
//                    });
//                }


            } else if (mHolder instanceof LoadingMessageHolder) {
                LoadingMessageHolder holder = (LoadingMessageHolder) mHolder;
                holder.layout_loading_message.setVisibility(View.VISIBLE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean isFetchdata(){

        return  isFetchData;

    }

    public List<PPEFetchSaveEntity> getPPEFetchSaveEntity(){
        return fetchdata;
    }

    public List<PPENameEntity> getPPENameEntity(){
        return data;
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
        TextView tv_ppe_name;
        RadioButton rd_yes, rd_no, rd_nr;
        RadioGroup radiogroup_ppe;
        View itemView;
        Button btnSavePpe;

        public PpeViewHolder(View itemView) {
            super(itemView);
            try {
                this.itemView = itemView;
                tv_ppe_name = (TextView) itemView.findViewById(R.id.tv_ppe_name);
                rd_yes = (RadioButton) itemView.findViewById(R.id.rd_yes);
                rd_no = (RadioButton) itemView.findViewById(R.id.rd_no);
                rd_nr = (RadioButton) itemView.findViewById(R.id.rd_nr);
                btnSavePpe = (Button) itemView.findViewById(R.id.btnSavePpe);
                radiogroup_ppe = (RadioGroup) itemView.findViewById(R.id.radiogroup_ppe);

                tv_ppe_name.setTypeface(font.getHelveticaRegular());
                btnSavePpe.setTypeface(font.getHelveticaRegular());
                btnSavePpe.setVisibility(View.GONE);

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
