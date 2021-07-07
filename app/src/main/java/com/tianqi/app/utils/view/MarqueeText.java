package com.tianqi.app.utils.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class MarqueeText extends TextView {

    public MarqueeText(Context context) {
        super(context);

    }

    public MarqueeText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public MarqueeText(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public boolean isFocused() {
        return true;//返回true 使得其有textview具有Focused属性
    }
}
