package com.mafeibiao.testapplication.customView;

/**
 * Created by mafeibiao on 2017/11/21.
 */


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.mafeibiao.testapplication.R;


public class MyEditText extends AppCompatEditText {

    // 每隔多少位以空格进行分隔一次，卡号一般都是每4位以空格分隔一次
    public int splitNumber = 4;
    // 自定义输入框的模式 当值为true:银行卡号输入框模式，false:普通输入框模式
    private int editTextMode = 1;

    public MyEditText(Context context) {
        this(context, null);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    // 内容清除图标
    private Drawable mClearDrawable;

    /**
     * 初始化方法
     */
    private void init(AttributeSet attrs) {
        // 设置单行显示所有输入框内容
        setSingleLine();
        // 设置输入框可获得焦点
        setFocusable(true);
        setFocusableInTouchMode(true);
        TypedArray t = this.getResources().obtainAttributes(attrs, R.styleable.MyEditText);
        editTextMode = t.getInt(R.styleable.MyEditText_editTextMode, editTextMode);
        splitNumber = t.getInt(R.styleable.MyEditText_splitNumber, splitNumber);
        t.recycle();
        mClearDrawable = this.getResources().getDrawable(R.drawable.clear);
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
        initEvent();
    }

    // 输入框内容改变后onTextChanged方法会调用多次，设置一个变量让其每次改变之后只调用一次
    private boolean isTextChanged = false;

    /**
     * 处理事件的方法
     */
    private void initEvent() {
        addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isTextChanged) {
                    isTextChanged = false;
                    return;
                }
                isTextChanged = true;
                // 处理输入内容空格与位数以及光标位置的逻辑
                handleInputContent(s, start,before,count);
                // 处理清除图标的显示与隐藏逻辑
                handleClearIcon(true);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    // 卡号内容
    private String content;
    // 卡号最大长度,卡号一般最长19位
    public static final int MAX_CARD_NUMBER_LENGHT = 19;
    //手机号
    public static final int MAX_PHONE_NUMBER_LENGHT = 11;
    // 缓冲分隔后的新内容串
    private String result = "";

    /**
     * 处理输入内容空格与位数的逻辑
     */
    private void handleInputContent(CharSequence s, int start, int before, int count) {
        content = s.toString();
        // 先缓存输入框内容
        result = content;
        // 去掉空格，以防止用户自己输入空格
        content = content.replace(" ", "");
        switch (editTextMode){
            case 1://普通模式
                break;
            case 2://银行卡号模式

                // 限制输入的数字位数最多21位（银行卡号一般最多21位）
                if (content != null && content.length() <= MAX_CARD_NUMBER_LENGHT) {
                    result = "";
                    int i = 0;
                    // 先把splitNumber倍的字符串进行分隔
                    while (i + splitNumber < content.length()) {
                        result += content.substring(i, i + splitNumber) + " ";
                        i += splitNumber;
                    }
                    // 最后把不够splitNumber倍的字符串加到末尾
                    result += content.substring(i, content.length());
                } else {
                    // 如果用户输入的位数
                    result = result.substring(0, result.length() - 1);
                }

                break;
            case 3://手机号模式

                if (content != null && content.length() <= MAX_PHONE_NUMBER_LENGHT) {
                    int length = s.toString().length();
                    if (length == 3 || length == 8){
                        result += " ";
                    }
                } else {
                    // 如果用户输入的位数
                    result = result.substring(0, result.length() - 1);
                }

                break;
        }
        // 获取光标开始位置
        // 必须放在设置内容之前
        int j = getSelectionStart();
        setText(result);
        // 处理光标位置
        handleCursor(before, j);

    }


    /**
     * 处理光标位置
     *
     * @param before
     * @param j
     */
    private void handleCursor(int before, int j) {
        // 处理光标位置
        try {
            if (j + 1 < result.length()) {
                // 添加字符
                if (before == 0) {
                    // 遇到空格，光标跳过空格，定位到空格后的位置
                    if (j % splitNumber + 1 == 0) {
                        setSelection(j + 1);
                    } else {
                        // 否则，光标定位到内容之后 （光标默认定位方式）
                        setSelection(result.length());
                    }
                    // 回退清除一个字符
                } else if (before == 1) {
                    // 回退到上一个位置（遇空格跳过）
                    setSelection(j);
                }
            } else {
                MyEditText.this.setSelection(result.length());
            }
        } catch (Exception e) {

        }
    }


    /**
     * 处理清除图标的逻辑
     */
    private void handleClearIcon(boolean focused) {
        if (content != null && content.length() > 0) {
            // 显示
            if (focused) {
                setEditTextIcon(null, null, mClearDrawable, null);
            } else {
                // 隐藏
                setEditTextIcon(null, null, null, null);
            }
        } else {
            // 隐藏
            setEditTextIcon(null, null, null, null);
        }
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 获取用户点击的坐标，这里只对X轴做了判断，
        float x = event.getX();
        // 当用户抬起手指时，判断坐标是否在图标交互区域，如果在则清空输入框内容，同时隐藏图标自己
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (x > (getWidth() - getPaddingRight() - mClearDrawable.getIntrinsicWidth())) {
                // 清空输入框内容
                setText("");
                // 隐藏图标
                setEditTextIcon(null, null, null, null);
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        handleClearIcon(focused);
        //刷新界面，防止有时候出现的不刷新界面情况
        invalidate();
    }

    /**
     * 设置输入框的左，上，右，下图标
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    private void setEditTextIcon(Drawable left, Drawable top, Drawable right, Drawable bottom) {

        setCompoundDrawables(left, top, right, bottom);
    }

    /**
     * 重写onMeasure,主要目的是让EditText的高度与我们显示在右侧的清空图标的高度相同，否则输入的时候会动态改变EditText的高度
     * 用户体验不好
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.AT_MOST
                && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mClearDrawable.getIntrinsicWidth(), heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, mClearDrawable.getIntrinsicHeight());
        }
    }
}