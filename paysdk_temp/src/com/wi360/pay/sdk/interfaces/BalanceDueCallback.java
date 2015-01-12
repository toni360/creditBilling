package com.wi360.pay.sdk.interfaces;

/**
 * isBalanceDue(BalanceDueCallback balanceDueCallback)
 * 该接口用于游戏在启动后调用，用于判断该用户是否有欠款到期需要偿还。 通过回调notifyBalanceDueMomey(int
 * money)通知开发者欠款金额 如果有到期欠款的，money等于欠款金额，如果无到期欠款，money等于0。
 * >1)如果该用户未登录，money等于0，表示这个无法识别的用户没有欠款。
 * 
 * @author Administrator
 * 
 */
public interface BalanceDueCallback {
	void notifyBalanceDueMomey(int momey);
}
