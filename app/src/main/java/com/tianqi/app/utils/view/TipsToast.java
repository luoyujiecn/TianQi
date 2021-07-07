package com.tianqi.app.utils.view;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tianqi.app.R;


/**
 * Created by xionghao on 15/11/30.
 */
public class TipsToast extends Toast {

    private TextView textView;
    private ImageView image;
    private Context context;
    private static TipsToast result;

    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    
    public static TipsToast newInstance(Context context) {
        TipsToast toast = new TipsToast(context);
        return toast;
    }
    private TipsToast(Context context) {
        super(context);
        this.context = context;
        setGravity(Gravity.CENTER, 0, 0);
        View view = LayoutInflater.from(context).inflate(R.layout.totaslayout, null);
        textView = (TextView) view.findViewById(R.id.tips_text);
        image = (ImageView) view.findViewById(R.id.tips_image);
        setView(view);
        setDuration(Toast.LENGTH_SHORT);
    }

    public static TipsToast makeText(Context context, CharSequence text) {
        if (result == null) {
            result = new TipsToast(context);
        }
        result.setText(text);
        return result;
    }

    public static TipsToast makeText(Context context, int text) {
        if (result == null) {
            result = new TipsToast(context);
        }
        result.setText(text);
        return result;
    }

    public static TipsToast makeText(Context context, CharSequence text, int duration) {
        if (result == null) {
            result = new TipsToast(context);
        }
        result.setText(text);
        return result;
    }

    public static TipsToast makeText(Context context, int text, int duration) {
        if (result == null) {
            result = new TipsToast(context);
        }
        result.setText(text);
        return result;
    }
    public void setImage(int ig){
        image.setVisibility(View.VISIBLE);
        image.setImageResource(ig);
    }

    @Override
    public void setText(int resId) {
        textView.setText(context.getString(resId));
    }

    @Override
    public void setText(CharSequence s) {
        textView.setText(s!= null?s.toString():"");
    }
}
