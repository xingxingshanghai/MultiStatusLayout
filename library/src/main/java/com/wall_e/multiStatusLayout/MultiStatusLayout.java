package com.wall_e.multiStatusLayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.RelativeLayout;


/**
 * Created by Wall-E on 2017/4/14.
 */

public class MultiStatusLayout extends RelativeLayout {


    private Context mContext;
    /**
     * 默认的layout
     */
    private static final int DEFAULT_LAYOUT = R.layout.default_layout;

    /**
     * 网络错误的layout
     */
    private int mNetErrorLayout = DEFAULT_LAYOUT;

    /**
     * 加载中的layout
     */
    private int mLoadingLayout = DEFAULT_LAYOUT;

    /**
     * 没有数据layout
     */
    private int mEmptyLayout = DEFAULT_LAYOUT;

    /**
     * 数据错误layout
     */
    private int mErrorLayout = DEFAULT_LAYOUT;
    /**
     * 扩充layout
     */
    private int mOtherLayout = DEFAULT_LAYOUT;
    /**
     * 布局中的不隐藏的View的id
     */
    private int mTargetViewId = -999;

    /**
     * 重新加载的监听
     */
    private OnReloadDataListener onReloadDataListener;

    private int mViewType = -1;


    /**
     * 放置其他五种状态的索引容器
     */
    private View[] mContentViews = new View[5];


    private final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


    public MultiStatusLayout(Context context) {
        this(context, null);
    }

