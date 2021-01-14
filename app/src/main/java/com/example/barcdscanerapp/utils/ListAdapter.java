package com.example.barcdscanerapp.utils;


import android.content.Context;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barcdscanerapp.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *   리스트 어탭터
 * @author LCY
 * @version 1.0.0
 * @since 2020-07-07 오전 11:20
 *
 * ROWS_ID
 * 필 수 : ROWS_ID
 * 선 택 : WEIGHT , TEXT_SIZE
 **/

public class ListAdapter extends BaseAdapter
{
    LayoutInflater inflater = null;
    private LinearLayout container;
    LinearLayout.LayoutParams layoutParams = null;
    private ArrayList<HashMap<String,Object>> m_oData = null;
    HashMap<String,Object>  _options = null;
    TextView view1 = null;
    String[] ROWS_IDS = null;
    private int nListCnt = 0;

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    //OnItemClickListener 리스터 객체 참조를 어댑터에 전달하는 매서드
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    //리스너 객체 참조를 저장하는 변수
    private OnItemClickListener mListener = null;

    public ListAdapter(ArrayList<HashMap<String,Object>> _oData  , HashMap<String,Object> list_options)
    {
        m_oData = _oData;
        nListCnt = m_oData.size();
        _options = list_options;
        ROWS_IDS = (String[]) list_options.get("ROWS_ID");


    }
    /**
     *  rows add
     * @author LCY
     * @version 1.0.0
     * @since 2020-07-07 오전 11:20
     **/
    public void addRows(HashMap<String,Object> data){
        m_oData.add(data);
        nListCnt=m_oData.size();
        this.notifyDataSetChanged();
    }
    /**
     *  row remove
     * @author LCY
     * @version 1.0.0
     * @since 2020-07-07 오전 11:20
     **/
    public void removeRow(int position){
        m_oData.remove(position);
        nListCnt=m_oData.size();
        this.notifyDataSetChanged();
    }

    /**
     *  리스트 재 설정
     * @author LCY
     * @version 1.0.0
     * @since 2020-07-07 오전 11:20
     **/
    public void reSetList(ArrayList<HashMap<String,Object>> _oData ){
        m_oData = _oData;
        nListCnt=m_oData.size();
        this.notifyDataSetChanged();
    };
    public ArrayList<HashMap<String,Object>> getDataList(){
        return m_oData;
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }
    @Override
    public int getCount()
    {
        return nListCnt;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {

        if (convertView == null)
        {
            final Context context = parent.getContext();
            if (inflater == null)
            {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            };

            convertView = setLayouts(convertView,parent,_options);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClick(v, position);
                    }
                }
            });
        };

        convertView = setColumn(position,convertView,parent , inflater, m_oData , _options );
        return convertView;
    };

    public View setLayouts( View convertView, ViewGroup parent ,
                            HashMap<String, Object> p_options){
        //부모 layout setting
        convertView = inflater.inflate(R.layout.list_adapter_item, parent, false);
        container = (LinearLayout) convertView.findViewById(R.id.container);
        for(int i=0; i<ROWS_IDS.length ; i++){
            layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);

            if( !(StringUtils.noNull(  p_options.get("WEIGHT") )).equals("")){
                layoutParams.weight = Integer.parseInt(StringUtils.noNull(((String[]) p_options.get("WEIGHT"))[i] , "1"));
            }else{
                layoutParams.weight = 1;
            };

            layoutParams.gravity = Gravity.CENTER;
            view1 = new TextView(convertView.getContext());
            view1.setId(i);
            if( !(StringUtils.noNull(  p_options.get("TEXT_SIZE") )).equals("")){
                view1.setTextSize( Integer.parseInt(  StringUtils.noNull( ((String[]) p_options.get("TEXT_SIZE"))[i] , "12" )  ) );
            }else{
                view1.setTextSize(24);
            };

            view1.setGravity(Gravity.CENTER);
            view1.setLayoutParams(layoutParams);
            container.addView(view1 );
        };

        return container;
    }

    public View setColumn(int position,View convertView ,
                          ViewGroup parent , LayoutInflater inflater ,
                          ArrayList<HashMap<String,Object>> m_oData ,
                          HashMap<String, Object> p_options ){
        for(int i=0; i<ROWS_IDS.length ; i++){
            TextView text1 = (TextView) convertView.findViewById(i);
            ((TextView) convertView.findViewById(i)).setText(  Html.fromHtml(StringUtils.noNull(m_oData.get(position).get(StringUtils.noNull(ROWS_IDS[i]))))  );
        };

        return convertView;
    };


}
