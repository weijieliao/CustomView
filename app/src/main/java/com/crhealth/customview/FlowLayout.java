package com.crhealth.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weijieliao on 2016/12/29.
 */

public class FlowLayout extends ViewGroup {

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
//        return super.generateLayoutParams(attrs);

        return new MarginLayoutParams( getContext() , attrs ) ;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int modeWidth = MeasureSpec.getMode( widthMeasureSpec ) ;
        int modeHeight = MeasureSpec.getMode( heightMeasureSpec ) ;
        int sizeWidth = MeasureSpec.getSize( widthMeasureSpec ) ;
        int sizeHeight = MeasureSpec.getSize( heightMeasureSpec ) ;

        int width = 0 ;
        int height = 0 ;

        //记录每一行的宽度，width不断取最大宽度
        int lineWidth = 0 ;

        //每一行的高度，累加至height
        int lineHeight = 0 ;

        int cCount = getChildCount() ;

        for( int i = 0 ; i < cCount ; i++ ){
            View child = getChildAt( i ) ;
            //测量每一个child的宽和高
            measureChild( child , widthMeasureSpec , heightMeasureSpec ); ;
            MarginLayoutParams cParams = (MarginLayoutParams) child.getLayoutParams();
            //当前子空间实际占据的宽度
            int childWidth = child.getMeasuredWidth()+cParams.leftMargin+cParams.rightMargin ;
            //当前子空间实际占据的高度
            int childHeight = child.getMeasuredHeight()+cParams.topMargin+cParams.bottomMargin ;
            //如果加入当前child，则超出最大宽度，则的到目前最大宽度给width，类加height 然后开启新行
            if( lineWidth+childWidth > sizeWidth ){
                width = Math.max( lineWidth , childWidth ) ;
                // 重新开启新行，开始记录
                lineWidth = childWidth ;
                // 叠加当前高度
                height += lineHeight ;
                lineHeight = childHeight ;
            }
            else{
                // 否则累加值lineWidth,lineHeight取最大高度
                lineWidth += childWidth ;
                lineHeight = Math.max( lineHeight , childHeight ) ;
            }
            // 如果是最后一个，则将当前记录的最大宽度和当前lineWidth做比较
            if( i == cCount-1 ){
                width = Math.max( width , lineWidth ) ;
                height += lineHeight ;
            }

        }
        setMeasuredDimension( ( modeWidth == MeasureSpec.EXACTLY )?sizeWidth:width ,
                ( modeHeight == MeasureSpec.EXACTLY )?sizeHeight:height ) ;

    }

    //存储所有的View，按行记录
    private List<List<View>> mAllViews = new ArrayList<List<View>>() ;
    //记录每一行的最大高度
    private List<Integer> mLineHeight = new ArrayList<Integer>() ;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        mAllViews.clear() ;
        mLineHeight.clear() ;

        int width = getWidth() ;

        int lineWidth = 0 ;
        int lineHeight = 0 ;

        //存储每一行所有的childView
        List<View> lineViews = new ArrayList<View>() ;
        int cCount = getChildCount() ;
        for( int i = 0 ; i < cCount ; i++ ){
            View childView = getChildAt( i ) ;
            MarginLayoutParams cParams = (MarginLayoutParams) childView.getLayoutParams();
            int childWidth = childView.getMeasuredWidth() ;
            int childHeight = childView.getMeasuredHeight() ;

            //如果已经需要换行
            if( childWidth+cParams.leftMargin+cParams.rightMargin+lineWidth > width ){
                //记录这一行所有的View以及最大高度
                mLineHeight.add( lineHeight ) ;
                //将当前行的childView保存，然后开启新的ArrayList保存下一行的childView
                mAllViews.add( lineViews ) ;
                // 重置行宽
                lineWidth = 0 ;
                lineViews = new ArrayList<View>() ;
            }
            //如果不需要换行，则累加
            lineWidth += childWidth+cParams.leftMargin+cParams.rightMargin ;
            lineHeight = Math.max( lineHeight , childHeight+cParams.topMargin+cParams
                    .bottomMargin ) ;
            lineViews.add( childView ) ;
        }
        // 记录最后一行
        mLineHeight.add( lineHeight ) ;
        mAllViews.add( lineViews ) ;

        int left = 0 ;
        int top = 0 ;
        // 得到总行数
        int lineNums = mAllViews.size() ;
        for( int i = 0 ; i < lineNums ; i++ ) {
            lineViews = mAllViews.get( i ) ;
            lineHeight = mLineHeight.get( i ) ;
            for( int j = 0 ; j < lineViews.size() ; j++ ){
                View child = lineViews.get( j ) ;
                if (child.getVisibility() == View.GONE){
                    continue;
                }
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc =lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();
                child.layout(lc, tc, rc, bc);

                left += child.getMeasuredWidth() + lp.rightMargin
                        + lp.leftMargin;
            }
            left = 0;
            top += lineHeight;
        }

    }
}
