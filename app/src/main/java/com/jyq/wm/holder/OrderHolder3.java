package com.jyq.wm.holder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jyq.wm.R;
import com.jyq.wm.activity.BNaviMainActivity;
import com.jyq.wm.bean.OrderInfo;
import com.jyq.wm.listener.MyItemClickListener;
import com.jyq.wm.utils.ToastUtil;


/**
 */
public class OrderHolder3 extends RecyclerView.ViewHolder
{
    private TextView mShopNameTv;
    private TextView mTimeTv;
    private TextView mNumberTv;
    private TextView mPhoneTv;
    private TextView mNameTv;
    private TextView mAddressTv;
    private TextView mPikupTv;
    private TextView mNavigationTv;
    private TextView mPayStyleTv;
    private MyItemClickListener listener;
    private Context context;


    public OrderHolder3(View rootView, Context context, MyItemClickListener listener)
    {
        super(rootView);
        this.listener = listener;
        this.context = context;
        mNumberTv = (TextView) rootView.findViewById(R.id.tv_code);
        mShopNameTv = (TextView) rootView.findViewById(R.id.tv_shop_name);
        mTimeTv = (TextView) rootView.findViewById(R.id.tv_time);
        mPhoneTv = (TextView) rootView.findViewById(R.id.tv_customer_phone);
        mNameTv = (TextView) rootView.findViewById(R.id.tv_customer_name);
        mAddressTv = (TextView) rootView.findViewById(R.id.tv_customer_address);
        mPikupTv = (TextView) rootView.findViewById(R.id.tv_pickup);
        mPayStyleTv = (TextView) rootView.findViewById(R.id.tv_pay_style);
        mNavigationTv = (TextView) rootView.findViewById(R.id.tv_navigation);
    }


    public void setOrderInfo(final OrderInfo mOrderInfo, final int p)
    {

        mNumberTv.setText(mOrderInfo.getId());
        mShopNameTv.setText(mOrderInfo.getStoreName());
        mTimeTv.setText(mOrderInfo.getAddTime());
        mPhoneTv.setText("客户电话:" + mOrderInfo.getPhone());
        mNameTv.setText("客户姓名:" + mOrderInfo.getName());
        mAddressTv.setText("客户地址:"+mOrderInfo.getAddress());
        mPayStyleTv.setText("offline".endsWith(mOrderInfo.getPayType()) ? "货到付款" : "微信支付");

        mPikupTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.onItemClick(v, p);
            }
        });
        mNavigationTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mOrderInfo.getStoreLnt() == 0)
                {
                    ToastUtil.show(context, "客户坐标缺失！");
                }
                else
                {
                    Intent mItent = new Intent(context, BNaviMainActivity.class);
                    mItent.putExtra("endLnt", mOrderInfo.getUserLnt());
                    mItent.putExtra("endLat", mOrderInfo.getUserLat());
                    context.startActivity(mItent);
                }

            }
        });
    }


}