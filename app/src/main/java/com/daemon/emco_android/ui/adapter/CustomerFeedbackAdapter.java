package com.daemon.emco_android.ui.adapter;

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
import com.hardik.clickshrinkeffect.ClickShrinkEffectKt;
import java.util.ArrayList;
import java.util.List;
import static com.daemon.emco_android.ui.fragments.survey.SurveyHeader.DETAILED;
import static com.daemon.emco_android.ui.fragments.survey.SurveyHeader.SUMMARY;

public class CustomerFeedbackAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static String MODULE = "SURVEYAdapter";
    public static String TAG = "";

    private List<ServeyQuestionnaire> fetchdata = new ArrayList<>();
    private String surveyType;
    private AppCompatActivity mActivity;
    private PpeListener mCallBack;
    private Font font;
    private int mSelectedPosition;
    private boolean isFooterEnabled = false;
    private boolean isFetchData = false;
    private boolean isCompleted = false;

   private OptionTouchListner listner;

    public CustomerFeedbackAdapter(AppCompatActivity mActivity, ArrayList<ServeyQuestionnaire> fetchdata,String surveyType,OptionTouchListner listner) {
        TAG = "RC_ListAdapter";
        Log.d(MODULE, TAG);
        this.surveyType=surveyType;
        this.mActivity = mActivity;
        this.fetchdata = fetchdata;
        this.listner=listner;
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


            if(surveyType.equalsIgnoreCase(DETAILED)){
                View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_survey_question, parent, false);
                mHolder = new SurveyQuestion(layoutView);
            }
            else{
                View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_survey_question_summary, parent, false);
                mHolder = new SurveyQuestion(layoutView);
            }

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

                ArrayList<Integer> smileyResource=new ArrayList<>();

                if(surveyType.equalsIgnoreCase(SUMMARY)) {
                    smileyResource = getSmileyResource(current.getSurveyQuesValue().size());
                }

                holder.tv_survey_ques.setText(current.getSurveyQues());

               if(current.getSurveyQuesValue().size()>0) {

                   holder.rd_1.setText(current.getSurveyQuesValue().get(0));
                   if(current.getSurveyQuesAns()!=null)
                   if(current.getSurveyQuesValue().get(0).equalsIgnoreCase(current.getSurveyQuesAns())){
                       holder.rd_1.setChecked(true);
                       holder.rd_1.setTextColor(mActivity.getResources().getColor(R.color.colorPrimary));
                       holder.rd_1.setTypeface(font.getHelveticaBold());
                   }

                   if(surveyType.equalsIgnoreCase(SUMMARY)) {
                       holder.rd_1.setButtonDrawable(smileyResource.get(0));
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
                        holder.rd_2.setTextColor(mActivity.getResources().getColor(R.color.colorPrimary));
                        holder.rd_2.setTypeface(font.getHelveticaBold());
                    }

                    if(surveyType.equalsIgnoreCase(SUMMARY)) {
                        holder.rd_2.setButtonDrawable(smileyResource.get(1));
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
                        holder.rd_3.setTextColor(mActivity.getResources().getColor(R.color.colorPrimary));
                        holder.rd_3.setTypeface(font.getHelveticaBold());
                    }

                    if(surveyType.equalsIgnoreCase(SUMMARY)) {
                        holder.rd_3.setButtonDrawable(smileyResource.get(2));
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
                        holder.rd_4.setTextColor(mActivity.getResources().getColor(R.color.colorPrimary));
                        holder.rd_4.setTypeface(font.getHelveticaBold());
                    }


                    if(surveyType.equalsIgnoreCase(SUMMARY)){
                        holder.rd_4.setButtonDrawable(smileyResource.get(3));
                    }

                }
                else{
                    holder.rd_4.setVisibility(View.GONE);
                }

                if(current.getSurveyQuesValue().size()>4) {

                    holder.rd_5.setText(current.getSurveyQuesValue().get(4));
                    if(current.getSurveyQuesAns()!=null)
                        if(current.getSurveyQuesValue().get(4).equalsIgnoreCase(current.getSurveyQuesAns())) {
                            holder.rd_5.setChecked(true);
                            holder.rd_5.setTextColor(mActivity.getResources().getColor(R.color.colorPrimary));
                            holder.rd_5.setTypeface(font.getHelveticaBold());
                        }

                    if(surveyType.equalsIgnoreCase(SUMMARY)){
                        holder.rd_5.setButtonDrawable(smileyResource.get(4));
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
                            holder.rd_6.setTextColor(mActivity.getResources().getColor(R.color.colorPrimary));
                            holder.rd_6.setTypeface(font.getHelveticaBold());
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
                            holder.rd_7.setTextColor(mActivity.getResources().getColor(R.color.colorPrimary));
                            holder.rd_7.setTypeface(font.getHelveticaBold());
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
                            holder.rd_8.setTextColor(mActivity.getResources().getColor(R.color.colorPrimary));
                            holder.rd_8.setTypeface(font.getHelveticaBold());
                        }
                }
                else{
                    holder.rd_8.setVisibility(View.GONE);
                }


                holder.rd_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        resetChecked(holder);
                        holder.rd_1.setChecked(true);

                        setcheckedData(position,holder.rd_1);

                        resetColor(holder);
                        holder.rd_1.setTextColor(mActivity.getResources().getColor(R.color.colorPrimary));
                        holder.rd_1.setTypeface(font.getHelveticaBold());


                    }
                });

                holder.rd_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        resetChecked(holder);
                        holder.rd_2.setChecked(true);

                        setcheckedData(position,holder.rd_2);

                        resetColor(holder);
                        holder.rd_2.setTextColor(mActivity.getResources().getColor(R.color.colorPrimary));
                        holder.rd_2.setTypeface(font.getHelveticaBold());
                    }
                });

                holder.rd_3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        resetChecked(holder);
                        holder.rd_3.setChecked(true);

                        setcheckedData(position,holder.rd_3);
                        resetColor(holder);
                        holder.rd_3.setTextColor(mActivity.getResources().getColor(R.color.colorPrimary));
                        holder.rd_3.setTypeface(font.getHelveticaBold());
                    }
                });


                holder.rd_4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        resetChecked(holder);
                        holder.rd_4.setChecked(true);


                        setcheckedData(position,holder.rd_4);
                        resetColor(holder);
                        holder.rd_4.setTextColor(mActivity.getResources().getColor(R.color.colorPrimary));
                        holder.rd_4.setTypeface(font.getHelveticaBold());
                    }
                });


                holder.rd_5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        resetChecked(holder);
                        holder.rd_5.setChecked(true);

                        setcheckedData(position,holder.rd_5);
                        resetColor(holder);
                        holder.rd_5.setTextColor(mActivity.getResources().getColor(R.color.colorPrimary));
                        holder.rd_5.setTypeface(font.getHelveticaBold());

                    }
                });
                holder.rd_6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        resetChecked(holder);
                        holder.rd_6.setChecked(true);

                        setcheckedData(position,holder.rd_6);
                        resetColor(holder);
                        holder.rd_6.setTextColor(mActivity.getResources().getColor(R.color.colorPrimary));
                        holder.rd_6.setTypeface(font.getHelveticaBold());

                    }
                });

                holder.rd_7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        resetChecked(holder);
                        holder.rd_7.setChecked(true);

                        setcheckedData(position,holder.rd_7);
                        resetColor(holder);
                        holder.rd_7.setTextColor(mActivity.getResources().getColor(R.color.colorPrimary));
                        holder.rd_7.setTypeface(font.getHelveticaBold());

                    }
                });

                holder.rd_8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        resetChecked(holder);

                        holder.rd_8.setChecked(true);

                        setcheckedData(position,holder.rd_8);
                        resetColor(holder);
                        holder.rd_8.setTextColor(mActivity.getResources().getColor(R.color.colorPrimary));
                        holder.rd_8.setTypeface(font.getHelveticaBold());

                    }
                });

            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void resetColor(SurveyQuestion holder){
        holder.rd_1.setTextColor(mActivity.getResources().getColor(R.color.secondary_text));
        holder.rd_2.setTextColor(mActivity.getResources().getColor(R.color.secondary_text));
        holder.rd_3.setTextColor(mActivity.getResources().getColor(R.color.secondary_text));
        holder.rd_4.setTextColor(mActivity.getResources().getColor(R.color.secondary_text));
        holder.rd_5.setTextColor(mActivity.getResources().getColor(R.color.secondary_text));
        holder.rd_6.setTextColor(mActivity.getResources().getColor(R.color.secondary_text));
        holder.rd_7.setTextColor(mActivity.getResources().getColor(R.color.secondary_text));
        holder.rd_8.setTextColor(mActivity.getResources().getColor(R.color.secondary_text));


        holder.rd_1.setTypeface(font.getHelveticaRegular());
        holder.rd_2.setTypeface(font.getHelveticaRegular());
        holder.rd_3.setTypeface(font.getHelveticaRegular());
        holder.rd_4.setTypeface(font.getHelveticaRegular());
        holder.rd_5.setTypeface(font.getHelveticaRegular());
        holder.rd_6.setTypeface(font.getHelveticaRegular());
        holder.rd_7.setTypeface(font.getHelveticaRegular());
        holder.rd_8.setTypeface(font.getHelveticaRegular());

    }

    public void resetChecked(SurveyQuestion holder){

        holder.rd_1.setChecked(false);
        holder.rd_2.setChecked(false);
        holder.rd_3.setChecked(false);
        holder.rd_4.setChecked(false);
        holder.rd_5.setChecked(false);
        holder.rd_6.setChecked(false);
        holder.rd_7.setChecked(false);
        holder.rd_8.setChecked(false);

    }


    public void setcheckedData(int position, View v) {


        int id = v.getId();
        switch (id) {
            case R.id.rd_1:
            {
                fetchdata.get(position).setSurveyQuesAns(fetchdata.get(position).getSurveyQuesValue().get(0));
                fetchdata.get(position).setSurveyOptionScore(Integer.parseInt(fetchdata.get(position).getOptionScore().get(0)));
            }
                break;
            case R.id.rd_2:   {
                fetchdata.get(position).setSurveyQuesAns(fetchdata.get(position).getSurveyQuesValue().get(1));
                fetchdata.get(position).setSurveyOptionScore(Integer.parseInt(fetchdata.get(position).getOptionScore().get(1)));
            }
                break;
            case R.id.rd_3:  {
                fetchdata.get(position).setSurveyQuesAns(fetchdata.get(position).getSurveyQuesValue().get(2));
                fetchdata.get(position).setSurveyOptionScore(Integer.parseInt(fetchdata.get(position).getOptionScore().get(2)));
            }
                break;
            case R.id.rd_4:  {
                fetchdata.get(position).setSurveyQuesAns(fetchdata.get(position).getSurveyQuesValue().get(3));
                fetchdata.get(position).setSurveyOptionScore(Integer.parseInt(fetchdata.get(position).getOptionScore().get(3)));
            }
                break;

            case R.id.rd_5:  {
                fetchdata.get(position).setSurveyQuesAns(fetchdata.get(position).getSurveyQuesValue().get(4));
                fetchdata.get(position).setSurveyOptionScore(Integer.parseInt(fetchdata.get(position).getOptionScore().get(4)));
            }
                break;
            case R.id.rd_6:   {
                fetchdata.get(position).setSurveyQuesAns(fetchdata.get(position).getSurveyQuesValue().get(5));
                fetchdata.get(position).setSurveyOptionScore(Integer.parseInt(fetchdata.get(position).getOptionScore().get(5)));
            }
                break;
            case R.id.rd_7:  {
                fetchdata.get(position).setSurveyQuesAns(fetchdata.get(position).getSurveyQuesValue().get(6));
                fetchdata.get(position).setSurveyOptionScore(Integer.parseInt(fetchdata.get(position).getOptionScore().get(6)));
            }
                break;
            case R.id.rd_8:  {
                fetchdata.get(position).setSurveyQuesAns(fetchdata.get(position).getSurveyQuesValue().get(7));
                fetchdata.get(position).setSurveyOptionScore(Integer.parseInt(fetchdata.get(position).getOptionScore().get(7)));
            }
                break;

            default:
                break;
        }

        if(surveyType.equalsIgnoreCase(SUMMARY)){
            listner.onOptionSelected();
        }

    }

    public int getOptionScore(int pos){
      return  fetchdata.get(pos).getSurveyOptionScore();
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

                tv_survey_ques.setTypeface(font.getHelveticaBold());
                txt_sno.setTypeface(font.getHelveticaBold());

                ClickShrinkEffectKt.applyClickShrink(rd_1);
                ClickShrinkEffectKt.applyClickShrink(rd_2);
                ClickShrinkEffectKt.applyClickShrink(rd_3);
                ClickShrinkEffectKt.applyClickShrink(rd_4);
                ClickShrinkEffectKt.applyClickShrink(rd_5);
                ClickShrinkEffectKt.applyClickShrink(rd_6);
                ClickShrinkEffectKt.applyClickShrink(rd_7);
                ClickShrinkEffectKt.applyClickShrink(rd_8);


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public interface OptionTouchListner{

        void onOptionSelected();

    }



    public ArrayList<Integer> getSmileyResource(int size){

        ArrayList<Integer> temp=new ArrayList<>();
        temp.add(R.drawable.excellent_smiley);
        temp.add(R.drawable.good_smiley);
        temp.add(R.drawable.satisfactory_smiley);
        temp.add(R.drawable.expectation_smiley);
        temp.add(R.drawable.poor_smiley);

        if(size==4){
            temp.remove(0);
            return temp;
        }

        if(size==3){
            temp.remove(0);
            temp.remove(2);
            return temp;
        }

        if(size==2){
            temp.remove(0);
            temp.remove(1);
            temp.remove(1);
            return temp;
        }

        return temp;
    }


}
