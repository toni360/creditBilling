package com.wi360.pay.sdk;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.wi360.pay.sdk.base.HttpUtils;
import com.wi360.pay.sdk.base.MyAsyncTask;
import com.wi360.pay.sdk.base.QidaDialog;
import com.wi360.pay.sdk.bean.GetPayBean;
import com.wi360.pay.sdk.bean.IsBalanceDueSafeBean;
import com.wi360.pay.sdk.bean.IsMoneyBean;
import com.wi360.pay.sdk.bean.PayOrderBean;
import com.wi360.pay.sdk.bean.ResultBean;
import com.wi360.pay.sdk.interfaces.BalanceDueCallback;
import com.wi360.pay.sdk.interfaces.MyRequestCallBack2;
import com.wi360.pay.sdk.interfaces.Pay;
import com.wi360.pay.sdk.interfaces.ResponseCallback;
import com.wi360.pay.sdk.util.CommonUtil;
import com.wi360.pay.sdk.util.Constants;
import com.wi360.pay.sdk.util.GsonTools;
import com.wi360.pay.sdk.util.SharedPreferencesUtils;
import com.wi360.pay.sdk.util.StringUtils;
import com.wi360.pay.sdk.util.UIUtils;

/**
 * 立即信用支付
 * 
 * @author Administrator
 * 
 */
public class PayController implements Pay {
	private String TAG = "PayController";
	private View view;
	private Activity context;

	private TextView tv_name;

	private TextView tv_momey;

	private TextView tv_desc;

	private Button bt_submit;

	private Dialog dialog;
	public static PayOrderBean orderBaen;
	public static ResponseCallback responseCallback;
	private com.wi360.pay.sdk.bean.PayOrderBean.Pay payBean;

	public static String appId;
	public static String appKey;

