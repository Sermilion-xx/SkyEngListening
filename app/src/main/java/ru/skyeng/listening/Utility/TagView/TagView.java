package ru.skyeng.listening.Utility.TagView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ru.skyeng.listening.R;

public class TagView extends RelativeLayout {
    protected List<Tag> mTags = new ArrayList<>();
    protected LayoutInflater mInflater;
    protected ViewTreeObserver mViewTreeObserver;
    protected TagView.OnTagClickListener mClickListener;
    protected TagView.OnTagDeleteListener mDeleteListener;
    protected TagView.OnTagLongClickListener mTagLongClickListener;
    protected int mWidth;
    protected boolean mInitialized = false;
    protected int lineMargin;
    protected int tagMargin;
    protected int textPaddingLeft;
    protected int textPaddingRight;
    protected int textPaddingTop;
    protected int textPaddingBottom;

    public TagView(Context ctx) {
        super(ctx, null);
        this.initialize(ctx, null, 0);
    }

    public TagView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        this.initialize(ctx, attrs, 0);
    }

    public TagView(Context ctx, AttributeSet attrs, int defStyle) {
        super(ctx, attrs, defStyle);
        this.initialize(ctx, attrs, defStyle);
    }

    protected void initialize(Context ctx, AttributeSet attrs, int defStyle) {
        this.mInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mViewTreeObserver = this.getViewTreeObserver();
        this.mViewTreeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if(!TagView.this.mInitialized) {
                    TagView.this.mInitialized = true;
                    TagView.this.drawTags();
                }

            }
        });
        TypedArray typeArray = ctx.obtainStyledAttributes(attrs, R.styleable.TagView, defStyle, defStyle);
        this.lineMargin = (int)typeArray.getDimension(R.styleable.TagView_lineMargin, (float) TagViewUtils.dipToPx(this.getContext(), 5.0F));
        this.tagMargin = (int)typeArray.getDimension(R.styleable.TagView_tagMargin, (float) TagViewUtils.dipToPx(this.getContext(), 5.0F));
        this.textPaddingLeft = (int)typeArray.getDimension(R.styleable.TagView_textPaddingLeft, (float) TagViewUtils.dipToPx(this.getContext(), 8.0F));
        this.textPaddingRight = (int)typeArray.getDimension(R.styleable.TagView_textPaddingRight, (float) TagViewUtils.dipToPx(this.getContext(), 8.0F));
        this.textPaddingTop = (int)typeArray.getDimension(R.styleable.TagView_textPaddingTop, (float) TagViewUtils.dipToPx(this.getContext(), 5.0F));
        this.textPaddingBottom = (int)typeArray.getDimension(R.styleable.TagView_textPaddingBottom, (float) TagViewUtils.dipToPx(this.getContext(), 5.0F));
        typeArray.recycle();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = this.getMeasuredWidth();
        if(width > 0) {
            this.mWidth = this.getMeasuredWidth();
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.drawTags();
    }

    protected void drawTags() {
        if(this.mInitialized) {
            this.removeAllViews();
            float total = (float)(this.getPaddingLeft() + this.getPaddingRight());
            int listIndex = 1;
            int indexBottom = 1;
            int indexHeader = 1;
            Tag tagPre = null;

            for(Iterator var6 = this.mTags.iterator(); var6.hasNext(); ++listIndex) {
                final Tag item = (Tag)var6.next();
                final int position = listIndex - 1;
                View tagLayout = this.mInflater.inflate(R.layout.tagview_item, null);
                tagLayout.setId(listIndex);
                if(VERSION.SDK_INT < 16) {
                    tagLayout.setBackground(this.getSelector(item));
                } else {
                    tagLayout.setBackground(this.getSelector(item));
                }

                TextView tagView = (TextView)tagLayout.findViewById(R.id.tv_tag_item_contain);
                tagView.setText(item.text);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tagView.getLayoutParams();
                params.setMargins(this.textPaddingLeft, this.textPaddingTop, this.textPaddingRight, this.textPaddingBottom);
                tagView.setLayoutParams(params);
                tagView.setTextColor(item.tagTextColor);
                tagView.setTextSize(2, item.tagTextSize);
                tagView.setTypeface(item.typeface);
                tagLayout.setOnClickListener(v -> {
                    if(TagView.this.mClickListener != null) {
                        TagView.this.mClickListener.onTagClick(item, position);
                    }

                });
                tagLayout.setOnLongClickListener(v -> {
                    if(TagView.this.mTagLongClickListener != null) {
                        TagView.this.mTagLongClickListener.onTagLongClick(item, position);
                    }

                    return true;
                });
                float tagWidth = tagView.getPaint().measureText(item.text) + (float)this.textPaddingLeft + (float)this.textPaddingRight;
                TextView deletableView = (TextView)tagLayout.findViewById(R.id.tv_tag_item_delete);
                if(item.isDeletable) {
                    deletableView.setVisibility(VISIBLE);
                    deletableView.setText(item.deleteIcon);
                    int tagParams = TagViewUtils.dipToPx(this.getContext(), 2.0F);
                    deletableView.setPadding(tagParams, this.textPaddingTop, this.textPaddingRight + tagParams, this.textPaddingBottom);
                    deletableView.setTextColor(item.deleteIndicatorColor);
                    deletableView.setTextSize(2, item.deleteIndicatorSize);
                    deletableView.setOnClickListener(v -> {
                        if(TagView.this.mDeleteListener != null) {
                            TagView.this.mDeleteListener.onTagDeleted(TagView.this, item, position);
                        }
                    });
                    tagWidth += deletableView.getPaint().measureText(item.deleteIcon) + (float)this.textPaddingLeft + (float)this.textPaddingRight;
                } else {
                    deletableView.setVisibility(GONE);
                }

                android.widget.RelativeLayout.LayoutParams var16 = new android.widget.RelativeLayout.LayoutParams(-2, -2);
                var16.bottomMargin = this.lineMargin;
                if((float)this.mWidth <= total + tagWidth + (float) TagViewUtils.dipToPx(this.getContext(), 2.0F)) {
                    var16.addRule(3, indexBottom);
                    total = (float)(this.getPaddingLeft() + this.getPaddingRight());
                    indexBottom = listIndex;
                    indexHeader = listIndex;
                } else {
                    var16.addRule(6, indexHeader);
                    if(listIndex != indexHeader) {
                        var16.addRule(1, listIndex - 1);
                        var16.leftMargin = this.tagMargin;
                        total += (float)this.tagMargin;
                        if(tagPre.tagTextSize < item.tagTextSize) {
                            indexBottom = listIndex;
                        }
                    }
                }

                total += tagWidth;
                this.addView(tagLayout, var16);
                tagPre = item;
            }

        }
    }

    protected Drawable getSelector(Tag tag) {
        if(tag.background != null) {
            return tag.background;
        } else {
            StateListDrawable states = new StateListDrawable();
            GradientDrawable gdNormal = new GradientDrawable();
            gdNormal.setColor(tag.layoutColor);
            gdNormal.setCornerRadius(tag.radius);
            if(tag.layoutBorderSize > 0.0F) {
                gdNormal.setStroke(TagViewUtils.dipToPx(this.getContext(), tag.layoutBorderSize), tag.layoutBorderColor);
            }

            GradientDrawable gdPress = new GradientDrawable();
            gdPress.setColor(tag.layoutColorPress);
            gdPress.setCornerRadius(tag.radius);
            states.addState(new int[]{16842919}, gdPress);
            states.addState(new int[0], gdNormal);
            return states;
        }
    }

    public void addTag(Tag tag) {
        this.mTags.add(tag);
        this.drawTags();
    }

    public void addTags(List<Tag> tags) {
        if(tags != null) {
            this.mTags = new ArrayList<>();
            if(tags.isEmpty()) {
                this.drawTags();
            }
            for (Tag item : tags) {
                this.addTag(item);
            }
        }
    }

    public void addTags(String[] tags) {
        if(tags != null) {
            for (String item : tags) {
                Tag tag = new Tag(item);
                this.addTag(tag);
            }
        }
    }

    public List<Tag> getTags() {
        return this.mTags;
    }

    public void remove(int position) {
        if(position < this.mTags.size()) {
            this.mTags.remove(position);
            this.drawTags();
        }
    }

    public void removeAll() {
        this.mTags.clear();
        this.removeAllViews();
    }

    public int getLineMargin() {
        return this.lineMargin;
    }

    public void setLineMargin(float lineMargin) {
        this.lineMargin = TagViewUtils.dipToPx(this.getContext(), lineMargin);
    }

    public int getTagMargin() {
        return this.tagMargin;
    }

    public void setTagMargin(float tagMargin) {
        this.tagMargin = TagViewUtils.dipToPx(this.getContext(), tagMargin);
    }

    public int getTextPaddingLeft() {
        return this.textPaddingLeft;
    }

    public void setTextPaddingLeft(float textPaddingLeft) {
        this.textPaddingLeft = TagViewUtils.dipToPx(this.getContext(), textPaddingLeft);
    }

    public int getTextPaddingRight() {
        return this.textPaddingRight;
    }

    public void setTextPaddingRight(float textPaddingRight) {
        this.textPaddingRight = TagViewUtils.dipToPx(this.getContext(), textPaddingRight);
    }

    public int getTextPaddingTop() {
        return this.textPaddingTop;
    }

    public void setTextPaddingTop(float textPaddingTop) {
        this.textPaddingTop = TagViewUtils.dipToPx(this.getContext(), textPaddingTop);
    }

    public int gettextPaddingBottom() {
        return this.textPaddingBottom;
    }

    public void settextPaddingBottom(float textPaddingBottom) {
        this.textPaddingBottom = TagViewUtils.dipToPx(this.getContext(), textPaddingBottom);
    }

    public void setOnTagLongClickListener(TagView.OnTagLongClickListener longClickListener) {
        this.mTagLongClickListener = longClickListener;
    }

    public void setOnTagClickListener(TagView.OnTagClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    public void setOnTagDeleteListener(TagView.OnTagDeleteListener deleteListener) {
        this.mDeleteListener = deleteListener;
    }

    public interface OnTagLongClickListener {
        void onTagLongClick(Tag var1, int var2);
    }

    public interface OnTagClickListener {
        void onTagClick(Tag var1, int var2);
    }

    public interface OnTagDeleteListener {
        void onTagDeleted(TagView var1, Tag var2, int var3);
    }
}
