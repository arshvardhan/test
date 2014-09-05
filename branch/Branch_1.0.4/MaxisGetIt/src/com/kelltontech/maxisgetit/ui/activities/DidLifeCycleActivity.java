package com.kelltontech.maxisgetit.ui.activities;

import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.R.layout;
import com.kelltontech.maxisgetit.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class DidLifeCycleActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_did_life_cycle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.did_life_cycle, menu);
		return true;
	}

}
