package com.kelltontech.maxisgetit.ui.activities;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.ui.adapter.SectionListAdapter;
import com.kelltontech.maxisgetit.ui.widgets.SectionListView;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class AdvanceSelectCity extends Activity {

	private StandardArrayAdapter arrayAdapter;
	private SectionListAdapter sectionAdapter;
	private SectionListView listView;
	private String selectedCity = "";

	ArrayList<String> displayCityList;
	ArrayList<String> selectedDisplayCityList;
	SectionListView sectionListView;

	EditText search;

	// sideIndex
	LinearLayout sideIndex;
	// height of side index
	private int sideIndexHeight, sideIndexSize;
	// list with items for side index
	private ArrayList<Object[]> sideIndexList = new ArrayList<Object[]>();

	boolean isFromFilters = false;

	// ImageView resetBtn;

	// an array with countries to display in the list
	// private ArrayList<String> BRANDS;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advance_select_city);
		sectionListView = (SectionListView) findViewById(R.id.section_list_view);
		findViewById(R.id.done_button).setVisibility(View.GONE);
		findViewById(R.id.clear_button).setVisibility(View.GONE);

		// doneBtn = (ImageButton)findViewById(R.id.done_btn);
		// resetBtn = (ImageView)findViewById(R.id.clear_button);
		// resetBtn.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		//
		// // clearSelectedvalues();
		// }
		// });

		// doneBtn.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		//
		// Intent returnIntent = new Intent();
		// returnIntent.putParcelableArrayListExtra("RECIEVED_DATA",displayCityList);
		// setResult(RESULT_OK,returnIntent);
		// finish();
		// }
		// });

		search = (EditText) findViewById(R.id.search_box);
		search.addTextChangedListener(filterTextWatcher);
		listView = (SectionListView) findViewById(R.id.section_list_view);
		sideIndex = (LinearLayout) findViewById(R.id.list_index);
		sideIndex.setOnTouchListener(new Indextouch());

		displayCityList = this.getIntent().getStringArrayListExtra("CITY_LIST");
		displayCityList.add(0, "Entire Malaysia");
		selectedCity = this.getIntent().getStringExtra("SELECTED_CITY");
		// sortedArrayList();

		// selectedDisplayValueList = displayValueList;

		// Log.i("RECIVED_BRAND_DATA_SIZE :", displayValueList.size()+"");

		setAdapter();

		PoplulateSideview();

		sectionListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				if (sectionListView.getAdapter().getItem(position).toString()
						.length() > 1) {
					int posInMainList = 0;

					for (int i = 0; i < displayCityList.size(); i++) {
						if (displayCityList.get(i).contains(
								sectionListView.getAdapter().getItem(position)
										.toString())) {
							posInMainList = i;
							break;
						}
					}

					Intent returnIntent = new Intent();
					returnIntent.putExtra("CITY_NAME", sectionListView
							.getAdapter().getItem(position).toString());
					returnIntent.putExtra("CITY_INDEX", posInMainList - 1);
					setResult(RESULT_OK, returnIntent);
					finish();

				} else {
					// Do Nothing
				}
			}
		});
		final View activityRootView = findViewById(R.id.activityRoot);
		activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						int heightDiff = activityRootView.getRootView()
								.getHeight() - activityRootView.getHeight();
						if (heightDiff > 100) { // if more than 100 pixels, its
												// probably a keyboard...
							sideIndex.setVisibility(View.INVISIBLE);
						} else {
							if (!isFromFilters) {
								sideIndex.setVisibility(View.VISIBLE);
							}

						}
					}
				});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		AnalyticsHelper.trackSession(AdvanceSelectCity.this, AppConstants.CityScreen);
	}

	private class Indextouch implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			if (event.getAction() == MotionEvent.ACTION_MOVE
					|| event.getAction() == MotionEvent.ACTION_DOWN) {

				sideIndex.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.rounded_rectangle_shape));
				// sideIndex.setBackgroundResource(R.color.black_light);
				// now you know coordinates of touch
				float sideIndexX = event.getX();
				float sideIndexY = event.getY();

				if (sideIndexX > 0 && sideIndexY > 0) {
					// and can display a proper item it country list
					displayListItem(sideIndexY);

				}
			} else {
//				sideIndex.setBackgroundResource(R.color.black_light);
				sideIndex.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.rounded_rectangle_shape));
			}

			return true;

		}

	};

	public void onWindowFocusChanged(boolean hasFocus) {
		// get height when component is poplulated in window
		sideIndexHeight = sideIndex.getHeight();
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// super.onWindowFocusChanged(hasFocus);
	}

	private class StandardArrayAdapter extends BaseAdapter implements
			Filterable {

		private final ArrayList<String> items;

		public StandardArrayAdapter(ArrayList<String> args) {
			this.items = args;
		}

		@Override
		public View getView(final int position, final View convertView,
				final ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = vi.inflate(R.layout.filter_listview_row, null);
			}
			TextView textView = (TextView) view
					.findViewById(R.id.filterOption_list_txt);
			if (textView != null) {
				textView.setText(items.get(position));
			}

			ImageView tickImg = (ImageView) view.findViewById(R.id.tickImg);
			if (tickImg != null) {
				if (items.get(position).equalsIgnoreCase(selectedCity)) {
					tickImg.setVisibility(View.VISIBLE);
				} else {
					tickImg.setVisibility(View.INVISIBLE);
				}
			}

			view.setTag(position);

			// view.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			//
			// // ImageView tickImg = (ImageView)v.findViewById(R.id.tickImg);
			// int index = Integer.parseInt(v.getTag().toString());
			// Log.i("Position", index+"");
			// if(tickImg!=null){
			//
			// if(tickImg.getVisibility()==View.VISIBLE){
			// tickImg.setVisibility(View.INVISIBLE);
			//
			// DisplayValue disObj = displayValueList.get(index);
			// disObj.setSelected(false);
			// displayValueList.set(index, disObj);
			//
			//
			// }else{
			//
			// tickImg.setVisibility(View.VISIBLE);
			// DisplayValue disObj = displayValueList.get(index);
			// disObj.setSelected(true);
			// displayValueList.set(index, disObj);
			// }
			// }

			/*
			 * Intent returnIntent = new Intent();
			 * returnIntent.putExtra("CITY_NAME",displayCityList.get(index));
			 * returnIntent.putExtra("CITY_INDEX", index);
			 * setResult(RESULT_OK,returnIntent);
			 * 
			 * finish();
			 */

			// }
			// });

			return view;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return items.size();
		}

		@Override
		public Filter getFilter() {
			Filter listfilter = new MyFilter();
			return listfilter;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
	}

	public class MyFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			// NOTE: this function is *always* called from a background thread,
			// and
			// not the UI thread.
			constraint = search.getText().toString();
			FilterResults result = new FilterResults();
			if (constraint != null && constraint.toString().length() > 0) {
				// do not show side index while filter results
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						((LinearLayout) findViewById(R.id.list_index))
								.setVisibility(View.INVISIBLE);
					}
				});

				ArrayList<String> filt = new ArrayList<String>();
				ArrayList<String> Items = new ArrayList<String>();
				synchronized (this) {
					Items = displayCityList;
				}
				for (int i = 0; i < Items.size(); i++) {
					String item = Items.get(i);
					if (item.toLowerCase().startsWith(
							constraint.toString().toLowerCase())) {
						filt.add(item);
					}
				}

				result.count = filt.size();
				result.values = filt;
				isFromFilters = true;

			} else {

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						((LinearLayout) findViewById(R.id.list_index))
								.setVisibility(View.VISIBLE);
						isFromFilters = false;
					}
				});
				synchronized (this) {
					result.count = displayCityList.size();
					result.values = displayCityList;
				}

			}
			return result;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			@SuppressWarnings("unchecked")
			ArrayList<String> filtered = (ArrayList<String>) results.values;
			arrayAdapter = new StandardArrayAdapter(filtered);
			sectionAdapter = new SectionListAdapter(getLayoutInflater(),
					arrayAdapter, false);
			listView.setAdapter(sectionAdapter);

		}

	}

	private void displayListItem(float sideIndexY) {
		// compute number of pixels for every side index item
		double pixelPerIndexItem = (double) sideIndexHeight / sideIndexSize;

		// compute the item index for given event position belongs to
		int itemPosition = (int) (sideIndexY / pixelPerIndexItem);

		if (itemPosition < sideIndexList.size()) {
			// get the item (we can do it since we know item index)
			Object[] indexItem = sideIndexList.get(itemPosition);
			listView.setSelectionFromTop((Integer) indexItem[1], 0);
		}
	}

	@SuppressLint("DefaultLocale")
	private void PoplulateSideview() {

		String latter_temp, latter = "";
		int index = 0;
		sideIndex.removeAllViews();
		sideIndexList.clear();
		for (int i = 1; i < displayCityList.size(); i++) {
			Object[] temp = new Object[2];
			latter_temp = (displayCityList.get(i)).substring(0, 1)
					.toUpperCase();
			if (!latter_temp.equals(latter)) {
				// latter with its array index
				latter = latter_temp;
				temp[0] = latter;
				temp[1] = i + index;
				index++;
				sideIndexList.add(temp);

				TextView latter_txt = new TextView(this);
				latter_txt.setText(latter);

				latter_txt.setSingleLine(true);
				latter_txt.setHorizontallyScrolling(false);
				latter_txt.setTypeface(null, Typeface.BOLD);
				latter_txt.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
						getResources().getDimension(R.dimen.sp_5));
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1);
				params.gravity = Gravity.CENTER_HORIZONTAL;

				latter_txt.setLayoutParams(params);
				latter_txt.setTextColor(getResources().getColor(R.color.section_side_text_color));
				latter_txt.setPadding(10, 0, 10, 0);

				sideIndex.addView(latter_txt);
			}
		}

		sideIndexSize = sideIndexList.size();

	}

	private TextWatcher filterTextWatcher = new TextWatcher() {

		public void afterTextChanged(Editable s) {

			if (s.toString().equalsIgnoreCase("e")
					&& displayCityList.get(0).equalsIgnoreCase(
							"Entire Malaysia")) {
				displayCityList.remove(0);
			} else if (!displayCityList.get(0).equalsIgnoreCase(
					"Entire Malaysia")) {
				displayCityList.add(0, "Entire Malaysia");
			}
			new StandardArrayAdapter(displayCityList).getFilter().filter(
					s.toString());

		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// your search logic here
		}

	};

	/*
	 * private void sortedArrayList(){
	 * 
	 * Collections.sort(displayValueList, new Comparator<DisplayValue>() {
	 * 
	 * @Override public int compare(DisplayValue o1, DisplayValue o2) { // TODO
	 * Auto-generated method stub if (o1.getValue() == null || o2.getValue() ==
	 * null) return 0; return o1.getValue().compareTo(o2.getValue());
	 * 
	 * } }); }
	 */

	// private String[] getBrandStringArray(){
	//
	// String[] brandArray = new String[displayValueList.size()];
	// for(int i=0;i<displayValueList.size();i++){
	// brandArray[i] = displayValueList.get(i).getValue();
	// }
	//
	// return brandArray;
	// }
	//

	// private void clearSelectedvalues(){
	//
	// for(int index = 0 ; index < displayValueList.size(); index++){
	// DisplayValue disObj = displayValueList.get(index);
	// disObj.setSelected(false);
	// displayValueList.set(index, disObj);
	// }
	//
	// setAdapter();
	//
	// }

	private void setAdapter() {

		// BRANDS = new ArrayList<String>(Arrays.asList(getBrandStringArray()));

		arrayAdapter = new StandardArrayAdapter(displayCityList);

		// adaptor for section
		sectionAdapter = new SectionListAdapter(this.getLayoutInflater(),
				arrayAdapter, false);
		listView.setAdapter(sectionAdapter);
	}
}
