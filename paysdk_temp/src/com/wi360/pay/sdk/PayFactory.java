package com.wi360.pay.sdk;

import android.app.Activity;

import com.wi360.pay.sdk.base.QidaDialog;

/**
 * 支付工厂,通过PayFactory.getInstance(Activity context)
 * 
 * @author Administrator
 * 
 */
public class PayFactory {
	/**
	 * 
	 * @param context
	 *            :上下文环境(Activity context)
	 * @return PayController(支付控制器)
	 */
	public static PayController getInstance(Activity context) {
		int layouId = context.getResources().getIdentifier("dialog_pay1", "layout", context.getPackageName());
		int styleId = context.getResources().getIdentifier("QidaDialog", "style", context.getPackageName());
		QidaDialog dialog = new QidaDialog(context, layouId, styleId,null);
		PayController payController = new PayController(context, dialog, dialog.view);
		return payController;
	}
}
