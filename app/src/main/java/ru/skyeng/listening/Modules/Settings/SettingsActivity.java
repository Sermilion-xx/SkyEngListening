package ru.skyeng.listening.Modules.Settings;

import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.skyeng.listening.CommonComponents.BaseActivity;
import ru.skyeng.listening.Modules.Settings.model.SettingsObject;
import ru.skyeng.listening.R;
import ru.skyeng.listening.Utility.FacadePreferences;
import ru.skyeng.listening.Utility.HelperMethod;
import ru.skyeng.listening.Utility.NotificationService;
import ru.skyeng.listening.Utility.asynctask.CommonAsyncTask;

import static ru.skyeng.listening.Utility.NotificationService.TAG_TASK_PERIODIC_LOG;

public class SettingsActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {

    private static final int selectedColor = R.color.colorAccent;
    private static final int deselectedColor = R.color.textColorDark;
    private static final String NOTIFICATION_RECEIVER = "ru.skyeng.listening.NotificationReceiver";

    @BindView(R.id.notification_switch)
    Switch mNotificationSwitch;
    @BindView(R.id.level_value)
    TextView mLevelText;
    @BindView(R.id.level_1)
    ImageView mLevel1;
    @BindView(R.id.level_2)
    ImageView mLevel2;
    @BindView(R.id.level_3)
    ImageView mLevel3;
    @BindView(R.id.level_4)
    ImageView mLevel4;
    @BindView(R.id.level_5)
    ImageView mLevel5;
    @BindView(R.id.level_6)
    ImageView mLevel6;
    @BindView(R.id.all_accents)
    TextView mAllAccentsText;
    @BindView(R.id.checkbox_all_accents)
    CheckBox mAllAccentsCheckBox;
    @BindView(R.id.accent_international)
    TextView mAccentsInternational;
    @BindView(R.id.checkbox_all_international)
    CheckBox mIntAccentsCheckBox;
    @BindView(R.id.accent_british)
    TextView mAccentBritish;
    @BindView(R.id.checkbox_all_british)
    CheckBox mBritishAccentsCheckBox;
    @BindView(R.id.accent_american)
    TextView mAccentAmerican;
    @BindView(R.id.checkbox_all_american)
    CheckBox mAmericanAccentsCheckBox;
    @BindView(R.id.button_apply)
    Button mApplyButton;
    @BindView(R.id.sliding_part_1)
    RelativeLayout mSlidingPart1;
    @BindView(R.id.sliding_part_2)
    RelativeLayout mSlidingPart2;
    @BindView(R.id.sliding_area)
    View mSlidingArea;

    @BindView(R.id.text_remainder_days_value)
    Spinner mDaysSpinner;
    @BindView(R.id.text_remainder_time_value)
    Spinner mTimeSpinner;

