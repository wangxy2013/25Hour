package com.jyq.qs.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jyq.qs.R;
import com.jyq.qs.bean.GoodsInfo;
import com.jyq.qs.utils.StringUtils;


/**
 */
public class GoodsHolder extends RecyclerView.ViewHolder
{
    private TextView mTotalPriceTv;
    private TextView mGoodsNameTv;
    private TextView mSizeTv;

    private View lineView;
    private Context context;


    public GoodsHolder(View rootView, Context context)
    {
        super(rootView);
        this.context = context;
        mGoodsNameTv = (TextView) rootView.findViewById(R.id.tv_goodsName);
        mTotalPriceTv = (TextView) rootView.findViewById(R.id.tv_total_price);
        mSizeTv = (TextView) rootView.findViewById(R.id.tv_size);
        lineView = (View) rootView.findViewById(R.id.line);
    }


    public void setGoodsInfo(GoodsInfo mGoodsInfo, int listSize, int p)
    {

        mTotalPriceTv.setText("¥" + Double.parseDouble(mGoodsInfo.getPrice()) * Integer.parseInt(mGoodsInfo.getSize()));
        mSizeTv.setText("x " + mGoodsInfo.getSize());

        if (StringUtils.stringIsEmpty(mGoodsInfo.getAttributesNamesValues()))
        {
            mGoodsNameTv.setText(mGoodsInfo.getGoodsName());
        }
        else
        {
            mGoodsNameTv.setText(mGoodsInfo.getGoodsName() + "(" + mGoodsInfo.getAttributesNamesValues() + ")");
        }

        if (p == (listSize - 1))
        {
            lineView.setVisibility(View.GONE);
        }
        else
        {
            lineView.setVisibility(View.VISIBLE);
        }
    }


}
