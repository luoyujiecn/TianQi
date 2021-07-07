package com.tianqi.app.fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import com.tianqi.app.R;
import com.tianqi.app.base.BaseFragment;


public class Fragment1 extends BaseFragment{

    View view;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        view = inflater.inflate(R.layout.fragment1, container, false);

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
