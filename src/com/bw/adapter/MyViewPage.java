package com.bw.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPage extends ViewPager{
	private float mLastMotionX;

	public MyViewPage(Context context) {
		super(context);
	}

	public MyViewPage(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

//	@Override
//	public boolean onInterceptTouchEvent(MotionEvent ev) {
//		final int action = ev.getAction();
//		final float x = ev.getX();
//	
//		switch (action) {
//		case MotionEvent.ACTION_DOWN:
//			mLastMotionX = x;
//			break;
//	
//		case MotionEvent.ACTION_MOVE:
//			final float dx = x - mLastMotionX;
//			if(dx>0&&getCurrentItem()==0){ 
//				System.out.println("dx>0");
//				return false;
//			}else if(dx<0&&getCurrentItem()==getChildCount()-1){
//				System.out.println("dx>0");
//				return false;
//			}
//			break;
//		}
//	return  true;
//}
	  @Override
	    public boolean onInterceptTouchEvent(MotionEvent arg0) {
			//System.out.println("MyViewPage2 onInterceptTouchEvent");
	        // TODO Auto-generated method stub
	        //�����ش����¼������λ�õ�ʱ�򣬷���true��
	        //˵����onTouch�����ڴ˿ؼ�������ִ�д˿ؼ���onTouchEvent
	        return true;
	    }

	    /** ����ʱ���µĵ� **/
	    PointF downP = new PointF();
	    /** ����ʱ��ǰ�ĵ� **/
	    PointF curP = new PointF(); 
	    @SuppressLint("ClickableViewAccessibility")
		@Override
	    public boolean onTouchEvent(MotionEvent arg0) {
			//System.out.println("MyViewPage2 onTouchEvent");
	        // TODO Auto-generated method stub
	        //ÿ�ν���onTouch�¼�����¼��ǰ�İ��µ�����
	        curP.x = arg0.getX();
	        curP.y = arg0.getY();

	        if(arg0.getAction() == MotionEvent.ACTION_DOWN){
	            //��¼����ʱ�������
	            //�мǲ����� downP = curP �������ڸı�curP��ʱ��downPҲ��ı�
	            downP.x = arg0.getX();
	            downP.y = arg0.getY();
	            //�˾������Ϊ��֪ͨ���ĸ�ViewPager���ڽ��е��Ǳ��ؼ��Ĳ�������Ҫ���ҵĲ������и���
	            getParent().requestDisallowInterceptTouchEvent(true);
	        }

	        if(arg0.getAction() == MotionEvent.ACTION_MOVE){
	            //�˾������Ϊ��֪ͨ���ĸ�ViewPager���ڽ��е��Ǳ��ؼ��Ĳ�������Ҫ���ҵĲ������и���
	            getParent().requestDisallowInterceptTouchEvent(true);
	        }

	        if(arg0.getAction() == MotionEvent.ACTION_UP){
	            //��upʱ�ж��Ƿ��º����ֵ�����Ϊһ����
	            //�����һ���㣬��ִ�е���¼����������Լ�д�ĵ���¼���������onclick
	            if(downP.x==curP.x && downP.y==curP.y){
//	                onSingleTouch();
	                return true;
	            }
	        }

	        try{
				return super.onTouchEvent(arg0);
			} catch(IllegalArgumentException ex) {  
				ex.printStackTrace();
			}  
			return false; 
	    }
}