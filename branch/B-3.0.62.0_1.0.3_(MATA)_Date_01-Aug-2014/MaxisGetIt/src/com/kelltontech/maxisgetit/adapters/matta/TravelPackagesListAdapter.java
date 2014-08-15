package com.kelltontech.maxisgetit.adapters.matta;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kelltontech.framework.imageloader.ImageLoader;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.model.matta.booths.detail.PackageList;

public class TravelPackagesListAdapter extends BaseAdapter implements OnClickListener {
	private Context 					mContext;
	private ArrayList<PackageList> 		list;
	private Drawable 					dummyDrawable;
	private Drawable 					errorDrawable;
	private PackageList 				pkgList;

	public TravelPackagesListAdapter(Context context) {
		this.mContext = context;
		ImageLoader.initialize(mContext);
		dummyDrawable = mContext.getResources().getDrawable(R.drawable.travel_package_default);
		errorDrawable = mContext.getResources().getDrawable(R.drawable.travel_package_default);
	}

	public void setData(ArrayList<PackageList> packageList) {
		this.list = packageList;
	}

	@Override
	public int getCount() {
		if (list != null)
			return list.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Model model;
		if (convertView == null) {
			LayoutInflater inflater 	= 	(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView 				= 	inflater.inflate(R.layout.activity_matta_travel_package_list_row, null);
			model = new Model();
			model.travelPkgImg 			= 	(ImageView) convertView.findViewById(R.id.travel_pkg_image);
			model.travelPkgTitle 		= 	(TextView) convertView.findViewById(R.id.travel_pkg_title);
			convertView.setTag(model);
		} else {
			model = (Model) convertView.getTag();
		}
		pkgList = list.get(position);
		if (pkgList != null && (!StringUtil.isNullOrEmpty(pkgList.getId())) && ("-1".equalsIgnoreCase(pkgList.getId()))) {
			model.travelPkgImg.setVisibility(View.GONE);
			model.travelPkgTitle.setVisibility(View.INVISIBLE);
		} else {
			model.travelPkgImg.setVisibility(View.VISIBLE);
			model.travelPkgTitle.setVisibility(View.VISIBLE);
			ImageLoader.start(pkgList.getImage(), model.travelPkgImg, dummyDrawable, errorDrawable);
			if (!StringUtil.isNullOrEmpty(pkgList.getValue()))
				model.travelPkgTitle.setText(Html.fromHtml(pkgList.getValue()));
			else
				model.travelPkgTitle.setText(Html.fromHtml(""));
		}
		return convertView;
	}

	class Model {
		TextView 		travelPkgTitle;
		ImageView 		travelPkgImg;
	}

	@Override
	public void onClick(View v) { }

}