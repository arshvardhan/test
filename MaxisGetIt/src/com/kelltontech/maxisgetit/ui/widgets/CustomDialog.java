package com.kelltontech.maxisgetit.ui.widgets;

import java.util.HashMap;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.kelltontech.framework.ui.BaseMainActivity;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.dao.MaxisStore;
import com.kelltontech.maxisgetit.service.AppSharedPreference;
import com.kelltontech.maxisgetit.service.ContestLocationFinderService;
import com.kelltontech.maxisgetit.ui.activities.ContestHomeActivity;
import com.kelltontech.maxisgetit.ui.activities.ContestPoiSearchActivity;
import com.kelltontech.maxisgetit.ui.activities.MaxisFragmentBaseActivity;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;
import com.kelltontech.maxisgetit.utils.Utility;

public class CustomDialog implements OnKeyListener {
	
	/**
	 * Dialogs of findit333 app.
	 */
	public static final int		DIALOG_NONE							= 0;
	public static final int		INFO_DIALOG							= 1;
	public static final int		CONFIRMATION_DIALOG					= 2;
	public static final int		FINAL_DIALOG						= 3;
	public static final int		MAP_DIALOG							= 4;
	public static final int		TNC_DIALOG							= 5;
	public static final int		PROGRESS_DIALOG						= 6;
	public static final int		COMMON_ERROR_DIALOG					= 7;
	public static final int		LOCATION_USE_ALERT					= 8;
	public static final int		SINGLE_BUTTON_CONFIRMATION_DIALOG	= 9;
	public static final int		LOCATION_DIALOG						= 10;
	public static final int		DATA_USAGE_DIALOG					= 11;
	public static final int		DATA_USAGE_DIALOG_FOR_WEBSITE		= 12;
	public static final int		DATA_USAGE_DIALOG_FOR_EMAIL			= 13;
	public static final int		DATA_USAGE_DIALOG_FOR_FACEBOOK		= 14;
	public static final int		DATA_USAGE_DIALOG_FOR_TWITTER		= 15;
	public static final int		DATA_USAGE_DIALOG_FOR_CALL			= 16;
	public static final int		CHANGE_PASSWORD_DIALOG				= 17;
	public static final int		PLAY_STORE_DIALOG					= 18;
	public static final int		LOGIN_CONFIRMATION_DIALOG			= 19;
	public static final int		DELETE_CONFIRMATION_DIALOG			= 20;
	public static final int		ADD_NEW_POI_CONFIRMATION_DIALOG		= 21;
	
	
	private BaseMainActivity mActivity;
	private int mId;
	private Dialog dialog = null;
	protected MaxisStore mStore;

