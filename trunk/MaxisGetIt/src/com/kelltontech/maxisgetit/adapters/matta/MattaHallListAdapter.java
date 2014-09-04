package com.kelltontech.maxisgetit.adapters.matta;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kelltontech.framework.imageloader.ImageLoader;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.dao.matta.MattaHallList;

public class MattaHallListAdapter extends BaseAdapter {
	private Context mcontext;
	private ArrayList<MattaHallList> hallList;
	private static Drawable dummyDrawable;
	private static Drawable errorDrawable;

	public MattaHallListAdapter(Context context) {
		mcontext = context;
		ImageLoader.initialize(mcontext);
		dummyDrawable=mcontext.getResources().getDrawable(R.drawable.hall_loading);
		errorDrawable=mcontext.getResources().getDrawable(R.drawable.hall_no_image);
	}
	public void setData(ArrayList<MattaHallList> hallList){
		if(hallList!=null && hallList.size()>0)
			this.hallList=hallList;
	}
	@Override
	public int getCount() {
		if(hallList!=null)
			return hallList.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(hallList!=null)
			return hallList.get(position);
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
			convertView = inflater.inflate(R.layout.activity_matta_hall_list_row_layout, null);
			holder = new Viewholder();
			holder.v = (ImageView) convertView.findViewById(R.id.home_icon_view);
			holder.tv=(TextView) convertView.findViewById(R.id.home_title_field);
			convertView.setTag(holder);
		} else {
			holder = (Viewholder) convertView.getTag();
		}
		MattaHallList mHallList=hallList.get(position);
		if(!StringUtil.isNullOrEmpty(mHallList.getmHallName()))
			holder.tv.setText(Html.fromHtml(mHallList.getmHallName()));                    //TODO
		ImageLoader.start(mHallList.getmHallImage(), holder.v, dummyDrawable, errorDrawable);
		return convertView;
	}

	class Viewholder {
		ImageView v;
		TextView tv;
	}
}