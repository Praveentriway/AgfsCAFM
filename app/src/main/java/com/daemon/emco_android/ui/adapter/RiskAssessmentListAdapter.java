package com.daemon.emco_android.ui.adapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.model.response.Object;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;

import java.util.ArrayList;
import java.util.List;

public class RiskAssessmentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static String TAG = RiskAssessmentListAdapter.class.getSimpleName();
    private List<Object> data = new ArrayList<>();
    private AppCompatActivity mActivity;
    private Font font;
    private List<String> dailogItems = new ArrayList<>();

    public RiskAssessmentListAdapter(AppCompatActivity mActivity, List<Object> data) {
        Log.d(TAG, "PPMListAdapter");
        this.mActivity = mActivity;
        this.data = data;
        font = App.getInstance().getFontInstance();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        RecyclerView.ViewHolder mHolder = null;
        View layoutView =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_item_risk_assesment, parent, false);
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
        Log.d(TAG, "onBindViewHolder");
        try {
            if (mHolder instanceof ComplaintListViewHolder) {
                final ComplaintListViewHolder holder = (ComplaintListViewHolder) mHolder;
                final Object current = data.get(position);
                if (current.getRaetCode() != null)
                    holder.tv_refno.setText(Html.fromHtml(current.getRaetCode() + AppUtils.mandatory));
                if (current.getRaetName() != null)
                    holder.tv_desc.setText(current.getRaetName());
                System.out.println(data.get(position).getRaetComments() + "ppppppp" + data.get(0).getRaetComments());
                if (data.get(position).getRaetComments() != null && !data.get(position).getRaetComments().isEmpty()) {
                    holder.tv_confirm.setText(current.getRaetComments());
                }
                holder.tv_confirm.setTag(position);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getRemarks(final int position) {
        try {
            if (dailogItems.size() != 0) {
                dailogItems.clear();
            }
            MaterialDialog.Builder dialog = new MaterialDialog.Builder(mActivity);
            for (int i = 0; i < data.get(0).getRaetCommentsList().size(); i++) {
                dailogItems.add(data.get(0).getRaetCommentsList().get(i).getRaComments());
            }
            dialog.title(R.string.confirmation);
            dialog.itemsCallbackSingleChoice(
                    -1,
                    new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(
                                MaterialDialog dialog, View view, int which, CharSequence text) {
                            if (which >= 0) {
                                data.get(position).setRaetComments(data.get(0).getRaetCommentsList().get(which).getRaComments());
                                notifyDataSetChanged();
                            }
                            dialog.dismiss();
                            return true;
                        }
                    });
            dialog.positiveText(R.string.lbl_done);
            dialog.negativeText(R.string.lbl_close);
            dialog.items(dailogItems);
            dialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
        TextView tv_refno, tv_desc, tv_confirm;

        public ComplaintListViewHolder(final View itemView) {
            super(itemView);
            try {
                tv_refno = (TextView) itemView.findViewById(R.id.tv_refno);
                tv_desc = (TextView) itemView.findViewById(R.id.tv_desc);
                tv_confirm = (TextView) itemView.findViewById(R.id.tv_confirm);
                tv_refno.setTypeface(font.getHelveticaRegular());
                tv_desc.setTypeface(font.getHelveticaRegular());
                tv_confirm.setTypeface(font.getHelveticaRegular());

                tv_confirm.setOnClickListener(new View.OnClickListener() {
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
}
