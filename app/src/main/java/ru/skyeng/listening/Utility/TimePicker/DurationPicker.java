package ru.skyeng.listening.Utility.TimePicker;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 28/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ru.skyeng.listening.R;

/**
 * PopWindow for Date Pick
 */
public class DurationPicker extends PopupWindow implements OnClickListener {

    private static final int DEFAULT_MIN_VALUE = 0;
    public Button cancelBtn;
    public Button confirmBtn;
    public LoopView columnTwoView;
    public LoopView columnOneView;
    public View pickerContainerV;
    public View contentView;//root view
    public List<Pair<Integer, Integer>> mPositionValues;

    private int minValue;
    private int maxValue;
    private int valueTwo = 0;
    private int valueOne = 0;
    private Context mContext;
    private String textCancel;
    private String textConfirm;
    private int colorCancel;
    private int colorConfirm;
    private int buttonTextSize;
    private int viewTextSize;
    private boolean showDayMonthYear;

    private List<String> columnOne = new ArrayList<>();
    private List<String> columnTwo = new ArrayList<>();

    public static class Builder {
        private Context context;
        private OnDatePickedListener listener;

        public Builder(Context context, OnDatePickedListener listener) {
            this.context = context;
            this.listener = listener;
        }

        //Option
        private boolean showDayMonthYear = false;
        private int minValue = DEFAULT_MIN_VALUE;
        private int maxValue = 1000;
        private String textCancel = "Отмена";
        private String textConfirm = "Готово";
        private String dateChose = "0";
        private int colorCancel = Color.parseColor("#999999");
        private int colorConfirm = Color.parseColor("#303F9F");
        private int btnTextSize = 16;
        private int viewTextSize = 25;
        private List<String> columnOne;
        private List<String> columnTwo;
        public List<Pair<Integer, Integer>> mPositionValues;

        public Builder positionValues(List<Pair<Integer, Integer>> positionValues) {
            this.mPositionValues = positionValues;
            return this;
        }

        public Builder minYear(int minYear) {
            this.minValue = minYear;
            return this;
        }

        public Builder columnOneValues(List<String> values) {
            this.columnOne = values;
            return this;
        }

        public Builder columnTwoValues(List<String> values) {
            this.columnTwo = values;
            return this;
        }

        public Builder maxYear(int maxYear) {
            this.maxValue = maxYear;
            return this;
        }

        public Builder textCancel(String textCancel) {
            this.textCancel = textCancel;
            return this;
        }

        public Builder textConfirm(String textConfirm) {
            this.textConfirm = textConfirm;
            return this;
        }

        public Builder dateChose(String dateChose) {
            this.dateChose = dateChose;
            return this;
        }

        public Builder colorCancel(int colorCancel) {
            this.colorCancel = colorCancel;
            return this;
        }

        public Builder colorConfirm(int colorConfirm) {
            this.colorConfirm = colorConfirm;
            return this;
        }

        /**
         * set btn text btnTextSize
         *
         * @param textSize dp
         */
        public Builder btnTextSize(int textSize) {
            this.btnTextSize = textSize;
            return this;
        }

        public Builder viewTextSize(int textSize) {
            this.viewTextSize = textSize;
            return this;
        }

        public DurationPicker build() {
            if (minValue > maxValue) {
                throw new IllegalArgumentException();
            }
            return new DurationPicker(this);
        }

        public Builder showDayMonthYear(boolean useDayMonthYear) {
            this.showDayMonthYear = useDayMonthYear;
            return this;
        }
    }

    public DurationPicker(Builder builder) {
        this.minValue = builder.minValue;
        this.maxValue = builder.maxValue;
        this.textCancel = builder.textCancel;
        this.textConfirm = builder.textConfirm;
        this.mContext = builder.context;
        this.mListener = builder.listener;
        this.colorCancel = builder.colorCancel;
        this.colorConfirm = builder.colorConfirm;
        this.buttonTextSize = builder.btnTextSize;
        this.viewTextSize = builder.viewTextSize;
        this.showDayMonthYear = builder.showDayMonthYear;
        this.mPositionValues = builder.mPositionValues;
        initView(builder.columnOne, builder.columnTwo);
    }

