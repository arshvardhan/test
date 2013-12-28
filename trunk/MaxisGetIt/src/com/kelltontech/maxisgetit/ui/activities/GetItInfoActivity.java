package com.kelltontech.maxisgetit.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.kelltontech.maxisgetit.R;

public class GetItInfoActivity extends Activity {

	Button mBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_it_info);
		mBack = (Button) findViewById(R.id.atnc_back);
		
		mBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

}
