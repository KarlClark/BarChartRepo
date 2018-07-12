package com.clarkgarrett.barchart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * Created by Karl on 6/17/2015.
 */
public class BarChart extends View {
    private int mAxisLabelTextSize = (int)dipOrSpToPixels(22f, "dp");
    private int mChartLabelTextSize = (int)dipOrSpToPixels(26f, "dp");
    private int mTickMarkLabelTextSize = (int)dipOrSpToPixels(15f, "dp");
    private int mBarLabelTextSize = (int)dipOrSpToPixels(15f, "dp");
    private int mXAxisLabelTextSize = (int)dipOrSpToPixels(22f, "dp");
    private int mYAxisLabelTextSize = (int)dipOrSpToPixels(22f, "dp");
    private int mBarValueTextSize = (int)dipOrSpToPixels(22f, "dp");
    private int mBarValuePadding = (int)dipOrSpToPixels(3f, "dp");
    private int mTickMarkLabelPadding = (int)dipOrSpToPixels(3f, "dp");
    private int mTextColor = Color.BLACK;
    private int mChartLabelTextColor = Color.BLACK;
    private int mAxisLabelTextColor = Color.BLACK;
    private int mXAxisLabelTextColor = Color.BLACK;
    private int mYAxisLabelTextColor = Color.BLACK;
    private int mTickMarkLabelTextColor = Color.BLACK;
    private int mBarLabelTextColor = Color.BLACK;
    private int mBarValueTextColor = Color.BLACK;
    private int mFrameColor = Color.BLACK;
    private int mTickMarkColor = Color.BLACK;
    private int mMajorTickMarkColor = Color.BLACK;
    private int mMinorTickMarkColor = Color.BLACK;
    private int mLineColor = Color.GRAY;
    private int mMajorLineColor = Color.GRAY;
    private int mMinorLineColor = Color.GRAY;
    private int mBarColor = Color.BLUE;
    private int mChartBackground = Color.LTGRAY;
    private int mFrameWidth = (int)dipOrSpToPixels(2,"dp");
    private int mTickMarkThickness = (int)dipOrSpToPixels(1,"dp");
    private int mMajorTickMarkThickness = (int)dipOrSpToPixels(1,"dp");
    private int mMinorTickMarkThickness = (int)dipOrSpToPixels(1,"dp");
    private int mLineThickness = (int)dipOrSpToPixels(1,"dp");
    private int mMajorLineThickness = (int)dipOrSpToPixels(1,"dp");
    private int mMinorLineThickness = (int)dipOrSpToPixels(1,"dp");
    private int mLabelStyle = NORMAL;
    private int mChartLabelStyle = NORMAL;
    private int mXAxisLabelStyle = NORMAL;
    private int mYAxisLabelStyle = NORMAL;
    private int mBarLabelStyle = NORMAL;
    private int mBarValueStyle = NORMAL;
    private int mTickMarkLabelStyle = NORMAL;
    private float mBarWidth = 0;
    private float mBarSpacingWidth = 0;
    private float mBarLabelTextAngle = 30.0f;
    private boolean mFramed = true, mHasLeftTickMarks = true, mHasRightTickMarks = false, mHasLines = true,
            mHasLeftLabels = true, mHasRightLabels = false, mHasBarValues=false;
    private String mChartLabel = "";
    private String mYAxisLabel = "";
    private String mXAxisLabel = "";
    private String[] mTickMarkLabels = new String[] {"0", "20", "40", "300", "80", "100"};
    private float[] mTickMarkValues = new float[] {0f, 20f, 40f, 60f, 80f, 100f};
    private float[] mTickMarkLabelWidths;
    private float[] mBarValueWidths;
    private int mNumberOfMinorTickmarks = 0; // number of tick marks between labeled tick marks
    private int mMajorTickMarkLength = (int)dipOrSpToPixels(10, "dp");
    private int mMinorTickMarkLength = (int)dipOrSpToPixels(5, "dp");
    private float mBarLabelOffset = dipOrSpToPixels(5, "dp");
    private float mMaxTickMarkLabelWidth;
    private float mMaxBarValue;
    private float mTickMarkLabelHeight;
    private float mBarValueHeight;
    private Paint mFramePaint,mXAxisLabelTextPaint,mYAxisLabelTextPaint, mTickmarkPaint, mTickmarkLabelPaint,
            mBarLabelPaint, mBarPaint, mChartBackgroundPaint, mLinePaint, mChartLabelTextPaint, mBarValuePaint;
    private float mTotalWidth,mTotalHeight;
    private Rect mChartLabelBounds = new Rect();
    private Rect mXLabelBounds = new Rect();
    private Rect mYLabelBounds = new Rect();
    private float mXAxisOffset, mYAxisOffset;
    private DecimalFormat mTickMarkLabelFormat = new DecimalFormat("0");
    private DecimalFormat mBarValueFormat = new DecimalFormat("0");
    private Path mYAxisTextPath= new Path();
    private float mBarLabelHorizontalSpace, mBarLabelVerticalSpace, mMaxBarLabelHeight, mMaxBarLabelHeightMinusBottom;
    private float[] mBarValues = new float[] {20, 60, 100};
    private float[] mBarValuesForLabels = null;
    private String[] mBarLabels = new String[] {"Bar Label 1","Bar Label 2","Bar Label 3"};
    private String[] mBarValueLabels = null;
    private int[] mBarColors= null;
    private int[] mBarValueTextColors = null;
    private static final int NORMAL = 0;
    private static final int BOLD = 1;
    private static final int ITALIC = 2;
    private static final int BOLD_ITALIC = 3;

