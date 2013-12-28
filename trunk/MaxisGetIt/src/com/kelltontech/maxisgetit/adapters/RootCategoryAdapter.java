package com.kelltontech.maxisgetit.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kelltontech.framework.imageloader.ImageLoader;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.dao.CategoryGroup;
import com.kelltontech.maxisgetit.ui.activities.ContestHomeActivity;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class RootCategoryAdapter extends BaseAdapter {
	private ArrayList<CategoryGroup> categories;
	private static Drawable dummyDrawable;
	private static Drawable errorDrawable;
	private Context mContext;

	public RootCategoryAdapter(Context context) {
		mContext = context;
		ImageLoader.initialize(mContext);
		dummyDrawable = mContext.getResources().getDrawable(R.drawable.group_load);
		errorDrawable = mContext.getResources().getDrawable(R.drawable.group_cross);
	}

	public void setData(ArrayList<CategoryGroup> categories) {
		if (categories != null && categories.size() > 0)
			this.categories = categories;
	}

	@Override
	public int getCount() {
		if (categories != null)
			return categories.size();
		return 0;
		// return categories.size();
	}

	@Override
	public Object getItem(int position) {
		if (categories != null)
			return categories.get(position);
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder viewholder;
		if (convertView == null) {
			convertView = ((LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.home_category_row_layout, null);
			viewholder = new Holder();
			viewholder.iconField = (ImageView) convertView.findViewById(R.id.home_icon_view);
			viewholder.titleField = (TextView) convertView.findViewById(R.id.home_title_field);
			viewholder.btnPhotoContest = (Button) convertView.findViewById(R.id.home_btn_photo_contest);
			viewholder.btnPhotoContest.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					AnalyticsHelper.logEvent(FlurryEventsConstants.PHOTO_CONTEST_CLICK);
					mContext.startActivity(new Intent(mContext,ContestHomeActivity.class));
				}
			});
			viewholder.rootListItem = (LinearLayout) convertView.findViewById(R.id.root_list_item);
			convertView.setTag(viewholder);
		} else {
			viewholder = (Holder) convertView.getTag();
		}
		CategoryGroup cg = categories.get(position);
		
		if(cg.getCategoryId().equals(AppConstants.PHOTO_CONTEST_CAT_ID))
		{
			viewholder.btnPhotoContest.setVisibility(View.VISIBLE);
			viewholder.rootListItem.setVisibility(View.GONE);
		}
		else{
			viewholder.btnPhotoContest.setVisibility(View.GONE);
			viewholder.rootListItem.setVisibility(View.VISIBLE);
			if(!StringUtil.isNullOrEmpty(cg.getCategoryTitle()))
				viewholder.titleField.setText(Html.fromHtml(cg.getCategoryTitle()));
	//		if(position%2==0)
				ImageLoader.start(cg.getIconUrl(), viewholder.iconField, dummyDrawable, errorDrawable);
	//		else if(position%3==0)
	//			ImageLoader.start("http://s23.postimg.org/u1c3rg7jb/subcat_loading.png", viewholder.iconField, dummyDrawable, errorDrawable);
	//		else
	//			ImageLoader.start("http://s23.postimg.org/uh5r3fmzr/buyused_item.png", viewholder.iconField, dummyDrawable, errorDrawable);
		}
		return convertView;
	}

	private class Holder {
		ImageView iconField;
		TextView titleField;
		Button btnPhotoContest;
		LinearLayout rootListItem;
	}
}
