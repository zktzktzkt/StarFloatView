package com.smoke.zhangchen.floatviewlib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smoke.zhangchen.floatviewlib.utils.ScreenSizeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2018/3/13 0013.
 */

public class FloatView extends RelativeLayout {
    private static final long ANIMATION_TIME = 1000;
    private static final long ANIMATION_DEFAULT_TIME = 2000;
    private static final String TAG = "FloatView";
    private Context mcontext;
    private List<? extends Number> mFloat;
    private List<View> mViews = new ArrayList<>();;
    public RelativeLayout parentView;
    private int parentWidth;
    private int parentHeight;
    private TextView defaultView;
    private OnItemClickListener mListener;
    private int textColor;
    private int childId;
    private int parentId;
    private String defaultViewText;
    private float childSize;

    public FloatView(Context context) {
        this(context,null);
        mcontext = context;
    }

    public FloatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mcontext = context;
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.myFloatView);
        textColor = typedArray.getColor(R.styleable.myFloatView_childTextColor, getResources().getColor(R.color.white));
        childSize = typedArray.getDimension(R.styleable.myFloatView_chidTextSize, 6);
        childId = typedArray.getResourceId(R.styleable.myFloatView_childViewBackground, R.drawable.shape_circle);
        parentId = typedArray.getResourceId(R.styleable.myFloatView_parentViewBackground, R.mipmap.star_bg);
        defaultViewText = typedArray.getString(R.styleable.myFloatView_defaultViewText);
        //一定会要释放资源
        typedArray.recycle();
    }

    public FloatView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
        mcontext = context;
    }

    private void init() {
        setDefaultView();
        addChidView();
    }

    //添加小球
    private void addChidView() {
        for (int i = 0; i < mFloat.size(); i++) {
            TextView floatview = (TextView) LayoutInflater.from(mcontext).inflate(R.layout.view_float, this, false);
            floatview.setTextColor(textColor);
            floatview.setTextSize(childSize);
            floatview.setBackgroundResource(childId);
            floatview.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            floatview.setText(mFloat.get(i)+"");
            floatview.setTag(i);
            floatview.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    childClick(v);
                }
            });
            setChildViewPosition(floatview);
            initAnim(floatview);
            initFloatAnim(floatview);
            mViews.add(floatview);
            addView(floatview);
        }
    }

    //设置初始化的小球
    private void setDefaultView() {
        parentView = (RelativeLayout) LayoutInflater.from(mcontext).inflate(R.layout.view_item, this, true);
        parentView.setBackgroundResource(parentId);
        int width = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        parentView.measure(width,height);
        parentHeight = parentView.getMeasuredHeight();
        parentWidth = parentView.getMeasuredWidth();


        LayoutParams params = new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        defaultView = (TextView) LayoutInflater.from(mcontext).inflate(R.layout.view_float, this, false);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        defaultView.setTextColor(textColor);
        defaultView.setTextSize(childSize);
        defaultView.setText(defaultViewText);
        defaultView.setBackgroundResource(childId);
        if (mFloat.size() != 0){
            defaultView.setVisibility(GONE);
        }
        addView(defaultView,params);
        //设置动画
        initAnim(defaultView);
        //设置上下抖动的动画
        initFloatAnim(defaultView);
    }
    //FloatView上下抖动的动画
    private void initFloatAnim(View view) {
        Animation anim = new TranslateAnimation(0,0,-10,20);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(ANIMATION_TIME);
        anim.setRepeatCount(Integer.MAX_VALUE);
        anim.setRepeatMode(Animation.REVERSE);//反方向执行
        view.startAnimation(anim);
    }

    //FloatView初始化时动画
    private void initAnim(View view) {
        view.setAlpha(0);
        view.setScaleX(0);
        view.setScaleY(0);
        view.animate().alpha(1).scaleX(1).scaleY(1).setDuration(ANIMATION_DEFAULT_TIME).start();
    }

    //设置数据添加子小球
    public void setList(List<? extends Number> list){
        this.mFloat = list;
        //使用post方法确保在UI加载完的情况下 调用init() 避免获取到的宽高为0
        post(new Runnable() {
            @Override
            public void run() {
                init();
            }
        });
    }

    //设置子view的位置
    private void setChildViewPosition(View childView) {
        //设置随机位置
        Random randomX = new Random();
        Random randomY = new Random();
        float x = randomX.nextFloat() * (parentWidth - childView.getMeasuredWidth());
        float y = randomY.nextFloat() * (parentHeight - childView.getMeasuredHeight());
        Log.d(TAG, "setChildViewPosition: parentWidth="+parentWidth+",parentHeight="+parentHeight);
        Log.d(TAG, "setChildViewPosition: childWidth="+childView.getMeasuredWidth()+",childHeight="+childView.getMeasuredHeight());
        Log.d(TAG, "setChildViewPosition: x="+x+",y="+y);
        childView.setX(x);
        childView.setY(y);
    }

    private void childClick(View view) {
        //设置接口回调

        mViews.remove(view);
        animRemoveView(view);
        if (mViews.size() == 0){
            defaultView.setVisibility(VISIBLE);
        }
        mListener.itemClick((int)view.getTag(),mFloat.get((int)view.getTag()));
    }

    private void animRemoveView(final View view) {
//
        ValueAnimator animator = ValueAnimator.ofFloat(parentHeight,0);
        animator.setDuration(1000);
        animator.setInterpolator(new LinearInterpolator());

        //动画更新的监听
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float Value = (float) animation.getAnimatedValue();
                Log.d(TAG, "onAnimationUpdate: "+view.getTranslationY());
                Log.d(TAG, "onAnimationUpdate: "+view.getY());
                view.setAlpha(Value/parentHeight);
                view.setTranslationY(view.getY()-(parentHeight-Value));
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                removeView(view);
            }
        });
        animator.start();
    }

    public interface OnItemClickListener{
        void  itemClick(int position, Number value);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
}
