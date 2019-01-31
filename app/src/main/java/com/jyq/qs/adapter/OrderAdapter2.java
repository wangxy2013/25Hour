package com.jyq.qs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jyq.qs.R;
import com.jyq.qs.bean.OrderInfo;
import com.jyq.qs.holder.OrderHolder2;
import com.jyq.qs.listener.MyItemClickListener;

import java.util.List;

/**
 * 取货
 */
public class OrderAdapter2 extends RecyclerView.Adapter<OrderHolder2>
{

    private MyItemClickListener listener;
    private List<OrderInfo> list;
    private Context mContext;

    public OrderAdapter2(List<OrderInfo> list, Context mContext, MyItemClickListener listener)
    {
        this.list = list;
        this.mContext = mContext;
        this.listener = listener;
    }

    @Override
    public OrderHolder2 onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order2, parent, false);
        OrderHolder2 mHolder = new OrderHolder2(itemView, parent.getContext(), listener);
        return mHolder;
    }


    @Override
    public void onBindViewHolder(OrderHolder2 holder, int position)
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
