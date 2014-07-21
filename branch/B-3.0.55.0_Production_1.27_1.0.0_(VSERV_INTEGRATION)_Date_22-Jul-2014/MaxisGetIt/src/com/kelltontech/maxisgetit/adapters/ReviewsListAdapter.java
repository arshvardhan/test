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
import android.widget.RatingBar;
import android.widget.TextView;

import com.kelltontech.framework.imageloader.ImageLoader;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.dao.CompanyReview;

public class ReviewsListAdapter extends BaseAdapter {
	private Context mcontext;
	private ArrayList<CompanyReview> mCompReviewsList;
	private static Drawable dummyDrawable;
	private static Drawable errorDrawable;
	
	public ReviewsListAdapter(Context context) {
		mcontext = context;
	}
	public void setData(ArrayList<CompanyReview> categories){
		if(categories!=null && categories.size()>0)
			this.mCompReviewsList=categories;
	}
	@Override
	public int getCount() {
		if(mCompReviewsList!=null)
			return mCompReviewsList.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(mCompReviewsList!=null)
			return mCompReviewsList.get(position);
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
			LayoutInflater inflater 		= (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView 					= inflater.inflate(R.layout.reviews_list_row, null);
			holder 							= new Viewholder();
			holder.reviewTvUser 			= (TextView) convertView.findViewById(R.id.review_txt_user);
			holder.reviewTvDescription 		= (TextView) convertView.findViewById(R.id.review_txt_description);
			holder.reviewTvPostedOn 		= (TextView) convertView.findViewById(R.id.review_txt_posted_on);
			holder.reviewRating 			= (RatingBar) convertView.findViewById(R.id.review_rating);
			convertView.setTag(holder);
		} else {
			holder = (Viewholder) convertView.getTag();
		}
			CompanyReview companyReview 	= mCompReviewsList.get(position);
			if(!StringUtil.isNullOrEmpty(companyReview.getUserName()))
				holder.reviewTvUser.setText(Html.fromHtml(companyReview.getUserName()));
			else
				holder.reviewTvUser.setText("");
			
			if(!StringUtil.isNullOrEmpty(companyReview.getReviewDesc()))
				holder.reviewTvDescription.setText(Html.fromHtml(companyReview.getReviewDesc()));
			else
				holder.reviewTvDescription.setText("");
			
			if(!StringUtil.isNullOrEmpty(companyReview.getReportedOn()))
				holder.reviewTvPostedOn.setText(mcontext.getResources().getString(R.string.postedOn) + " " + Html.fromHtml(companyReview.getReportedOn()));
			else
				holder.reviewTvPostedOn.setText("");
			
			if(companyReview.getRating() > 0)
				holder.reviewRating.setRating(companyReview.getRating());
			else
				holder.reviewRating.setRating(0);
		return convertView;
	}

	class Viewholder {
		TextView reviewTvUser;
		TextView reviewTvDescription;
		TextView reviewTvPostedOn;
		RatingBar reviewRating;
		
	}

}
