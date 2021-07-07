package com.tianqi.app.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentActivity;


import com.tianqi.app.R;
import com.tianqi.app.manager.AppManager;
import com.tianqi.app.utils.view.LoadingView;
import com.tianqi.app.utils.view.TipsToast;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Stack;

public class BaseActivity extends FragmentActivity {

    private Dialog dialog;
    private TipsToast toast;
    private Stack activityStack = new Stack();
    private static final int NOT_NOTICE = 2;//如果勾选了不再询问
    private AlertDialog alertDialog;
    private AlertDialog mDialog;
    private String[] permissionString;
    private static Boolean isQuit = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStatusBarColor(this, R.color.white);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//黑色通知栏背景字体色
            }
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /*
     * 改变状态栏颜色
     * */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setStatusBarColor(Activity activity, int statusColor) {
        Window window = activity.getWindow();
        //取消状态栏透明
        // window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//设置statusbar应用所占的屏幕扩大到全屏，但是最顶上会有背景透明的状态栏，它的文字可能会盖着你的应用的标题栏
        //设置状态栏颜色
        window.setStatusBarColor(getResources().getColor(statusColor));
        //设置系统状态栏处于可见状态
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        //让view不根据系统窗口来调整自己的布局
        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
            ViewCompat.requestApplyInsets(mChildView);
        }
    }

    /**
     * 裁剪
     *
     * @param uri
     * @param outputX
     * @param outputY
     * @param requestCode
     */
    public void cropPhoto(Uri uri, Uri uritempFile, int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        //uritempFile为Uri类变量，实例化uritempFile
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);//no face detection
        startActivityForResult(intent, requestCode);
    }

    /**
     * 通过设置全屏，设置状态栏透明
     */
    public void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            decorView.setSystemUiVisibility(option);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                try {
                    Class decorViewClazz = Class.forName("com.android.internal.policy.DecorView");
                    Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");
                    field.setAccessible(true);
                    field.setInt(window.getDecorView(), Color.TRANSPARENT);  //改为透明
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Window window = activity.getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            int flagTranslucentStatus = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            }
            attributes.flags |= flagTranslucentStatus;
            window.setAttributes(attributes);
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//解决华为手机等状态栏上面有一个蒙层问题
//            try {
//                @SuppressLint("PrivateApi") Class decorViewClazz = Class.forName("com.android.internal.policy.DecorView");
//                Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");
//                field.setAccessible(true);
//                field.setInt(getWindow().getDecorView(), Color.TRANSPARENT);  //改为透明
//            } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException e) {
//                e.printStackTrace();
//            }
//        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 界面跳转
     */
    public void goToActivity(Class clazz) {
        startActivity(new Intent(this, clazz));
        //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    /**
     * 界面跳转
     */
    public void goToActivity(Class clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    /**
     * 界面跳转
     */
    public void goToActivity(Class clazz, Bundle bundle, int requestcode) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestcode);
        //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    /**
     * 显示加载框
     */
    public void showLoading(String message) {
        if (dialog == null) {
            dialog = LoadingView.createLoadingDialog(this, message);
        }
    }

    /**
     * 显示加载框
     */
    public void showLoading(int message) {
        if (dialog == null) {
            dialog = LoadingView.createLoadingDialog(this, getString(message));
        }
    }

    /**
     * 隐藏加载框
     */
    public void hideLoading() {
        if (dialog != null) {
            LoadingView.closeDialog(dialog);
            dialog = null;
        }
    }

    /**
     * 隐藏加载框
     */
    public void hideLoading(String mes) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                if (dialog != null) {
                    LoadingView.closeDialog(dialog);
                    dialog = null;
                }
                showToast(mes);
                Looper.loop();
            }
        }).start();
    }

    /**
     * 隐藏加载框
     */
    public void hideLoadingSleep() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                if (dialog != null) {
                    LoadingView.closeDialog(dialog);
                    dialog = null;
                }
            }
        }).start();
    }

    public void hideLoadingSleep(int millis) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(millis);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                if (dialog != null) {
                    LoadingView.closeDialog(dialog);
                    dialog = null;
                }
            }
        }).start();
    }


//    public void showToast(String msg) {
//        if (toast == null) {
//            toast = new TipsToast(this);
//        }
//        toast.setText(msg);
//        toast.show();
//    }

    public void showImageToast(String msg, int ig) {
        if (toast == null) {
            toast = TipsToast.newInstance(this);
        }
        toast.setText(msg);
        toast.setImage(ig);
        toast.show();
    }

    public void showToast(String msg) {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        toast = TipsToast.newInstance(this);
        toast.setText(msg);
        toast.show();
    }

    public void showToast(String msg, boolean isToastNull) {
        if (toast != null) {
            if (isToastNull) {
                toast.cancel();
                toast = null;
            }
            showToast(msg);
        } else {
            showToast(msg);
        }
        toast.show();
    }

    /**
     * 隐藏toast
     */
    public void hideToast() {
        if (toast != null) {
            toast.cancel();
        }
        toast = null;
    }

    //隐藏软键盘
    public void hiddenSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(this).getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindow().getDecorView().getWindowToken(), 0);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isFastDoubleClick()) {
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    private long lastClickTime = 0;

    public boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        lastClickTime = time;
        return timeD <= 300;
    }

    public void finishThis() {
        AppManager.getAppManager().finishActivity();
    }

    public void finishOther(Activity activity) {
//        goToActivity(LoginActivity.class);
        //获取栈中所有activity，遍历关闭除登录界面以外的所有界面
//        if(AppManager.activityStack != null){
//            for(Activity act:AppManager.activityStack){
//                if(act.getClass() != LoginActivity.class ){
//                    act.finish();
////                    AppManager.getAppManager().finishActivity(act);
//                }
//            }
//        }
    }

    @Override
    public void finishActivity(int requestCode) {
        super.finishActivity(requestCode);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finishThis();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hideToast();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        BaseRequest.getInstance(this,false).cancelTag(this);
        hideLoading();
        hideToast();
    }


    /**
     * 收起键盘
     */
    public void packUpKeyboard() {
        InputMethodManager manager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null)
            manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
