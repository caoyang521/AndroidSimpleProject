package com.example.three.custionview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Zj on 2015/12/17.
 */
public class SimpleLayout extends ViewGroup {

    public SimpleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() > 0) {
            View childView = getChildAt(0);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() > 0) {
            View childView = getChildAt(0);
            Log.d("getwidth",String.valueOf(getWidth()));
            Log.d("getheight",String.valueOf(getHeight()));
            Log.d("getMeasuredWidth",String.valueOf(getMeasuredWidth()));
            Log.d("getMeasuredheight",String.valueOf(getMeasuredHeight()));
            childView.layout(0, 0, childView.getMeasuredWidth(), childView.getMeasuredHeight());
            Log.d("getwidth1",String.valueOf(getWidth()));
            Log.d("getheight1",String.valueOf(getHeight()));
            Log.d("getMeasuredWidth1",String.valueOf(getMeasuredWidth()));
            Log.d("getMeasuredheight1", String.valueOf(getMeasuredHeight()));
        }
    }

}
