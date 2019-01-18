package com.jyq.wm.holder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jyq.wm.R;
import com.jyq.wm.activity.BNaviMainActivity;
import com.jyq.wm.activity.OrderDetailActivity;
import com.jyq.wm.bean.OrderInfo;
import com.jyq.wm.listener.MyItemClickListener;
import com.jyq.wm.utils.DialogUtils;
import com.jyq.wm.utils.ToastUtil;


/**
 */
public class OrderHolder2 extends RecyclerView.ViewHolder
{
    private TextView mUrgeFlagTv;
    private LinearLayout mOrderLayout;
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
    private LinearLayout mItemLayout;

    public OrderHolder2(View rootView, Context context, MyItemClickListener listener)
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
        mItemLayout = (LinearLayout)rootView.findViewById(R.id.ll_item);
        mOrderLayout =  (LinearLayout)rootView.findViewById(R.id.ll_order);
        mUrgeFlagTv = (TextView) rootView.findViewById(R.id.tv_urgeFlag);
    }


    public void setOrderInfo(final OrderInfo mOrderInfo, final int p)
    {


        if("1".equals(mOrderInfo.getUrgeFlag()))
        {
            mOrderLayout.setBackgroundResource(R.drawable.blue_frame_5dp);
            mUrgeFlagTv.setVisibility(View.VISIBLE);
        }
        else
        {
            mOrderLayout.setBackgroundResource(R.drawable.gray_frame_5dp);
            mUrgeFlagTv.setVisibility(View.GONE);
        }
        mNumberTv.setText(mOrderInfo.getOrderId());
        mShopNameTv.setText(mOrderInfo.getStoreName());
        mTimeTv.setText(mOrderInfo.getAddTime());
        mPhoneTv.setText("客户电话:" + mOrderInfo.getPhone());
        mNameTv.setText("客户姓名:" + mOrderInfo.getName());
        mAddressTv.setText("客户地址:"+mOrderInfo.getAddress());
        mPayStyleTv.setText("offline".equals(mOrderInfo.getPayType()) ? "货到付款" : "微信支付");
        if ("offline".equals(mOrderInfo.getPayType()))
        {
            mPayStyleTv.setTextColor(ContextCompat.getColor(context,R.color.redA));
        }
        else
        {
            mPayStyleTv.setTextColor(ContextCompat.getColor(context,R.color.green));
        }
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
                    ToastUtil.show(context, "商家坐标缺失！");
                }
                else
                {
                    Intent mItent = new Intent(context, BNaviMainActivity.class);
                    mItent.putExtra("endLnt", mOrderInfo.getStoreLnt());
                    mItent.putExtra("endLat", mOrderInfo.getStoreLat());
                    context.startActivity(mItent);
                }

            }
        });

        mItemLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                context.startActivity(new Intent(context, OrderDetailActivity.class).putExtra
                        ("ORDER_ID", mOrderInfo.getOrderId()));
            }
        });
        mPhoneTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!TextUtils.isEmpty(mOrderInfo.getPhone()))
                {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + mOrderInfo.getPhone());
                    intent.setData(data);
                    context. startActivity(intent);

                }
            }
        });
    }


}
