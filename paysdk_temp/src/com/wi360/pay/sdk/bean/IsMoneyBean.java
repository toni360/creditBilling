package com.wi360.pay.sdk.bean;

import android.content.Context;

import com.wi360.pay.sdk.base.BaseBean;

public class IsMoneyBean extends BaseBean {
	public String mobileNum;
	public String appId;

	public IsMoneyBean(Context context, String mobileNum) {
		this.mobileNum = mobileNum;
		this.appId = "GM01";
//		this.appId = getAppId(context);
	}
}
