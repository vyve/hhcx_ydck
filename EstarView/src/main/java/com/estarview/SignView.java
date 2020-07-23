package com.estarview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint("DrawAllocation")
public class SignView extends View{
	private Context context;
	private int width ;//屏幕宽度；
	private int height ;//屏幕高度
	private int radius;//圆半径
	private boolean left_button=false;//默认左边按钮未被点击
	private boolean right_button=false;//默认右边按钮未被点击
	private Sign_click sign_click;

	public static int SING=0;//签到初始状态
	public static int SINGIN=1;//签到状态
	public static int SINGOUT=2;//签出状态

	private int sign=SING;//签到状态

	private Bitmap bmp_sign_on = BitmapFactory.decodeResource(this.getResources(), R.drawable.sign_on);
	private Bitmap bmp_sign_off = BitmapFactory.decodeResource(this.getResources(), R.drawable.sign_off); 
	
	public SignView(Context context) {
		super(context);
		this.context=context;
	}

	public SignView(Context context, AttributeSet attrs) {
		super( context, attrs );
		this.context=context;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float getX=event.getX();
		float getY=event.getY();
		float x=getX-width/2;
		float y=getY-height/2;
		if((x*x+y*y)<=(radius*radius)){
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if(y<=-x){
				left_button=true;
				invalidate();
			}else{
				right_button=true;
				invalidate();
			}
			break;
		case MotionEvent.ACTION_UP:
			if(y<=-x){
				left_button=false;
				invalidate();
				sign_click.signClick(1);
			}else{
				right_button=false;
				invalidate();
				sign_click.signClick(2);
			}
			break;

		default:
			break;
		   }
		}
		return true;
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (changed) {
			height = this.getMeasuredHeight();
			width  = this.getMeasuredWidth();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint p=new Paint();
		radius=width/2-80;
		p.setDither(true);
		p.setAntiAlias(true);
		RectF oval=new RectF(80, height/2-radius, width-80, height/2+radius);
		
		p.setColor(Color.WHITE);
		canvas.drawCircle(width/2, height/2, radius+6, p);
		
		//绘制右边半圆
		if(!right_button){
			p.setARGB(255, 65,105, 255);
		}else{
//			p.setARGB(255, 65,105, 200);
			p.setARGB(255, 0, 0, 200);
		}
		
		canvas.drawArc(oval, -45, 180, false, p);
		
		//绘制左边半圆
		if(!left_button){
			p.setARGB(255, 65,105, 255);
		}else{
//			p.setARGB(255, 65,105, 200);
			p.setARGB(255, 0, 0, 200);
		}
//		if(!left_button){
//			p.setARGB(255, 0, 0, 255);
//		}else{
//			p.setARGB(255, 0, 0, 200);
//		}
		canvas.drawArc(oval, 135, 180, false, p);



		/**
		 * 修改签到签退状态 0 签到初始状态  1 签到状态  2 签出状态
		 * @param sign
		 */
		if (sign==SINGIN){//上班状态
			p.setARGB(255, 65,105, 200);
			canvas.drawArc(oval, -45, 180, false, p);
			p.setARGB(255, 0, 0, 200);
			canvas.drawArc(oval, 135, 180, false, p);
		}else if (sign==SINGOUT){//下班状态
			p.setARGB(255, 0, 0, 200);
			canvas.drawArc(oval, -45, 180, false, p);
			p.setARGB(255, 65,105, 255);
			canvas.drawArc(oval, 135, 180, false, p);
		}
		
		//将上下班签到图标和文字绘制上去
		p.setTextSize(3*radius/16);
		p.setColor(Color.WHITE);
		canvas.drawBitmap(bmp_sign_on, width/2-5*radius/7, height/2-2*radius/5, null);
		canvas.drawText("上班签到", width/2-4*radius/9, height/2-11*radius/20, p);
		canvas.drawBitmap(bmp_sign_off, width/2-radius/6, height/2+radius/4, null);
		canvas.drawText("下班签退", width/2+radius/8, height/2+radius/10, p);
		
		p.setColor(Color.WHITE);
		p.setStrokeWidth(6);
		canvas.drawLine((float)(width/2-radius/Math.sqrt(2)), (float)(height/2+radius/Math.sqrt(2)), (float)(width/2+radius/Math.sqrt(2)), (float)(height/2-radius/Math.sqrt(2)), p);
		super.onDraw(canvas);
	}
	
	
	public void setSign_click(Sign_click sign_click) {
		this.sign_click = sign_click;
	}


	public interface Sign_click{
		void signClick(int a) ;
	}

	/**
	 * 修改签到签退状态 0 签到初始状态  1 签到状态  2 签出状态
	 * @param sign
     */
	public void setSignIn(int sign){
		this.sign=sign;
		invalidate();
	}

}