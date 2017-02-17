package ru.skyeng.listening.Modules.Settings;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.skyeng.listening.Modules.Settings.model.SettingsObject;
import ru.skyeng.listening.R;
import ru.skyeng.listening.Utility.FacadePreferences;
import ru.skyeng.listening.Utility.HelperMethod;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final int selectedColor = R.color.colorBlue2;
    private static final int deselectedColor = R.color.textColorDark;
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

    private SettingsObject mSettings;
    private List<ImageView> mLevelViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        initLevelViewsList();
        mSettings = FacadePreferences.getSettingsFromPref(this);
        if(mSettings==null){
            mSettings = new SettingsObject();
        }
        applySettings(mSettings);
        setClickListeners();
    }

    private void applySettings(SettingsObject settings) {
        if (settings.isRemainderOn()) {
            mNotificationSwitch.setChecked(true);
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
                text = "Начальный";
                break;
            case 2:
                text = "Элементарный";
                break;
            case 3:
                text = "Средний";
                break;
            case 4:
                text = "Высший средний";
                break;
            case 5:
                text = "Сложный";
                break;
            case 6:
                text = "Самый сложный";
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

    private void saveSettings(SettingsObject mSettings) {
        FacadePreferences.setSettingsToPref(this, mSettings);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.notification_switch:
                mSettings.setRemainderOn(isChecked);
                break;
            case R.id.checkbox_all_accents:
                if (mSettings.isAllAccents()) {
                    mSettings.setAllAccents(false);
                    updateAccentsViewsAndCheckBoxes(false);
                } else {
                    mSettings.setAllAccents(true);
                    updateAccentsViewsAndCheckBoxes(true);
                }
                break;
            case R.id.checkbox_all_international:
                if (mIntAccentsCheckBox.isChecked()) {
                    mSettings.setIntAccent(true);
                    setTextColor(mAccentsInternational, selectedColor, true);
                }else {
                    mSettings.setIntAccent(false);
                    setTextColor(mAccentsInternational, deselectedColor, false);
                }
                break;
            case R.id.checkbox_all_british:
                if (mBritishAccentsCheckBox.isChecked()) {
                    mSettings.setBritishAccent(true);
                    setTextColor(mAccentBritish, selectedColor, true);
                }else {
                    mSettings.setBritishAccent(false);
                    setTextColor(mAccentBritish, deselectedColor, false);
                }
                break;
            case R.id.checkbox_all_american:
                if (mAmericanAccentsCheckBox.isChecked()) {
                    mSettings.setAmericanAccent(true);
                    setTextColor(mAccentAmerican, selectedColor, true);
                }else {
                    mSettings.setAmericanAccent(true);
                    setTextColor(mAccentAmerican, deselectedColor, false);
                }
                break;
        }
    }

}
