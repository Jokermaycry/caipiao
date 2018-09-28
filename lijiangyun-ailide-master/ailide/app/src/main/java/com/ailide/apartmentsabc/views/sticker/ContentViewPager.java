package com.ailide.apartmentsabc.views.sticker;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class ContentViewPager extends ViewPager {

    private boolean noScroll = false;

    public ContentViewPager(Context context) {
        super(context);
    }

    public ContentViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int indent = getCurrentItem();//当前显示fragment下标
        int weights = 0;
        Fragment fragment = ((FragmentPagerAdapter)getAdapter()).getItem(indent);
        if(fragment.getView()!= null){
            View view = fragment.getView();
            view.measure(widthMeasureSpec , MeasureSpec.makeMeasureSpec(0 , MeasureSpec.UNSPECIFIED));
            weights = view.getMeasuredHeight();
        }else{
//            Toast.makeText(getContext() , "我是空的fragment" , Toast.LENGTH_SHORT).show();
        }
        weights = MeasureSpec.makeMeasureSpec(weights , MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, weights);
    }

    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (noScroll)
            return true;
        else
            return super.onTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (noScroll)
            return false;
        else
            return super.onInterceptTouchEvent(arg0);
    }
}