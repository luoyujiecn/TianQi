package com.tianqi.app.utils.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianqi.app.R;


public class LoadingView {
    public interface ShowLoading{
        void isShowLoad(Boolean b);
    }
    public interface RefreshHousing{
        void dismiss();
      
    }
    static TextView tipTextView;

    public static void setMsg(String msg) {
        if(tipTextView != null){
            tipTextView.setText(msg);
        }
    }

    public static Dialog createLoadingDialog(Context context, String msg) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v ;// 得到加载view

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            v = inflater.inflate(R.layout.load_view, null);
        }else{
            v = inflater.inflate(R.layout.load_view1, null);
        }
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_loading_view);// 加载布局
        tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        tipTextView.setText(msg);// 设置加载信息

        Dialog loadingDialog = new Dialog(context, R.style.NormalDialogStyle);// 创建自定义样式dialog
        loadingDialog.setCancelable(false); // 是否可以按“返回键”消失
        loadingDialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域
        loadingDialog.getWindow().setDimAmount(0f);
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));// 设置布局
        /**
         *将显示Dialog的方法封装在这里面
         */
        Window window = loadingDialog.getWindow();
//        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
//        window.setAttributes(lp);
        window.setBackgroundDrawableResource(R.color.bg_white);
        window.setWindowAnimations(R.style.normalDialogAnim);
        loadingDialog.show();

        return loadingDialog;
    }

    /**
     * 关闭dialog
     *
     * http://blog.csdn.net/qq_21376985
     *
     * @param mDialogUtils
     */
    public static void closeDialog(Dialog mDialogUtils) {
        if (mDialogUtils != null && mDialogUtils.isShowing()) {
            mDialogUtils.dismiss();
        }
    }

}