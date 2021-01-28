package com.example.barcdscanerapp.utils;


import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.barcdscanerapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.CustomViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    //리스너 객체 참조를 저장하는 변수
    private OnItemClickListener mListener = null;

    //OnItemClickListener 리스터 객체 참조를 어댑터에 전달하는 매서드
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    private ArrayList<HashMap<String,Object>> mList = null;

    public RecyclerAdapter(ArrayList<HashMap<String,Object>> list){this.mList = list;}

    public class CustomViewHolder extends RecyclerView.ViewHolder{

        protected TextView QrCode;
        protected TextView DateYmd;

        public CustomViewHolder(View view) {
            super(view);

            this.QrCode = view.findViewById(R.id.QrCode);
            this.DateYmd = view.findViewById(R.id.DateYmd);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            mListener.onItemClick(view, pos);
                        }
                    }
                }
            });
        }

    }

    @Override
    public RecyclerAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_adapter_item, viewGroup, false);
        RecyclerAdapter.CustomViewHolder viewHolder = new RecyclerAdapter.CustomViewHolder(view);

        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.CustomViewHolder holder, int position) {
        holder.QrCode.setText(StringUtils.noNull(mList.get(position).get("QRCODE")));
        holder.DateYmd.setText(StringUtils.noNull(mList.get(position).get("DATE")));

        holder.QrCode.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        holder.DateYmd.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }
}
