package com.crhealth.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by weijieliao on 2016/12/29.
 */

public class CustomView01 extends ViewGroup {

    public CustomView01(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
//        return super.generateLayoutParams(attrs);

        return new MarginLayoutParams( getContext() , attrs ) ;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode( widthMeasureSpec ) ;
        int heightMode = MeasureSpec.getMode( heightMeasureSpec ) ;
        int sizeWidth = MeasureSpec.getSize( widthMeasureSpec ) ;
        int sizeHeight = MeasureSpec.getSize( heightMeasureSpec ) ;

        //计算出所有的childView的宽和高
        measureChildren( widthMeasureSpec , heightMeasureSpec ) ;

        //记录如果是wrap_content是设置的宽和高
        int width = 0 ;
        int height = 0 ;

        int cCount = getChildCount() ;

        int cWidth = 0 ;
        int cHeight = 0 ;
        MarginLayoutParams cParams = null ;

        //用于计算左边两个childView的高度
        int lHeight = 0 ;
        //用于计算右边两个childView的高度，最终高度取二者之间大值
        int rHeight = 0 ;

        //用于计算上边两个childView的宽度
        int tWidth = 0 ;
        //用于计算下面两个childiew的宽度，最终宽度取二者之间大值
        int bWidth = 0 ;

        //根据childView计算的出的宽和高，以及设置的margin计算容器的宽和高，主要用于容器是warp_content时
        for( int i = 0 ; i < cCount ; i++ ){
            View childView = getChildAt( i ) ;
            cWidth = childView.getMeasuredWidth() ;
            cHeight = childView.getMeasuredHeight() ;
            cParams = (MarginLayoutParams) childView.getLayoutParams();

            //上面两个childView
            if( i == 0 || i == 1 ){
                tWidth += cWidth+cParams.leftMargin+cParams.rightMargin ;
            }
            if( i == 2 || i == 3 ){
                bWidth += cWidth+cParams.leftMargin+cParams.rightMargin ;
            }
            if( i == 0 || i == 2 ){
                lHeight += cHeight+cParams.topMargin+cParams.bottomMargin ;
            }
            if( i == 1 || i == 3 ){
                rHeight += cHeight+cParams.topMargin+cParams.bottomMargin ;
            }
        }
        width = Math.max( tWidth , bWidth ) ;
        height = Math.max( lHeight , rHeight ) ;

        setMeasuredDimension( ( widthMode == MeasureSpec.EXACTLY )?sizeWidth:width ,
                ( heightMode == MeasureSpec.EXACTLY )?sizeHeight:height ) ;

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int cCount = getChildCount() ;
        int cWidth = 0 ;
        int cHeight = 0 ;
        MarginLayoutParams cParams = null ;

        //遍历所有childView根据其宽和高，以及margin进行布局
        for( int i = 0 ; i < cCount ; i++ ){
            View childView = getChildAt( i ) ;
            cWidth = childView.getMeasuredWidth() ;
            cHeight = childView.getMeasuredHeight() ;
            cParams = ( MarginLayoutParams )childView.getLayoutParams() ;

            int cl = 0 ;
            int cr = 0 ;
            int ct = 0 ;
            int cb = 0 ;
            switch( i ){
                case 0 :{
                    cl = cParams.leftMargin ;
                    ct = cParams.topMargin ;
                    break ;
                }
                case 1 :{
                    cl = getWidth()-cWidth-cParams.rightMargin ;
                    ct = cParams.topMargin ;
                    break ;
                }
                case 2 :{
                    cl = cParams.leftMargin ;
                    ct = getHeight()-cHeight-cParams.bottomMargin ;
                    break ;
                }
                case 3 :{
                    cl = getWidth()-cWidth-cParams.rightMargin ;
                    ct = getHeight()-cHeight-cParams.bottomMargin ;
                    break ;
                }
            }
            cr = cl+cWidth ;
            cb = ct+cHeight ;
            childView.layout( cl , ct , cr , cb ) ;
        }

    }


}
