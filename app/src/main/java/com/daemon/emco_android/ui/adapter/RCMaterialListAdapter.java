package com.daemon.emco_android.ui.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;
import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.repository.db.entity.MaterialMasterEntity;
import com.daemon.emco_android.repository.db.entity.SaveMaterialEntity;
import com.daemon.emco_android.listeners.Material_Listener;
import com.daemon.emco_android.utils.Font;

import java.util.ArrayList;
import java.util.List;

public class RCMaterialListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  public static String MODULE = "RCLAdapter";
  public static String TAG = "";

  private final int VIEW_ITEM = 1;
  private final int VIEW_PROG = 0;
  private List<MaterialMasterEntity> data = new ArrayList<>();
  private List<SaveMaterialEntity> getdata = new ArrayList<>();
  private AppCompatActivity mActivity;
  private Material_Listener mCallBack;
  private Font font;
  private int mSelectedPosition;
  private boolean isFooterEnabled = false;
  private boolean isGetMaterial = false;

  public RCMaterialListAdapter(
      AppCompatActivity mActivity,
      List<MaterialMasterEntity> data,
      List<SaveMaterialEntity> getdata) {
    TAG = "RC_ListAdapter";
    Log.d(MODULE, TAG);

    this.mActivity = mActivity;
    this.data = data;
    this.getdata = getdata;
    font = App.getInstance().getFontInstance();
  }

  public void setGetMaterial(boolean getMaterial) {
    isGetMaterial = getMaterial;
  }

  public void setListener(Material_Listener mCallBack) {
    this.mCallBack = mCallBack;
  }

  public List<SaveMaterialEntity> onRCMaterialUpdate() {
    return getdata;
  }

  public List<MaterialMasterEntity> onRCMaterialInsert() {
    return data;
  }

  public void delete(int position) {
    if (isGetMaterial) {
      getdata.remove(position);
    } else {
      data.remove(position);
    }
    notifyItemRemoved(position);
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    TAG = "onCreateViewHolder";
    Log.d(MODULE, TAG);
    RecyclerView.ViewHolder mHolder = null;
    if (viewType == VIEW_ITEM) {
      View layoutView =
          LayoutInflater.from(parent.getContext())
              .inflate(R.layout.view_item_rc_materialrequired, parent, false);
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
    TAG = "onBindViewHolder";
    Log.d(MODULE, TAG);
    try {
      if (mHolder instanceof ComplaintListViewHolder) {
        final ComplaintListViewHolder holder = (ComplaintListViewHolder) mHolder;
        if (isGetMaterial) {
          SaveMaterialEntity current = getdata.get(position);
          holder.et_quantity.setTag(position);
          holder.et_remarks.setTag(position);
          holder.tv_itemcode.setText(current.getItemCode());
          holder.tv_description.setText(current.getItemDescription());
          holder.et_quantity.setText(current.getItemQuantity());
          if (current.getRemarks() != null && !current.getRemarks().equals("null")) {
            holder.et_remarks.setText(current.getRemarks());
          } else {
            holder.et_remarks.setText("");
          }

        } else {
          MaterialMasterEntity current = data.get(position);
          holder.et_quantity.setTag(position);
          holder.et_remarks.setTag(position);
          holder.tv_itemcode.setText(current.getMaterialCode());
          holder.tv_description.setText(current.getMaterialName());
          holder.et_quantity.setText(current.getItemQuantity());
          if (current.getRemarks() != null && !current.getRemarks().equals("null")) {
            holder.et_remarks.setText(current.getRemarks());
          } else {
            holder.et_remarks.setText("");
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

  public void enableFooter(boolean footerEnabled) {
    isFooterEnabled = footerEnabled;
  }

  @Override
  public int getItemCount() {
    return (isFooterEnabled)
        ? (isGetMaterial ? getdata.size() + 1 : data.size() + 1)
        : (isGetMaterial ? getdata.size() : data.size());
  }

  @Override
  public int getItemViewType(int position) {
    return isGetMaterial
        ? (isFooterEnabled && position >= getdata.size()) ? VIEW_PROG : VIEW_ITEM
        : (isFooterEnabled && position >= data.size()) ? VIEW_PROG : VIEW_ITEM;
  }

  public void selectPosition(int position) {
    int lastPosition = mSelectedPosition;
    mSelectedPosition = position;
    notifyItemChanged(lastPosition);
    notifyItemChanged(position);
  }

  public class ComplaintListViewHolder extends RecyclerView.ViewHolder {
    TextView tv_itemcode, tv_description, et_quantity, et_remarks;
    View itemView;

    public ComplaintListViewHolder(final View itemView) {
      super(itemView);
      try {
        this.itemView = itemView;
        tv_itemcode = (TextView) itemView.findViewById(R.id.tv_itemcode);
        tv_description = (TextView) itemView.findViewById(R.id.tv_description);
        et_quantity = (TextView) itemView.findViewById(R.id.tv_quantity);
        et_remarks = (TextView) itemView.findViewById(R.id.tv_remarks);

        tv_itemcode.setTypeface(font.getHelveticaRegular());
        tv_description.setTypeface(font.getHelveticaRegular());
        et_quantity.setTypeface(font.getHelveticaRegular());
        et_remarks.setTypeface(font.getHelveticaRegular());

        et_quantity.setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                new MaterialDialog.Builder(mActivity)
                    .content(R.string.lbl_quantity)
                    .inputRange(0, 2)
                    .inputType(InputType.TYPE_CLASS_NUMBER)
                    .positiveText(R.string.lbl_choose)
                    .negativeText(R.string.lbl_cancel)
                    .stackingBehavior(StackingBehavior.ADAPTIVE)
                    .input(
                        mActivity.getString(R.string.lbl_quantity),
                        null,
                        new MaterialDialog.InputCallback() {
                          @Override
                          public void onInput(MaterialDialog dialog, CharSequence charSequence) {
                            try {
                              if (charSequence.toString().startsWith("0")||charSequence.toString().startsWith("00"))
                              {
                                MaterialDialog.Builder builder =
                                        new MaterialDialog.Builder(mActivity)
                                                .content(R.string.valid_quantity)
                                                .positiveText(R.string.lbl_okay)
                                                .stackingBehavior(StackingBehavior.ADAPTIVE)
                                                .onPositive(
                                                        new MaterialDialog.SingleButtonCallback() {
                                                          @Override
                                                          public void onClick(
                                                                  @NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                            dialog.dismiss();
                                                          }
                                                        });

                                MaterialDialog dialogless = builder.build();
                                dialogless.show();

                              }else{
                                Log.d(
                                        MODULE,
                                        "onInput p:" + et_quantity.getTag() + "\t" + charSequence);
                                et_quantity.setText(charSequence);
                                if (isGetMaterial) {
                                  getdata
                                          .get((int) et_quantity.getTag())
                                          .setItemQuantity(
                                                  TextUtils.isEmpty(charSequence.toString())
                                                          ? "0"
                                                          : charSequence.toString());
                                } else {
                                  data.get((int) et_quantity.getTag())
                                          .setItemQuantity(
                                                  TextUtils.isEmpty(charSequence.toString())
                                                          ? "0"
                                                          : charSequence.toString());
                                }
                              }

                            } catch (Exception e) {
                              e.printStackTrace();
                            }
                          }
                        })
                    .show();
              }
            });

        et_remarks.setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                new MaterialDialog.Builder(mActivity)
                    .content(R.string.lbl_remarks)
                    .inputRange(0, 250)
                    .inputType(InputType.TYPE_CLASS_TEXT)
                    .positiveText(R.string.lbl_choose)
                    .negativeText(R.string.lbl_cancel)
                    .stackingBehavior(StackingBehavior.ADAPTIVE)
                    .input(
                        mActivity.getString(R.string.lbl_remarks),
                        null,
                        new MaterialDialog.InputCallback() {
                          @Override
                          public void onInput(MaterialDialog dialog, CharSequence charSequence) {
                            try {
                              Log.d(
                                  MODULE, "onInput p:" + et_remarks.getTag() + "\t" + charSequence);
                              et_remarks.setText(charSequence);
                              if (isGetMaterial) {
                                getdata
                                    .get((int) et_remarks.getTag())
                                    .setRemarks(charSequence.toString());
                              } else {
                                data.get((int) et_remarks.getTag())
                                    .setRemarks(charSequence.toString());
                              }
                            } catch (Exception e) {
                              e.printStackTrace();
                            }
                          }
                        })
                    .show();
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
