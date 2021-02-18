package androidx.preference.MiuiColorPicker;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class ColorPickerPresetView extends View implements View.OnClickListener {
  private int mColor;
  
  private OnPresetClickListener mListener;
  
  public ColorPickerPresetView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public ColorPickerPresetView(Context paramContext, int paramInt) {
    this(paramContext);
    this.mColor = paramInt;
    setOnClickListener(this);
    init();
  }
  
  public ColorPickerPresetView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }
  
  public void init() {
    Log.d("colorwer", "init: ");
    GradientDrawable gradientDrawable = new GradientDrawable();
    gradientDrawable.setCornerRadius(41.0F);
    gradientDrawable.setColorFilter(this.mColor, PorterDuff.Mode.SRC_IN);
    setBackgroundDrawable((Drawable)gradientDrawable);
  }
  
  public void onClick(View paramView) {
    if (this.mListener != null)
      this.mListener.onPresetClick(this.mColor); 
  }
  
  public void setOnPresetClickListener(OnPresetClickListener paramOnPresetClickListener) {
    this.mListener = paramOnPresetClickListener;
  }
  
  public static interface OnPresetClickListener {
    void onPresetClick(int param1Int);
  }
}
