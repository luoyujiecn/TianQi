package com.tianqi.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tianqi.app.base.BaseActivity;
import com.tianqi.app.fragment.Fragment1;
import com.tianqi.app.fragment.Fragment2;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    RadioButton rb1;
    RadioButton rb2;
    private Fragment[] mFragments;
    private String[] tags;
    private int mIndex;

    private static Boolean isQuit = false;
    Timer timer = new Timer();

    private String type;
    private String[] permissions =
            {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_EXTERNAL_STORAGE};
    List<String> mPermissionList = new ArrayList<>();
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initPermission();
    }
    private void initView() {
        rb1 = findViewById(R.id.rb_1);
        rb2 = findViewById(R.id.rb_2);
        rb1.setOnClickListener(this);
        rb2.setOnClickListener(this);
        Fragment1 fg1 = new Fragment1();
        Fragment2 fg2 = Fragment2.newInstance();

        //添加到数组
        mFragments = new Fragment[]{fg1, fg2};
        tags = new String[]{"fg1", "fg2"};
        //开启事务
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        //添加首页
        ft.add(R.id.fragment, fg1, tags[0]).commit();

        setIndexSelected(0);
    }
    @SuppressLint("NonConstantResourceId")
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rb_1:
                setIndexSelected(0);
                break;
            case R.id.rb_2:
                setIndexSelected(1);
                break;
        }
    }
    //方法一，选中显示与隐藏
    private void setIndexSelected(int index) {
        switch (index) {
            case 0:
                rb1.setTextSize(13);
                rb2.setTextSize(12);
                rb1.setSelected(true);
                rb2.setSelected(false);
                break;
            case 1:
                rb1.setTextSize(12);
                rb2.setTextSize(13);
                rb1.setSelected(false);
                rb2.setSelected(true);
                break;
        }
        if (mIndex == index) {
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        //隐藏
        ft.hide(mFragments[mIndex]);
        //判断是否添加
        if (!mFragments[index].isAdded() && null == fragmentManager.findFragmentByTag(tags[index])) {
            ft.add(R.id.fragment, mFragments[index], tags[index]).show(mFragments[index]);
        } else {
            ft.show(mFragments[index]);
        }
        ft.commit();
        //再次赋值
        mIndex = index;
    }
    private void initPermission() {
        mPermissionList.clear();//清空没有通过的权限
        //逐个判断你要的权限是否已经通过
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);//添加还未授予的权限
            }
        }
        //申请权限
        if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
            ActivityCompat.requestPermissions(this, permissions, 321);
        } else {
            //说明权限都已经通过，可以做你想做的事情去
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //TODO 当权限拒绝的时候需要提示 需要此权限。
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}