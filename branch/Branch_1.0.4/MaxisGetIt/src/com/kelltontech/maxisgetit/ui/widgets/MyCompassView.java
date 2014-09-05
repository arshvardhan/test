package com.kelltontech.maxisgetit.ui.widgets;
import com.kelltontech.maxisgetit.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;

public class MyCompassView extends View {

  private Paint paint;
  private float position = 0;
  private Bitmap outerCircleBmp;
  private Bitmap handBmp;
  private Bitmap textBg;
  private float currentDegree = 0f;
  
  private float mAngleFromNorth, mBearing;
private String mDistance;
  
  MyCompassView(Context context,AttributeSet attributeSet){
	  super(context, attributeSet);
	  init();
  }
  public MyCompassView(Context context) {
    super(context);
    init();
  }

  private void init() {
    paint = new Paint();
    paint.setAntiAlias(true);
    paint.setStrokeWidth(2);
    paint.setTextSize(25);
    paint.setStyle(Paint.Style.STROKE);
    paint.setColor(Color.BLACK);
    textBg=BitmapFactory.decodeResource(getResources(), R.drawable.meterbox);
    outerCircleBmp=BitmapFactory.decodeResource(getResources(), R.drawable.outercircle);
    handBmp=BitmapFactory.decodeResource(getResources(), R.drawable.hand);
  }
  public static Bitmap RotateBitmap(Bitmap source, float angle)
  {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap temp= Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        source.recycle();
        return temp;
  }
  
  //By Rupesh Sir
  /*@Override
  protected void onDraw(Canvas canvas) {
    int xPoint = getMeasuredWidth() / 2;
    int yPoint = getMeasuredHeight() / 2;
    
    Matrix omatrix = new Matrix();
    omatrix.setTranslate((getMeasuredWidth()-outerCircleBmp.getWidth())/2, (getMeasuredHeight()-outerCircleBmp.getHeight())/2);
  //  omatrix.postRotate(-position-45, getMeasuredWidth()/2, getMeasuredHeight()/2);
//    omatrix.setRotate(currentDegree-45, outerCircleBmp.getWidth()/2, outerCircleBmp.getHeight()/2);
    canvas.drawBitmap(outerCircleBmp, omatrix, null);
    
    Matrix matrix = new Matrix();
    matrix.setRotate(-position, handBmp.getWidth()/2, handBmp.getHeight()/2);
    matrix.postTranslate((getMeasuredWidth()-handBmp.getWidth())/2, (getMeasuredHeight()-handBmp.getHeight())/2);
    canvas.drawBitmap(handBmp, matrix, null);
//    canvas.drawBitmap(outerCircleBmp, (getMeasuredWidth()-outerCircleBmp.getWidth())/2,(getMeasuredHeight()-outerCircleBmp.getHeight())/2, null);
    float radius = (float) (Math.max(xPoint, yPoint) * 0.6);
//    canvas.drawCircle(xPoint, yPoint, radius, paint);
//    canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), paint);
    // 3.143 is a good approximation for the circle
//    canvas.drawLine(xPoint, yPoint,(float) (xPoint + radius
//    		* Math.sin((double) (-position) / 180 * 3.143)),
//        (float) (yPoint - radius
//            * Math.cos((double) (-position) / 180 * 3.143)), paint);
    String content=String.valueOf(position);
    Rect bounds=new Rect();
    paint.getTextBounds(content, 0	, content.length(), bounds);
//    canvas.drawBitmap(handBmp, null, bounds, null);
//    canvas.drawText(content, (getMeasuredWidth()-bounds.width())/2, (getMeasuredHeight()-bounds.height())/2, paint);
//    Log.e("maxis", String.valueOf(position));
    currentDegree=-position;
  }*/
  
  protected void onDraw(Canvas canvas) {
	    int xPoint = getMeasuredWidth() / 2;
	    int yPoint = getMeasuredHeight() / 2;
	    
	    Matrix omatrix = new Matrix();
	    omatrix.setTranslate((getMeasuredWidth()-outerCircleBmp.getWidth())/2, (getMeasuredHeight()-outerCircleBmp.getHeight())/2);
	    omatrix.postRotate(-mAngleFromNorth, getMeasuredWidth()/2, getMeasuredHeight()/2);
//	    omatrix.setRotate(currentDegree-45, outerCircleBmp.getWidth()/2, outerCircleBmp.getHeight()/2);
	    canvas.drawBitmap(outerCircleBmp, omatrix, null);
//	    28.589345,77.040825 dwarka
//	    okhla 28.57608,77.288017
//	    sector 92 28.408917,76.910362
//	    pali 28.393213,77.248878
	    Matrix matrix = new Matrix();
	    matrix.setRotate(-mAngleFromNorth + mBearing, handBmp.getWidth()/2, handBmp.getHeight()/2);
	    matrix.postTranslate((getMeasuredWidth()-handBmp.getWidth())/2, (getMeasuredHeight()-handBmp.getHeight())/2);
	    canvas.drawBitmap(handBmp, matrix, null);
	    
	    Matrix matrix1 = new Matrix();
	    matrix1.postTranslate((getMeasuredWidth()-textBg.getWidth())/2, (getMeasuredHeight()-textBg.getHeight())/2);
	    canvas.drawBitmap(textBg, matrix1, paint);
	    
//	    canvas.drawBitmap(outerCircleBmp, (getMeasuredWidth()-outerCircleBmp.getWidth())/2,(getMeasuredHeight()-outerCircleBmp.getHeight())/2, null);
	    float radius = (float) (Math.max(xPoint, yPoint) * 0.6);
//	    canvas.drawCircle(xPoint, yPoint, radius, paint);
//	    canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), paint);
	    // 3.143 is a good approximation for the circle
//	    canvas.drawLine(xPoint, yPoint,(float) (xPoint + radius
//	    		* Math.sin((double) (-position) / 180 * 3.143)),
//	        (float) (yPoint - radius
//	            * Math.cos((double) (-position) / 180 * 3.143)), paint);
	    String content= mDistance == null ? "0 KM" : String.valueOf(mDistance);
	    Rect bounds=new Rect();
	    paint.setTextSize(45);
	    paint.getTextBounds(content, 0	, content.length(), bounds);
	    paint.setStyle(Paint.Style.FILL_AND_STROKE);
	    //paint.setARGB(127, 49, 49, 49);
	    //paint.setARGB(255, 200, 200, 200);
	    //Rect boundryRect  = new Rect((getMeasuredWidth()-bounds.width())/2 - 10, (getMeasuredHeight()-bounds.height())/2 + 15, getMeasuredWidth()-bounds.width() + 20, getMeasuredHeight()-bounds.height() + 25);
	   // canvas.drawRect(boundryRect, paint);
	    paint.setARGB(255, 73, 73, 73);
	    //canvas.drawArc(boundryRect, 0.0f, 0.0f, false, paint);
	   
	    canvas.drawText(content, (getMeasuredWidth()-bounds.width())/2, (getMeasuredHeight()-bounds.height())/2 + 35, paint);
//	    Log.e("maxis", String.valueOf(position));
	    currentDegree=-position;
	  }

  public void updateData(float position) {
    this.position = position;
    invalidate();
  }
  
  public void updateData(float angleFromNorth, float bearing, String distance)
  {
	  mAngleFromNorth 	= angleFromNorth;
	  mBearing		 	= bearing;
	  mDistance			= distance;
	  invalidate();
  }

} 