	public PayController(Activity context, final Dialog dialog, View view) {
		this.context = context;
		this.dialog = dialog;
		this.view = view;
		int tv_titleId = context.getResources().getIdentifier("tv_title", "id", context.getPackageName());
		TextView tv_title = (TextView) this.view.findViewById(tv_titleId);
		tv_title.setText("博升信用支付");
		int tv_name_id = context.getResources().getIdentifier("tv_title", "id", context.getPackageName());
		int tv_momey_id = context.getResources().getIdentifier("tv_momey", "id", context.getPackageName());
		int tv_desc_id = context.getResources().getIdentifier("tv_desc", "id", context.getPackageName());
		int bt_submit_id = context.getResources().getIdentifier("bt_submit", "id", context.getPackageName());
		int bt_find_desc_id = context.getResources().getIdentifier("bt_find_desc", "id", context.getPackageName());
		tv_name = (TextView) this.view.findViewById(tv_name_id);
		tv_momey = (TextView) this.view.findViewById(tv_momey_id);
		tv_desc = (TextView) this.view.findViewById(tv_desc_id);
		bt_submit = (Button) this.view.findViewById(bt_submit_id);
		TextView bt_find_desc = (TextView) this.view.findViewById(bt_find_desc_id);
		bt_find_desc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 查看详情
				Intent intent = new Intent(PayController.this.context, WebViewRechargeActivity.class);
				intent.putExtra("url", "http://www.dlspay.com");
				PayController.this.context.startActivity(intent);
			}
		});
		// 立即信用支付
		bt_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendPayRequest();
				dialog.dismiss();
			}
		});
		// 取消信用支付
		this.view.findViewById(context.getResources().getIdentifier("bt_cancel", "id", context.getPackageName()))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						((QidaDialog) dialog).responseCallback = responseCallback;
						((QidaDialog) dialog).onClickCancleBut();
						dialog.dismiss();
					}
				});
	}

	/**
	 * 发送支付请求
	 * 
	 * @param url
	 * @param json
	 */
	private void sendPayRequest() {

		String json = GsonTools.createGsonString(orderBaen);

		new MyAsyncTask<ResultBean>(context, null, payBean, responseCallback) {
			@Override
			public String connectNetWorkSuccess(int statusCode, String responseInfo) {
				if (statusCode == 200) {
					resBean = GsonTools.changeGsonToBean(responseInfo, ResultBean.class);
					if (resBean.errcode == 0) {
						SharedPreferencesUtils.saveInt(PayController.this.context, Constants.creditLimit,
								resBean.credit.creditLimit);
						SharedPreferencesUtils.saveInt(PayController.this.context, Constants.usedCredit,
								resBean.credit.usedCredit);
						// User2 user =
						// DBUtils.getUser(PayController.this.context);
						// user.setCreditLimit(resBean.credit.creditLimit);
						// user.setUsedCredit(resBean.credit.usedCredit);
						// DBUtils.updateUser(context, user);
						// UIUtils.showToast(context, resBean.errmsg);
					} else {
						UIUtils.showToast(context, resBean.errmsg);
					}
				}
				return null;
			}

			@Override
			protected void onPostExecute(String msg) {
				super.onPostExecute(msg);
				// 支付成功后弹出成功dialog
				if (resBean != null && resBean.errcode == 0) {
					int layout_id = CommonUtil.getResourcesId(context, "dialog_pay_success", "layout");
					int style_id = CommonUtil.getResourcesId(context, "QidaDialog", "style");
					QidaDialog dialog = new QidaDialog(context, layout_id, style_id, responseCallback);
					PaySuccessController paySuccessController = new PaySuccessController(context, dialog, dialog.view,
							responseCallback);
					paySuccessController.setData(resBean);

				}
			}
		}.execute(new String[] { Constants.PAY_URL, json });

	}

	private void sendPayRequest2() {
		new Thread() {
			@Override
			public void run() {
				String json = GsonTools.createGsonString(orderBaen);
				HttpUtils.sendPost(Constants.PAY_URL, json, new MyRequestCallBack2() {

					@Override
					public void onSuccess(int stateCode, String responseInfo) {
						// TODO Auto-generated method stub
						System.out.println(stateCode);
					}

					@Override
					public void onFailure(Exception error, String msg) {
						// TODO Auto-generated method stub
						System.out.println(msg);

					}
				});
			}
		}.start();

	}

	@Override
	public void creditPay(String appId, String appkey, String productName, int sum, String alias, String sellerUserId,
			ResponseCallback responseCallback) {
		// 检测是否在UI线程中调用该接口
		if (!CommonUtil.checkUiThread()) {
			try {
				throw new Exception("请在UI线程中调用计费接口...");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		if (StringUtils.isEmpty(appId) || StringUtils.isEmpty(appkey)) {
			try {
				throw new Exception("请填写appId或appKey");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		this.appId = appId;
		this.appKey = appkey;

		this.responseCallback = responseCallback;
		// User2 user = DBUtils.getUser(context);
		String token = SharedPreferencesUtils.getString(context, Constants.token, "");
		// if (user != null) {
		// token = user.getToken();
		// }
		// 保存开发者录入信息
		payBean = new PayOrderBean().new Pay(sum, alias, productName, sellerUserId);
		// 检查数据格式是否正确
		int checkCode = payBean.check();
		if (checkCode != 0) {
			responseCallback.responseStateCode(checkCode);
			return;
		}

		// 如果用户没有登陆
		if (StringUtils.isEmpty(token)) {
			int dialog_login_id = context.getResources().getIdentifier("dialog_login", "layout",
					context.getPackageName());
			int qidaDialog_id = context.getResources().getIdentifier("QidaDialog", "style", context.getPackageName());
			QidaDialog dialog = new QidaDialog(context, dialog_login_id, qidaDialog_id, this.responseCallback);
			dialog.show();
			LoginController login = new LoginController(context, dialog, dialog.view, payBean, this.responseCallback);

			return;
		}
		// 如果登录,进入支付页面
		if (PayController.this.dialog != null && !PayController.this.dialog.isShowing()) {
			orderBaen = new PayOrderBean(context, sum, alias, productName, sellerUserId);

			// 进入支付页面前检查信用额度是否足够
			{
				int momey = SharedPreferencesUtils.getInt(PayController.this.context, Constants.creditLimit, 0)
						- SharedPreferencesUtils.getInt(PayController.this.context, Constants.usedCredit, 0);
				// int momey = user.getCreditLimit() - user.getUsedCredit();

				// 若果信用额度不足,跟新本地 ,与数据库同步
				if (sum > momey) {
					synchronizationMomey();
					return;
				}
			}

			// 初始化数据
			initData();
			// 显示支付dialog
			PayController.this.dialog.show();
		} else {
			UIUtils.showToast(context, "请联系客服");
		}

	}

	/**
	 * 本地与数据库同步信用额度,和使用额度
	 */
	private void synchronizationMomey() {
		GetPayBean getBean = new GetPayBean(context);
		String json = GsonTools.createGsonString(getBean);
		new MyAsyncTask<ResultBean>(context, null, null, responseCallback) {
			@Override
			public String connectNetWorkSuccess(int statusCode, String responseInfo) {
				if (statusCode == 200) {
					resBean = GsonTools.changeGsonToBean(responseInfo, ResultBean.class);
					// 同步成功
					if (resBean.errcode == 0) {
						SharedPreferencesUtils.saveString(context, Constants.mobileNum, resBean.mobileNum);
						SharedPreferencesUtils.saveInt(context, Constants.creditLimit, resBean.creditLimit);
						SharedPreferencesUtils.saveInt(context, Constants.usedCredit, resBean.usedCredit);

						// User2 user = DBUtils.getUser(context);
						// user.setMobileNum(resBean.mobileNum);
						// user.setCreditLimit(resBean.creditLimit);
						// user.setUsedCredit(resBean.usedCredit);
						// DBUtils.updateUser(context, user);

					}
				}
				return null;
			}

			protected void onPostExecute(String msg) {
				// 检测使用额度是否足够,如果不够跳转到充值页面
				{
					// User2 user = DBUtils.getUser(context);
					// int momey = user.getCreditLimit() - user.getUsedCredit();
					int momey = SharedPreferencesUtils.getInt(PayController.this.context, Constants.creditLimit, 0)
							- SharedPreferencesUtils.getInt(PayController.this.context, Constants.usedCredit, 0);
					// 若果信用额度不足跳转到充值页面
					if (orderBaen.pay.sum > momey) {
						int dialog_recharge = context.getResources().getIdentifier("dialog_recharge", "layout",
								context.getPackageName());
						int QidaDialog = context.getResources().getIdentifier("QidaDialog", "style",
								context.getPackageName());
						QidaDialog dialog = new QidaDialog(PayController.this.context, dialog_recharge, QidaDialog,
								responseCallback);
						RechargePayController rechargePay = new RechargePayController(PayController.this.context,
								dialog, dialog.view, payBean, responseCallback);
						rechargePay.setData(orderBaen);
						PayController.this.dialog.dismiss();
						return;
					} else if (orderBaen.pay.sum < momey) {
						// 如果充值后信用额度足够跳转到支付页面
						// 初始化数据
						initData();
						// 显示支付dialog
						PayController.this.dialog.show();
					}
				}
				// super.onPostExecute(msg);
			};
		}.execute(new String[] { Constants.GET_PAY_URL, json });

	}

	private void initData() {
		tv_name.setText(orderBaen.pay.productName);
		// 信用额度保留两位小数
		tv_momey.setText(String.format("%.2f", orderBaen.pay.sum * 0.01) + "元");
		int momey = SharedPreferencesUtils.getInt(context, Constants.creditLimit, 0)
				- SharedPreferencesUtils.getInt(context, Constants.usedCredit, 0);
		// User2 user = DBUtils.getUser(context);
		// int momey = user.getCreditLimit() - user.getUsedCredit();
		tv_desc.setText(Html.fromHtml("你有<font color=#EA7914>" + String.format("%.2f", momey * 0.01)
				+ "</font>元信用额度可以使用"));
	}

	@Override
	public void isBalanceDue(String appId, String appKey, final BalanceDueCallback balanceDueCallback) {
		if (balanceDueCallback == null) {
			throw new RuntimeException("balanceDueCallback 不能等于null");
		}
		if (StringUtils.isEmpty(appId) || StringUtils.isEmpty(appKey)) {
			try {
				throw new Exception("请填写appId或appKey");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		this.appId = appId;
		this.appKey = appKey;
		Log.i(TAG, "isBalanceDue");
		String token = SharedPreferencesUtils.getString(context, Constants.token, "");
		if (StringUtils.isEmpty(token)) {
			balanceDueCallback.notifyBalanceDueMomey(0);
			return;
		}
		IsBalanceDueSafeBean sendBean = new IsBalanceDueSafeBean(context);
		String json = GsonTools.createGsonString(sendBean);
		new MyAsyncTask<ResultBean>(context, null, null, null) {
			@Override
			public String connectNetWorkSuccess(int statusCode, String responseInfo) {
				if (statusCode == 200 && !StringUtils.isEmpty(responseInfo)) {
					resBean = GsonTools.changeGsonToBean(responseInfo, ResultBean.class);
				}
				return null;
			}

			protected void onPostExecute(String msg) {
				super.onPostExecute(msg);
				// 若果没有网络resBean == null
				// if (resBean != null && resBean.errcode == 0 &&
				// resBean.isBalanceDue) {
				// // 如果有到期欠款的，直接弹出还款界面。
				// // Intent intent = new Intent(PayController.this.context,
				// // WebViewRechargeActivity.class);
				// // // intent.putExtra("url", "http://www.dlspay.com");
				// // intent.putExtra("url", resBean.url);
				// // PayController.this.context.startActivity(intent);
				// }
				if (resBean != null && resBean.errcode == 0 && resBean.isBalanceDue) {
					balanceDueCallback.notifyBalanceDueMomey(resBean.sum);
				} else {
					balanceDueCallback.notifyBalanceDueMomey(0);
				}

			};
		}.execute(Constants.isBalanceDueSafe_URL, json);
	}

	public int isBalanceDueSafe() {
		Log.i(TAG, "isBalanceDue");
		int retCode = Pay.NOT_MONEY_CODE;
		String token = SharedPreferencesUtils.getString(context, Constants.token, "");
		if (StringUtils.isEmpty(token)) {
			// 没有登录
			retCode = Pay.NOT_LOGIN_CODE;
		} else {
			retCode = Pay.GO_MONEY_CODE;
			IsBalanceDueSafeBean sendBean = new IsBalanceDueSafeBean(context);
			String json = GsonTools.createGsonString(sendBean);
			new MyAsyncTask<ResultBean>(context, null, null, null) {

				@Override
				public String connectNetWorkSuccess(int statusCode, String responseInfo) {
					return null;
				}

				protected void onPostExecute(String msg) {
					super.onPostExecute(msg);
					// 若果没有网络resBean == null
					if (resBean != null && resBean.errcode == 0 && resBean.isBalanceDue) {
						// 如果有到期欠款的，直接弹出还款界面。
						Intent intent = new Intent(PayController.this.context, WebViewRechargeActivity.class);
						// intent.putExtra("url", "http://www.dlspay.com");
						intent.putExtra("url", resBean.url);
						PayController.this.context.startActivity(intent);
					}

				};
			}.execute(Constants.isBalanceDueSafe_URL, json);

		}

		return retCode;
	}
}
