package com.daemon.emco_android.ui.adapter;


import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.repository.db.entity.ServeyQuestionnaire;
import com.daemon.emco_android.listeners.PpeListener;
import com.daemon.emco_android.utils.Font;

import java.util.ArrayList;
import java.util.List;

public class CustomerFeedbackAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static String MODULE = "SURVEYAdapter";
    public static String TAG = "";

    private List<ServeyQuestionnaire> fetchdata = new ArrayList<>();
    private AppCompatActivity mActivity;
    private PpeListener mCallBack;
    private Font font;
    private int mSelectedPosition;
    private boolean isFooterEnabled = false;
    private boolean isFetchData = false;
    private boolean isCompleted = false;

    public CustomerFeedbackAdapter(AppCompatActivity mActivity, ArrayList<ServeyQuestionnaire> fetchdata) {
        TAG = "RC_ListAdapter";
        Log.d(MODULE, TAG);

        this.mActivity = mActivity;
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

//    public void delete(int position) {
//        data.remove(position);
//        notifyItemRemoved(position);
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TAG = "onCreateViewHolder";
        Log.d(MODULE, TAG);
        RecyclerView.ViewHolder mHolder = null;

            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_survey_question, parent, false);
            mHolder = new SurveyQuestion(layoutView);

        return mHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder mHolder, final int position) {
       // TAG = (isFetchData ? fetchdata.size() : data.size()) + " onBindViewHolder " + position;
        Log.d(MODULE, TAG);
        try {
            if (mHolder instanceof SurveyQuestion) {
              final  SurveyQuestion holder = (SurveyQuestion) mHolder;
                holder.txt_sno.setText(position+1+".");

                    ServeyQuestionnaire current = fetchdata.get(position);
                    holder.txt_sno.setText(position+1+".");

                holder.tv_survey_ques.setText(current.getSurveyQues());


               if(current.getSurveyQuesValue().size()>0) {

                   holder.rd_1.setText(current.getSurveyQuesValue().get(0));
                   if(current.getSurveyQuesAns()!=null)
                   if(current.getSurveyQuesValue().get(0).equalsIgnoreCase(current.getSurveyQuesAns())){
                       holder.rd_1.setChecked(true);
                   }
               }
               else{
                   holder.rd_1.setVisibility(View.GONE);
               }

                if(current.getSurveyQuesValue().size()>1) {
                    holder.rd_2.setText(current.getSurveyQuesValue().get(1));
                    if(current.getSurveyQuesAns()!=null)
                    if(current.getSurveyQuesValue().get(1).equalsIgnoreCase(current.getSurveyQuesAns())){
                        holder.rd_2.setChecked(true);
                    }
                }
                else{
                    holder.rd_2.setVisibility(View.GONE);
                }


                if(current.getSurveyQuesValue().size()>2) {

                    holder.rd_3.setText(current.getSurveyQuesValue().get(2));
                    if(current.getSurveyQuesAns()!=null)
                    if(current.getSurveyQuesValue().get(2).equalsIgnoreCase(current.getSurveyQuesAns())){
                        holder.rd_3.setChecked(true);
                    }
                }

                else{
                    holder.rd_3.setVisibility(View.GONE);
                }

                if(current.getSurveyQuesValue().size()>3) {

                    holder.rd_4.setText(current.getSurveyQuesValue().get(3));
                    if(current.getSurveyQuesAns()!=null)
                    if(current.getSurveyQuesValue().get(3).equalsIgnoreCase(current.getSurveyQuesAns())){
                        holder.rd_4.setChecked(true);
                    }
                }
                else{
                    holder.rd_4.setVisibility(View.GONE);
                }

                if(current.getSurveyQuesValue().size()>4) {

                    holder.rd_5.setText(current.getSurveyQuesValue().get(4));
                    if(current.getSurveyQuesAns()!=null)
                        if(current.getSurveyQuesValue().get(4).equalsIgnoreCase(current.getSurveyQuesAns())){
                            holder.rd_5.setChecked(true);
                        }
                }
                else{
                    holder.rd_5.setVisibility(View.GONE);
                }


                if(current.getSurveyQuesValue().size()>5) {

                    holder.rd_6.setText(current.getSurveyQuesValue().get(5));
                    if(current.getSurveyQuesAns()!=null)
                        if(current.getSurveyQuesValue().get(5).equalsIgnoreCase(current.getSurveyQuesAns())){
                            holder.rd_6.setChecked(true);
                        }
                }
                else{
                    holder.rd_6.setVisibility(View.GONE);
                }


                if(current.getSurveyQuesValue().size()>6) {

                    holder.rd_7.setText(current.getSurveyQuesValue().get(6));
                    if(current.getSurveyQuesAns()!=null)
                        if(current.getSurveyQuesValue().get(6).equalsIgnoreCase(current.getSurveyQuesAns())){
                            holder.rd_7.setChecked(true);
                        }
                }
                else{
                    holder.rd_7.setVisibility(View.GONE);
                }

                if(current.getSurveyQuesValue().size()>7) {

                    holder.rd_8.setText(current.getSurveyQuesValue().get(7));
                    if(current.getSurveyQuesAns()!=null)
                        if(current.getSurveyQuesValue().get(7).equalsIgnoreCase(current.getSurveyQuesAns())){
                            holder.rd_8.setChecked(true);
                        }
                }
                else{
                    holder.rd_8.setVisibility(View.GONE);
                }


                holder.rd_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        holder.rd_1.setChecked(true);
                        holder.rd_2.setChecked(false);
                        holder.rd_3.setChecked(false);
                        holder.rd_4.setChecked(false);
                        holder.rd_5.setChecked(false);
                        holder.rd_6.setChecked(false);
                        holder.rd_7.setChecked(false);
                        holder.rd_8.setChecked(false);

                        setcheckedData(position,holder.rd_1);


                    }
                });

                holder.rd_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        holder.rd_1.setChecked(false);
                        holder.rd_2.setChecked(true);
                        holder.rd_3.setChecked(false);
                        holder.rd_4.setChecked(false);
                        holder.rd_5.setChecked(false);
                        holder.rd_6.setChecked(false);
                        holder.rd_7.setChecked(false);
                        holder.rd_8.setChecked(false);

                        setcheckedData(position,holder.rd_2);

                    }
                });

                holder.rd_3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        holder.rd_1.setChecked(false);
                        holder.rd_2.setChecked(false);
                        holder.rd_3.setChecked(true);
                        holder.rd_4.setChecked(false);
                        holder.rd_5.setChecked(false);
                        holder.rd_6.setChecked(false);
                        holder.rd_7.setChecked(false);
                        holder.rd_8.setChecked(false);

                        setcheckedData(position,holder.rd_3);

                    }
                });


                holder.rd_4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        holder.rd_1.setChecked(false);
                        holder.rd_2.setChecked(false);
                        holder.rd_3.setChecked(false);
                        holder.rd_4.setChecked(true);
                        holder.rd_5.setChecked(false);
                        holder.rd_6.setChecked(false);
                        holder.rd_7.setChecked(false);
                        holder.rd_8.setChecked(false);

                        setcheckedData(position,holder.rd_4);

                    }
                });


                holder.rd_5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        holder.rd_1.setChecked(false);
                        holder.rd_2.setChecked(false);
                        holder.rd_3.setChecked(false);
                        holder.rd_4.setChecked(false);
                        holder.rd_5.setChecked(true);
                        holder.rd_6.setChecked(false);
                        holder.rd_7.setChecked(false);
                        holder.rd_8.setChecked(false);


                        setcheckedData(position,holder.rd_5);
                    }
                });
                holder.rd_6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        holder.rd_1.setChecked(false);
                        holder.rd_2.setChecked(false);
                        holder.rd_3.setChecked(false);
                        holder.rd_4.setChecked(false);
                        holder.rd_5.setChecked(false);
                        holder.rd_6.setChecked(true);
                        holder.rd_7.setChecked(false);
                        holder.rd_8.setChecked(false);

                        setcheckedData(position,holder.rd_6);

                    }
                });

                holder.rd_7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        holder.rd_1.setChecked(false);
                        holder.rd_2.setChecked(false);
                        holder.rd_3.setChecked(false);
                        holder.rd_4.setChecked(false);
                        holder.rd_5.setChecked(false);
                        holder.rd_6.setChecked(false);
                        holder.rd_7.setChecked(true);
                        holder.rd_8.setChecked(false);

                        setcheckedData(position,holder.rd_7);

                    }
                });

                holder.rd_8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        holder.rd_1.setChecked(false);
                        holder.rd_2.setChecked(false);
                        holder.rd_3.setChecked(false);
                        holder.rd_4.setChecked(false);
                        holder.rd_5.setChecked(false);
                        holder.rd_6.setChecked(false);
                        holder.rd_7.setChecked(false);
                        holder.rd_8.setChecked(true);

                        setcheckedData(position,holder.rd_8);

                    }
                });



            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void setcheckedData(int position, View v) {
        int id = v.getId();
        switch (id) {
            case R.id.rd_1: fetchdata.get(position).setSurveyQuesAns(fetchdata.get(position).getSurveyQuesValue().get(0));
                break;
            case R.id.rd_2:  fetchdata.get(position).setSurveyQuesAns(fetchdata.get(position).getSurveyQuesValue().get(1));
                break;
            case R.id.rd_3:  fetchdata.get(position).setSurveyQuesAns(fetchdata.get(position).getSurveyQuesValue().get(2));
                break;
            case R.id.rd_4:  fetchdata.get(position).setSurveyQuesAns(fetchdata.get(position).getSurveyQuesValue().get(3));
                break;

            case R.id.rd_5: fetchdata.get(position).setSurveyQuesAns(fetchdata.get(position).getSurveyQuesValue().get(4));
                break;
            case R.id.rd_6:  fetchdata.get(position).setSurveyQuesAns(fetchdata.get(position).getSurveyQuesValue().get(5));
                break;
            case R.id.rd_7:  fetchdata.get(position).setSurveyQuesAns(fetchdata.get(position).getSurveyQuesValue().get(6));
                break;
            case R.id.rd_8:  fetchdata.get(position).setSurveyQuesAns(fetchdata.get(position).getSurveyQuesValue().get(7));
                break;

            default:
                break;
        }
    }

    public List<ServeyQuestionnaire> getQuestionnaire(){

        List<ServeyQuestionnaire> tt=null;

      for(ServeyQuestionnaire f :fetchdata){

          if(f.getSurveyQuesAns()==null || f.getSurveyQuesAns().equalsIgnoreCase("")){
             return null;
          }
          else{
              tt= fetchdata;
          }
      }
        return tt;
    }


    @Override
    public int getItemCount() {
        return fetchdata.size() ;
    }

    public void selectPosition(int position) {
        int lastPosition = mSelectedPosition;
        mSelectedPosition = position;
        notifyItemChanged(lastPosition);
        notifyItemChanged(position);
    }

    public class SurveyQuestion extends RecyclerView.ViewHolder {
        TextView tv_survey_ques,txt_sno;
        RadioButton rd_1, rd_2, rd_3,rd_4,rd_5, rd_6, rd_7,rd_8;
        RadioGroup radiogroup_ppe,radiogroup_ppe2;
        View itemView;

        public SurveyQuestion(View itemView) {
            super(itemView);
            try {
                this.itemView = itemView;
                tv_survey_ques = (TextView) itemView.findViewById(R.id.tv_survey_ques);
                txt_sno= (TextView) itemView.findViewById(R.id.txt_sno);
                rd_1 = (RadioButton) itemView.findViewById(R.id.rd_1);
                rd_2 = (RadioButton) itemView.findViewById(R.id.rd_2);
                rd_3 = (RadioButton) itemView.findViewById(R.id.rd_3);
                rd_4 = (RadioButton) itemView.findViewById(R.id.rd_4);

                rd_5 = (RadioButton) itemView.findViewById(R.id.rd_5);
                rd_6 = (RadioButton) itemView.findViewById(R.id.rd_6);
                rd_7 = (RadioButton) itemView.findViewById(R.id.rd_7);
                rd_8 = (RadioButton) itemView.findViewById(R.id.rd_8);

//                radiogroup_ppe = (RadioGroup) itemView.findViewById(R.id.radiogroup_ppe);
//                radiogroup_ppe2= (RadioGroup) itemView.findViewById(R.id.radiogroup_ppe2);


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


}
