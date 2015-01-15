package com.example.credit_pay_demo;

import com.creditpay.CreditPayManager;
import com.creditpay.CreditPayManager.CreditPayManagerCallBack;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends ActionBarActivity
		 {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		CreditPayManager.init(this);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment implements CreditPayManagerCallBack {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			rootView.findViewById(R.id.show_btn).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View view) {
							CreditPayManager.getInstance().creditPay("zjhtwallet","测试商品", 1,
									"abcdefghijk", "1", PlaceholderFragment.this);
						}
					});
			return rootView;
		}
		@Override
		public void success() {
			// TODO Auto-generated method stub
			Toast.makeText(getActivity(), "支付成功！", Toast.LENGTH_LONG).show();
		}

		@Override
		public void fail(int errorCode, String errorMsg) {
			// TODO Auto-generated method stub
			Toast.makeText(getActivity(), "支付失败，errorCode="+errorCode+",errorMsg="+errorMsg, Toast.LENGTH_LONG).show();
		}
	}

	

}
