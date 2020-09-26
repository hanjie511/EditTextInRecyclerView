package com.example.hj.testrecyclerview;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyHolder> implements  View.OnTouchListener, View.OnFocusChangeListener{
    private List<String> dataList;
    private Context context;
    private int selectedEditTextPosition = -1;
    private Map<Integer,String> map=new HashMap<>();
    public MyRecyclerViewAdapter(List<String> list, Context context){
        this.dataList=list;
        this.context=context;
    }
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (selectedEditTextPosition != -1) {
                Log.w("MyAdapter", "onTextCha " + selectedEditTextPosition);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            dataList.set(selectedEditTextPosition,s.toString());
        }
    };
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
            holder.edit.setText(map.get(position));
        holder.edit.setTag(position);
        holder.edit.setOnTouchListener(this); // 正确写法
        holder.edit.setOnFocusChangeListener(this);
        if (selectedEditTextPosition != -1 && position == selectedEditTextPosition) { // 保证每个时刻只有一个EditText能获取到焦点
            holder.edit.requestFocus();
        } else {
            holder.edit.clearFocus();
        }
        holder.edit.setText(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        EditText editText = (EditText) v;
        if (hasFocus) {
            editText.addTextChangedListener(mTextWatcher);
        } else {
            editText.removeTextChangedListener(mTextWatcher);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            EditText editText = (EditText) v;
            selectedEditTextPosition = (int) editText.getTag();
            Log.w("MyAdapter", "clicked position: " + selectedEditTextPosition);
        }
        return false;
    }

    static class MyHolder extends RecyclerView.ViewHolder{
        private EditText edit;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            edit=itemView.findViewById(R.id.edit);
        }
    }
}
