package com.jyq.qs.widget.list.refresh;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

import com.jyq.qs.R;


public class PullToRefreshStaggeredGridLayout extends  PullToRefreshBase<RecyclerView>
{
    public PullToRefreshStaggeredGridLayout(Context context)
    {
        super(context);
    }

    public PullToRefreshStaggeredGridLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);

    }


    //2  滑动的View
    @Override
    protected RecyclerView createRefreshableView(Context context, AttributeSet attrs)
    {
        RecyclerView view = new RecyclerView(context, attrs);
        //设置布局方式，此处的2 表示每行2个item
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL);
        view.setLayoutManager(mLayoutManager);
        view.setId(R.id.straggereddGridLayout);
        return view;
    }

    @Override
    protected boolean isReadyForPullDown()
    {
        View view = getRefreshableView().getChildAt(getRefreshableView().getChildCount() - 1);
        if (null != view)
        {
            return getRefreshableView().getBottom() >= view.getBottom();
        }
        return false;
    }

    @Override
    protected boolean isReadyForPullUp()
    {
        View view = getRefreshableView().getChildAt(0);

        if (view != null)
        {
            return view.getTop() >= getRefreshableView().getTop();
        }
        return false;

    }


}