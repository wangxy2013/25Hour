package com.jyq.qs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jyq.qs.R;
import com.jyq.qs.bean.GoodsInfo;
import com.jyq.qs.holder.GoodsHolder;

import java.util.List;

/**
 */
public class GoodsAdapter extends RecyclerView.Adapter<GoodsHolder>
{

    private List<GoodsInfo> list;
    private Context mContext;

    public GoodsAdapter(List<GoodsInfo> list, Context mContext)
    {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public GoodsHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods, parent, false);
        GoodsHolder mHolder = new GoodsHolder(itemView, parent.getContext());
        return mHolder;
    }


    @Override
    public void onBindViewHolder(GoodsHolder holder, int position)
    {
        GoodsInfo mGoodsInfo = list.get(position);
        holder.setGoodsInfo(mGoodsInfo,list.size(),position);
    }

    @Override
    public int getItemCount()
    {

        return list.size();


    }
}
