package androidx.preference.MiuiColorPicker;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;

public class ColorPickerAlphaPatternDrawable extends Drawable {
  private Bitmap mBitmap;
  
  private Paint mPaint = new Paint();
  
  private Paint mPaintGray = new Paint();
  
  private Paint mPaintWhite = new Paint();
  
  private int mRectangleSize = 10;
  
  private int numRectanglesHorizontal;
  
  private int numRectanglesVertical;
  
  public ColorPickerAlphaPatternDrawable(int paramInt) {
    this.mRectangleSize = paramInt;
    this.mPaintWhite.setColor(-1);
    this.mPaintGray.setColor(-3421237);
  }
  
  private void generatePatternBitmap() {
    if (getBounds().width() > 0 && getBounds().height() > 0) {
      this.mBitmap = Bitmap.createBitmap(getBounds().width(), getBounds().height(), Bitmap.Config.ARGB_8888);
      Canvas canvas = new Canvas(this.mBitmap);
      Rect rect = new Rect();
      boolean bool = true;
      for (byte b = 0; b <= this.numRectanglesVertical; b++) {
        boolean bool1 = bool;
        for (byte b1 = 0; b1 <= this.numRectanglesHorizontal; b1++) {
          Paint paint;
          rect.top = this.mRectangleSize * b;
          rect.left = this.mRectangleSize * b1;
          rect.bottom = rect.top + this.mRectangleSize;
          rect.right = rect.left + this.mRectangleSize;
          if (bool1) {
            paint = this.mPaintWhite;
          } else {
            paint = this.mPaintGray;
          } 
          canvas.drawRect(rect, paint);
          if (!bool1) {
            bool1 = true;
          } else {
            bool1 = false;
          } 
        } 
        if (!bool) {
          bool = true;
        } else {
          bool = false;
        } 
      } 
      setRoundedCornerBitmap();
    } 
  }
  
  private void setRoundedCornerBitmap() {
    Bitmap bitmap = Bitmap.createBitmap(this.mBitmap.getWidth(), this.mBitmap.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    Paint paint = new Paint();
    Rect rect = new Rect(0, 0, this.mBitmap.getWidth(), this.mBitmap.getHeight());
    RectF rectF = new RectF(rect);
    paint.setAntiAlias(true);
    canvas.drawARGB(0, 0, 0, 0);
    canvas.drawRoundRect(rectF, 25.0F, 25.0F, paint);
    paint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    canvas.drawBitmap(this.mBitmap, rect, rect, paint);
    this.mBitmap = bitmap;
  }
  
  public void draw(Canvas paramCanvas) {
    paramCanvas.drawBitmap(this.mBitmap, null, getBounds(), this.mPaint);
  }
  
  public int getOpacity() {
    return PixelFormat.UNKNOWN;
  }
  
  protected void onBoundsChange(Rect paramRect) {
    super.onBoundsChange(paramRect);
    int i = paramRect.height();
    this.numRectanglesHorizontal = (int)Math.ceil((paramRect.width() / this.mRectangleSize));
    this.numRectanglesVertical = (int)Math.ceil((i / this.mRectangleSize));
    generatePatternBitmap();
  }
  
  public void setAlpha(int paramInt) {
    throw new UnsupportedOperationException("Alpha is not supported by this drawwable.");
  }
  
  public void setColorFilter(ColorFilter paramColorFilter) {
    throw new UnsupportedOperationException("ColorFilter is not supported by this drawwable.");
  }
}
