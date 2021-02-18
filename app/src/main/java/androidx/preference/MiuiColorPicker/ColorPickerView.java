package androidx.preference.MiuiColorPicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ColorPickerView extends View implements ColorPickerPresetView.OnPresetClickListener {
  private static final float BORDER_WIDTH_PX = 1.0F;
  
  private static final int PANEL_ALPHA = 2;
  
  private static final int PANEL_HUE = 1;
  
  private static final int PANEL_SAT_VAL = 0;
  
  private float ALPHA_PANEL_HEIGHT = 20.0F;
  
  private float HUE_PANEL_WIDTH = 30.0F;
  
  private float PALETTE_CIRCLE_TRACKER_RADIUS = 5.0F;
  
  private float PANEL_SPACING = 10.0F;
  
  private float RECTANGLE_TRACKER_OFFSET = 2.0F;
  
  private int mAlpha = 255;
  
  private Paint mAlphaPaint;
  
  private ColorPickerAlphaPatternDrawable mAlphaPattern;
  
  private RectF mAlphaRect;
  
  private Shader mAlphaShader;
  
  private String mAlphaSliderText = "";
  
  private Paint mAlphaTextPaint;
  
  private int mBorderColor = -9539986;
  
  private Paint mBorderPaint;
  
  private float mDensity = 1.0F;
  
  private float mDrawingOffset;
  
  private RectF mDrawingRect;
  
  private float mHue = 360.0F;
  
  private Paint mHuePaint;
  
  private RectF mHueRect;
  
  private Shader mHueShader;
  
  private Paint mHueTrackerPaint;
  
  private int mLastTouchedPanel = 0;
  
  private OnColorChangedListener mListener;
  
  private float mSat = 0.0F;
  
  private Shader mSatShader;
  
  private Paint mSatValPaint;
  
  private RectF mSatValRect;
  
  private Paint mSatValTrackerPaint;
  
  private boolean mShowAlphaPanel = false;
  
  private int mSliderTrackerColor = -14935012;
  
  private Point mStartTouchPoint = null;
  
  private float mVal = 0.0F;
  
  private Shader mValShader;
  
  public ColorPickerView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public ColorPickerView(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public ColorPickerView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init();
  }
  
  private Point alphaToPoint(int paramInt) {
    RectF rectF = this.mAlphaRect;
    float f = rectF.width();
    Point point = new Point();
    point.x = (int)(f - paramInt * f / 255.0F + rectF.left);
    point.y = (int)rectF.top;
    return point;
  }
  
  private int[] buildHueColorArray() {
    int[] arrayOfInt = new int[361];
    byte b = 0;
    int i = arrayOfInt.length - 1;
    while (i >= 0) {
      arrayOfInt[b] = Color.HSVToColor(new float[] { i, 1.0F, 1.0F });
      i--;
      b++;
    } 
    return arrayOfInt;
  }
  
  private float calculateRequiredOffset() {
    return 1.5F * Math.max(Math.max(this.PALETTE_CIRCLE_TRACKER_RADIUS, this.RECTANGLE_TRACKER_OFFSET), 1.0F * this.mDensity);
  }

  private int chooseWidth(int mode, int size) {
    return (mode == Integer.MIN_VALUE || mode == 1073741824) ? size : getPrefferedWidth();
  }

  private int chooseHeight(int mode, int size) {
    return (mode == Integer.MIN_VALUE || mode == 1073741824) ? size : getPrefferedHeight();
  }



  private void drawAlphaPanel(Canvas paramCanvas) {
    if (this.mShowAlphaPanel && this.mAlphaRect != null && this.mAlphaPattern != null) {
      RectF rectF1 = this.mAlphaRect;
      this.mBorderPaint.setColor(this.mBorderColor);
      paramCanvas.drawRoundRect(new RectF(new Rect((int)(rectF1.left - 1.0F), (int)(rectF1.top - 1.0F), (int)(rectF1.right + 1.0F), (int)(rectF1.bottom + 1.0F))), 25.0F, 25.0F, this.mBorderPaint);
      this.mAlphaPattern.draw(paramCanvas);
      float[] arrayOfFloat = new float[3];
      arrayOfFloat[0] = this.mHue;
      arrayOfFloat[1] = this.mSat;
      arrayOfFloat[2] = this.mVal;
      int i = Color.HSVToColor(arrayOfFloat);
      int j = Color.HSVToColor(0, arrayOfFloat);
      this.mAlphaShader = (Shader)new LinearGradient(rectF1.left, rectF1.top, rectF1.right, rectF1.top, i, j, Shader.TileMode.CLAMP);
      this.mAlphaPaint.setShader(this.mAlphaShader);
      paramCanvas.drawRoundRect(rectF1, 25.0F, 25.0F, this.mAlphaPaint);
      if (this.mAlphaSliderText != null && this.mAlphaSliderText != "")
        paramCanvas.drawText(this.mAlphaSliderText, rectF1.centerX(), rectF1.centerY() + 4.0F * this.mDensity, this.mAlphaTextPaint); 
      float f = 4.0F * this.mDensity / 2.0F;
      Point point = alphaToPoint(this.mAlpha);
      RectF rectF2 = new RectF();
      rectF2.left = point.x - f;
      rectF2.right = point.x + f;
      rectF1.top -= this.RECTANGLE_TRACKER_OFFSET;
      rectF1.bottom += this.RECTANGLE_TRACKER_OFFSET;
      paramCanvas.drawRoundRect(rectF2, 10.0F, 10.0F, this.mHueTrackerPaint);
    } 
  }
  
  private void drawHuePanel(Canvas paramCanvas) {
    RectF rectF1 = this.mHueRect;
    this.mBorderPaint.setColor(this.mBorderColor);
    paramCanvas.drawRoundRect(new RectF(new Rect((int)(rectF1.left - 1.0F), (int)(rectF1.top - 1.0F), (int)(rectF1.right + 1.0F), (int)(rectF1.bottom + 1.0F))), 30.0F, 30.0F, this.mBorderPaint);
    if (this.mHueShader == null) {
      this.mHueShader = (Shader)new LinearGradient(rectF1.left, rectF1.top, rectF1.left, rectF1.bottom, buildHueColorArray(), null, Shader.TileMode.CLAMP);
      this.mHuePaint.setShader(this.mHueShader);
    } 
    paramCanvas.drawRoundRect(rectF1, 30.0F, 30.0F, this.mHuePaint);
    float f = 4.0F * this.mDensity / 2.0F;
    Point point = hueToPoint(this.mHue);
    RectF rectF2 = new RectF();
    rectF1.left -= this.RECTANGLE_TRACKER_OFFSET;
    rectF1.right += this.RECTANGLE_TRACKER_OFFSET;
    rectF2.top = point.y - f;
    rectF2.bottom = point.y + f;
    paramCanvas.drawRoundRect(rectF2, 10.0F, 10.0F, this.mHueTrackerPaint);
  }
  
  private void drawSatValPanel(Canvas paramCanvas) {
    RectF rectF = this.mSatValRect;
    this.mBorderPaint.setColor(this.mBorderColor);
    paramCanvas.drawRect(this.mDrawingRect.left, this.mDrawingRect.top, 1.0F + rectF.right, 1.0F + rectF.bottom, this.mBorderPaint);
    if (this.mValShader == null)
      this.mValShader = (Shader)new LinearGradient(rectF.left, rectF.top, rectF.left, rectF.bottom, -1, -16777216, Shader.TileMode.CLAMP); 
    int i = Color.HSVToColor(new float[] { this.mHue, 1.0F, 1.0F });
    this.mSatShader = (Shader)new LinearGradient(rectF.left, rectF.top, rectF.right, rectF.top, -1, i, Shader.TileMode.CLAMP);
    ComposeShader composeShader = new ComposeShader(this.mValShader, this.mSatShader, PorterDuff.Mode.MULTIPLY);
    this.mSatValPaint.setShader((Shader)composeShader);
    paramCanvas.drawRect(rectF, this.mSatValPaint);
    Point point = satValToPoint(this.mSat, this.mVal);
    this.mSatValTrackerPaint.setColor(-16777216);
    paramCanvas.drawCircle(point.x, point.y, this.PALETTE_CIRCLE_TRACKER_RADIUS - 1.0F * this.mDensity, this.mSatValTrackerPaint);
    this.mSatValTrackerPaint.setColor(-2236963);
    paramCanvas.drawCircle(point.x, point.y, this.PALETTE_CIRCLE_TRACKER_RADIUS, this.mSatValTrackerPaint);
  }
  
  private int getPrefferedHeight() {
    int i = (int)(200.0F * this.mDensity);
    int j = i;
    if (this.mShowAlphaPanel)
      j = (int)(i + this.PANEL_SPACING + this.ALPHA_PANEL_HEIGHT); 
    return j;
  }
  
  private int getPrefferedWidth() {
    int i = getPrefferedHeight();
    int j = i;
    if (this.mShowAlphaPanel)
      j = (int)(i - this.PANEL_SPACING + this.ALPHA_PANEL_HEIGHT); 
    return (int)(j + this.HUE_PANEL_WIDTH + this.PANEL_SPACING);
  }
  
  private Point hueToPoint(float paramFloat) {
    RectF rectF = this.mHueRect;
    float f = rectF.height();
    Point point = new Point();
    point.y = (int)(f - paramFloat * f / 360.0F + rectF.top);
    point.x = (int)rectF.left;
    return point;
  }
  
  private void init() {
    this.mDensity = (getContext().getResources().getDisplayMetrics()).density;
    this.PALETTE_CIRCLE_TRACKER_RADIUS *= this.mDensity;
    this.RECTANGLE_TRACKER_OFFSET *= this.mDensity;
    this.HUE_PANEL_WIDTH *= this.mDensity;
    this.ALPHA_PANEL_HEIGHT *= this.mDensity;
    this.PANEL_SPACING *= this.mDensity;
    this.mDrawingOffset = calculateRequiredOffset();
    initPaintTools();
    setFocusable(true);
    setFocusableInTouchMode(true);
  }
  
  private void initPaintTools() {
    this.mSatValPaint = new Paint();
    this.mSatValTrackerPaint = new Paint();
    this.mHuePaint = new Paint();
    this.mHueTrackerPaint = new Paint();
    this.mAlphaPaint = new Paint();
    this.mAlphaTextPaint = new Paint();
    this.mBorderPaint = new Paint();
    this.mSatValTrackerPaint.setStyle(Paint.Style.STROKE);
    this.mSatValTrackerPaint.setStrokeWidth(this.mDensity * 2.0F);
    this.mSatValTrackerPaint.setAntiAlias(true);
    this.mHueTrackerPaint.setColor(this.mSliderTrackerColor);
    this.mHueTrackerPaint.setStyle(Paint.Style.STROKE);
    this.mHueTrackerPaint.setStrokeWidth(this.mDensity * 2.0F);
    this.mHueTrackerPaint.setAntiAlias(true);
    this.mAlphaTextPaint.setColor(-14935012);
    this.mAlphaTextPaint.setTextSize(14.0F * this.mDensity);
    this.mAlphaTextPaint.setAntiAlias(true);
    this.mAlphaTextPaint.setTextAlign(Paint.Align.CENTER);
    this.mAlphaTextPaint.setFakeBoldText(true);
  }

  private boolean moveTrackersIfNeeded(MotionEvent event) {
    if (this.mStartTouchPoint == null) {
      return false;
    }
    int startX = this.mStartTouchPoint.x;
    int startY = this.mStartTouchPoint.y;
    if (this.mHueRect.contains((float) startX, (float) startY)) {
      this.mLastTouchedPanel = 1;
      this.mHue = pointToHue(event.getY());
      return true;
    } else if (this.mSatValRect.contains((float) startX, (float) startY)) {
      this.mLastTouchedPanel = 0;
      float[] result = pointToSatVal(event.getX(), event.getY());
      this.mSat = result[0];
      this.mVal = result[1];
      return true;
    } else if (this.mAlphaRect == null || !this.mAlphaRect.contains((float) startX, (float) startY)) {
      return false;
    } else {
      this.mLastTouchedPanel = 2;
      this.mAlpha = pointToAlpha((int) event.getX());
      return true;
    }
  }



  private int pointToAlpha(int paramInt) {
    RectF rectF = this.mAlphaRect;
    int i = (int)rectF.width();
    if (paramInt < rectF.left) {
      paramInt = 0;
      return 255 - paramInt * 255 / i;
    } 
    if (paramInt > rectF.right) {
      paramInt = i;
      return 255 - paramInt * 255 / i;
    } 
    paramInt -= (int)rectF.left;
    return 255 - paramInt * 255 / i;
  }
  
  private float pointToHue(float paramFloat) {
    RectF rectF = this.mHueRect;
    float f = rectF.height();
    if (paramFloat < rectF.top) {
      paramFloat = 0.0F;
      return 360.0F - paramFloat * 360.0F / f;
    } 
    if (paramFloat > rectF.bottom) {
      paramFloat = f;
      return 360.0F - paramFloat * 360.0F / f;
    } 
    paramFloat -= rectF.top;
    return 360.0F - paramFloat * 360.0F / f;
  }
  
  private float[] pointToSatVal(float paramFloat1, float paramFloat2) {
    RectF rectF = this.mSatValRect;
    float f1 = rectF.width();
    float f2 = rectF.height();
    if (paramFloat1 < rectF.left) {
      paramFloat1 = 0.0F;
    } else if (paramFloat1 > rectF.right) {
      paramFloat1 = f1;
    } else {
      paramFloat1 -= rectF.left;
    } 
    if (paramFloat2 < rectF.top) {
      paramFloat2 = 0.0F;
      return new float[] { 1.0F / f1 * paramFloat1, 1.0F - 1.0F / f2 * paramFloat2 };
    } 
    if (paramFloat2 > rectF.bottom) {
      paramFloat2 = f2;
      return new float[] { 1.0F / f1 * paramFloat1, 1.0F - 1.0F / f2 * paramFloat2 };
    } 
    paramFloat2 -= rectF.top;
    return new float[] { 1.0F / f1 * paramFloat1, 1.0F - 1.0F / f2 * paramFloat2 };
  }
  
  private Point satValToPoint(float paramFloat1, float paramFloat2) {
    RectF rectF = this.mSatValRect;
    float f1 = rectF.height();
    float f2 = rectF.width();
    Point point = new Point();
    point.x = (int)(paramFloat1 * f2 + rectF.left);
    point.y = (int)((1.0F - paramFloat2) * f1 + rectF.top);
    return point;
  }
  
  private void setUpAlphaRect() {
    if (this.mShowAlphaPanel) {
      RectF rectF = this.mDrawingRect;
      float f1 = rectF.left;
      float f2 = rectF.bottom;
      float f3 = this.ALPHA_PANEL_HEIGHT;
      float f4 = rectF.bottom;
      this.mAlphaRect = new RectF(f1 + 1.0F, f2 - f3 + 1.0F, rectF.right - 1.0F, f4 - 1.0F);
      this.mAlphaPattern = new ColorPickerAlphaPatternDrawable((int)(5.0F * this.mDensity));
      this.mAlphaPattern.setBounds(Math.round(this.mAlphaRect.left), Math.round(this.mAlphaRect.top), Math.round(this.mAlphaRect.right), Math.round(this.mAlphaRect.bottom));
    } 
  }
  
  private void setUpHueRect() {
    float f5;
    RectF rectF = this.mDrawingRect;
    float f1 = rectF.right;
    float f2 = this.HUE_PANEL_WIDTH;
    float f3 = rectF.top;
    float f4 = rectF.bottom;
    if (this.mShowAlphaPanel) {
      f5 = this.PANEL_SPACING + this.ALPHA_PANEL_HEIGHT;
    } else {
      f5 = 0.0F;
    } 
    this.mHueRect = new RectF(f1 - f2 + 1.0F, f3 + 1.0F, rectF.right - 1.0F, f4 - 1.0F - f5);
  }
  
  private void setUpSatValRect() {
    RectF rectF = this.mDrawingRect;
    float f1 = rectF.height() - 2.0F;
    float f2 = f1;
    if (this.mShowAlphaPanel)
      f2 = f1 - this.PANEL_SPACING + this.ALPHA_PANEL_HEIGHT; 
    float f3 = rectF.left + 1.0F;
    f1 = rectF.top + 1.0F;
    this.mSatValRect = new RectF(f3, f1, f3 + f2, f1 + f2);
  }
  
  public String getAlphaSliderText() {
    return this.mAlphaSliderText;
  }
  
  public boolean getAlphaSliderVisible() {
    return this.mShowAlphaPanel;
  }
  
  public int getBorderColor() {
    return this.mBorderColor;
  }
  
  public int getColor() {
    return Color.HSVToColor(this.mAlpha, new float[] { this.mHue, this.mSat, this.mVal });
  }
  
  public float getDrawingOffset() {
    return this.mDrawingOffset;
  }
  
  public int getSliderTrackerColor() {
    return this.mSliderTrackerColor;
  }
  
  protected void onDraw(Canvas paramCanvas) {
    if (this.mDrawingRect.width() > 0.0F && this.mDrawingRect.height() > 0.0F) {
      drawSatValPanel(paramCanvas);
      drawHuePanel(paramCanvas);
      drawAlphaPanel(paramCanvas);
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int i = View.MeasureSpec.getMode(paramInt1);
    int j = View.MeasureSpec.getMode(paramInt2);
    paramInt1 = View.MeasureSpec.getSize(paramInt1);
    paramInt2 = View.MeasureSpec.getSize(paramInt2);
    paramInt1 = chooseWidth(i, paramInt1);
    paramInt2 = chooseHeight(j, paramInt2);
    if (!this.mShowAlphaPanel) {
      j = (int)(paramInt1 - this.PANEL_SPACING - this.HUE_PANEL_WIDTH);
      if (j > paramInt2 || getTag().equals("landscape")) {
        paramInt1 = paramInt2;
        paramInt2 = (int)(paramInt1 + this.PANEL_SPACING + this.HUE_PANEL_WIDTH);
      } else {
        paramInt2 = paramInt1;
        paramInt1 = j;
      } 
    } else {
      j = (int)(paramInt2 - this.ALPHA_PANEL_HEIGHT + this.HUE_PANEL_WIDTH);
      if (j > paramInt1) {
        paramInt2 = paramInt1;
        paramInt1 = (int)(paramInt1 - this.HUE_PANEL_WIDTH + this.ALPHA_PANEL_HEIGHT);
      } else {
        paramInt1 = paramInt2;
        paramInt2 = j;
      } 
    } 
    setMeasuredDimension(paramInt2, paramInt1);
  }
  
  public void onPresetClick(int paramInt) {
    setColor(paramInt, true);
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    this.mDrawingRect = new RectF();
    this.mDrawingRect.left = this.mDrawingOffset + getPaddingLeft();
    this.mDrawingRect.right = paramInt1 - this.mDrawingOffset - getPaddingRight();
    this.mDrawingRect.top = this.mDrawingOffset + getPaddingTop();
    this.mDrawingRect.bottom = paramInt2 - this.mDrawingOffset - getPaddingBottom();
    setUpSatValRect();
    setUpHueRect();
    setUpAlphaRect();
  }

  public boolean onTouchEvent(MotionEvent event) {
    boolean update = false;
    switch (event.getAction()) {
      case 0:
        this.mStartTouchPoint = new Point((int) event.getX(), (int) event.getY());
        update = moveTrackersIfNeeded(event);
        break;
      case 1:
        this.mStartTouchPoint = null;
        update = moveTrackersIfNeeded(event);
        break;
      case 2:
        update = moveTrackersIfNeeded(event);
        break;
    }
    if (!update) {
      return super.onTouchEvent(event);
    }
    if (this.mListener != null) {
      this.mListener.onColorChanged(Color.HSVToColor(this.mAlpha, new float[]{this.mHue, this.mSat, this.mVal}));
    }
    invalidate();
    return true;
  }

  public boolean onTrackballEvent(MotionEvent event) {
    float x = event.getX();
    float y = event.getY();
    boolean update = false;
    if (event.getAction() == 2) {
      switch (this.mLastTouchedPanel) {
        case 0:
          float sat = this.mSat + (x / 50.0f);
          float val = this.mVal - (y / 50.0f);
          if (sat < 0.0f) {
            sat = 0.0f;
          } else if (sat > 1.0f) {
            sat = 1.0f;
          }
          if (val < 0.0f) {
            val = 0.0f;
          } else if (val > 1.0f) {
            val = 1.0f;
          }
          this.mSat = sat;
          this.mVal = val;
          update = true;
          break;
        case 1:
          float hue = this.mHue - (y * 10.0f);
          if (hue < 0.0f) {
            hue = 0.0f;
          } else if (hue > 360.0f) {
            hue = 360.0f;
          }
          this.mHue = hue;
          update = true;
          break;
        case 2:
          if (this.mShowAlphaPanel && this.mAlphaRect != null) {
            int alpha = (int) (((float) this.mAlpha) - (x * 10.0f));
            if (alpha < 0) {
              alpha = 0;
            } else if (alpha > 255) {
              alpha = 255;
            }
            this.mAlpha = alpha;
            update = true;
            break;
          } else {
            update = false;
            break;
          }
      }
    }
    if (!update) {
      return super.onTrackballEvent(event);
    }
    if (this.mListener != null) {
      this.mListener.onColorChanged(Color.HSVToColor(this.mAlpha, new float[]{this.mHue, this.mSat, this.mVal}));
    }
    invalidate();
    return true;
  }


  public void setAlphaSliderText(int paramInt) {
    setAlphaSliderText(getContext().getString(paramInt));
  }
  
  public void setAlphaSliderText(String paramString) {
    this.mAlphaSliderText = paramString;
    invalidate();
  }
  
  public void setAlphaSliderVisible(boolean paramBoolean) {
    if (this.mShowAlphaPanel != paramBoolean) {
      this.mShowAlphaPanel = paramBoolean;
      this.mValShader = null;
      this.mSatShader = null;
      this.mHueShader = null;
      this.mAlphaShader = null;
      requestLayout();
    } 
  }
  
  public void setBorderColor(int paramInt) {
    this.mBorderColor = paramInt;
    invalidate();
  }
  
  public void setColor(int paramInt) {
    setColor(paramInt, false);
  }
  
  public void setColor(int paramInt, boolean paramBoolean) {
    int i = Color.alpha(paramInt);
    float[] arrayOfFloat = new float[3];
    Color.colorToHSV(paramInt, arrayOfFloat);
    this.mAlpha = i;
    this.mHue = arrayOfFloat[0];
    this.mSat = arrayOfFloat[1];
    this.mVal = arrayOfFloat[2];
    if (paramBoolean && this.mListener != null)
      this.mListener.onColorChanged(Color.HSVToColor(this.mAlpha, new float[] { this.mHue, this.mSat, this.mVal })); 
    invalidate();
  }
  
  public void setOnColorChangedListener(OnColorChangedListener paramOnColorChangedListener) {
    this.mListener = paramOnColorChangedListener;
  }
  
  public void setSliderTrackerColor(int paramInt) {
    this.mSliderTrackerColor = paramInt;
    this.mHueTrackerPaint.setColor(this.mSliderTrackerColor);
    invalidate();
  }
  
  public static interface OnColorChangedListener {
    void onColorChanged(int param1Int);
  }
}
