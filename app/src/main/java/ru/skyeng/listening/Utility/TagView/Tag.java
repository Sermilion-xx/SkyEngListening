package ru.skyeng.listening.Utility.TagView;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;


public class Tag {
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
        this.init(0, text, TagViewConstants.DEFAULT_TAG_TEXT_COLOR,
                14.0F, TagViewConstants.DEFAULT_TAG_LAYOUT_COLOR,
                TagViewConstants.DEFAULT_TAG_LAYOUT_COLOR_PRESS,
                false,
                TagViewConstants.DEFAULT_TAG_DELETE_INDICATOR_COLOR,
                14.0F, 100.0F, "×", 0.0F,
                TagViewConstants.DEFAULT_TAG_LAYOUT_BORDER_COLOR,
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
