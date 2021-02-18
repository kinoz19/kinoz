package androidx.preference.MiuiColorPicker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ColorPickerDialogLayout extends LinearLayout {
  public ColorPickerView colorPickerView;
  
  public boolean isNight;
  
  public EditText mHexVal;
  
  public ColorPickerPanelView mNewColor;
  
  public ColorPickerPanelView mOldColor;
  
  public LinearLayout presetLayout;
  
  public LinearLayout textHexWrapper;
  
  public ColorPickerDialogLayout(Context paramContext) {
    super(paramContext);
    init(paramContext);
  }
  
  private Drawable getHexWrapperBack() {
    GradientDrawable gradientDrawable = new GradientDrawable();
    gradientDrawable.setCornerRadius(pX(15.0F));
    if (this.isNight) {
      String str1 = "#60ffffff";
      gradientDrawable.setColorFilter(Color.parseColor(str1), PorterDuff.Mode.SRC_IN);
      return (Drawable)gradientDrawable;
    } 
    String str = "#60000000";
    gradientDrawable.setColorFilter(Color.parseColor(str), PorterDuff.Mode.SRC_IN);
    return (Drawable)gradientDrawable;
  }
  
  private LinearLayout.LayoutParams getPresetParams() {
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(pX(40.0F), pX(40.0F));
    layoutParams.setMargins(pX(3.0F), 0, pX(3.0F), 0);
    layoutParams.gravity = 16;
    layoutParams.weight = 1.0F;
    return layoutParams;
  }
  
  private void init(Context paramContext) {
    boolean bool;
    if (((paramContext.getResources().getConfiguration()).uiMode & 0x30) == 32) {
      bool = true;
    } else {
      bool = false;
    } 
    this.isNight = bool;
    setOrientation(HORIZONTAL);
    setPadding(pX(5.0F), pX(5.0F), pX(5.0F), 0);
    this.colorPickerView = new ColorPickerView(paramContext);
    this.colorPickerView.setTag("portrait");
    this.colorPickerView.setLayerType(LAYER_TYPE_NONE, new Paint());
    addView(this.colorPickerView, (ViewGroup.LayoutParams)new LinearLayout.LayoutParams(-2, -2));
    this.textHexWrapper = new LinearLayout(paramContext);
    this.textHexWrapper.setGravity(17);
    this.textHexWrapper.setBackground(getHexWrapperBack());
    LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
    layoutParams2.setMargins(pX(6.0F), pX(5.0F), pX(6.0F), pX(5.0F));
    addView((View)this.textHexWrapper, (ViewGroup.LayoutParams)layoutParams2);
    this.mHexVal = new EditText(paramContext);
    this.mHexVal.setHint("HEX");
    this.mHexVal.setMinEms(5);
    this.mHexVal.setImeOptions(6);
    this.mHexVal.setSingleLine();
    this.mHexVal.setMaxEms(7);
    this.mHexVal.setInputType(4096);
    this.mHexVal.setVisibility(GONE);
    this.textHexWrapper.addView((View)this.mHexVal, (ViewGroup.LayoutParams)new LinearLayout.LayoutParams(-2, -2));
    LinearLayout linearLayout = new LinearLayout(paramContext);
    linearLayout.setGravity(16);
    LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(-2, -1);
    layoutParams4.leftMargin = pX(6.0F);
    layoutParams4.gravity = 1;
    this.textHexWrapper.addView((View)linearLayout, (ViewGroup.LayoutParams)layoutParams4);
    ColorPickerPresetView colorPickerPresetView = new ColorPickerPresetView(paramContext, -65536);
    linearLayout.addView(colorPickerPresetView, (ViewGroup.LayoutParams)getPresetParams());
    colorPickerPresetView.setOnPresetClickListener(this.colorPickerView);
    colorPickerPresetView = new ColorPickerPresetView(paramContext, -16711936);
    linearLayout.addView(colorPickerPresetView, (ViewGroup.LayoutParams)getPresetParams());
    colorPickerPresetView.setOnPresetClickListener(this.colorPickerView);
    colorPickerPresetView = new ColorPickerPresetView(paramContext, -16776961);
    linearLayout.addView(colorPickerPresetView, (ViewGroup.LayoutParams)getPresetParams());
    colorPickerPresetView.setOnPresetClickListener(this.colorPickerView);
    colorPickerPresetView = new ColorPickerPresetView(paramContext, -1);
    linearLayout.addView(colorPickerPresetView, (ViewGroup.LayoutParams)getPresetParams());
    colorPickerPresetView.setOnPresetClickListener(this.colorPickerView);
    colorPickerPresetView = new ColorPickerPresetView(paramContext, -16777216);
    linearLayout.addView(colorPickerPresetView, (ViewGroup.LayoutParams)getPresetParams());
    colorPickerPresetView.setOnPresetClickListener(this.colorPickerView);
    this.presetLayout = linearLayout;
    linearLayout = new LinearLayout(paramContext);
    linearLayout.setOrientation(0);
    LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(-2, pX(40.0F));
    layoutParams3.bottomMargin = pX(10.0F);
    addView((View)linearLayout, (ViewGroup.LayoutParams)layoutParams3);
    this.mOldColor = new ColorPickerPanelView(paramContext);
    layoutParams3 = new LinearLayout.LayoutParams(-2, -1);
    layoutParams3.weight = 0.5F;
    linearLayout.addView(this.mOldColor, (ViewGroup.LayoutParams)layoutParams3);
    TextView textView = new TextView(paramContext);
    textView.setGravity(17);
    LinearLayout.LayoutParams layoutParams5 = new LinearLayout.LayoutParams(-2, -1);
    layoutParams5.leftMargin = pX(10.0F);
    layoutParams5.rightMargin = pX(10.0F);
    textView.setText("→");
    textView.setTextSize(2, 20.0F);
    linearLayout.addView((View)textView, (ViewGroup.LayoutParams)layoutParams5);
    this.mNewColor = new ColorPickerPanelView(paramContext);
    LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(-2, -1);
    layoutParams1.weight = 0.5F;
    this.mNewColor.setContentDescription("new");
    linearLayout.addView(this.mNewColor, (ViewGroup.LayoutParams)layoutParams1);
  }
  
  public int pX(float paramFloat) {
    return (int)((getResources().getDisplayMetrics()).density * paramFloat);
  }
}
