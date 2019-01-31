package com.jyq.qs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jyq.qs.R;
import com.jyq.qs.bean.OrderInfo;
import com.jyq.qs.holder.OrderHolder4;
import com.jyq.qs.listener.MyItemClickListener;

import java.util.List;

/**
 * 已完成
 */
public class OrderAdapter4 extends RecyclerView.Adapter<OrderHolder4>
{

    private MyItemClickListener listener;
    private List<OrderInfo> list;
    private Context mContext;

    public OrderAdapter4(List<OrderInfo> list, Context mContext, MyItemClickListener listener)
    {
        this.list = list;
        this.mContext = mContext;
        this.listener = listener;
    }

    @Override
    public OrderHolder4 onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order4, parent, false);
        OrderHolder4 mHolder = new OrderHolder4(itemView, parent.getContext(), listener);
        return mHolder;
    }


    @Override
    public void onBindViewHolder(OrderHolder4 holder, int position)
    {
        OrderInfo mOrderInfo = list.get(position);
        holder.setOrderInfo(mOrderInfo, position);
    }

    @Override
    public int getItemCount()
    {

        return list.size();


    }
}
