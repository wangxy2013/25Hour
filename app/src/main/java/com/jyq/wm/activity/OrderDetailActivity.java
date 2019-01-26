package com.jyq.wm.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jyq.wm.R;
import com.jyq.wm.adapter.GoodsAdapter;
import com.jyq.wm.bean.GoodsInfo;
import com.jyq.wm.bean.OrderDetailInfo;
import com.jyq.wm.http.DataRequest;
import com.jyq.wm.http.HttpRequest;
import com.jyq.wm.http.IRequestListener;
import com.jyq.wm.json.OrderDetailHandler;
import com.jyq.wm.utils.ConstantUtil;
import com.jyq.wm.utils.NetWorkUtil;
import com.jyq.wm.utils.StringUtils;
import com.jyq.wm.utils.ToastUtil;
import com.jyq.wm.utils.Urls;
import com.jyq.wm.widget.FullyLinearLayoutManager;
import com.jyq.wm.widget.MaxRecyclerView;
import com.jyq.wm.widget.statusbar.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderDetailActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_orderInterId)
    TextView tvOrderInterId;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_dateline)
    TextView tvDateline;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.rv_goods)
    MaxRecyclerView rvGoods;
    @BindView(R.id.tv_deposit)
    TextView tvDeposit;
    @BindView(R.id.tv_deliveryFee)
    TextView tvDeliveryFee;
    @BindView(R.id.tv_minusPrice)
    TextView tvMinusPrice;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_payType)
    TextView tvPayType;
    @BindView(R.id.tv_payStatus)
    TextView tvPayStatus;
    @BindView(R.id.tv_note)
    TextView tvNote;
    @BindView(R.id.tv_store_phone)
    TextView tvStorePhone;
    private String orderId;
    @BindView(R.id.tv_store_name)
    TextView tvStoreName;
    @BindView(R.id.tv_store_address)
    TextView tvStoreAddress;
    private GoodsAdapter mGoodsAdapter;
    private List<GoodsInfo> goodsInfoList = new ArrayList<>();
    private static final String GET_ORDER_DETAIL = "get_order_detail";
    private static final int REQUEST_SUCCESS = 0x01;
    private static final int REQUEST_FAIL = 0x02;
    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(OrderDetailActivity.this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case REQUEST_SUCCESS:
                    OrderDetailHandler mOrderDetailHandler = (OrderDetailHandler) msg.obj;
                    OrderDetailInfo mOrderDetailInfo = mOrderDetailHandler.getOrderDetailInfo();

                    if (null != mOrderDetailInfo)
                    {
                        tvOrderInterId.setText(mOrderDetailInfo.getId());
                        tvName.setText(mOrderDetailInfo.getName());

                        String phone = mOrderDetailInfo.getPhone();

                        if (!TextUtils.isEmpty(phone) && phone.length() > 6)
                        {
                            //                            String phoneNumber = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
                            //                            tvPhone.setText(phoneNumber);
                            tvPhone.setText(phone);
                        }
                        else
                        {
                            tvPhone.setText(phone);
                        }
                        tvStorePhone.setText(mOrderDetailInfo.getStorePhone());
                        tvStoreName.setText(mOrderDetailInfo.getStoreName());
                        tvStoreAddress.setText(mOrderDetailInfo.getStoreAddress());
                        tvDateline.setText(mOrderDetailInfo.getSubmitOrderTime());
                        tvAddress.setText(mOrderDetailInfo.getAddress());
                        tvDeposit.setText("¥:" + mOrderDetailInfo.getDeposit());
                        tvDeliveryFee.setText("¥:" + mOrderDetailInfo.getDeliveryFee());
                        tvMinusPrice.setText("¥:" + mOrderDetailInfo.getMinusPrice());
                        tvTotalPrice.setText("¥:" + mOrderDetailInfo.getTotalPrice());

                        String price = mOrderDetailInfo.getPrice();

                        if (StringUtils.stringIsEmpty(price))
                        {
                            tvPrice.setText("¥:0");
                        }
                        else
                        {
                            tvPrice.setText("¥:" + price);
                        }
                        tvPayType.setText(("online".equals(mOrderDetailInfo.getPayType())) ? "微信支付" : "货到付款");
                        tvPayStatus.setText(("1".equals(mOrderDetailInfo.getPayStatue())) ? "已支付" : "未支付");
                        tvNote.setText(mOrderDetailInfo.getNote());
                        goodsInfoList.clear();
                        goodsInfoList.addAll(mOrderDetailInfo.getGoodsInfoList());
                        mGoodsAdapter.notifyDataSetChanged();
                    }

                    break;

                case REQUEST_FAIL:
                    ToastUtil.show(OrderDetailActivity.this, msg.obj.toString());
                    break;


            }
        }
    };

    @Override
    protected void initData()
    {
        orderId = getIntent().getStringExtra("ORDER_ID");
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_order_detail);
        StatusBarUtil.setStatusBarBackground(this, R.drawable.main_bg);
        StatusBarUtil.StatusBarLightMode(OrderDetailActivity.this, false);
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(this);
        tvStorePhone.setOnClickListener(this);
        tvPhone.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        tvTitle.setText("订单详情");
        rvGoods.setLayoutManager(new FullyLinearLayoutManager(OrderDetailActivity.this));
        mGoodsAdapter = new GoodsAdapter(goodsInfoList, this);
        rvGoods.setAdapter(mGoodsAdapter);


    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (!NetWorkUtil.isConn(this))
        {
            NetWorkUtil.showNoNetWorkDlg(this);
            return;
        }
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("id", orderId);
        Gson gson = new Gson();
        Map<String, String> postMap = new HashMap<>();
        postMap.put("json", gson.toJson(valuePairs));
        DataRequest.instance().request(OrderDetailActivity.this, Urls.getOrederDetail(), this, HttpRequest.POST, GET_ORDER_DETAIL, postMap, new
                OrderDetailHandler());
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        hideProgressDialog();
        if (GET_ORDER_DETAIL.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_SUCCESS, obj));
            }
            else
            {
                hideProgressDialog();
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
    }


    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == ivBack)
        {
            finish();
        }
        else if(v == tvStorePhone)
        {
            if(!TextUtils.isEmpty(tvStorePhone.getText().toString()))
            {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + tvStorePhone.getText().toString());
                intent.setData(data);
                startActivity(intent);

            }
        }
        else if(v == tvPhone)
        {
            if(!TextUtils.isEmpty(tvPhone.getText().toString()))
            {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + tvPhone.getText().toString());
                intent.setData(data);
                startActivity(intent);

            }
        }
    }


}
