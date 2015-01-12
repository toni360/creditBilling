package com.wi360.pay.sdk.interfaces;


/*
 * 连接网络回调方法
 */
public abstract class MyRequestCallBack2 {
	public abstract void onFailure(Exception error, String msg);

	/**
	 * 成功返回json数据
	 * 
	 * @param responseInfo
	 * @return
	 */
	public abstract void onSuccess(int stateCode,String responseInfo);
}