	public CustomDialog(int id, BaseMainActivity activity) {
		this.mId = id;
		this.mActivity = activity;
	}
	public Dialog createDisclaimerDialog(String title,String text) {
			dialog = new Dialog(mActivity, R.style.Theme_Almost_full_transparent);
			dialog.setContentView(R.layout.dialog_layout);
			TextView titleTxtView=(TextView) dialog.findViewById(R.id.dialog_title);
			titleTxtView.setText(title);
			TextView contectView = (TextView) dialog.findViewById(R.id.dialog_content);
			contectView.setText(text);
			Button closeBtn=(Button) dialog.findViewById(R.id.dialog_close);
			closeBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			dialog.show();
			dialog.setCancelable(true);
			return dialog;
	}
	public Dialog createCustomDialog(String info) {
		switch (mId) {
		case INFO_DIALOG:
			dialog = new Dialog(mActivity, R.style.Theme_Almost_full_transparent);
			dialog.setContentView(R.layout.dialog_layout);
			TextView contectView = (TextView) dialog.findViewById(R.id.dialog_content);
			contectView.setText(info);
			Button closeBtn=(Button) dialog.findViewById(R.id.dialog_close);
			closeBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			dialog.show();
			dialog.setCancelable(true);
			break;
		
		case SINGLE_BUTTON_CONFIRMATION_DIALOG:
			dialog = new Dialog(mActivity, R.style.Theme_Almost_full_transparent);
			dialog.setContentView(R.layout.dialog_layout);
			contectView = (TextView) dialog.findViewById(R.id.dialog_content);
			contectView.setText(info);
			closeBtn=(Button) dialog.findViewById(R.id.dialog_close);
			closeBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					mActivity.onPositiveDialogButton(mId);
				}
			});
			dialog.show();
			dialog.setCancelable(true);
			break;
			
		case FINAL_DIALOG:
			dialog = new Dialog(mActivity, R.style.Theme_Almost_full_transparent);
			dialog.setContentView(R.layout.dialog_layout);
			TextView fcontectView = (TextView) dialog.findViewById(R.id.dialog_content);
			fcontectView.setText(info);
			Button fcloseBtn=(Button) dialog.findViewById(R.id.dialog_close);
			fcloseBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					mActivity.finish();
				}
			});
			dialog.show();
			dialog.setCancelable(false);
			break;
		case CONFIRMATION_DIALOG:
		case LOCATION_DIALOG:
			dialog = new Dialog(mActivity, R.style.Theme_Almost_full_transparent);
			dialog.setContentView(R.layout.dialog_confirmation);
			TextView contentView = (TextView) dialog.findViewById(R.id.dialog_content);
			contentView.setText(info);
			TextView okBtn = (TextView) dialog.findViewById(R.id.dialog_ok);
			okBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mActivity.onPositiveDialogButton(mId);
					dialog.dismiss();
				}
			});
			TextView cancelBtn = (TextView) dialog.findViewById(R.id.dialog_cancel);
			cancelBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					mActivity.onNegativeDialogbutton(mId);
				}
			});
			dialog.show();
			dialog.setCancelable(true);
			break;
		case LOGIN_CONFIRMATION_DIALOG:
			dialog = new Dialog(mActivity, R.style.Theme_Almost_full_transparent);
			dialog.setContentView(R.layout.dialog_confirmation);
			TextView cntView = (TextView) dialog.findViewById(R.id.dialog_content);
			cntView.setText(info);
			TextView loginBtn = (TextView) dialog.findViewById(R.id.dialog_ok);
			loginBtn.setText(R.string.login_btn);
			loginBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mActivity.onPositiveDialogButton(mId);
					dialog.dismiss();
				}
			});
			TextView canclBtn = (TextView) dialog.findViewById(R.id.dialog_cancel);
			canclBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					mActivity.onNegativeDialogbutton(mId);
				}
			});
			dialog.show();
			dialog.setCancelable(true);

			break;
			
		case DELETE_CONFIRMATION_DIALOG:
			dialog = new Dialog(mActivity, R.style.Theme_Almost_full_transparent);
			dialog.setContentView(R.layout.dialog_confirmation);
			TextView cnView = (TextView) dialog.findViewById(R.id.dialog_content);
			cnView.setText(info);
			TextView YesBtn = (TextView) dialog.findViewById(R.id.dialog_ok);
			YesBtn.setText(R.string.confirm_btn);
			YesBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mActivity.onPositiveDialogButton(mId);
					dialog.dismiss();
				}
			});
			TextView NoBtn = (TextView) dialog.findViewById(R.id.dialog_cancel);
			NoBtn.setText(R.string.cancel_btn);
			NoBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					mActivity.onNegativeDialogbutton(mId);
				}
			});
			dialog.show();
			dialog.setCancelable(true);

			break;
			
		case ADD_NEW_POI_CONFIRMATION_DIALOG:
			dialog = new Dialog(mActivity, R.style.Theme_Almost_full_transparent);
			dialog.setContentView(R.layout.dialog_confirmation);
			TextView poiView = (TextView) dialog.findViewById(R.id.dialog_content);
			poiView.setText(info);
			TextView addNewPOIBtn = (TextView) dialog.findViewById(R.id.dialog_ok);
			addNewPOIBtn.setBackgroundResource(R.drawable.selector_blue_dialog_positive);
			addNewPOIBtn.setText(R.string.anp_header);
			addNewPOIBtn.setPadding(10, 0, 10, 0);
			addNewPOIBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mActivity.onPositiveDialogButton(mId);
					dialog.dismiss();
				}
			});
			TextView cancelPOIBtn = (TextView) dialog.findViewById(R.id.dialog_cancel);
			cancelPOIBtn.setText(R.string.cancel_btn);
			cancelPOIBtn.setPadding(10, 0, 10, 0);
			cancelPOIBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					mActivity.onNegativeDialogbutton(mId);
				}
			});
			dialog.show();
			dialog.setCancelable(true);

			break;
			
		case CHANGE_PASSWORD_DIALOG:
			dialog = new Dialog(mActivity, R.style.Theme_Almost_full_transparent);
			dialog.setContentView(R.layout.dialog_confirmation);
			contentView = (TextView) dialog.findViewById(R.id.dialog_content);
			contentView.setText(info);
			okBtn = (TextView) dialog.findViewById(R.id.dialog_ok);
			okBtn.setText("Change");
			okBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mActivity.onPositiveDialogButton(mId);
					dialog.dismiss();
				}
			});
			cancelBtn = (TextView) dialog.findViewById(R.id.dialog_cancel);
			cancelBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					mActivity.onNegativeDialogbutton(mId);
				}
			});
			dialog.show();
			dialog.setCancelable(true);

			break;
			
		case DATA_USAGE_DIALOG:
		case DATA_USAGE_DIALOG_FOR_EMAIL: 
		case DATA_USAGE_DIALOG_FOR_WEBSITE: 
		case DATA_USAGE_DIALOG_FOR_FACEBOOK:
		case DATA_USAGE_DIALOG_FOR_TWITTER:
		case DATA_USAGE_DIALOG_FOR_CALL:
			dialog = new Dialog(mActivity, R.style.Theme_Almost_full_transparent);
			dialog.setContentView(R.layout.dialog_remember_me);
			TextView contentUsageDialog = (TextView) dialog.findViewById(R.id.dialog_content);
			contentUsageDialog.setText(info);
			TextView okBtnUsageDialog = (TextView) dialog.findViewById(R.id.dialog_ok);
			final CheckBox checkRememberMe = (CheckBox) dialog.findViewById(R.id.dialog_check_remember_me);
			okBtnUsageDialog.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(checkRememberMe.isChecked()){
						mStore = MaxisStore.getStore(mActivity);
						mStore.setUserDecisionWithTime(true);//, System.currentTimeMillis()
					}
					mActivity.onPositiveDialogButton(mId);
					dialog.dismiss();
				}
			});
			TextView cancelBtnUsageDialog = (TextView) dialog.findViewById(R.id.dialog_cancel);
			cancelBtnUsageDialog.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					mActivity.onNegativeDialogbutton(mId);
				}
			});
			dialog.show();
			dialog.setCancelable(true);

			break;
		case TNC_DIALOG:
			dialog = new Dialog(mActivity, R.style.Theme_Almost_full_transparent);
			dialog.setContentView(R.layout.dialog_confirmation);
			TextView tncContentView = (TextView) dialog.findViewById(R.id.dialog_content);
			tncContentView.setText(info);
			TextView tncExitBtn = (TextView) dialog.findViewById(R.id.dialog_ok);
			tncExitBtn.setText(mActivity.getResources().getText(R.string.exit));
			tncExitBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mActivity.onPositiveDialogButton(mId);
					dialog.dismiss();
				}
			});
			TextView tncBackBtn = (TextView) dialog.findViewById(R.id.dialog_cancel);
			tncBackBtn.setText(mActivity.getResources().getText(R.string.back));
			tncBackBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					mActivity.onNegativeDialogbutton(mId);
				}
			});
			dialog.show();
			dialog.setCancelable(true);

			break;
		case PROGRESS_DIALOG:			// back-key on PROGRESS_DIALOG will also call getSyncAgent().setValid(false)
		{
			dialog = new Dialog(mActivity, R.style.Theme_Dialog2);
			dialog.setContentView(R.layout.progress_dialog);
			ImageView spin = (ImageView)dialog.findViewById(R.id.spin);
			TextView spinMsg = (TextView)dialog.findViewById(R.id.spinMessage);
			spinMsg.setText(mActivity.getString(R.string.please_wait));
			spin.startAnimation(AnimationUtils.loadAnimation(mActivity,R.anim.rotate_indefinitely)); 
			dialog.setCancelable(true);
			dialog.setOnKeyListener(mActivity);
			dialog.show();
			break;
		}
		case LOCATION_USE_ALERT:
		case COMMON_ERROR_DIALOG:	
		{
			dialog = new Dialog(mActivity, R.style.Theme_Almost_full_transparent);
			dialog.setContentView(R.layout.common_error_dialog);
			dialog.findViewById(R.id.login_error_dialog_inner_layout).setOnClickListener(mFakeListener);
			dialog.findViewById(R.id.login_error_dialog_root_layout).setOnClickListener(mFakeListener);
			Button ok_btn=(Button) dialog.findViewById(R.id.login_error_ok_btn);
			ok_btn.setOnClickListener(okBtnListener);
			Button no_btn=(Button) dialog.findViewById(R.id.login_error_register_btn);
			no_btn.setOnClickListener(noBtnListener);
			dialog.setCancelable(false);
			if(mId==LOCATION_USE_ALERT)
			{
				((TextView)dialog.findViewById(R.id.dialog_error_header_msg)).setText("Enable GPS");
				((TextView)dialog.findViewById(R.id.dialog_error_msg)).setText("This app will require the access to your current location.");
				ok_btn.setText(R.string.dialog_btn_ok);
				no_btn.setText(R.string.dialog_btn_no);
			}
			dialog.setCancelable(false);
			dialog.setOnKeyListener(mActivity);
			dialog.show();
			break;
		}
		case PLAY_STORE_DIALOG:
			dialog = new Dialog(mActivity, R.style.Theme_Almost_full_transparent);
			dialog.setContentView(R.layout.dialog_app_force_update);
			/*contentView = (TextView) dialog.findViewById(R.id.dialog_content);
			TextView content_part2 = (TextView)dialog.findViewById(R.id.dialog_content_part2);
			contentView.setText(info);
			content_part2.setText()*/
			okBtn = (TextView) dialog.findViewById(R.id.dialog_ok);
			okBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!StringUtil.isNullOrEmpty(AppConstants.API_VERSION)) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put(FlurryEventsConstants.Current_API_Version, AppConstants.API_VERSION);
					AnalyticsHelper.logEvent(FlurryEventsConstants.UPDATE_CLICK, map);
					} else {
						AnalyticsHelper.logEvent(FlurryEventsConstants.UPDATE_CLICK);
					}
					mActivity.runOnUiThread(new Runnable()
				    {           
				        @Override
				        public void run()
				        {
				        	String appUri;
				        	String appName =  Utility.getPackageName(mActivity) != null ? Utility.getPackageName(mActivity) : "";
				        	try {
				        		appUri = "market://details?id=" + appName;
							    mActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(appUri)));
							} catch (android.content.ActivityNotFoundException anfe) {
								appUri = "http://play.google.com/store/apps/details?id=" + appName;
								mActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(appUri)));
							} 
				        }
				    });
				}
			});
			cancelBtn = (TextView) dialog.findViewById(R.id.dialog_cancel);
			cancelBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					mActivity.stopSppiner();
				}
			});
			dialog.show();
			dialog.setCancelable(true);

			break;
		}
		return dialog;
	}
	
	public static Dialog CreateCustomDialog(final MaxisFragmentBaseActivity activity, int id, String info)
	{
		Dialog mapDialog = null;
		switch (id) {
		case MAP_DIALOG:
		{
			final Dialog dialog = new Dialog(activity, R.style.Theme_Almost_full_transparent);
			dialog.setContentView(R.layout.dialog_map_options);
			TextView titleView = (TextView) dialog.findViewById(R.id.dialog_title);
			titleView.setText(info);
			dialog.findViewById(R.id.dialog_map_walking).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					activity.onPositiveDialogButton(R.id.dialog_map_walking);
					dialog.dismiss();
				}
			});
			dialog.findViewById(R.id.dialog_map_driving).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					activity.onPositiveDialogButton(R.id.dialog_map_driving);
					dialog.dismiss();
				}
			});
			dialog.findViewById(R.id.dialog_map_details).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					activity.onPositiveDialogButton(R.id.dialog_map_details);
					dialog.dismiss();
				}
			});
			dialog.findViewById(R.id.dialog_map_cancel).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			mapDialog = dialog;
			
			break;
		}
		default:
			break;
		}
		if(mapDialog != null)
		{
			mapDialog.show();
			mapDialog.setCancelable(true);
		}

		
		return mapDialog;
	}

	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		return false;
	}

	/**
	 *	use this listener to close dialog along with its parent
	 */
	private OnClickListener mDialogRemoverWidParent = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			mActivity.removeDialog(mId);
			mActivity.startActivity(new Intent(mActivity,ContestHomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			mActivity.finish();
		}
	};

	/**
	 *	use this listener if nothing extra to be done after removing dialog.
	 */
	private OnClickListener okBtnListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			mActivity.removeDialog(mId);
			if(mId==LOCATION_USE_ALERT)
			{
				AppSharedPreference.putBoolean(AppSharedPreference.LOCATION_PERMISSION_ENABLED, true, mActivity);
				mActivity.startService(new Intent(mActivity,ContestLocationFinderService.class));
				mActivity.startActivity(new Intent(mActivity,ContestPoiSearchActivity.class));
			}
			else
				mActivity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			/*if( mId == NETWORK_ERROR_DIALOG||mId==SERVER_ERROR_DIALOG &&( mActivity instanceof ContestHomeActivity||  mActivity instanceof NewsListActivity||  mActivity instanceof SplashActivity))
			{
				mActivity.finish();	// SplashActivity is to be closed after no_network dialog.
			}*/
		}
	};

	private OnClickListener noBtnListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			mActivity.removeDialog(mId);
			//mActivity.startActivity(new Intent(mActivity,ContestUploadImageActivity.class));

		}
	};

	/**
	 *	use this listener if nothing to be done on click event.
	 */
	private OnClickListener mFakeListener = new OnClickListener()
	{
		@Override
		public void onClick(View v) { /* It does nothing. */ }
	};

}
