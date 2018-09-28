package com.daotian.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 
 *	@author
 *
 */
public class ExpandGridView extends GridView {

	public ExpandGridView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public ExpandGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ExpandGridView(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