    private SettingsObject mSettings;
    private List<ImageView> mLevelViews;
    private GcmNetworkManager mGcmNetworkManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.colorGrey4), PorterDuff.Mode.SRC_ATOP);
        setupToolbar(getString(R.string.settings), upArrow);
        ButterKnife.bind(this);
        initLevelViewsList();
        mSettings = FacadePreferences.getSettingsFromPref(this);
        if (mSettings == null) {
            mSettings = new SettingsObject();
        }
        applySettings(mSettings);
        setClickListeners();

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.notification_days, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDaysSpinner.setAdapter(adapter1);
        mDaysSpinner.setSelection(mSettings.getRemindEvery());
        mDaysSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.notification_time, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTimeSpinner.setAdapter(adapter2);
        mTimeSpinner.setSelection(mSettings.getTime().get(Calendar.HOUR));
        mTimeSpinner.setOnItemSelectedListener(this);
    }

    private void applySettings(SettingsObject settings) {
        if (settings.isRemainderOn()) {
            mNotificationSwitch.setChecked(true);
            new Handler().postDelayed(this::showNotificationPanel, 500);
        } else {
            mNotificationSwitch.setChecked(false);
        }
        updateLevelViews(settings.getLevel());
        if (settings.isAllAccents()) {
            updateAccentsViewsAndCheckBoxes(true);
        } else {
            if (settings.isIntAccent()) {
                mIntAccentsCheckBox.setChecked(true);
                setTextColor(mAccentsInternational, selectedColor, true);
            } else if (settings.isBritishAccent()) {
                mBritishAccentsCheckBox.setChecked(true);
                setTextColor(mAccentBritish, selectedColor, true);
            } else if (settings.isAmericanAccent()) {
                mAmericanAccentsCheckBox.setChecked(true);
                setTextColor(mAccentAmerican, selectedColor, true);
            }
        }
    }

    private void setClickListeners() {
        mNotificationSwitch.setOnCheckedChangeListener(this);
        mAllAccentsCheckBox.setOnCheckedChangeListener(this);
        mIntAccentsCheckBox.setOnCheckedChangeListener(this);
        mBritishAccentsCheckBox.setOnCheckedChangeListener(this);
        mAmericanAccentsCheckBox.setOnCheckedChangeListener(this);
        mApplyButton.setOnClickListener(this);
        for (ImageView view : mLevelViews) {
            view.setOnClickListener(this);
        }
    }

    private void initLevelViewsList() {
        mLevelViews = new ArrayList<>();
        mLevelViews.add(mLevel1);
        mLevelViews.add(mLevel2);
        mLevelViews.add(mLevel3);
        mLevelViews.add(mLevel4);
        mLevelViews.add(mLevel5);
        mLevelViews.add(mLevel6);
    }

    private void updateLevelViews(int selectedLevel) {
        String text = "";
        switch (selectedLevel) {
            case 1:
                text = "Beginner";
                break;
            case 2:
                text = "Elementary";
                break;
            case 3:
                text = "Pre-Intermediate";
                break;
            case 4:
                text = "Intermediate";
                break;
            case 5:
                text = "Upper-Intermediate";
                break;
            case 6:
                text = "Advanced";
                break;
        }
        mLevelViews.get(mSettings.getLevel() - 1).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.oval_grey));
        mSettings.setLevel(selectedLevel);
        mLevelViews.get(mSettings.getLevel() - 1).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.oval_blue));
        mLevelText.setText(text);
    }

    private void updateAccentsViewsAndCheckBoxes(boolean allAccents) {
        if (allAccents) {
            updateAllAccentsCheckBoxes(true);
            updateAllAccentsTextColor(selectedColor, true);
        } else {
            updateAllAccentsCheckBoxes(false);
            updateAllAccentsTextColor(deselectedColor, false);
        }
    }

    @HelperMethod("updateAccentsViewsAndCheckBoxes")
    private void updateAllAccentsCheckBoxes(boolean checked) {
        mAllAccentsCheckBox.setChecked(checked);
        mAmericanAccentsCheckBox.setChecked(checked);
        mBritishAccentsCheckBox.setChecked(checked);
        mIntAccentsCheckBox.setChecked(checked);
        mSettings.setAllAccents(checked);
    }

    @HelperMethod("updateAccentsViewsAndCheckBoxes")
    private void updateAllAccentsTextColor(int color, boolean clicked) {
        setTextColor(mAllAccentsText, color, clicked);
        setTextColor(mAccentsInternational, color, clicked);
        setTextColor(mAccentBritish, color, clicked);
        setTextColor(mAccentAmerican, color, clicked);
    }

    private void setTextColor(TextView view, int color, boolean clicked) {
        if (clicked) {
            view.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            view.setTypeface(Typeface.DEFAULT);
        }
        view.setTextColor(ContextCompat.getColor(this, color));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.level_1:
                updateLevelViews(1);
                break;
            case R.id.level_2:
                updateLevelViews(2);
                break;
            case R.id.level_3:
                updateLevelViews(3);
                break;
            case R.id.level_4:
                updateLevelViews(4);
                break;
            case R.id.level_5:
                updateLevelViews(5);
                break;
            case R.id.level_6:
                updateLevelViews(6);
                break;
            case R.id.button_apply:
                saveSettings(mSettings);
                break;
        }
    }

    private void removeNotification() {
        mGcmNetworkManager.cancelAllTasks(NotificationService.class);
    }

    private void setNotification() {
        mGcmNetworkManager = GcmNetworkManager.getInstance(this);
        Task task = new PeriodicTask.Builder()
                .setService(NotificationService.class)
                .setPeriod(mSettings.getTime().getTimeInMillis())
                .setFlex(10)
                .setTag(TAG_TASK_PERIODIC_LOG)
                .setPersisted(true)
                .build();
        mGcmNetworkManager.schedule(task);
    }

    private void saveSettings(SettingsObject mSettings) {
        if (mSettings.isRemainderOn()) {
            setNotification();
        }else {
            removeNotification();
        }
        CommonAsyncTask<Void, Void, Void> saveSettingsTask = new CommonAsyncTask<>();
        saveSettingsTask.setDoInBackground(param -> {
            FacadePreferences.setSettingsToPref(SettingsActivity.this, mSettings);
            return null;
        });
        saveSettingsTask.setConsumer(param -> showToast(R.string.settings_saved));
        saveSettingsTask.execute();
    }

    boolean doNotTriggerAllAccents = false;

    public void updateSingleCheckboxClicked() {
        doNotTriggerAllAccents = true;
        if (mSettings.getAccentIds().size() == 3) {
            mAllAccentsCheckBox.setChecked(true);
        } else {
            mAllAccentsCheckBox.setChecked(false);
        }
    }

    private void showNotificationPanel() {
        mSlidingArea.setVisibility(View.VISIBLE);
        mSlidingPart1.animate().translationY(300);
        mSlidingPart2.animate().translationY(300);
    }

    private void hideNotificationPanel() {
        mSlidingArea.setVisibility(View.GONE);
        mSlidingPart1.animate().translationY(0);
        mSlidingPart2.animate().translationY(0);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.notification_switch:
                mSettings.setRemainderOn(isChecked);
                if (isChecked) {
                    showNotificationPanel();
                } else {
                    hideNotificationPanel();
                }
                break;
            case R.id.checkbox_all_accents:
                mAllAccentsCheckBox.setChecked(isChecked);
                if (!doNotTriggerAllAccents) {
                    if (!isChecked && mSettings.isAllAccents()) {
                        mSettings.setAllAccents(false);
                        updateAccentsViewsAndCheckBoxes(false);
                    } else {
                        mSettings.setAllAccents(true);
                        updateAccentsViewsAndCheckBoxes(true);
                    }

                }
                doNotTriggerAllAccents = false;
                break;
            case R.id.checkbox_all_international:
                mIntAccentsCheckBox.setChecked(isChecked);
                if (isChecked) {
                    mSettings.setIntAccent(true);
                    setTextColor(mAccentsInternational, selectedColor, true);
                } else {
                    mSettings.setIntAccent(false);
                    setTextColor(mAccentsInternational, deselectedColor, false);
                }
                updateSingleCheckboxClicked();
                break;
            case R.id.checkbox_all_british:
                mBritishAccentsCheckBox.setChecked(isChecked);
                if (isChecked) {
                    mSettings.setBritishAccent(true);
                    setTextColor(mAccentBritish, selectedColor, true);
                } else {
                    mSettings.setBritishAccent(false);
                    setTextColor(mAccentBritish, deselectedColor, false);
                }
                updateSingleCheckboxClicked();
                break;
            case R.id.checkbox_all_american:
                mAmericanAccentsCheckBox.setChecked(isChecked);
                if (isChecked) {
                    mSettings.setAmericanAccent(true);
                    setTextColor(mAccentAmerican, selectedColor, true);
                } else {
                    mSettings.setAmericanAccent(true);
                    setTextColor(mAccentAmerican, deselectedColor, false);
                }
                updateSingleCheckboxClicked();
                break;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView textView = ((TextView) parent.getChildAt(0));
        textView.setTextColor(ContextCompat.getColor(SettingsActivity.this, R.color.colorAccent));
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setGravity(Gravity.END);
        if (parent.getId() == R.id.text_remainder_days_value) {
            mSettings.setRemindEvery(position);
        } else if (parent.getId() == R.id.text_remainder_time_value) {
            createNotificationTime(textView.getText().toString());
        }
    }

    private void createNotificationTime(String time) {
        String[] timeString = time.split(":");
        int hours = Integer.parseInt(timeString[0]);
        int minutes;
        String minutesString = "";
        try {
            minutesString = timeString[1];
            minutes = Integer.parseInt(minutesString);
        } catch (Exception e) {
            minutes = Integer.parseInt(minutesString.substring(1, 2));
        }
        mSettings.setTime(hours, minutes);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
