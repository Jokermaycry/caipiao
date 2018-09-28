package com.ailide.apartmentsabc.framework.views;

/**
 * 数字动画自定义
 *
 * @author zengtao 2015年7月17日 上午11:48:27
 *
 */
public interface RiseNumberBase {
	public void start();

	public RiseNumberTextView withNumber(float number);

	public RiseNumberTextView withNumber(float number, boolean flag);

	public RiseNumberTextView withNumber(int number);

	public RiseNumberTextView setDuration(long duration);

	public void setOnEnd(RiseNumberTextView.EndListener callback);
}
