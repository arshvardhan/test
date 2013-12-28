package com.kelltontech.maxisgetit.ui.activities;

import java.math.BigDecimal;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

public class PaymentDemoActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(null);

	    Intent intent = new Intent(this, PayPalService.class);

	    // live: don't put any environment extra
	    // sandbox: use PaymentActivity.ENVIRONMENT_SANDBOX
	    intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, PaymentActivity.ENVIRONMENT_NO_NETWORK);

	    intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, "<YOUR_CLIENT_ID>");

	    startService(intent);
	}

	@Override
	public void onDestroy() {
	    stopService(new Intent(this, PayPalService.class));
	    super.onDestroy();
	}
	public void onBuyPressed(View pressed) {
	    PayPalPayment payment = new PayPalPayment(new BigDecimal("8.75"), "USD", "hipster jeans");

	    Intent intent = new Intent(this, PaymentActivity.class);

	    // comment this line out for live or set to PaymentActivity.ENVIRONMENT_SANDBOX for sandbox
	    intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, PaymentActivity.ENVIRONMENT_NO_NETWORK);

	    // it's important to repeat the clientId here so that the SDK has it if Android restarts your
	    // app midway through the payment UI flow.
	    intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, "credential-from-developer.paypal.com");

	    // Provide a payerId that uniquely identifies a user within the scope of your system,
	    // such as an email address or user ID.
	    intent.putExtra(PaymentActivity.EXTRA_PAYER_ID, "<someuser@somedomain.com>");

	    intent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL, "<YOUR_PAYPAL_EMAIL_ADDRESS>");
	    intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

	    startActivityForResult(intent, 0);
	}
	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data) {
	    if (resultCode == Activity.RESULT_OK) {
	        PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
	        if (confirm != null) {
	            try {
	                Log.i("paymentExample", confirm.toJSONObject().toString(4));

	                // TODO: send 'confirm' to your server for verification.
	                // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
	                // for more details.

	            } catch (JSONException e) {
	                //Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
	                //e.printStackTrace();
	                AnalyticsHelper.onError(FlurryEventsConstants.DATA_VALIDATION_ERR, "PaymentDemoActivity" + AppConstants.DATA_VALIDATION_ERROR_MSG, e);
	            }
	        }
	    }
	    else if (resultCode == Activity.RESULT_CANCELED) {
	        Log.i("paymentExample", "The user canceled.");
	    }
	    else if (resultCode == PaymentActivity.RESULT_PAYMENT_INVALID) {
	        Log.i("paymentExample", "An invalid payment was submitted. Please see the docs.");
	    }
	}


}
