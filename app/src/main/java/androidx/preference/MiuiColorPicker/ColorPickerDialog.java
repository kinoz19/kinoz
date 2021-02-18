package androidx.preference.MiuiColorPicker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Locale;

public class ColorPickerDialog extends AlertDialog.Builder implements ColorPickerView.OnColorChangedListener, View.OnClickListener, ViewTreeObserver.OnGlobalLayoutListener {
  private boolean firstInitial = true;
  
  private AlertDialog mBuilder;
  
  private ColorPickerView mColorPicker;
  
  private ColorStateList mHexDefaultTextColor;
  
  private EditText mHexVal;
  
  private boolean mHexValueEnabled = true;
  
  private ColorPickerDialogLayout mLayout;
  
  private OnColorChangedListener mListener;
  
  private ColorPickerPanelView mNewColor;
  
  private ColorPickerPanelView mOldColor;
  
  private int mOrientation;
  
  private boolean mPresetColorEnable = true;
  
  private View mPresetColorLayout;
  
  public ColorPickerDialog(Context paramContext, int paramInt) {
    super(paramContext);
    init(paramInt);
  }
  
  public static String convertToARGB(int paramInt) {
    String str1 = Integer.toHexString(Color.alpha(paramInt));
    String str2 = Integer.toHexString(Color.red(paramInt));
    String str3 = Integer.toHexString(Color.green(paramInt));
    String str4 = Integer.toHexString(Color.blue(paramInt));
    String str5 = str1;
    if (str1.length() == 1)
      str5 = "0" + str1; 
    str1 = str2;
    if (str2.length() == 1)
      str1 = "0" + str2; 
    str2 = str3;
    if (str3.length() == 1)
      str2 = "0" + str3; 
    str3 = str4;
    if (str4.length() == 1)
      str3 = "0" + str4; 
    return "#" + str5 + str1 + str2 + str3;
  }
  
  public static int convertToColorInt(String paramString) throws IllegalArgumentException {
    String str = paramString;
    if (!paramString.startsWith("#"))
      str = "#" + paramString; 
    return Color.parseColor(str);
  }
  
  public static String convertToRGB(int paramInt) {
    String str1 = Integer.toHexString(Color.red(paramInt));
    String str2 = Integer.toHexString(Color.green(paramInt));
    String str3 = Integer.toHexString(Color.blue(paramInt));
    String str4 = str1;
    if (str1.length() == 1)
      str4 = "0" + str1; 
    str1 = str2;
    if (str2.length() == 1)
      str1 = "0" + str2; 
    str2 = str3;
    if (str3.length() == 1)
      str2 = "0" + str3; 
    return "#" + str4 + str1 + str2;
  }
  
