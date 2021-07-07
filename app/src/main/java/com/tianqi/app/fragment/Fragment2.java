package com.tianqi.app.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tianqi.app.R;
import com.tianqi.app.base.BaseFragment;

public class Fragment2 extends BaseFragment {
    View view;


    public static   Fragment2 newInstance() {
        Bundle args = new Bundle();
          Fragment2 fragment = new   Fragment2();
        return fragment;
    }
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        view = inflater.inflate(R.layout.fragment2, container, false);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}