package com.jyq.wm.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.jyq.wm.MyApplication;
import com.jyq.wm.R;
import com.jyq.wm.adapter.OrderAdapter2;
import com.jyq.wm.adapter.OrderAdapter4;
import com.jyq.wm.bean.OrderInfo;
import com.jyq.wm.http.DataRequest;
import com.jyq.wm.http.HttpRequest;
import com.jyq.wm.http.IRequestListener;
import com.jyq.wm.json.OrderListHandler;
import com.jyq.wm.json.ResultHandler;
import com.jyq.wm.listener.MyItemClickListener;
import com.jyq.wm.utils.ConfigManager;
import com.jyq.wm.utils.ConstantUtil;
import com.jyq.wm.utils.NetWorkUtil;
import com.jyq.wm.utils.ToastUtil;
import com.jyq.wm.utils.Urls;
import com.jyq.wm.widget.list.refresh.PullToRefreshBase;
import com.jyq.wm.widget.list.refresh.PullToRefreshRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class OrderFragment4 extends BaseFragment  implements IRequestListener, PullToRefreshBase.OnRefreshListener<RecyclerView>
{

    @BindView(R.id.refreshRecyclerView)
    PullToRefreshRecyclerView mPullToRefreshRecyclerView;

    @BindView(R.id.ll_no_order)
    LinearLayout mNoOrderLayout;
    private View rootView = null;
    private Unbinder unbinder;
    private RecyclerView mRecyclerView;

    private int pn = 1;
    private int mRefreshStatus;

    private List<OrderInfo> orderInfoList = new ArrayList<>();

    private OrderAdapter4 mAdapter;

    private static final String PICK_UP_REQUEST = "pick_up_request";
    private static final String GET_ORDER_REQUEST = "get_delivery_order_request";
    private static final int REQUEST_SUCCESS = 0x01;
    private static final int REQUEST_FAIL = 0x02;
    private static final int ROB_ORDER_SUCCESS = 0x03;
    private static final int ROB_ORDER_FAIL = 0x04;
    private static final int GET_ORDER_LIST = 0x05;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {

                case REQUEST_SUCCESS:

                    OrderListHandler mOrderListHandler = (OrderListHandler) msg.obj;


                    List<OrderInfo> newOrderInfoList = mOrderListHandler.getOrderInfoList();

                    if (pn == 1)
                    {
                        orderInfoList.clear();
                    }
                    orderInfoList.addAll(newOrderInfoList);
                    mAdapter.notifyDataSetChanged();

                    if (orderInfoList.isEmpty())
                    {
                        mNoOrderLayout.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        mNoOrderLayout.setVisibility(View.GONE);
                    }


                    break;

                case REQUEST_FAIL:
                    if (orderInfoList.isEmpty())
                    {
                        mNoOrderLayout.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        mNoOrderLayout.setVisibility(View.GONE);
                    }


                    break;

                case ROB_ORDER_SUCCESS:

                    ToastUtil.show(getActivity(), "操作成功");
                    pn = 1;
                    mRefreshStatus = 0;
                    loadData();
                    break;

                case ROB_ORDER_FAIL:
                    ToastUtil.show(getActivity(), msg.obj.toString());
                    break;

                case GET_ORDER_LIST:
                    pn = 1;
                    mRefreshStatus = 0;
                    loadData();

                  //  mHandler.sendEmptyMessageDelayed(GET_ORDER_LIST, 30 * 1000);


                    break;


            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if (rootView == null)
        {
            rootView = inflater.inflate(R.layout.fragment_order2, null);
            unbinder = ButterKnife.bind(this, rootView);
            initData();
            initViews();
            initViewData();
            initEvent();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null)
        {
            parent.removeView(rootView);
        }
        return rootView;
    }

    private static OrderFragment4 instance = null;

    public static OrderFragment4 newInstance()
    {
        if (instance == null)
        {
            instance = new OrderFragment4();
        }
        return instance;
    }

    @Override
    protected void initData()
    {


    }

    @Override
    protected void initViews()
    {

    }

    @Override
    protected void initEvent()
    {
        mNoOrderLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showProgressDialog(getActivity());
                pn = 1;
                mRefreshStatus = 0;
                loadData();
            }
        });
    }
    @Override
    public void onResume()
    {
        super.onResume();
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //相当于Fragment的onResume
            mHandler.sendEmptyMessage(GET_ORDER_LIST);
        } else {
            //相当于Fragment的onPause
        }
    }
    @Override
    protected void initViewData()
    {
        mPullToRefreshRecyclerView.setPullLoadEnabled(true);
        mRecyclerView = mPullToRefreshRecyclerView.getRefreshableView();
        mPullToRefreshRecyclerView.setOnRefreshListener(this);
        mPullToRefreshRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        mAdapter = new OrderAdapter4(orderInfoList, getActivity(), new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                if (MyApplication.getInstance().isOnline())
                {

                    pikupOrder(orderInfoList.get(position).getId());
                }
                else
                {
                    ToastUtil.show(getActivity(), "请先进行上线操作");
                }
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        mHandler.sendEmptyMessage(GET_ORDER_LIST);
    }


    private void loadData()
    {
        if (!NetWorkUtil.isConn(getActivity()))
        {
            hideProgressDialog(getActivity());
            if (mRefreshStatus == 1)
            {
                mPullToRefreshRecyclerView.onPullUpRefreshComplete();
            }
            else
            {
                mPullToRefreshRecyclerView.onPullDownRefreshComplete();
            }

            NetWorkUtil.showNoNetWorkDlg(getActivity());
            return;
        }
        Map<String, Object> valuePairs = new HashMap<>();
        valuePairs.put("pageNum", pn);
        valuePairs.put("pageSize", 15);
        valuePairs.put("status", 1);
        valuePairs.put("deliverUserId", ConfigManager.instance().getUserID());
        Gson gson = new Gson();
        Map<String, String> postMap = new HashMap<>();
        postMap.put("json", gson.toJson(valuePairs));
        DataRequest.instance().request(getActivity(), Urls.getSendOutUrl(), this, HttpRequest.POST, GET_ORDER_REQUEST, postMap, new
                OrderListHandler());
    }


    private void pikupOrder(String orderId)
    {
        if (!NetWorkUtil.isConn(getActivity()))
        {
            NetWorkUtil.showNoNetWorkDlg(getActivity());
            return;
        }
        showProgressDialog(getActivity());
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("deliverUserId", ConfigManager.instance().getUserID());
        valuePairs.put("id", orderId);
        Gson gson = new Gson();
        Map<String, String> postMap = new HashMap<>();
        postMap.put("json", gson.toJson(valuePairs));
        DataRequest.instance().request(getActivity(), Urls.getTakemealConfirmUrl(), this, HttpRequest.POST, PICK_UP_REQUEST, postMap, new ResultHandler());
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (null != unbinder)
        {
            unbinder.unbind();
            unbinder = null;
        }

        mHandler.removeCallbacksAndMessages(null);
    }


    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        hideProgressDialog(getActivity());
        if (mRefreshStatus == 1)
        {
            mPullToRefreshRecyclerView.onPullUpRefreshComplete();
        }
        else
        {
            mPullToRefreshRecyclerView.onPullDownRefreshComplete();
        }

        if (GET_ORDER_REQUEST.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (PICK_UP_REQUEST.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(ROB_ORDER_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(ROB_ORDER_FAIL, resultMsg));
            }
        }

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView)
    {
        pn = 1;
        mRefreshStatus = 0;
        loadData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView)
    {
        pn += 1;
        mRefreshStatus = 1;
        loadData();
    }

}
