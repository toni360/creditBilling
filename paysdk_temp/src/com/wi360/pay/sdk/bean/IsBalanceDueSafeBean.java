package com.wi360.pay.sdk.bean;

import android.content.Context;

import com.wi360.pay.sdk.base.BaseBean;

public class IsBalanceDueSafeBean extends BaseBean {
	public String appId;// 是AUTH系统统一分配的APPID
	public String timeStamp;// 是AUTH系统统一分配的APPID
	public String nonce;// 是AUTH系统统一分配的APPID
	public String token;// 是AUTH系统统一分配的APPID
	public String signature;// 是AUTH系统统一分配的APPID
	public String mobileNum;// 是AUTH系统统一分配的APPID

	public IsBalanceDueSafeBean(Context context) {
		// this.appId = getAppId(context);
		// this.appId = "GM01";
		// this.appId = "zjhtwallet";
		this.appId = getAppId(context);
		this.timeStamp = System.currentTimeMillis() + "";
		this.nonce = genRandNum(12) + "";
		this.token = getToken(context);
		// String appkey = "wcMZS2ltxNcrtesPmm9uRdL0cU8=";
		this.signature = getMD5Str(appId + getAppKey(context) + nonce + timeStamp + token);
		this.mobileNum = getMobileNum(context);
	}
}