    private static final String TAG ="## My Info ##";

    public BarChart(Context context){
        super(context);
        initBarChart();
    }

    public BarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttributes(context, attrs);
        initBarChart();
    }

    public BarChart(Context context, AttributeSet attrs, int defaultStyle){
        super(context, attrs, defaultStyle);
        getAttributes(context, attrs);
        initBarChart();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Save the width and height of our view and set the measured dimension
        // to the maximum value.
        mTotalWidth = measure(widthMeasureSpec);
        mTotalHeight = measure(heightMeasureSpec);

        setMeasuredDimension((int) mTotalWidth, (int) mTotalHeight);
    }

    private int measure(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.UNSPECIFIED){
            result = 600;
        }else {
            result=specSize;
        }
        return result;
    }

    private void initBarChart(){

        mFramePaint = new Paint();
        mFramePaint.setColor(mFrameColor);
        mFramePaint.setStrokeWidth(mFrameWidth);
        mFramePaint.setStyle(Paint.Style.STROKE);

        mChartLabelTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mChartLabelTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mChartLabelTextPaint.setColor(mChartLabelTextColor);
        mChartLabelTextPaint.setTextSize(mChartLabelTextSize);
        setTextStyle(mChartLabelStyle, mChartLabelTextPaint);

        mXAxisLabelTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mXAxisLabelTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mXAxisLabelTextPaint.setColor(mXAxisLabelTextColor);
        mXAxisLabelTextPaint.setTextSize(mXAxisLabelTextSize);
        setTextStyle(mXAxisLabelStyle, mXAxisLabelTextPaint);

        mYAxisLabelTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mYAxisLabelTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mYAxisLabelTextPaint.setColor(mYAxisLabelTextColor);
        mYAxisLabelTextPaint.setTextSize(mYAxisLabelTextSize);
        setTextStyle(mYAxisLabelStyle, mYAxisLabelTextPaint);

        mTickmarkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTickmarkPaint.setStyle(Paint.Style.STROKE);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.STROKE);

        if (mChartLabel == null){
            mChartLabel = "";
        }
        if (mYAxisLabel == null){
            mYAxisLabel = "";
        }
        if (mXAxisLabel == null){
            mXAxisLabel = "";
        }
        mChartLabelTextPaint.getTextBounds(mChartLabel, 0, mChartLabel.length(), mChartLabelBounds);
        mXAxisLabelTextPaint.getTextBounds(mXAxisLabel, 0, mXAxisLabel.length(), mXLabelBounds);
        mYAxisLabelTextPaint.getTextBounds(mYAxisLabel, 0, mYAxisLabel.length(), mYLabelBounds);

        mTickmarkLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTickmarkLabelPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTickmarkLabelPaint.setColor(mTickMarkLabelTextColor);
        mTickmarkLabelPaint.setTextSize(mTickMarkLabelTextSize);
        setTextStyle(mTickMarkLabelStyle, mTickmarkLabelPaint);

        mBarLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBarLabelPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mBarLabelPaint.setColor(mBarLabelTextColor);
        mBarLabelPaint.setTextSize(mBarLabelTextSize);
        setTextStyle(mBarLabelStyle, mBarLabelPaint);
        getBarLabelSpacing();

        mBarValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBarValuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mBarValuePaint.setColor(mBarValueTextColor);
        mBarValuePaint.setTextSize(mBarValueTextSize);
        setTextStyle(mBarValueStyle, mBarValuePaint);

        mBarPaint = new Paint();
        mBarPaint.setColor(mBarColor);

        mChartBackgroundPaint = new Paint();
        mChartBackgroundPaint.setColor(mChartBackground);
    }

    public void getAttributes(Context context, AttributeSet attrs){

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BarChart, 0, 0);
        String s;

        s = a.getString(R.styleable.BarChart_tickMarkLabelFormat);
        if (s != null){
            mTickMarkLabelFormat.applyPattern(s);
        }

        s = a.getString(R.styleable.BarChart_barValueFormat);
        if (s == null){
            mBarValueFormat.applyPattern(mTickMarkLabelFormat.toPattern());
        }else{
            mBarValueFormat.applyPattern(s);
        }

        mChartLabelTextSize = a.getDimensionPixelSize(R.styleable.BarChart_chartLabelTextSize, mChartLabelTextSize);
        mAxisLabelTextSize = a.getDimensionPixelSize(R.styleable.BarChart_axisLabelTextSize, mAxisLabelTextSize);
        mXAxisLabelTextSize = a.getDimensionPixelSize(R.styleable.BarChart_xAxisLabelTextSize, mAxisLabelTextSize);
        mYAxisLabelTextSize = a.getDimensionPixelSize(R.styleable.BarChart_yAxisLabelTextSize, mAxisLabelTextSize);
        mBarLabelTextSize = a.getDimensionPixelSize(R.styleable.BarChart_barLabelTextSize, mBarLabelTextSize);
        mBarValueTextSize = a.getDimensionPixelSize(R.styleable.BarChart_barValueTextSize, mBarValueTextSize);
        mTickMarkLabelTextSize = a.getDimensionPixelSize(R.styleable.BarChart_tickMarkLabelTextSize, mTickMarkLabelTextSize);

        mTextColor = a.getColor(R.styleable.BarChart_textColor, Color.BLACK);
        mChartLabelTextColor = a.getColor(R.styleable.BarChart_chartLabelTextColor, mTextColor);
        mAxisLabelTextColor = a.getColor(R.styleable.BarChart_axisLabelTextColor, mTextColor);
        mXAxisLabelTextColor = a.getColor(R.styleable.BarChart_xAxisLabelTextColor, mAxisLabelTextColor);
        mYAxisLabelTextColor = a.getColor(R.styleable.BarChart_yAxisLabelTextColor, mAxisLabelTextColor);
        mTickMarkLabelTextColor = a.getColor(R.styleable.BarChart_tickMarkLabelTextColor, mTextColor);
        mBarLabelTextColor = a.getColor(R.styleable.BarChart_barLabelTextColor, mTextColor);
        mBarValueTextColor = a.getColor(R.styleable.BarChart_barValueTextColor, mTextColor);
        mFrameColor = a.getColor(R.styleable.BarChart_frameColor, Color.BLACK);
        mTickMarkColor = a.getColor(R.styleable.BarChart_tickMarkColor, mFrameColor);
        mMajorTickMarkColor = a.getColor(R.styleable.BarChart_majorTickMarkColor, mTickMarkColor);
        mMinorTickMarkColor = a.getColor(R.styleable.BarChart_minorTickMarkColor, mTickMarkColor);
        mLineColor = a.getColor(R.styleable.BarChart_lineColor, mMajorTickMarkColor);
        mMajorLineColor = a.getColor(R.styleable.BarChart_majorLineColor, mLineColor);
        mMinorLineColor = a.getColor(R.styleable.BarChart_minorLineColor, mLineColor);
        mChartBackground = a.getColor(R.styleable.BarChart_chartBackground, mChartBackground);

        mFrameWidth = a.getDimensionPixelSize(R.styleable.BarChart_frameWidth, mFrameWidth);

        mTickMarkThickness = a.getDimensionPixelSize(R.styleable.BarChart_tickMarkThickness, mTickMarkThickness);
        mMajorTickMarkThickness = a.getDimensionPixelSize(R.styleable.BarChart_majorTickMarkThickness, mTickMarkThickness);
        mMinorTickMarkThickness = a.getDimensionPixelSize(R.styleable.BarChart_minorTickMarkThickness, mTickMarkThickness);

        mLineThickness = a.getDimensionPixelSize(R.styleable.BarChart_lineThickness, mLineThickness);
        mMajorLineThickness = a.getDimensionPixelSize(R.styleable.BarChart_majorLineThickness, mLineThickness);
        mMinorLineThickness = a.getDimensionPixelSize(R.styleable.BarChart_minorLineThickness, mLineThickness);

        mLabelStyle = a.getInt(R.styleable.BarChart_LabelStyle , mLabelStyle);
        mChartLabelStyle = a.getInt(R.styleable.BarChart_chartLabelStyle, mLabelStyle);
        mXAxisLabelStyle = a.getInt(R.styleable.BarChart_xAxisLabelStyle, mLabelStyle);
        mYAxisLabelStyle = a.getInt(R.styleable.BarChart_yAxisLabelStyle, mLabelStyle);
        mBarValueStyle = a.getInt(R.styleable.BarChart_barValueStyle, mLabelStyle);
        mTickMarkLabelStyle = a.getInt(R.styleable.BarChart_tickMarkLabelStyle, mLabelStyle);

        mMajorTickMarkLength = a.getDimensionPixelSize(R.styleable.BarChart_majorTickMarkLength, mMajorTickMarkLength);
        mMinorTickMarkLength = a.getDimensionPixelSize(R.styleable.BarChart_minorTickMarkLength, mMajorTickMarkLength/2);

        mNumberOfMinorTickmarks = a.getInt(R.styleable.BarChart_minorTickMarkNumber, mNumberOfMinorTickmarks);

        mBarWidth = a.getDimensionPixelSize(R.styleable.BarChart_barWidth, 0);
        mBarSpacingWidth = a.getDimensionPixelSize(R.styleable.BarChart_barSpacingWidth, 0);

        mBarColor = a.getColor(R.styleable.BarChart_barColor, Color.BLUE);
        mBarLabelTextAngle = a.getInteger(R.styleable.BarChart_barLabelTextAngle, 45);

        mChartLabel = a.getString(R.styleable.BarChart_chartLabel);
        mYAxisLabel = a.getString(R.styleable.BarChart_yAxisLabel);
        mXAxisLabel = a.getString(R.styleable.BarChart_xAxisLabel);

        mFramed = a.getBoolean(R.styleable.BarChart_framed, mFramed);
        mHasLeftTickMarks = a.getBoolean(R.styleable.BarChart_hasLeftTickMarks, mHasLeftTickMarks);
        mHasRightTickMarks = a.getBoolean(R.styleable.BarChart_hasRightTickMarks, mHasRightTickMarks);
        mHasLeftLabels = a.getBoolean(R.styleable.BarChart_hasLeftLables, mHasLeftLabels);
        mHasRightLabels = a.getBoolean(R.styleable.BarChart_hasRightLabels, mHasRightLabels);
        mHasLines = a.getBoolean(R.styleable.BarChart_hasLines, mHasLines);
        mHasBarValues = a.getBoolean(R.styleable.BarChart_hasBarValues, mHasBarValues);
        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas){

        setTickMarkLabels();
        setBarValueLabels();
        float spaceForLabels =mMaxTickMarkLabelWidth + mTickMarkLabelPadding;
        float spaceForChartLabel = (mChartLabel.equals(""))? 0 : mChartLabelBounds.height() + dipOrSpToPixels(20, "dp");

        mXAxisOffset = mXLabelBounds.height() + mBarLabelVerticalSpace + mBarLabelOffset + ((mFramed)? mFrameWidth/2 : 0) + getPaddingBottom() + dipOrSpToPixels(15,"dp");
        mYAxisOffset = mYLabelBounds.height() + ((mHasLeftTickMarks)? mMajorTickMarkLength : 0) + ((mHasLeftLabels)? spaceForLabels :0) + ((mFramed)? mFrameWidth/2 : 0) + getPaddingLeft() + dipOrSpToPixels(15, "dp");
        canvas.drawRect(mYAxisOffset, getPaddingTop() + spaceForChartLabel,
                        mTotalWidth-getPaddingRight()-((mHasRightTickMarks)? mMajorTickMarkLength : 0)-((mHasRightLabels)? spaceForLabels : 0),
                        mTotalHeight-mXAxisOffset, mChartBackgroundPaint);

        canvas.drawText(mChartLabel, (mTotalWidth - mChartLabelBounds.width())/2 , mChartLabelBounds.height()+getPaddingTop(), mChartLabelTextPaint);
        canvas.drawText(mXAxisLabel, (mTotalWidth - mYAxisOffset - mXLabelBounds.width() - getPaddingLeft() - getPaddingRight())/2 + mYAxisOffset,
                        mTotalHeight - getPaddingBottom(), mXAxisLabelTextPaint);
        if (mFramed) {
            canvas.drawLine(mYAxisOffset , getPaddingTop()+spaceForChartLabel , mYAxisOffset , mTotalHeight - mXAxisOffset + mFrameWidth / 2 , mFramePaint);
            canvas.drawLine(mTotalWidth-getPaddingRight()-mFrameWidth/2-((mHasRightTickMarks)? mMajorTickMarkLength : 0) - ((mHasRightLabels)? spaceForLabels :0), getPaddingTop()+spaceForChartLabel,
                            mTotalWidth-getPaddingRight()-mFrameWidth/2-((mHasRightTickMarks)? mMajorTickMarkLength : 0) - ((mHasRightLabels)? spaceForLabels :0), mTotalHeight - mXAxisOffset + mFrameWidth / 2, mFramePaint);
            canvas.drawLine(mYAxisOffset, mTotalHeight - mXAxisOffset, mTotalWidth-getPaddingRight()-((mHasRightTickMarks)? mMajorTickMarkLength : 0)-((mHasRightLabels)? spaceForLabels :0), mTotalHeight - mXAxisOffset, mFramePaint);
            canvas.drawLine(mYAxisOffset-mFrameWidth/2, getPaddingTop() + spaceForChartLabel + mFrameWidth/2,
                            mTotalWidth-getPaddingRight()-((mHasRightTickMarks)? mMajorTickMarkLength : 0)-((mHasRightLabels)? spaceForLabels :0), getPaddingTop()+spaceForChartLabel+mFrameWidth/2, mFramePaint);
        }

        float lengthForTickmarks = mTotalHeight - spaceForChartLabel - mXAxisOffset - mTickMarkLabelHeight /2 - getPaddingBottom() - getPaddingTop() - ((mFramed)? mFrameWidth : 0);
        if (mHasBarValues){
            if ((mMaxBarValue/mTickMarkValues[mTickMarkValues.length-1])* lengthForTickmarks + mBarValueHeight +2*mBarValuePadding > lengthForTickmarks){
                lengthForTickmarks = lengthForTickmarks + mTickMarkLabelHeight/2 - mBarValueHeight - 2*mBarValuePadding;
            }
        }
        float numberOfTickmarks = mTickMarkLabels.length + mNumberOfMinorTickmarks* mTickMarkLabels.length - mNumberOfMinorTickmarks;
        float tickmarkSpacing = lengthForTickmarks/(numberOfTickmarks-1);
        float length;
        int j = 0;
        for (int i = 0; i<numberOfTickmarks; i++){
            if (i % (mNumberOfMinorTickmarks+1) == 0){
                length = (mHasLeftTickMarks)? mMajorTickMarkLength : 0;
                mTickmarkPaint.setColor(mMajorTickMarkColor);
                mTickmarkPaint.setStrokeWidth(mMajorTickMarkThickness);
                mLinePaint.setColor(mMajorLineColor);
                mLinePaint.setStrokeWidth(mMajorLineThickness);
                if (mHasLeftLabels) {
                    canvas.drawText(mTickMarkLabels[j], mYAxisOffset - length - mTickMarkLabelWidths[j] - ((mFramed) ? mFrameWidth / 2 : 0) - mTickMarkLabelPadding,
                            mTotalHeight - mXAxisOffset - ((mFramed) ? mFrameWidth / 2 : 0) - (i * tickmarkSpacing) + mTickMarkLabelHeight / 2,
                            mTickmarkLabelPaint);
                }
                if (mHasRightLabels){
                    canvas.drawText(mTickMarkLabels[j], mTotalWidth-getPaddingRight()-mMaxTickMarkLabelWidth, mTotalHeight-mXAxisOffset-((mFramed)? mFrameWidth/2 : 0)-(i*tickmarkSpacing)+ mTickMarkLabelHeight /2,mTickmarkLabelPaint );
                }
                j++;
            }else{
                length = mMinorTickMarkLength;
                mTickmarkPaint.setColor(mMinorTickMarkColor);
                mTickmarkPaint.setStrokeWidth(mMinorTickMarkThickness);
                mLinePaint.setColor(mMinorLineColor);
                mLinePaint.setStrokeWidth(mMinorLineThickness);
            }
            if (mHasLeftTickMarks) {
                canvas.drawLine(mYAxisOffset - ((mFramed) ? mFrameWidth / 2 : 0) - length, mTotalHeight - mXAxisOffset - ((mFramed) ? mFrameWidth / 2 : 0) - (i * tickmarkSpacing),
                        mYAxisOffset - ((mFramed) ? mFrameWidth / 2 : 0), mTotalHeight - mXAxisOffset - ((mFramed) ? mFrameWidth / 2 : 0) - (i * tickmarkSpacing), mTickmarkPaint);
            }
            if (mHasRightTickMarks){
                canvas.drawLine(mTotalWidth-getPaddingRight()-mMajorTickMarkLength-((mHasRightLabels)? spaceForLabels :0), mTotalHeight-mXAxisOffset-((mFramed)? mFrameWidth/2 : 0)-(i*tickmarkSpacing),
                                mTotalWidth-getPaddingRight()-mMajorTickMarkLength-((mHasRightLabels)? spaceForLabels :0)+length,mTotalHeight-mXAxisOffset-((mFramed)? mFrameWidth/2 : 0)-(i*tickmarkSpacing),
                                mTickmarkPaint);
            }
            if (mHasLines && ! (i == 0 && mFramed)){
                canvas.drawLine(mYAxisOffset+((mFramed)? mFrameWidth/2 : 0),
                                mTotalHeight-mXAxisOffset-((mFramed)? mFrameWidth/2 : 0)-(i*tickmarkSpacing),
                                mTotalWidth-getPaddingRight()-((mHasRightTickMarks)? mMajorTickMarkLength : 0)-((mHasRightLabels)? spaceForLabels :0)-((mFramed)? mFrameWidth : 0),
                                mTotalHeight-mXAxisOffset-((mFramed)? mFrameWidth/2 : 0)-(i*tickmarkSpacing),
                                mLinePaint);
            }
        }


        mYAxisTextPath.moveTo(mYLabelBounds.height() + getPaddingLeft(), mTotalHeight - mXAxisOffset);
        mYAxisTextPath.lineTo(mYLabelBounds.height() + getPaddingLeft(), getPaddingTop());
        float pathLength = mTotalHeight - mXAxisOffset;
        canvas.drawTextOnPath(mYAxisLabel, mYAxisTextPath, (pathLength - mYLabelBounds.width()) / 2, 0, mYAxisLabelTextPaint);

        float widthForBars = mTotalWidth - mYAxisOffset - getPaddingRight()- ((mFramed)? mFrameWidth*1.5f : 0)- ((mHasRightTickMarks)? mMajorTickMarkLength : 0)- ((mHasRightLabels)? spaceForLabels :0);

        if (mBarWidth == 0 && mBarSpacingWidth ==0) {
            float units = 3 * mBarValues.length +1;  // each bar take 2 units, space between bars are 1 unit;
            float spacePerUnit = widthForBars / units;
            mBarWidth = 2 * spacePerUnit;
            mBarSpacingWidth = spacePerUnit;
            while (mYAxisOffset+mBarSpacingWidth+mBarWidth/2f+((mBarValues.length -1)*(mBarSpacingWidth+mBarWidth))+
                    ((mFramed)? mFrameWidth :0)+((mHasRightTickMarks)? mMajorTickMarkLength : 0)+((mHasRightLabels)? spaceForLabels :0)+
                    mBarLabelHorizontalSpace+getPaddingRight() > mTotalWidth && widthForBars > 4){
                widthForBars -= 4;
                units = 3 * mBarValues.length +1;  // each bar take 2 units, space between bars are 1 unit;
                spacePerUnit = widthForBars / units;
                mBarWidth = 2 * spacePerUnit;
                mBarSpacingWidth = spacePerUnit;
            }
        }

        if (mBarWidth == 0){
            mBarWidth = (widthForBars - (mBarValues.length + 1)*mBarSpacingWidth)/mBarValues.length;
            while (mYAxisOffset+mBarSpacingWidth+mBarWidth/2f+((mBarValues.length -1)*(mBarSpacingWidth+mBarWidth))+
                    ((mFramed)? mFrameWidth :0)+((mHasRightTickMarks)? mMajorTickMarkLength : 0)+
                    ((mHasRightLabels)? spaceForLabels :0)+mBarLabelHorizontalSpace+getPaddingRight() > mTotalWidth && widthForBars > 4){
                widthForBars -= 4;
                mBarWidth = (widthForBars - (mBarValues.length + 1)*mBarSpacingWidth)/mBarValues.length;
            }
        }

        if (mBarSpacingWidth == 0){
            mBarSpacingWidth = (widthForBars - mBarValues.length*mBarWidth)/(mBarValues.length + 1);
            while (mYAxisOffset+mBarSpacingWidth+mBarWidth/2f+((mBarValues.length -1)*(mBarSpacingWidth+mBarWidth))+
                    ((mFramed)? mFrameWidth :0)+((mHasRightTickMarks)? mMajorTickMarkLength : 0)+((mHasRightLabels)? spaceForLabels :0)+mBarLabelHorizontalSpace+getPaddingRight() > mTotalWidth  && widthForBars > 4) {
                widthForBars -= 4;
                mBarSpacingWidth = (widthForBars - mBarValues.length*mBarWidth)/(mBarValues.length + 1);
            }
        }

        for (int i = 0; i < mBarValues.length; i++){
            if (mBarColors != null){
                mBarPaint.setColor(mBarColors[Math.min(i, mBarValues.length-1)]);
            }
            canvas.drawRect(mYAxisOffset+mBarSpacingWidth+((mFramed)? mFrameWidth/2 : 0)+(i*(mBarSpacingWidth+mBarWidth)),
                    mTotalHeight-mXAxisOffset-((mFramed)? mFrameWidth/2 : 0) - (mBarValues[i]/mTickMarkValues[mTickMarkValues.length-1])* lengthForTickmarks,
                    mYAxisOffset+mBarSpacingWidth+mBarWidth+((mFramed)? mFrameWidth/2 : 0)+(i*(mBarSpacingWidth+mBarWidth)),
                    mTotalHeight-mXAxisOffset-((mFramed)? mFrameWidth/2 : 0), mBarPaint);
            if(mHasBarValues){
                if (mBarValueTextColors != null){
                    mBarValuePaint.setColor(mBarValueTextColors[i]);
                }
                canvas.drawRect(mYAxisOffset+mBarSpacingWidth+((mFramed)? mFrameWidth/2 : 0)+(i*(mBarSpacingWidth+mBarWidth))+mBarWidth/2 - mBarValueWidths[i]/2-mBarValuePadding,
                        mTotalHeight-mXAxisOffset-((mFramed)? mFrameWidth/2 : 0) - (mBarValues[i]/mTickMarkValues[mTickMarkValues.length-1])* lengthForTickmarks-mBarValuePadding - mBarValueHeight,
                        mYAxisOffset+mBarSpacingWidth+((mFramed)? mFrameWidth/2 : 0)+(i*(mBarSpacingWidth+mBarWidth))+mBarWidth/2 + mBarValueWidths[i]/2+mBarValuePadding,
                        mTotalHeight-mXAxisOffset-((mFramed)? mFrameWidth/2 : 0) - (mBarValues[i]/mTickMarkValues[mTickMarkValues.length-1])* lengthForTickmarks-mBarValuePadding,
                        mChartBackgroundPaint);
                canvas.drawText(mBarValueLabels[i],
                        mYAxisOffset+mBarSpacingWidth+((mFramed)? mFrameWidth/2 : 0)+(i*(mBarSpacingWidth+mBarWidth))+mBarWidth/2 - mBarValueWidths[i]/2,
                        mTotalHeight-mXAxisOffset-((mFramed)? mFrameWidth/2 : 0) - (mBarValues[i]/mTickMarkValues[mTickMarkValues.length-1])* lengthForTickmarks-mBarValuePadding,
                        mBarValuePaint);
            }
        }

        float cos = (float)Math.cos(Math.toRadians(mBarLabelTextAngle));
        float sin = (float)Math.sin(Math.toRadians(mBarLabelTextAngle));
        float yPoint = mTotalHeight - mXAxisOffset + mBarLabelOffset + ((mFramed)? mFrameWidth/2 : 0) + mMaxBarLabelHeight*cos;
        float xPoint = mYAxisOffset - mMaxBarLabelHeightMinusBottom*sin/2;
        canvas.rotate(mBarLabelTextAngle, xPoint, yPoint);
        for (int i = 0; i < mBarLabels.length; i++){
            canvas.drawText(mBarLabels[i], xPoint+(mBarSpacingWidth+mBarWidth/2f+((mFramed)? mFrameWidth/2 : 0)+(i*(mBarSpacingWidth+mBarWidth)))*cos,
                    yPoint-(mBarSpacingWidth+mBarWidth/2f+((mFramed)? mFrameWidth/2 : 0)+(i*(mBarSpacingWidth+mBarWidth)))*sin, mBarLabelPaint);
        }
        //canvas.drawText("xxxxxxx", mYAxisOffset, yPoint, mAxisLabelTextPaint);
        //canvas.drawText("dfkhhkjj",mYAxisOffset+100*cos,yPoint-100*sin, mAxisLabelTextPaint);
       // canvas.drawText("aaaaaaa",mYAxisOffset+200*cos,yPoint-200*sin, mAxisLabelTextPaint);
    }

    private float getPixels(String textSize){
        String numberPart = textSize.replaceAll("[^0-9]", "");
        float value = Long.parseLong(numberPart);
        String alphaPart = textSize.replaceAll("[0-9]", "");
        if ( ! alphaPart.equals("")){
            return dipOrSpToPixels(value, alphaPart);
        }else{
            return value;
        }
    }

    private float dipOrSpToPixels(float value, String dpOrSp){
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        if (dpOrSp.equals("dp") || dpOrSp.equals("dip")) {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, metrics);
        }else{
            if (dpOrSp.equals("sp")){
                return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, metrics);
            }else{
                return 22;
            }
        }
    }

    private void getBarLabelSpacing() {
        mBarLabelVerticalSpace = 0;
        mMaxBarLabelHeight = 0;
        mMaxBarLabelHeightMinusBottom = 0;
        float len;
        float height;
        float bottom;
        Rect rect = new Rect();
        for(int i = 0; i < mBarLabels.length; i++){
            mBarLabelPaint.getTextBounds(mBarLabels[i], 0, mBarLabels[i].length(), rect);
            len= rect.width()*(float)Math.sin(Math.toRadians(mBarLabelTextAngle));
            if (len > mBarLabelVerticalSpace){
                mBarLabelVerticalSpace = len;
            }
            height = rect.height();
            bottom = rect.bottom;
            if (height > mMaxBarLabelHeight){
                mMaxBarLabelHeight = height;
            }
            if (height-bottom > mMaxBarLabelHeightMinusBottom){
                mMaxBarLabelHeightMinusBottom = height - bottom;
            }
            mBarLabelHorizontalSpace = rect.width()*(float)Math.cos(Math.toRadians(mBarLabelTextAngle)) + rect.height()* (float)Math.sin(Math.toRadians(mBarLabelTextAngle)) ;  // just use the last label.
        }
        mBarLabelVerticalSpace += mMaxBarLabelHeight * (float)Math.cos(Math.toRadians(mBarLabelTextAngle));
    }

    private void setTextStyle(int style, Paint paint){
        switch (style){
            case NORMAL:
                paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                break;
            case BOLD:
                paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                break;
            case ITALIC:
                paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
                break;
            case BOLD_ITALIC:
                paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));
        }
    }

    private void setTickMarkLabels(){
        mMaxTickMarkLabelWidth = 0;
        Rect rect = new Rect();
        mTickMarkLabelWidths = new float[mTickMarkValues.length];
        mTickMarkLabels = new String[mTickMarkValues.length];
        for (int i = 0; i < mTickMarkLabels.length; i++){
            mTickMarkLabels[i] = mTickMarkLabelFormat.format(mTickMarkValues[i]);
            mTickmarkLabelPaint.getTextBounds(mTickMarkLabels[i], 0, mTickMarkLabels[i].length(), rect);
            mTickMarkLabelWidths[i] = rect.width();
            if(rect.width() > mMaxTickMarkLabelWidth){
                mMaxTickMarkLabelWidth = rect.width();
            }
            mTickMarkLabelHeight = rect.height();
        }
    }

    private void setBarValueLabels(){
        if (mBarValuesForLabels == null){
            mBarValuesForLabels = mBarValues;
        }
        mMaxBarValue=0;
        for (int i = 0; i < mBarValues.length; i++){
            if (mMaxBarValue < mBarValues[i]){
                mMaxBarValue = mBarValues[i];
            }
        }
        Rect rect = new Rect();
        mBarValueWidths = new float[mBarValuesForLabels.length];
        mBarValueLabels = new String[mBarValuesForLabels.length];
        for (int i=0; i < mBarValuesForLabels.length; i++){
            mBarValueLabels[i] = mBarValueFormat.format(mBarValuesForLabels[i]);
            mBarValuePaint.getTextBounds(mBarValueLabels[i], 0, mBarValueLabels[i].length(), rect);
            mBarValueWidths[i] = rect.width();
            mBarValueHeight = rect.height();
        }
    }

    public void setBarValues(float[] barValues) {
        mBarValues = Arrays.copyOf(barValues, barValues.length);
        invalidate();
        requestLayout();
    }

    public void setBarValuesForLabels(float[] barValuesForLabels){
        mBarValuesForLabels = Arrays.copyOf(barValuesForLabels, barValuesForLabels.length);
        invalidate();
        requestLayout();
    }

    public void setBarLabels(String[] barLabels) {
        mBarLabels = Arrays.copyOf(barLabels, barLabels.length);
        getBarLabelSpacing();
        invalidate();
        requestLayout();
    }

    public void setBarValueTextColors(int[] barValueTextColors){
        mBarValueTextColors = Arrays.copyOf(barValueTextColors, barValueTextColors.length);
        invalidate();
        requestLayout();
    }

    public void setBarColors(int[] barColors){
        mBarColors = Arrays.copyOf(barColors, barColors.length);
        invalidate();
        requestLayout();
    }

    public void setTickMarkValues(float[] tickMarkValues){
        mTickMarkValues = Arrays.copyOf(tickMarkValues, tickMarkValues.length);
        invalidate();
        requestLayout();
    }

    public void setTickMarkLabelFormat(String pattern){
        mTickMarkLabelFormat.applyPattern(pattern);
        invalidate();
        requestLayout();
    }

    public void setBarValueFormat(String pattern){
        mBarValueFormat.applyPattern(pattern);
        invalidate();
        requestLayout();
    }

}