  private void init(int paramInt) {
    setUp(paramInt);
    if (this.firstInitial) {
      setView((View)this.mLayout);
      this.mBuilder = create();
      this.mBuilder.setCanceledOnTouchOutside(true);
      Window window = this.mBuilder.getWindow();
      window.setGravity(80);
      WindowManager.LayoutParams layoutParams = window.getAttributes();
      Display display = ((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
      DisplayMetrics displayMetrics = new DisplayMetrics();
      display.getRealMetrics(displayMetrics);
      layoutParams.width = displayMetrics.widthPixels;
      window.setAttributes(layoutParams);
      this.firstInitial = false;
    } 
  }

  private void setUp(int color) {
    this.mLayout = new ColorPickerDialogLayout(getContext());
    this.mLayout.getViewTreeObserver().addOnGlobalLayoutListener(this);
    this.mOrientation = getContext().getResources().getConfiguration().orientation;
    this.mColorPicker = this.mLayout.colorPickerView;
    this.mOldColor = this.mLayout.mOldColor;
    this.mNewColor = this.mLayout.mNewColor;
    this.mPresetColorLayout = this.mLayout.presetLayout;
    this.mPresetColorLayout.setVisibility(this.mPresetColorEnable ? View.VISIBLE : View.GONE);
    this.mHexVal = this.mLayout.mHexVal;
    this.mHexVal.setInputType(524288);
    this.mHexDefaultTextColor = this.mHexVal.getTextColors();
    this.mHexVal.setOnEditorActionListener(new TextView.OnEditorActionListener() {

      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId != 6) {
          return false;
        }
        ((InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
        String s = ColorPickerDialog.this.mHexVal.getText().toString();
        if (s.length() <= 5 || s.length() >= 10) {
          ColorPickerDialog.this.mHexVal.setTextColor(-65536);
          return true;
        }
        try {
          ColorPickerDialog.this.mColorPicker.setColor(ColorPickerDialog.convertToColorInt(s), true);
          ColorPickerDialog.this.mHexVal.setTextColor(ColorPickerDialog.this.mHexDefaultTextColor);
          return true;
        } catch (IllegalArgumentException e) {
          ColorPickerDialog.this.mHexVal.setTextColor(-65536);
          return true;
        }
      }
    });
    ((LinearLayout) this.mOldColor.getParent()).setPadding(Math.round(this.mColorPicker.getDrawingOffset()), 0, Math.round(this.mColorPicker.getDrawingOffset()), 0);
    this.mOldColor.setOnClickListener(this);
    this.mNewColor.setOnClickListener(this);
    this.mColorPicker.setOnColorChangedListener(this);
    this.mOldColor.setColor(color);
    this.mColorPicker.setColor(color, true);
  }

  private void updateHexLengthFilter() {
    if (getAlphaSliderVisible()) {
      this.mHexVal.setFilters(new InputFilter[] { (InputFilter)new InputFilter.LengthFilter(9) });
      return;
    } 
    this.mHexVal.setFilters(new InputFilter[] { (InputFilter)new InputFilter.LengthFilter(7) });
  }
  
  private void updateHexValue(int paramInt) {
    if (getAlphaSliderVisible()) {
      this.mHexVal.setText(convertToARGB(paramInt).toUpperCase(Locale.getDefault()));
    } else {
      this.mHexVal.setText(convertToRGB(paramInt).toUpperCase(Locale.getDefault()));
    } 
    this.mHexVal.setTextColor(this.mHexDefaultTextColor);
  }
  
  public boolean getAlphaSliderVisible() {
    return this.mColorPicker.getAlphaSliderVisible();
  }
  
  public int getColor() {
    return this.mColorPicker.getColor();
  }
  
  public boolean getHexValueEnabled() {
    return this.mHexValueEnabled;
  }
  
  public void onClick(View paramView) {
    Log.d("colorwer", "onClick: ");
    if (paramView == this.mNewColor && this.mListener != null)
      this.mListener.onColorChanged(this.mNewColor.getColor()); 
    this.mBuilder.cancel();
  }
  
  public void onColorChanged(int paramInt) {
    this.mNewColor.setColor(paramInt);
    if (this.mHexValueEnabled)
      updateHexValue(paramInt); 
  }
  
  public void onGlobalLayout() {
    if ((getContext().getResources().getConfiguration()).orientation != this.mOrientation) {
      int i = this.mOldColor.getColor();
      int j = this.mNewColor.getColor();
      this.mLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
      setUp(i);
      this.mNewColor.setColor(j);
      this.mColorPicker.setColor(j);
    } 
  }
  
  public ColorPickerDialog setAlphaSliderVisible(boolean paramBoolean) {
    this.mColorPicker.setAlphaSliderVisible(paramBoolean);
    if (this.mHexValueEnabled) {
      updateHexLengthFilter();
      updateHexValue(getColor());
    } 
    return this;
  }
  
  public ColorPickerDialog setHexValueEnabled(boolean paramBoolean) {
    this.mHexValueEnabled = paramBoolean;
    if (paramBoolean) {
      this.mHexVal.setVisibility(View.VISIBLE);
      updateHexLengthFilter();
      updateHexValue(getColor());
      return this;
    } 
    this.mHexVal.setVisibility(View.GONE);
    return this;
  }
  
  public ColorPickerDialog setInitialColor(int paramInt) {
    this.mOldColor.setColor(paramInt);
    this.mColorPicker.setColor(paramInt, true);
    return this;
  }
  
  public ColorPickerDialog setOnColorChangedListener(OnColorChangedListener paramOnColorChangedListener) {
    this.mListener = paramOnColorChangedListener;
    return this;
  }

  public ColorPickerDialog setPresetColorEnable(boolean visible) {
    this.mPresetColorEnable = visible;
    this.mPresetColorLayout.setVisibility(this.mPresetColorEnable ? View.VISIBLE : View.GONE);
    return this;
  }

  public AlertDialog show() {
    this.mBuilder.show();
    return this.mBuilder;
  }
  
  public static interface OnColorChangedListener {
    void onColorChanged(int param1Int);
  }
}
