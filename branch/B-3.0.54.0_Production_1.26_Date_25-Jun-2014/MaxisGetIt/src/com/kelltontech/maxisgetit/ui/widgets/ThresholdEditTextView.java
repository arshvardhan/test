package com.kelltontech.maxisgetit.ui.widgets;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

public class ThresholdEditTextView extends EditText {

	// =========================================
	// Private members
	// =========================================

	private int threshold;
	private ThresholdTextChangedLinstner thresholdTextChanged;
	private Handler handler;
	private Runnable invoker;
	private boolean disableThresholdOnEmptyInput;

	// =========================================
	// Constructors
	// =========================================

	public ThresholdEditTextView(Context context) {
		super(context);
		initAttributes(null);
		init();
	}

	public ThresholdEditTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAttributes(attrs);
		init();
	}

	// =========================================
	// Public properties
	// =========================================

	/**
	 * Get the current threshold value
	 */
	public int getThreshold() {
		return threshold;
	}

	/**
	 * Set the threshold value (in milliseconds)
	 *
	 * @param threshold
	 *            Threshold value
	 */
	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	/**
	 * @return True = the callback will fire immediately when the content of the
	 *         EditText is emptied False = The threshold will be used even on
	 *         empty input
	 */
	public boolean isDisableThresholdOnEmptyInput() {
		return disableThresholdOnEmptyInput;
	}

	/**
	 * @param disableThresholdOnEmptyInput
	 *            Set to true if you want the callback to fire immediately when
	 *            the content of the EditText is emptied
	 */
	public void setDisableThresholdOnEmptyInput(
			boolean disableThresholdOnEmptyInput) {
		this.disableThresholdOnEmptyInput = disableThresholdOnEmptyInput;
	}

	/**
	 * Set the callback to the OnThresholdTextChanged event
	 *
	 * @param listener
	 */
	public void setOnThresholdTextChanged(ThresholdTextChangedLinstner listener) {
		this.thresholdTextChanged = listener;
	}

	// =========================================
	// Private / Protected methods
	// =========================================

	/**
	 * Load properties values from xml layout
	 */
	private void initAttributes(AttributeSet attrs) {
		try {
		if (attrs != null) {
			String namespace = "http://schemas.android.com/apk/res/com.MobileAnarchy.ThresholdEditText";

			// Load values to local members
			this.threshold = attrs.getAttributeIntValue(namespace, "threshold",
					500);
			;
			this.disableThresholdOnEmptyInput = attrs.getAttributeBooleanValue(
					namespace, "disableThresholdOnEmptyInput", true);
			;
		} else {
			// Default threshold value is 0.5 seconds
			threshold = 500;

			// Default behaviour on emptied text - no threshold
			disableThresholdOnEmptyInput = true;
		}
	}catch ( Exception e) {
		Log.e("ERROR", " ThresholdEditText : initAttributes - Got exception - ");
		e.printStackTrace();
	}
	}

	/**
	 * Initialize the private members with default values
	 */
	private void init() {

		handler = new Handler();

		invoker = new Runnable() {

			public void run() {
				try {

				invokeCallback();
				}catch ( Exception e) {
					Log.e("ERROR", " ThresholdEditText : init - run  - Got exception - ");
					e.printStackTrace();
				}
			}

		};

		this.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				try {
					// Remove any existing pending callbacks
					handler.removeCallbacks(invoker);

					if ((s.length() == 0) && disableThresholdOnEmptyInput) {
						// The text is empty, so invoke the callback immediately
						invoker.run();
					} else {
						// Post a new delayed callback
						handler.postDelayed(invoker, threshold);
					}
				}catch ( Exception e) {
					Log.e("ERROR", " ThresholdEditText : invokeCallback - Got exception - " + e.getMessage());
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Invoking the callback on the listener provided (if provided)
	 */
	private void invokeCallback() {
		try {
		if (thresholdTextChanged != null) {
			thresholdTextChanged.onThersholdTextChanged(this.getText());
		}
		}catch ( Exception e) {
			Log.e("ERROR", " ThresholdEditText : onTextChanged - Got exception - "+e.getMessage());
			e.printStackTrace();
		}
	}
	public interface ThresholdTextChangedLinstner {
		void onThersholdTextChanged(Editable text);
	}
}