package com.kelltontech.maxisgetit.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.kelltontech.maxisgetit.R;

public class MultiSelectListAdapter extends BaseAdapter {
	ArrayList<String> values;
	SparseBooleanArray msSparseBooleanArray;
	Context mContext;
	public ArrayList<String> getSelectedValues(){
		ArrayList<String> selectedVals=new ArrayList<String>();
		for (int i = 0; i < values.size(); i++) {
			if(msSparseBooleanArray.get(i, false)){
				selectedVals.add(values.get(i));
			}
		}
		return selectedVals;
	}
	public MultiSelectListAdapter(Context context, ArrayList<String> values) {
		this.values = values;
		mContext = context;
		msSparseBooleanArray = new SparseBooleanArray();
	}

	@Override
	public int getCount() {
		return values.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder;
//		if (convertView == null) {
			convertView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.multiselect_list_row, null);
			holder=new Holder();
			holder.cBox=(CheckBox) convertView.findViewById(R.id.mull_checkIcon);
//			holder.textView=(TextView) convertView.findViewById(R.id.mull_desc);
			holder.cBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					msSparseBooleanArray.append(position,isChecked);
				}
			});
			convertView.setTag(holder);
//		}else{
//			holder=(Holder) convertView.getTag();
//		}
		holder.cBox.setText(values.get(position));
		if(msSparseBooleanArray.get(position)){
			Log.i("List multi Sele", "position "+position);
		}
		if(msSparseBooleanArray.get(position))
			holder.cBox.setChecked(msSparseBooleanArray.get(position));
//			System.out.println(values.get(position)+" "+msSparseBooleanArray.get(position, false));
		return convertView;
	}

	private class Holder {
		CheckBox cBox;
//		TextView textView;
	}
}
