package com.kelltontech.maxisgetit.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kelltontech.framework.imageloader.ImageLoader;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.dao.SubCategory;
import com.kelltontech.maxisgetit.ui.activities.SubCategoryListActivity;

public class SubCategoryAdapter extends BaseAdapter {
	private Context mcontext;
	private ArrayList<SubCategory> categories;
//	private String[] cats = { "Restaurants", "Multiplexes", "Doctors", "Banks & ATM", "Automobile", "Malls", "Sell used", "Buy new", "On Rent" };
//	private int[] icons={R.drawable.t_auto,R.drawable.t_bank,R.drawable.t_doc,R.drawable.t_malls,R.drawable.t_multi,R.drawable.t_rest};
	private static Drawable dummyDrawable;
	private static Drawable errorDrawable;
	
	public SubCategoryAdapter(Context context) {
		mcontext = context;
		ImageLoader.initialize(mcontext);
		dummyDrawable=mcontext.getResources().getDrawable(R.drawable.group_load);
		errorDrawable=mcontext.getResources().getDrawable(R.drawable.group_cross);
	}
	public void setData(ArrayList<SubCategory> categories){
		if(categories!=null && categories.size()>0)
			this.categories=categories;
	}
	@Override
	public int getCount() {
		if(categories!=null)
			return categories.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(categories!=null)
			return categories.get(position);
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Viewholder holder;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.subcategory_view, null);
			holder = new Viewholder();
			holder.v = (ImageView) convertView.findViewById(R.id.subcat_list_image);
			holder.tv=(TextView) convertView.findViewById(R.id.subcat_title_list);
			holder.imageManager=(LinearLayout) convertView.findViewById(R.id.subcat_image_manager);
			convertView.setTag(holder);
		} else {
			holder = (Viewholder) convertView.getTag();
		}
			SubCategory cat=categories.get(position);
			if(!StringUtil.isNullOrEmpty(cat.getCategoryTitle()))
				holder.tv.setText(Html.fromHtml(cat.getCategoryTitle()));
			holder.imageManager.setTag(cat);
			holder.imageManager.setOnClickListener((SubCategoryListActivity)mcontext);
			ImageLoader.start(cat.getIconUrl(), holder.v, dummyDrawable, errorDrawable);
		return convertView;
	}

	class Viewholder {
		ImageView v;
		TextView tv;
		LinearLayout imageManager;
	}

}