    public MultiStatusLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiStatusLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MultiStatusLayout, defStyleAttr, 0);
        mNetErrorLayout = array.getResourceId(R.styleable.MultiStatusLayout_netErrorLayout, mNetErrorLayout);
        mLoadingLayout = array.getResourceId(R.styleable.MultiStatusLayout_loadingLayout, mLoadingLayout);
        mErrorLayout = array.getResourceId(R.styleable.MultiStatusLayout_errorLayout, mErrorLayout);
        mEmptyLayout = array.getResourceId(R.styleable.MultiStatusLayout_emptyLayout, mEmptyLayout);
        mOtherLayout = array.getResourceId(R.styleable.MultiStatusLayout_otherLayout, mOtherLayout);
        mTargetViewId = array.getResourceId(R.styleable.MultiStatusLayout_targetViewId, mTargetViewId);
        array.recycle();
    }


    /**
     * 显示此View,如果view==null的时候才去加载这个布局
     *
     * @param layoutId 布局id
     * @param index 存放布局容器中的索引
     */
    private void showView(int layoutId, int index) {
        //如果子控件处于显示状态先隐藏所有的子控件
        hideViews();
        View view = mContentViews[index];
        if (view==null){
            view = inflateAndAddViewInLayout(null,index,layoutId);
        }else {
            view.setVisibility(VISIBLE);
        }
        //设置网络错误或者数据错误布局的点击事件
        if (index == 2 || index == 4) {
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onReloadDataListener != null) onReloadDataListener.reloadData();
                }
            });
        }
    }


    /**
     * 显示扩充布局
     */
    public void showOther() {
        if (mViewType == 0)
            return;
        mViewType = 0;
        //显示加载中的布局
        showView(mOtherLayout,0);
    }

    /**
     * 显示加载中布局
     */
    public void showLoading() {
        if (mViewType == 1)
            return;
        mViewType = 1;
        //显示加载中的布局
        showView(mLoadingLayout, 1);
    }

    /**
     * 显示网络错误的布局
     */
    public void showNetError() {
        if (mViewType == 2)
            return;
        mViewType = 2;
        showView(mNetErrorLayout, 2);
    }

    /**
     * 列表数据为空时显示此布局
     */
    public void showEmpty() {
        if (mViewType == 3)
            return;
        mViewType = 3;
        showView(mEmptyLayout, 3);
    }


    /**
     * 加载数据错误时显示此布局
     */
    public void showError() {
        if (mViewType == 4)
            return;
        mViewType = 4;
        showView(mErrorLayout, 4);
    }


    /**
     * 隐藏所有的View
     */
    private void hideViews() {
        int count= getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (mTargetViewId != view.getId() && view.getVisibility() != GONE && !(view instanceof ViewStub)) {
                view.setVisibility(GONE);
            }
        }
    }


    /**
     * 显示
     */
    public void showContent() {
        if (mViewType == 5)
            return;
        mViewType = 5;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            view.setVisibility(VISIBLE);
        }
        for (View view:mContentViews){
            if (view!=null)
            view.setVisibility(GONE);
        }
    }


    /**
     * 获取扩展布局
     * @return 扩展布局
     */
    public View getOtherView() {
        View view = mContentViews[0];
        return inflateAndAddViewInLayout(view,0,mOtherLayout);
    }

    /**
     * 获取加载布局
     *
     * @return 加载布局
     */
    public View getLoadingView() {
        View view = mContentViews[1];
        return inflateAndAddViewInLayout(view,1,mLoadingLayout);
    }

    /**
     * 获取网络错误时的布局
     *
     * @return 网络错误时的布局
     */
    public View getNetErrorView() {
        View view = mContentViews[2];
        return inflateAndAddViewInLayout(view,2,mNetErrorLayout);
    }

    /**
     * 获取数据为空时的布局
     *
     * @return 数据为空时的布局
     */
    public View getEmptyView() {
        View view = mContentViews[3];
        return inflateAndAddViewInLayout(view,3,mEmptyLayout);
    }

    /**
     * 获取数据加载错误时的布局
     *
     * @return 数据加载错误时的布局
     */
    public View getErrorView() {
        View view = mContentViews[4];
        return inflateAndAddViewInLayout(view,4,mErrorLayout);
    }

    /**
     * 设置扩展布局的view
     * @param layoutResId 布局资源id
     */
    public void setOtherView(int layoutResId){
        mOtherLayout = layoutResId;
        inflateAndAddViewInLayout(null,0,layoutResId);
    }

    /**
     * 设置扩展布局的view
     * @param layoutResId 布局资源id
     */
    public void setLoadingView(int layoutResId){
        mLoadingLayout = layoutResId;
        inflateAndAddViewInLayout(null,1,layoutResId);
    }

    /**
     * 设置扩展布局的view
     * @param layoutResId 布局资源id
     */
    public void setNetErrorView(int layoutResId){
        mNetErrorLayout = layoutResId;
        inflateAndAddViewInLayout(null,2,layoutResId);
    }

    /**
     * 设置扩展布局的view
     * @param layoutResId 布局资源id
     */
    public void setEmptyView(int layoutResId){
        mEmptyLayout = layoutResId;
        inflateAndAddViewInLayout(null,3,layoutResId);
    }

    /**
     * 设置扩展布局的view
     * @param layoutResId 布局资源id
     */
    public void setErrorView(int layoutResId){
        mErrorLayout = layoutResId;
        inflateAndAddViewInLayout(null,4,layoutResId);
    }

    /**
     * 设置不隐藏的view id
     * @param targetViewId 不隐藏的view id
     */
    public void setTargetViewId(int targetViewId){
        mTargetViewId = targetViewId;
    }

    /**
     * 加载相应状态的布局，并且添加至ViewGroup中
     * @param index 存放布局容器的索引
     * @param layoutResId 布局资源id
     * @return 返回相应状态的布局
     */
    private View inflateAndAddViewInLayout(View view,int index,int layoutResId){
        if (view == null) {
            view = inflate(mContext, layoutResId, null);
            if (mTargetViewId != -999) {
                layoutParams.addRule(RelativeLayout.BELOW, mTargetViewId);
            }
            addView(view, layoutParams);
            mContentViews[index] = view;
        }
        return view;
    }
    /**
     * 获取当前显示的布局
     *  0：other
     *  1：loading
     *  2：net_error
     *  3：empty
     *  4：error
     *  5：content
     * @return 返回当前界面显示的状态
     */
    public int getShowViewType(){
        return mViewType;
    }


    /**
     * 重新加载数据
     */
    public void setOnReloadDataListener(final OnReloadDataListener onReloadDataListener) {
        this.onReloadDataListener = onReloadDataListener;
    }


}
