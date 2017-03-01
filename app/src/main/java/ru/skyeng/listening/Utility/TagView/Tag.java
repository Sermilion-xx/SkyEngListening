package ru.skyeng.listening.Utility.TagView;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;


public class Tag {

    static final int DEFAULT_TAG_LAYOUT_COLOR = Color.parseColor("#AED374");
    static final int DEFAULT_TAG_LAYOUT_COLOR_PRESS = Color.parseColor("#88363636");
    static final int DEFAULT_TAG_TEXT_COLOR = Color.parseColor("#ffffff");
    static final int DEFAULT_TAG_DELETE_INDICATOR_COLOR = Color.parseColor("#ffffff");
    static final int DEFAULT_TAG_LAYOUT_BORDER_COLOR = Color.parseColor("#ffffff");

    private static final String DELETE_ICON = "×";
    public int id;
    public String text;
    public int tagTextColor;
    public float tagTextSize;
    public int layoutColor;
    public int layoutColorPress;
    public boolean isDeletable;
    public int deleteIndicatorColor;
    public float deleteIndicatorSize;
    public float radius;
    public String deleteIcon;
    public float layoutBorderSize;
    public int layoutBorderColor;
    public Drawable background;
    public Typeface typeface;

    public Tag(String text) {
        this.init(0, text, DEFAULT_TAG_TEXT_COLOR,
                14.0F, DEFAULT_TAG_LAYOUT_COLOR,
                DEFAULT_TAG_LAYOUT_COLOR_PRESS,
                false,
                DEFAULT_TAG_DELETE_INDICATOR_COLOR,
                14.0F, 100.0F, DELETE_ICON, 0.0F,
                DEFAULT_TAG_LAYOUT_BORDER_COLOR,
                Typeface.DEFAULT);
    }

    private void init(int id, String text, int tagTextColor, float tagTextSize, int layoutColor, int layoutColorPress, boolean isDeletable, int deleteIndicatorColor, float deleteIndicatorSize, float radius, String deleteIcon, float layoutBorderSize, int layoutBorderColor, Typeface typeface) {
        this.id = id;
        this.text = text;
        this.tagTextColor = tagTextColor;
        this.tagTextSize = tagTextSize;
        this.layoutColor = layoutColor;
        this.layoutColorPress = layoutColorPress;
        this.isDeletable = isDeletable;
        this.deleteIndicatorColor = deleteIndicatorColor;
        this.deleteIndicatorSize = deleteIndicatorSize;
        this.radius = radius;
        this.deleteIcon = deleteIcon;
        this.layoutBorderSize = layoutBorderSize;
        this.layoutBorderColor = layoutBorderColor;
        this.typeface = typeface;
    }
}
