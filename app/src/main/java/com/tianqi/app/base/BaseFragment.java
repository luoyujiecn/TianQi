package com.tianqi.app.base;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.tianqi.app.R;
import com.tianqi.app.utils.view.LoadingView;
import com.tianqi.app.utils.view.TipsToast;

import java.io.File;
import java.util.Objects;




public abstract class BaseFragment extends Fragment {

    private Dialog dialog;
    private TipsToast toast;
    public abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle bundle);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        return initView(inflater, container, bundle);
    }
    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }
    /**
     * 跳转界面
     */
    public void goToActivity(Class clazz) {
        startActivity(new Intent(getActivity(), clazz));
        //getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    /**
     * 跳转界面
     */
    public void goToActivityA(Class clazz) {
        startActivity(new Intent(getActivity(), clazz));
        getActivity().overridePendingTransition(R.anim.normal_dialog_enter, R.anim.normal_dialog_exit);
    }

    /**
     * 跳转界面
     */
    public void goToActivity(Class clazz, Bundle bundle) {
        Intent intent = new Intent(getContext(), clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        //getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    /**
     * 跳转界面
     */
    public void goToActivity(Class clazz, Bundle bundle, int code) {
        Intent intent = new Intent(getContext(), clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, code);
        //getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    /**
     * 显示加载框
     */
    public void showLoading(String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                if (dialog == null) {
                    dialog = LoadingView.createLoadingDialog(getActivity(), message);
                }
                Looper.loop();
            }
        }).start();

    }

    /**
     * 显示加载框
     */
    public void showLoading(int message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                if (dialog == null) {
                    dialog = LoadingView.createLoadingDialog(getActivity(), getString(message));
                }
                Looper.loop();
            }
        }).start();

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

    public void showToast(String msg) {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        toast = TipsToast.newInstance(getContext());
        toast.setText(msg);
        toast.show();
    }
    public void showToast(String msg ,boolean isToastNull) {
        if (toast != null) {
            if(isToastNull){
                toast.cancel();
                toast = null;
            }
            showToast(msg);
        }else{
            showToast(msg);
        }
        toast.show();
    }
    /**
     * 隐藏toast
     */
    public void hideToast(){
        if (toast != null) {
            toast.cancel();
        }
        toast=null;
    }
    //隐藏软键盘
    public void hiddenSoftKeyboard(){
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
    }

    public  void openBrowser(Context context, String url) {
        if(TextUtils.isEmpty(url)){
            url = "https://www.baidu.com/";
        }else {
            final Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            // 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名
            // 官方解释 : Name of the component implementing an activity that can display the intent
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                final ComponentName componentName = intent.resolveActivity(context.getPackageManager());
                context.startActivity(Intent.createChooser(intent, "请选择浏览器"));
            } else {
//                showToast(getString(R.string.download_browser));
            }
        }
    }
    /**
     * 复制内容到剪贴板
     *
     * @param content
     * @param context
     */
    public void copyContentToClipboard(String content, Context context) {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText(null, content);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        showToast("复制成功");
    }

    /**
     * 根据路径获取当前文件
     * @param path
     * @return
     */
    public File getFile(String path){
        File f = new File(path);
        if (f.exists()) {
        }else {
            showToast("备份异常");

            return null;
        }
        return f;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
//        BaseRequest.getInstance(getActivity(),false).cancelTag(this);
        hideLoading();
        hideToast();
    }

}
