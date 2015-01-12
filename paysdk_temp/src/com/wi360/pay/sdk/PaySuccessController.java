package com.wi360.pay.sdk;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.wi360.pay.sdk.base.QidaDialog;
import com.wi360.pay.sdk.bean.ResultBean;
import com.wi360.pay.sdk.interfaces.ResponseCallback;
import com.wi360.pay.sdk.util.CommonUtil;
import com.wi360.pay.sdk.util.Constants;

/**
 * 支付成功dialog
 * 
 * @author Administrator
 * 
 */
public class PaySuccessController {
	private View view;
	private Activity context;

	private TextView tv_name;

	private TextView tv_momey;

	private TextView tv_date;

	private Button bt_submit;

	private QidaDialog dialog;
	private ResultBean resBean;
	private TextView tv_order_num;
	private TextView tv_state;

	private ResponseCallback responseCallback;

	public PaySuccessController(Activity context, final Dialog dialog, View view, ResponseCallback responseCallback) {
		this.context = context;
		this.dialog = (QidaDialog) dialog;
		this.view = view;
		this.responseCallback = responseCallback;

		dialog.show();
		int tv_title_id = CommonUtil.getResourcesId(context, "tv_title", "id");
		TextView tv_title = (TextView) this.view.findViewById(tv_title_id);
		tv_title.setText("博升信用支付");
		int tv_name_id = CommonUtil.getResourcesId(context, "tv_name", "id");
		int tv_momey_id = CommonUtil.getResourcesId(context, "tv_momey", "id");
		int tv_order_num_id = CommonUtil.getResourcesId(context, "tv_order_num", "id");
		int tv_date_id = CommonUtil.getResourcesId(context, "tv_date", "id");
		int tv_state_id = CommonUtil.getResourcesId(context, "tv_state", "id");
		int bt_submit_id = CommonUtil.getResourcesId(context, "bt_submit", "id");
		int bt_cancel_id = CommonUtil.getResourcesId(context, "bt_cancel", "id");

		tv_name = (TextView) this.view.findViewById(tv_name_id);
		tv_momey = (TextView) this.view.findViewById(tv_momey_id);
		tv_order_num = (TextView) this.view.findViewById(tv_order_num_id);
		tv_date = (TextView) this.view.findViewById(tv_date_id);
		tv_state = (TextView) this.view.findViewById(tv_state_id);

		bt_submit = (Button) this.view.findViewById(bt_submit_id);
		Button bt_cancel = (Button) this.view.findViewById(bt_cancel_id);

		bt_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PaySuccessController.this.dialog.onClickCancleBut(Constants.pay_code_success);
				dialog.dismiss();
			}
		});
		// 点击取消
		bt_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
				PaySuccessController.this.dialog.onClickCancleBut(Constants.pay_code_success);
			}
		});
	}

	public void setData(ResultBean resbBean) {
		this.resBean = resbBean;
		tv_name.setText(resbBean.order.productName);
		// 信用额度保留两位小数
		tv_momey.setText(String.format("%.2f", resbBean.order.sum * 0.01) + "元");
		if (resbBean.order.orderId != null) {
			tv_order_num.setText(resbBean.order.orderId);
		} else {

			view.findViewById(CommonUtil.getResourcesId(context, "ll_order_num", "id")).setVisibility(View.GONE);
			view.findViewById(CommonUtil.getResourcesId(context, "view_line_hidden", "id")).setVisibility(View.GONE);

		}
		tv_date.setText(resbBean.order.payTime);
		tv_state.setText(resbBean.order.status);
	}

}
