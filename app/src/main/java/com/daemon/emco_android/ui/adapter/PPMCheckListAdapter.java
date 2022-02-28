package com.daemon.emco_android.ui.adapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;
import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.repository.remote.GetPpmMonthlyService;
import com.daemon.emco_android.listeners.PPMService_Listner;
import com.daemon.emco_android.model.request.SaveRatedServiceRequest;
import com.daemon.emco_android.model.response.GetPpmParamValue;
import com.daemon.emco_android.model.response.ObjectMonthly;
import com.daemon.emco_android.model.response.ObjectSavedCheckListResponse;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;

import java.util.ArrayList;
import java.util.List;

import static com.daemon.emco_android.utils.AppUtils.showProgressDialog;

public class PPMCheckListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements PPMService_Listner {
    private static final int TYPE_CONTENT = 0;
    private static final int TYPE_HEADER = 1;
    public static String TAG = PPMCheckListAdapter.class.getSimpleName();
    private List<Object> data = new ArrayList<>();
    private AppCompatActivity mActivity;
    private Font font;
    private GetPpmMonthlyService getPostRateService_service;
    private List<String> dailogItems = new ArrayList<>();
    private int positionValue;


    public PPMCheckListAdapter(AppCompatActivity mActivity, ArrayList<Object> data) {
        Log.d(TAG, "PPMListAdapter");
        this.mActivity = mActivity;
        this.data = data;
        font = App.getInstance().getFontInstance();
        getPostRateService_service = new GetPpmMonthlyService(mActivity, this);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        RecyclerView.ViewHolder mHolder = null;
        if (viewType == TYPE_CONTENT) {
            View layoutView =
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.view_item_ppmcheckmonth_header_item, parent, false);
            mHolder = new ComplaintListViewHeaderItem(layoutView);
        } else if (viewType == TYPE_HEADER) {
            View layoutView =
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.view_item_ppmcheckmonth_header, parent, false);
            mHolder = new ComplaintListViewHeader(layoutView);
        }
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
    public void onBindViewHolder(RecyclerView.ViewHolder mHolder, int position) {
        Log.d(TAG, "onBindViewHolder");
        try {
            if (mHolder instanceof ComplaintListViewHeader) {
                final ComplaintListViewHeader holder = (ComplaintListViewHeader) mHolder;




                if ((String) data.get(position) != null && !((String) data.get(position)).isEmpty()) {
                    holder.tv_description.setText((String) data.get(position));
                }
            } else if (mHolder instanceof ComplaintListViewHeaderItem) {
                final ComplaintListViewHeaderItem complaintListViewHeaderItem = (ComplaintListViewHeaderItem) mHolder;
                ObjectMonthly objectMonthly = (ObjectMonthly) data.get(position);



                if ((position+1)%2 == 0){
                    ((ComplaintListViewHeaderItem) mHolder).relative_main.setBackgroundColor(mActivity.getResources().getColor(R.color.list_bg));
                }

                else{
                    ((ComplaintListViewHeaderItem) mHolder).relative_main.setBackgroundColor(mActivity.getResources().getColor(R.color.colorWhite ));
                }


                complaintListViewHeaderItem.edt_comments.setText(objectMonthly.getRemarks());
                if (objectMonthly.getParamValue() != null && !objectMonthly.getParamValue().isEmpty()) {
                    complaintListViewHeaderItem.edt_status.setText(objectMonthly.getParamValue());
                } else complaintListViewHeaderItem.edt_status.setText("select");
                complaintListViewHeaderItem.tv_description.setText(objectMonthly.getPpmServiceDetails());
                complaintListViewHeaderItem.edt_status.setTag(position);
                complaintListViewHeaderItem.edt_comments.setTag(position);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getRemarksComments(final int position) {
        new MaterialDialog.Builder(mActivity)
                .inputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .positiveText(R.string.lbl_choose)
                .negativeText(R.string.lbl_cancel)
                .stackingBehavior(StackingBehavior.ADAPTIVE)
                .input(
                        mActivity.getString(R.string.lbl_comments),
                        null,
                        new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, final CharSequence charSequence) {
                                try {
                                    if (!TextUtils.isEmpty(charSequence.toString())) {
                                        ObjectMonthly d = (ObjectMonthly) data.get(position);
                                        d.setRemarks(charSequence.toString());
                                        notifyDataSetChanged();
                                    }
                                    dialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                .show();
    }

    public void getParamValue(final GetPpmParamValue getPpmParamValue) {
        try {
            if (dailogItems.size() != 0) {
                dailogItems.clear();
            }
            MaterialDialog.Builder dialog = new MaterialDialog.Builder(mActivity);
            for (int i = 0; i < getPpmParamValue.getObject().size(); i++) {
                dailogItems.add(getPpmParamValue.getObject().get(i).getParameterValue());
            }
            dialog.title(R.string.confirmation);
            dialog.itemsCallbackSingleChoice(
                    -1,
                    new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(
                                MaterialDialog dialog, View view, int which, CharSequence text) {
                            if (which >= 0) {
                                ObjectMonthly d = (ObjectMonthly) data.get(positionValue);
                                d.setParamValue(getPpmParamValue.getObject().get(which).getParameterValue());
                                d.setDetailsCode(getPpmParamValue.getObject().get(which).getInsDetlCode());
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
        return (data.get(position) instanceof String) ? TYPE_HEADER : TYPE_CONTENT;
    }

    @Override
    public void onReceivedPPMParameterSuccess(GetPpmParamValue customerRemarks) {
        AppUtils.hideProgressDialog();
        getParamValue(customerRemarks);

    }

    @Override
    public void onReceivedSuccess(List<ObjectMonthly> customerRemarks) {
    }

    @Override
    public void onGetSavedDataSuccess(List<ObjectSavedCheckListResponse> customerRemarks) {
    }

    @Override
    public void onReceivedSavedSuccess(String customerRemarks) {
    }

    @Override
    public void onReceiveFailure(String strErr) {
        AppUtils.hideProgressDialog();
    }

    public class ComplaintListViewHeader extends RecyclerView.ViewHolder {
        View itemView;
        TextView tv_description;

        public ComplaintListViewHeader(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tv_description = (TextView) itemView.findViewById(R.id.txt_title);
        }
    }

    public class ComplaintListViewHeaderItem extends RecyclerView.ViewHolder {
        View itemView;
        TextView tv_description, edt_status;
        EditText edt_comments;
        RelativeLayout relative_main;

        public ComplaintListViewHeaderItem(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tv_description = (TextView) itemView.findViewById(R.id.tv_description);
            edt_comments = (EditText) itemView.findViewById(R.id.edt_remark);
            edt_status = (TextView) itemView.findViewById(R.id.edt_status);
            relative_main = (RelativeLayout) itemView.findViewById(R.id.linear_main);

            edt_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    positionValue = (Integer) view.getTag();
                    showProgressDialog(mActivity, "Loading...", false);
                    SaveRatedServiceRequest ratedServiceRequest = new SaveRatedServiceRequest();
                    ObjectMonthly d = (ObjectMonthly) data.get(positionValue);
                    ratedServiceRequest.setDetailsCode(d.getDetailsCode());
                    getPostRateService_service.getPpmParameterValue(ratedServiceRequest);
                }
            });

            edt_comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (Integer) view.getTag();
                    getRemarksComments(position);
                }
            });

        }
    }
}