    private OnDatePickedListener mListener;

    private void initView(List<String> columnOne, List<String> columnTwo) {
        contentView = LayoutInflater.from(mContext).inflate(showDayMonthYear ? R.layout.layout_duration_picker_inverted : R.layout.layout_duration_picker, null);
        cancelBtn = (Button) contentView.findViewById(R.id.btn_cancel);
        cancelBtn.setTextColor(colorCancel);
        cancelBtn.setTextSize(buttonTextSize);
        confirmBtn = (Button) contentView.findViewById(R.id.btn_confirm);
        confirmBtn.setTextColor(colorConfirm);
        confirmBtn.setTextSize(buttonTextSize);
        columnTwoView = (LoopView) contentView.findViewById(R.id.picker_month);
        columnOneView = (LoopView) contentView.findViewById(R.id.picker_day);
        pickerContainerV = contentView.findViewById(R.id.container_picker);

        columnTwoView.setLoopListener(new LoopScrollListener() {
            @Override
            public void onItemSelect(int item) {
                valueTwo = item;
//                initColumnOne();
            }
        });
        columnOneView.setLoopListener(new LoopScrollListener() {
            @Override
            public void onItemSelect(int item) {
                valueOne = item;
            }
        });

        initColumnTwo(columnTwo); // init year and month loop view
        initColumnOne(columnOne); //init day loop view

        cancelBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        contentView.setOnClickListener(this);

        if (!TextUtils.isEmpty(textConfirm)) {
            confirmBtn.setText(textConfirm);
        }

        if (!TextUtils.isEmpty(textCancel)) {
            cancelBtn.setText(textCancel);
        }

        setTouchable(true);
        setFocusable(true);
        // setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setAnimationStyle(R.style.FadeInPopWin);
        setContentView(contentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
    }

    private void initColumnTwo(List<String> values) {
        columnTwoView.setDataList(values);
        columnTwoView.setInitPosition(valueTwo);
    }


    private void initColumnOne(List<String> values) {
        columnOneView.setDataList(values);
        columnOneView.setInitPosition(valueOne);
    }

    /**
     * Show date picker popWindow
     *
     * @param activity
     */
    public void showPopWin(Activity activity) {

        if (null != activity) {

            TranslateAnimation trans = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
                    0, Animation.RELATIVE_TO_SELF, 1,
                    Animation.RELATIVE_TO_SELF, 0);

            showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM,
                    0, 0);
            trans.setDuration(400);
            trans.setInterpolator(new AccelerateDecelerateInterpolator());

            pickerContainerV.startAnimation(trans);
        }
    }

    /**
     * Dismiss date picker popWindow
     */
    public void dismissPopWin() {

        TranslateAnimation trans = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);

        trans.setDuration(400);
        trans.setInterpolator(new AccelerateInterpolator());
        trans.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismiss();
            }
        });

        pickerContainerV.startAnimation(trans);
    }

    @Override
    public void onClick(View v) {

        if (v == contentView || v == cancelBtn) {
            dismissPopWin();
        } else if (v == confirmBtn) {
            if (null != mListener) {
                int valueOne = mPositionValues.get(this.valueOne).first;
                int valueTwo = mPositionValues.get(this.valueTwo).second;
                mListener.onDatePickCompleted(valueOne, valueTwo);
            }
            dismissPopWin();
        }
    }

    /**
     * get long from yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static long getLongFromyyyyMMdd(String date) {
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date parse = null;
        try {
            parse = mFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (parse != null) {
            return parse.getTime();
        } else {
            return -1;
        }
    }


    public static String format2LenStr(int num) {
        return (num < 10) ? "0" + num : String.valueOf(num);
    }

    public static int spToPx(Context context, int spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    public interface OnDatePickedListener {
        void onDatePickCompleted(int valueOne, int valueTwo);
    }
}