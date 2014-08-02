package com.kelltontech.maxisgetit.ui.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ListView;

public class ExpandableListView extends ListView {

    private android.view.ViewGroup.LayoutParams params;
    private int old_count = 0;

    public ExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getCount() != old_count) {
            old_count = getCount();
            params = getLayoutParams();
            params.height = ((old_count > 0 && getCount() > 0) ? (getCount() * (getChildAt(0).getHeight() + getDividerHeight()) - getDividerHeight()) : 0);

            setLayoutParams(params);
        }

        super.onDraw(canvas);
    }

}