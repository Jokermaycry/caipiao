package com.ailide.apartmentsabc.tools.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ailide.apartmentsabc.R;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.utils.AutoUtils;

import framework.utils.KeyBoardUtil;
import me.huha.fontmanager.FontCallback;
import me.huha.fontmanager.FontManager;

/**
 * Created by Wenxc on 2017/2/13.
 * 仿照android.support.design.widget.TextInputLayout
 */

public class MustInputLayout extends AutoFrameLayout implements FontCallback {

    private ImageView ivClear;
    private EditText editText;
    private TextView tvHintLabel;
    private View baseLine;
    private static final int ANIM_DURATION = 500;
    private static final int DEFAULT_MARGIN_SIZE = 20;//px
    private static final int DEFAULT_PASSWORD_MARGIN_SIZE = 10;//px
    private static final float DEFAULT_SCALE_RATE = 24.0f / 30;
    private float scaleRate;
    private AnimationSet enterAnimSet;
    private AnimationSet exitAnimSet;
    private CheckedTextView ivPwd;
    private TextWatcher textWatcher;
    private ClearMode clearMode = ClearMode.NONE;
    private int textSize;

    @Override
    public void fontUpdate(float scaleSize) {
        switch ((int) scaleSize) {
            case 0:
                editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize - 4);
                tvHintLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize * scaleRate - 4);
                break;
            case 1:
                editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                tvHintLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize * scaleRate);
                break;
            case 2:
                editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize + 4);
                tvHintLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize * scaleRate + 4);
                break;
            case 3:
                editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize + 8);
                tvHintLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize * scaleRate + 8);
                break;
            case 4:
                editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize + 12);
                tvHintLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize * scaleRate + 12);
                break;
        }
        AutoUtils.autoTextSize(editText);
        AutoUtils.autoTextSize(tvHintLabel);
    }

    private enum ClearMode {
        TEXT, FOCUS, NONE
    }


    public void addTextChangedListener(TextWatcher textWatcher) {
        this.textWatcher = textWatcher;
    }

    public MustInputLayout(Context context) {
        this(context, null);
    }

    public MustInputLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public MustInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.widget_must_input_layout, null);
        ViewGroup.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ((FrameLayout.LayoutParams) params).gravity = Gravity.BOTTOM;
        addView(inflate, params);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.InputLayout);
        int inputType = typedArray.getInt(R.styleable.InputLayout_inputType, EditorInfo.TYPE_NULL);
        ColorStateList textColor = typedArray.getColorStateList(R.styleable.InputLayout_textColor);
        ColorStateList textColorHint = typedArray.getColorStateList(R.styleable.InputLayout_textColorHint);
        textSize = typedArray.getDimensionPixelSize(R.styleable.InputLayout_textSize, 15);
        int maxLines = typedArray.getInt(R.styleable.InputLayout_maxLines, -1);
        String hint = typedArray.getString(R.styleable.InputLayout_hint);
        String text = typedArray.getString(R.styleable.InputLayout_text);
        int bottomMargin = typedArray.getDimensionPixelSize(R.styleable.InputLayout_bottomMargin, DEFAULT_MARGIN_SIZE);
        int centerMargin = typedArray.getDimensionPixelSize(R.styleable.InputLayout_centerMargin, DEFAULT_MARGIN_SIZE);
        int margin = typedArray.getInt(R.styleable.InputLayout_margin, DEFAULT_MARGIN_SIZE);
        boolean isPasswordToggleEnable = typedArray.getBoolean(R.styleable.InputLayout_passwordToggleEnable, false);
        int passwordMargin = typedArray.getDimensionPixelSize(R.styleable.InputLayout_passwordMargin, DEFAULT_PASSWORD_MARGIN_SIZE);
        int maxLength = typedArray.getInteger(R.styleable.InputLayout_maxLength, -1);
        scaleRate = typedArray.getFloat(R.styleable.InputLayout_scaleRate, DEFAULT_SCALE_RATE);

        typedArray.recycle();

        if (margin != DEFAULT_MARGIN_SIZE) {
            bottomMargin = centerMargin = margin;
        }

        tvHintLabel = (TextView) findViewById(R.id.hint_label);
        ivClear = (ImageView) findViewById(R.id.iv_clear);
        editText = (EditText) findViewById(R.id.editText);
        baseLine = findViewById(R.id.baseLine);
        ivPwd = (CheckedTextView) findViewById(R.id.iv_pwd);
        ViewGroup.LayoutParams pwdParam = ivPwd.getLayoutParams();
        if (pwdParam instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) pwdParam).leftMargin = passwordMargin;
        }
        ivPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ivPwdChecked = ivPwd.isChecked();
                if (ivPwdChecked) {
                    editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                ivPwd.setChecked(!ivPwdChecked);
            }
        });

        tvHintLabel.setVisibility(INVISIBLE);
        setPasswordVisibilityToggleEnabled(isPasswordToggleEnable);

        editText.setOnFocusChangeListener(new FocusChangeListenerImpl());
        editText.setInputType(inputType);
        if (maxLines > -1) {
            editText.setMaxLines(maxLines);
        }

        if (maxLength != -1) {
            //手动设置maxLength为10
            InputFilter[] filters = {new InputFilter.LengthFilter(maxLength)};
            editText.setFilters(filters);
        }
        editText.setText(text);
        if (hint != null) editText.setHint(hint);
        if (hint != null) tvHintLabel.setHint(hint);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        tvHintLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, scaleRate * textSize);

        editText.setTextColor(textColor != null ? textColor : ColorStateList.valueOf(0xFF333333));
        editText.setHintTextColor(textColorHint != null ? textColorHint : ColorStateList.valueOf(0x33333333));
        tvHintLabel.setHintTextColor(textColorHint != null ? textColorHint : ColorStateList.valueOf(0x33333333));
        editText.addTextChangedListener(new TextWatcherImpl());
        ViewGroup.LayoutParams layoutParams = editText.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) layoutParams).topMargin = centerMargin;
            ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin = bottomMargin;
        }
        ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (clearMode) {
                    case FOCUS:
                        setClearDrawableVisible(false);
                        editText.clearFocus();
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    case TEXT:
                        editText.setText("");
                    case NONE:
                        break;
                }
            }
        });

        //init font manager

        AutoUtils.auto(editText);
        AutoUtils.auto(tvHintLabel);
        AutoUtils.auto(ivPwd);
        fontUpdate(FontManager.getInstance().getScaleType());

        FontManager.getInstance().addFontCallback(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Rect outRect = new Rect();
            ivClear.getGlobalVisibleRect(outRect);
            if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                if (!editText.isFocused()) {
                    editText.requestFocus();
                    KeyBoardUtil.openKeybord(editText, editText.getContext());
                }

            }
        }
        return super.onTouchEvent(event);
    }

    public void setPasswordVisibilityToggleEnabled(boolean enabled) {
        if (enabled) {
//            int inputType = editText.getInputType();
//            if (inputType == InputType.TYPE_NUMBER_VARIATION_PASSWORD ||
//                    inputType == InputType.TYPE_TEXT_VARIATION_PASSWORD ||
//                    inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD ||
//                    inputType == InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD) {
            //只有密码类型的输入框才允许开启密码查看功能
            ivPwd.setVisibility(VISIBLE);
//            }
        } else {
            ivPwd.setVisibility(GONE);
        }
    }

    public String getText() {
        return editText.getText().toString();
    }

    public EditText getEditText() {
        return editText;
    }

    private class FocusChangeListenerImpl implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            final boolean isVisible = editText.getText().toString().length() >= 1;
            if (hasFocus) {
                clearMode = ClearMode.FOCUS;
                if (isVisible) clearMode = ClearMode.TEXT;
                enterAnim(isVisible);
                setClearDrawableVisible(true);
                baseLine.setBackgroundResource(R.drawable.drawable_baseline_activated);
            } else {
                setClearDrawableVisible(false);
                clearMode = ClearMode.NONE;
                exitAnim(isVisible);
                baseLine.setBackgroundResource(R.drawable.drawable_baseline_default);
            }
        }
    }

    private void enterAnim(boolean isNeed) {
        if (enterAnimSet == null) {
            enterAnimSet = new AnimationSet(true);
            Animation translateAnim = new TranslateAnimation(editText.getLeft(), tvHintLabel.getLeft(), editText.getTop(), tvHintLabel.getTop());
            Animation scaleAnim = new ScaleAnimation(1.0f / scaleRate, 1.0f, 1.0f / scaleRate, 1.0f);
            enterAnimSet.addAnimation(scaleAnim);
            enterAnimSet.addAnimation(translateAnim);
            enterAnimSet.setDuration(ANIM_DURATION);
            enterAnimSet.setInterpolator(new AccelerateDecelerateInterpolator());
            enterAnimSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    editText.setHint("");
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    tvHintLabel.setVisibility(VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        if (!isNeed) {
            if (!enterAnimSet.hasEnded()) {
                enterAnimSet.cancel();
                enterAnimSet.reset();
            }
            tvHintLabel.startAnimation(enterAnimSet);
        }
    }

    private void exitAnim(final boolean isNeed) {
        if (exitAnimSet == null) {
            exitAnimSet = new AnimationSet(true);
            Animation translateAnim = new TranslateAnimation(tvHintLabel.getLeft(), editText.getLeft(), tvHintLabel.getTop(), editText.getTop());
            Animation scaleAnim = new ScaleAnimation(1.0f, 1.0f / scaleRate, 1.0f, 1.0f / scaleRate);
            exitAnimSet.addAnimation(scaleAnim);
            exitAnimSet.addAnimation(translateAnim);
            exitAnimSet.setDuration(ANIM_DURATION);
            exitAnimSet.setInterpolator(new AccelerateDecelerateInterpolator());
            exitAnimSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    tvHintLabel.setVisibility(INVISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    editText.setHint(tvHintLabel.getHint());
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        if (!isNeed) {
            if (!enterAnimSet.hasEnded()) {
                enterAnimSet.cancel();
                enterAnimSet.reset();
            }
            tvHintLabel.startAnimation(exitAnimSet);
        }
    }

    // 当输入结束后判断是否显示右边clean的图标
    private class TextWatcherImpl implements TextWatcher {
        @Override
        public void afterTextChanged(Editable s) {
            boolean isVisible = editText.getText().toString().length() >= 1;
//            setClearDrawableVisible(isVisible);
            clearMode = isVisible ? ClearMode.TEXT : ClearMode.FOCUS;
            if (textWatcher != null) textWatcher.afterTextChanged(s);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (textWatcher != null) textWatcher.beforeTextChanged(s, start, count, after);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (textWatcher != null) textWatcher.onTextChanged(s, start, before, count);
        }

    }

    // 隐藏或者显示右边clean的图标
    protected void setClearDrawableVisible(boolean isVisible) {
        ivClear.setVisibility(isVisible ? VISIBLE : GONE);
    }
}
