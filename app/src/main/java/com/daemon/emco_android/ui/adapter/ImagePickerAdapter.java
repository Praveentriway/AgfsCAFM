package com.daemon.emco_android.ui.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.ThumbnailUtils;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.listeners.ImagePickListener;
import com.daemon.emco_android.model.common.CustomGallery;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;


public class ImagePickerAdapter extends RecyclerView.Adapter<ImagePickerAdapter.MyViewHolder> {
    public static String MODULE = "ImagePickerAdapter";
    public static String TAG = "";

    ArrayList<CustomGallery> data = new ArrayList<CustomGallery>();
    Fragment mFragment;
    int mPadding;
    int width;
    int height;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    private LayoutInflater inflater;
    private AppCompatActivity mActivity;
    private Font font;
    private ImagePickListener mCallBack;

    public ImagePickerAdapter(Fragment mFragment, ArrayList<CustomGallery> data, ImagePickListener mCallBack) {
        TAG = "ImagePickerAdapter";
        Log.d(MODULE, TAG);
        this.mFragment = mFragment;
        this.mCallBack = mCallBack;
        mActivity = (AppCompatActivity) mFragment.getActivity();
        inflater = LayoutInflater.from(mActivity);
        this.data = data;
        font = App.getInstance().getFontInstance();
        imageLoader = ImageLoader.getInstance();
        options = AppUtils.getOptions();
        Display display = mActivity.getWindowManager().getDefaultDisplay();
        mPadding = (int) mActivity.getResources().getDimension(R.dimen.space_layout_padding_small);
        mPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, mPadding, new DisplayMetrics());
        mPadding = mPadding / 4;
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TAG = "onCreateViewHolder";
        Log.d(MODULE, TAG);

        View view = inflater.inflate(R.layout.view_item_image_picker, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        TAG = "onBindViewHolder";
        Log.d(MODULE, TAG);
        try {
            final CustomGallery current = data.get(position);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width / 2, height / 4);
            params.setMargins(mPadding, mPadding, mPadding, mPadding);
            holder.ll_root.setLayoutParams(params);
            final String Str_Path = "file://" + current.getSdcardPath();
            Log.d(MODULE, TAG + " Str_Path : " + Str_Path);
            if (current.isSeleted())
                holder.itemView.setBackgroundColor(mActivity.getResources().getColor(R.color.colorBlack));
            else
                holder.itemView.setBackgroundColor(mActivity.getResources().getColor(R.color.colorWhite));
            imageLoader.displayImage(Str_Path, holder.iv_list, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    holder.iv_list.setImageResource(R.drawable.ic_camera_24dp);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    holder.iv_list.setImageBitmap(ThumbnailUtils.extractThumbnail(loadedImage, 200, 200));
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                }
            });
            holder.iv_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                    builder.setMessage("Are you sure you want to add this Image?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    mCallBack.onSingleImagePicked(current.getSdcardPath());
                                    if (mFragment.getFragmentManager() != null)
                                        mFragment.getFragmentManager().popBackStack();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void selectAll(boolean selection) {
        for (int i = 0; i < data.size(); i++) {
            data.get(i).setSeleted(selection);
        }
        notifyDataSetChanged();
    }

    public boolean isAllSelected() {
        boolean isAllSelected = true;

        for (int i = 0; i < data.size(); i++) {
            if (!data.get(i).isSeleted()) {
                isAllSelected = false;
                break;
            }
        }

        return isAllSelected;
    }

    public boolean isAnySelected() {
        boolean isAnySelected = false;

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isSeleted()) {
                isAnySelected = true;
                break;
            }
        }

        return isAnySelected;
    }

    public ArrayList<CustomGallery> getSelected() {
        ArrayList<CustomGallery> dataT = new ArrayList<CustomGallery>();

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isSeleted()) {
                dataT.add(data.get(i));
            }
        }

        return dataT;
    }

    public void addAll(ArrayList<CustomGallery> files) {

        try {
            this.data.clear();
            this.data.addAll(files);

        } catch (Exception e) {
            e.printStackTrace();
        }

        notifyDataSetChanged();
    }

    public void changeSelection(View v, int position) {

        data.get(position).setSeleted(!data.get(position).isSeleted());
        ((MyViewHolder) v.getTag()).imgQueueMultiSelected.setSelected(data
                .get(position).isSeleted());
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_list;
        ImageView imgQueueMultiSelected;
        LinearLayout ll_root;
        View itemView;

        public MyViewHolder(View itemView) {
            super(itemView);
            try {
                this.itemView = itemView;
                ll_root = (LinearLayout) itemView.findViewById(R.id.ll_root);
                iv_list = (ImageView) itemView.findViewById(R.id.iv_list);
                imgQueueMultiSelected = (ImageView) itemView.findViewById(R.id.imgQueueMultiSelected);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


}
