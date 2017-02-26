package ru.skyeng.listening.Utility.TagView;

import android.graphics.Color;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 24/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

class TagViewConstants {

    static final int DEFAULT_TAG_LAYOUT_COLOR = Color.parseColor("#AED374");
    static final int DEFAULT_TAG_LAYOUT_COLOR_PRESS = Color.parseColor("#88363636");
    static final int DEFAULT_TAG_TEXT_COLOR = Color.parseColor("#ffffff");
    static final int DEFAULT_TAG_DELETE_INDICATOR_COLOR = Color.parseColor("#ffffff");
    static final int DEFAULT_TAG_LAYOUT_BORDER_COLOR = Color.parseColor("#ffffff");

    private TagViewConstants() throws InstantiationException {
        throw new InstantiationException("This class is not for instantiation");
    }
}
