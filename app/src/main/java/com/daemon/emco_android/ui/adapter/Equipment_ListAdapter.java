package com.daemon.emco_android.ui.adapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;
import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.listeners.Material_Listener;
import com.daemon.emco_android.model.response.Object;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;

import java.util.ArrayList;
import java.util.List;

public class Equipment_ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static String MODULE = "RCLAdapter";
    public static String TAG = "";
    private AppCompatActivity mActivity;
    private Material_Listener mCallBack;
    private Font font;
    private List<Object> data = new ArrayList<>();
    //private List<Object> saveData = new ArrayList<>();


    public Equipment_ListAdapter(AppCompatActivity mActivity, List<Object> data) {
        Log.d(TAG, "PPMListAdapter");
        this.mActivity = mActivity;
        this.data = data;
        font = App.getInstance().getFontInstance();
    }


    public void setListener(Material_Listener mCallBack) {
        this.mCallBack = mCallBack;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TAG = "onCreateViewHolder";
        Log.d(MODULE, TAG);
        RecyclerView.ViewHolder mHolder = null;
        View layoutView =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_item_rc_equipment, parent, false);
        mHolder = new ComplaintListViewHolder(layoutView);
        return mHolder;
    }

    public List<Object> onInsertData() {
        if (data != null && !data.isEmpty()) {
            return data;
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder mHolder, final int position) {
        TAG = "onBindViewHolder";
        Log.d(MODULE, TAG);
        try {
            if (mHolder instanceof ComplaintListViewHolder) {
                final ComplaintListViewHolder holder = (ComplaintListViewHolder) mHolder;
                final Object current = data.get(position);
                holder.tv_equipment_desc.setText(Html.fromHtml(current.getRaetName() + AppUtils.mandatory));
                if (data.get(position).getRaetTagNo() != null) {
                    holder.tv_equip_tag.setText(current.getRaetTagNo());
                }
                holder.tv_equip_tag.setTag(position);
            } else if (mHolder instanceof LoadingMessageHolder) {
                LoadingMessageHolder holder = (LoadingMessageHolder) mHolder;
                holder.layout_loading_message.setVisibility(View.VISIBLE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getRemarks(final int position) {
        new MaterialDialog.Builder(mActivity)
                .inputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .positiveText(R.string.lbl_choose)
                .negativeText(R.string.lbl_cancel)
                .stackingBehavior(StackingBehavior.ADAPTIVE)
                .input(
                        mActivity.getString(R.string.eqp_tag_no),
                        null,
                        new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, final CharSequence charSequence) {
                                try {
                                    if (!TextUtils.isEmpty(charSequence.toString())) {
                                        data.get(position).setRaetTagNo(charSequence.toString());
                                    }
                                    notifyDataSetChanged();
                                    dialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                .show();
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ComplaintListViewHolder extends RecyclerView.ViewHolder {
        TextView tv_equipment_desc, tv_equip_tag;
        View itemView;

        public ComplaintListViewHolder(final View itemView) {
            super(itemView);
            try {
                this.itemView = itemView;
                tv_equipment_desc = (TextView) itemView.findViewById(R.id.tv_equipment_desc);
                tv_equip_tag = (TextView) itemView.findViewById(R.id.tv_equip_tag);
                tv_equipment_desc.setTypeface(font.getHelveticaRegular());
                tv_equip_tag.setTypeface(font.getHelveticaRegular());

                tv_equip_tag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = (Integer) view.getTag();
                        getRemarks(position);
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
