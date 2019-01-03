package com.jyq.wm.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jyq.wm.R;
import com.jyq.wm.bean.GoodsInfo;
import com.jyq.wm.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 */
public class GoodsHolder extends RecyclerView.ViewHolder
{
    private ImageView mGoodsImageIv;
    private TextView mPriceTv;
    private TextView mTotalPriceTv;
    private TextView mGoodsNameTv;
    private TextView mSizeTv;
    private TextView mTributesNamesTv;

    private View lineView;
    private Context context;


    public GoodsHolder(View rootView, Context context)
    {
        super(rootView);
        this.context = context;
        mGoodsImageIv = (ImageView) rootView.findViewById(R.id.iv_goodsImage);
        mGoodsNameTv = (TextView) rootView.findViewById(R.id.tv_goodsName);
        mPriceTv = (TextView) rootView.findViewById(R.id.tv_price);
        mTotalPriceTv = (TextView) rootView.findViewById(R.id.tv_total_price);
        mSizeTv = (TextView) rootView.findViewById(R.id.tv_size);
        mTributesNamesTv = (TextView) rootView.findViewById(R.id.tv_tributesNames);
        lineView = (View) rootView.findViewById(R.id.line);
    }


    public void setGoodsInfo(GoodsInfo mGoodsInfo, int listSize, int p )
    {
        ImageLoader.getInstance().displayImage(mGoodsInfo.getGoodsImage(), mGoodsImageIv);
        mGoodsNameTv.setText(mGoodsInfo.getGoodsName());
        mPriceTv.setText("¥" + mGoodsInfo.getPrice());
        mTotalPriceTv.setText("¥" + Double.parseDouble(mGoodsInfo.getPrice()) * Integer.parseInt(mGoodsInfo.getSize()));
        mSizeTv.setText("* " + mGoodsInfo.getSize());

        if (StringUtils.stringIsEmpty(mGoodsInfo.getAttributesNamesValues()))
        {
            mTributesNamesTv.setText("默认");
        }
        else
        {
            mTributesNamesTv.setText(mGoodsInfo.getAttributesNamesValues());
        }

        if(p == (listSize-1))
        {
            lineView.setVisibility(View.GONE);
        }
        else
        {
            lineView.setVisibility(View.VISIBLE);
        }
    }


}
