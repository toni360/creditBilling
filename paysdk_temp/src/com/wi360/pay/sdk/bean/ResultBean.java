package com.wi360.pay.sdk.bean;

import java.io.Serializable;

public class ResultBean implements Serializable {

	public int errcode;
	public String errmsg;
	public String token;
	public String mobileNum;

	public Credit credit;
	public Order order;

	public class Credit {
		// 信用额度
		public int creditLimit;
		// 已使用信用
		public int usedCredit;
		public String mobileNum;
	}

	// 支付订单接口
	public class Order {
		public String orderId;
		public int isDisplay;
		public String sellerName;
		public String appName;
		public String productName;
		public int sum;
		public String payTime;
		public String status;
		public String buyerId;
		public String appId;
	}

	// 信用额度
	public int creditLimit;
	// 已使用信用
	public int usedCredit;
	/**
	 * 4.2.11 判断用户是否有到期欠款接口
	 */
	public boolean isBalanceDue;
	/**
	 * 4.2.12 判断用户是否有到期欠款接口
	 * 
	 * 该接口仅用SDK向计费系统查询某个用户是否有到期欠款。
	 */
	// public boolean isBalanceDue;
	public String url;
	public int sum;

}
