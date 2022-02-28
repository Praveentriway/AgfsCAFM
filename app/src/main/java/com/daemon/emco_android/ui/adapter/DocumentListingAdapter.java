package com.daemon.emco_android.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.daemon.emco_android.R;
import com.daemon.emco_android.model.request.DocumentTransaction;
import java.util.List;

public class DocumentListingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private List<DocumentTransaction> trans;
    private int imgCount;
    private ItemListner listner;
    private Context _con;

    public DocumentListingAdapter(List<DocumentTransaction> trans, int imgCount, ItemListner lstn, Context con) {
        this.trans = trans;

        if(trans.size()==0){
            trans.add(null);
        }

        this.imgCount=imgCount;
        this.listner=lstn;
        this._con=con;
    }

    public interface ItemListner{
        void onViewClick(int pos,DocumentTransaction trans,List<DocumentTransaction> transList);

        void onAddNewDocument();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder mHolder = null;
        if (trans.get(0)!=null) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_document_listing, parent, false);
            mHolder = new DocumentList(layoutView);

        } else {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_no_document, parent, false);
            mHolder = new NoDocumentLayout(layoutView);
        }

        return mHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder mHolder, final int position) {
        // TAG = (isFetchData ? fetchdata.size() : data.size()) + " onBindViewHolder " + position;

            if (mHolder instanceof DocumentList) {
                DocumentList holder = (DocumentList) mHolder;
                holder.tv_image_name.setText(trans.get(position).getImage_name());
              if(trans.get(position).getRemarks()!=null){
                  holder.tv_remarks.setText(trans.get(position).getRemarks());
              }

              else{
                  holder.tv_remarks.setText("--No Remarks recorded--");
              }

              try{
                  if(trans.get(position).getFileType().equalsIgnoreCase(".jpg") || trans.get(position).getFileType().equalsIgnoreCase(".jpeg") || trans.get(position).getFileType().equalsIgnoreCase(".png")){
                      byte[] decodedString = Base64.decode(trans.get(position).getBase64Image(), Base64.DEFAULT);
                      Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                      holder.imageV.setImageBitmap(decodedByte);
                  }
                  else if(trans.get(position).getFileType().equalsIgnoreCase(".doc") || trans.get(position).getFileType().equalsIgnoreCase(".docx")){
                      holder.imageV.setImageResource(R.drawable.word_icon);
                  }

                  else if(trans.get(position).getFileType().equalsIgnoreCase(".pdf")){
                      holder.imageV.setImageResource(R.drawable.pdf_icon);
                  }

                  else if(trans.get(position).getFileType().equalsIgnoreCase(".xls") || trans.get(position).getFileType().equalsIgnoreCase(".xlsx")){
                      holder.imageV.setImageResource(R.drawable.excel_icon);
                  }
              }

              catch (Exception e){
                  e.printStackTrace();
              }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listner.onViewClick(position,trans.get(position),trans);
                    }
                });
            }
            else{

                NoDocumentLayout holder = (NoDocumentLayout) mHolder;

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listner.onAddNewDocument();
                    }
                });

            }

    }


    @Override
    public int getItemCount() {
        return trans.size() ;
    }

    public class DocumentList extends RecyclerView.ViewHolder {
        CardView linear_view;
        AppCompatTextView tv_image_name,tv_remarks;
        ImageView imageV;
        View itemView;

        public DocumentList(View itemView) {
            super(itemView);
            try {
                this.itemView = itemView;
                linear_view = (CardView) itemView.findViewById(R.id.linear_view);
                tv_image_name= (AppCompatTextView) itemView.findViewById(R.id.tv_image_name);
                tv_remarks = (AppCompatTextView) itemView.findViewById(R.id.tv_remarks);
                imageV = (ImageView) itemView.findViewById(R.id.imageV);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public class NoDocumentLayout extends RecyclerView.ViewHolder {


        View itemView;

        public NoDocumentLayout(View itemView) {
            super(itemView);

            this.itemView=itemView;


        }
    }
}


