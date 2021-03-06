package com.example.wall_e.multistatuslayout;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wall_e.multiStatusLayout.MultiStatusLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmptyFragment extends BaseFragment {


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            statusLayout.showEmpty();
        }
    };
    MultiStatusLayout statusLayout;
    private boolean hasLoadFinish;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_empty, container, false);
        statusLayout = view.findViewById(R.id.statusLayout);
        isPrepared =true;
        lazyLoad();
        return view;
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || hasLoadFinish)
            return;
        hasLoadFinish = true;
        statusLayout.showLoading();
        handler.sendEmptyMessageDelayed(1, 2000);
    }
}
