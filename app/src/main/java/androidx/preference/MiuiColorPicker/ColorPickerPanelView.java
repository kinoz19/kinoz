package androidx.preference.MiuiColorPicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;

public class ColorPickerPanelView extends View {
  private static final float BORDER_WIDTH_PX = 1.0F;
  
  private ColorPickerAlphaPatternDrawable mAlphaPattern;
  
  private int mBorderColor = -9539986;
  
  private Paint mBorderPaint;
  
  private int mColor = -16777216;
  
  private Paint mColorPaint;
  
  private RectF mColorRect;
  
  private float mDensity = 1.0F;
  
  private RectF mDrawingRect;
  
  private Rect mTextBoundRect = new Rect();
  
  private Paint mTextPaint;
  
  private PorterDuffXfermode mXfermode;
  
  public ColorPickerPanelView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public ColorPickerPanelView(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public ColorPickerPanelView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init();
  }
  
  private void init() {
    this.mBorderPaint = new Paint();
    this.mColorPaint = new Paint();
    this.mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
    this.mDensity = (getContext().getResources().getDisplayMetrics()).density;
  }
  
  private void setUpColorRect() {
    RectF rectF = this.mDrawingRect;
    float f1 = rectF.left;
    float f2 = rectF.top;
    float f3 = rectF.bottom;
    this.mColorRect = new RectF(f1 + 1.0F, f2 + 1.0F, rectF.right - 1.0F, f3 - 1.0F);
    this.mAlphaPattern = new ColorPickerAlphaPatternDrawable((int)(5.0F * this.mDensity));
    this.mAlphaPattern.setBounds(Math.round(this.mColorRect.left), Math.round(this.mColorRect.top), Math.round(this.mColorRect.right), Math.round(this.mColorRect.bottom));
  }
  
  public int getBorderColor() {
    return this.mBorderColor;
  }
  
  public int getColor() {
    return this.mColor;
  }
  
  protected void onDraw(Canvas paramCanvas) {
    String str;
    this.mBorderPaint.setColor(this.mBorderColor);
    paramCanvas.drawRoundRect(this.mDrawingRect, 25.0F, 25.0F, this.mBorderPaint);
    RectF rectF = this.mColorRect;
    if (this.mAlphaPattern != null)
      this.mAlphaPattern.draw(paramCanvas); 
    if (getContentDescription() == "new") {
      str = "Готово";
    } else {
      str = "Отмена";
    } 
    this.mColorPaint.setColor(this.mColor);
    paramCanvas.drawRoundRect(rectF, 25.0F, 25.0F, this.mColorPaint);
    this.mColorPaint.setTextSize(60.0F);
    this.mColorPaint.setColor(-1);
    this.mColorPaint.getTextBounds(str, 0, str.length(), this.mTextBoundRect);
    int i = (int)this.mColorPaint.measureText(str);
    int j = this.mTextBoundRect.height();
    this.mColorPaint.setXfermode((Xfermode)this.mXfermode);
    paramCanvas.drawText(str, (getWidth() / 2 - i / 2), (getHeight() / 2 + j / 2), this.mColorPaint);
    this.mColorPaint.setXfermode(null);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    setMeasuredDimension(View.MeasureSpec.getSize(paramInt1), View.MeasureSpec.getSize(paramInt2));
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    this.mDrawingRect = new RectF();
    this.mDrawingRect.left = getPaddingLeft();
    this.mDrawingRect.right = (paramInt1 - getPaddingRight());
    this.mDrawingRect.top = getPaddingTop();
    this.mDrawingRect.bottom = (paramInt2 - getPaddingBottom());
    setUpColorRect();
  }
  
  public void setBorderColor(int paramInt) {
    this.mBorderColor = paramInt;
    invalidate();
  }
  
  public void setColor(int paramInt) {
    this.mColor = paramInt;
    invalidate();
  }
}