package com.jyq.qs.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.jyq.qs.MyApplication;
import com.jyq.qs.R;
import com.jyq.qs.adapter.OrderAdapter1;
import com.jyq.qs.bean.OrderInfo;
import com.jyq.qs.http.DataRequest;
import com.jyq.qs.http.HttpRequest;
import com.jyq.qs.http.IRequestListener;
import com.jyq.qs.json.OrderListHandler;
import com.jyq.qs.json.ResultHandler;
import com.jyq.qs.listener.MyItemClickListener;
import com.jyq.qs.utils.ConfigManager;
import com.jyq.qs.utils.ConstantUtil;
import com.jyq.qs.utils.NetWorkUtil;
import com.jyq.qs.utils.ToastUtil;
import com.jyq.qs.utils.Urls;
import com.jyq.qs.widget.VerticalSwipeRefreshLayout;
import com.jyq.qs.widget.list.refresh.PullToRefreshBase;
import com.jyq.qs.widget.list.refresh.PullToRefreshRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class OrderFragment1 extends BaseFragment implements PullToRefreshBase.OnRefreshListener<RecyclerView>, IRequestListener,SwipeRefreshLayout.OnRefreshListener
{
    @BindView(R.id.refreshRecyclerView)
    PullToRefreshRecyclerView mPullToRefreshRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    VerticalSwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.ll_no_order)
    LinearLayout mNoOrderLayout;
    private View rootView = null;
    private Unbinder unbinder;
    private RecyclerView mRecyclerView;

    private int pn = 1;
    private int mRefreshStatus;
    private List<String> oldOrderIdList = new ArrayList<>();
    private List<String> newOrderIdList = new ArrayList<>();
    private List<OrderInfo> orderInfoList = new ArrayList<>();
    private OrderAdapter1 mAdapter;

    private static final String ROB_ORDER_REQUEST = "rob_order_request";
    private static final String GET_ORDER_REQUEST = "get_order_request_1";
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


                    if (!newOrderInfoList.isEmpty() && !orderInfoList.isEmpty())
                    {
                        //                        if (orderInfoList.isEmpty() ||
                        // !newOrderInfoList.get(0).getId().equals(orderInfoList.get(0).getId
                        // ()))
                        //                    {
                        //                        //提示音
                        //                        playVoice(getActivity());
                        //
                        //                    }

                        oldOrderIdList.clear();
                        newOrderIdList.clear();

                        for (int i = 0; i < orderInfoList.size(); i++)
                        {
                            oldOrderIdList.add(orderInfoList.get(i).getId());
                        }
                        for (int i = 0; i < newOrderInfoList.size(); i++)
                        {
                            newOrderIdList.add(newOrderInfoList.get(i).getId());
                        }


                        for (int j = 0; j < newOrderIdList.size(); j++)
                        {
                            if (!oldOrderIdList.contains(newOrderIdList.get(j)))
                            {
                                playVoice(getActivity());
                                break;
                            }

                        }

                    }
                    else if (newOrderInfoList.size() > oldOrderIdList.size())
                    {
                        playVoice(getActivity());
                    }

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

                    ToastUtil.show(getActivity(), "抢单成功");
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

                    mHandler.sendEmptyMessageDelayed(GET_ORDER_LIST, 30 * 1000);


                    break;


            }
        }
    };

    @Override
    public void onResume()
    {
        super.onResume();
    }

    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
        {
            //相当于Fragment的onResume
            mHandler.sendEmptyMessage(GET_ORDER_LIST);
        }
        else
        {
            //相当于Fragment的onPause
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if (rootView == null)
        {
            rootView = inflater.inflate(R.layout.fragment_order1, null);
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

    private static OrderFragment1 instance = null;

    public static OrderFragment1 newInstance()
    {
        if (instance == null)
        {
            instance = new OrderFragment1();
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
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void initViewData()
    {
        mPullToRefreshRecyclerView.setPullLoadEnabled(true);
        mRecyclerView = mPullToRefreshRecyclerView.getRefreshableView();
        mPullToRefreshRecyclerView.setOnRefreshListener(this);
        mPullToRefreshRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        mAdapter = new OrderAdapter1(orderInfoList, getActivity(), new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                if (MyApplication.getInstance().isOnline())
                {

                    robOrder(orderInfoList.get(position).getId());
                }
                else
                {
                    ToastUtil.show(getActivity(), "请先进行上线操作");
                }
            }
        });

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                mSwipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
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

           // NetWorkUtil.showNoNetWorkDlg(getActivity());
            ToastUtil.show(getActivity(),"请检查网络是否可用");
            return;
        }
        Map<String, Integer> valuePairs = new HashMap<>();
        valuePairs.put("pageNum", pn);
        valuePairs.put("pageSize", 15);
        Gson gson = new Gson();
        Map<String, String> postMap = new HashMap<>();
        postMap.put("json", gson.toJson(valuePairs));
        DataRequest.instance().request(getActivity(), Urls.getOrderListUrl(), this, HttpRequest.POST, GET_ORDER_REQUEST, postMap, new
                OrderListHandler());
    }


    private void robOrder(String orderId)
    {
        if (!NetWorkUtil.isConn(getActivity()))
        {
            NetWorkUtil.showNoNetWorkDlg(getActivity());
            return;
        }
        showProgressDialog(getActivity());
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("deliverUserId", ConfigManager.instance().getUserID());
        valuePairs.put("orderId", orderId);
        Gson gson = new Gson();
        Map<String, String> postMap = new HashMap<>();
        postMap.put("json", gson.toJson(valuePairs));
        DataRequest.instance().request(getActivity(), Urls.getRobOrderUrl(), this, HttpRequest.POST, ROB_ORDER_REQUEST, postMap, new ResultHandler());
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
        else if (ROB_ORDER_REQUEST.equals(action))
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

    private static MediaPlayer mediaPlayer;

    public static void playVoice(Context context)
    {
        try
        {
            if (null == mediaPlayer)
            {
                mediaPlayer = MediaPlayer.create(context, R.raw.order);
            }
            mediaPlayer.start();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh()
    {
        if (mSwipeRefreshLayout != null)
        {
            pn = 1;
            mRefreshStatus = 0;
            loadData();
            mSwipeRefreshLayout.post(new Runnable()
            {
                @Override
                public void run()
                {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    }
}
