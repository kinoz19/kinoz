package androidx.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.preference.SettingsHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.preference.MiuiColorPicker.ColorPickerAlphaPatternDrawable;
import androidx.preference.MiuiColorPicker.ColorPickerDialog;
import java.lang.reflect.Field;

public class EmperorColorPickerPreference extends Preference implements ColorPickerDialog.OnColorChangedListener {
  private static int mDefValue = -16777216;
  private EmperorPreferenceHelper Helper;
  private boolean mAlphaEnabled;
  private float mDensity = 0.0f;
  private boolean mHexEnabled;
  private boolean mPresetEnabled;
  private int mValue;
  private View mView;

  static {
  }

  public EmperorColorPickerPreference(Context context, AttributeSet attrs) {
    super(context, attrs);
    setPersistent(false);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet set) {
    boolean z = true;
    this.Helper = new EmperorPreferenceHelper(context, set);
    this.mHexEnabled = this.Helper.getAttributeBool("hexValue", true);
    this.mAlphaEnabled = this.Helper.getAttributeBool("alphaSlider", true);
    this.mPresetEnabled = this.Helper.getAttributeBool("presetView", true);
    this.mDensity = context.getResources().getDisplayMetrics().density;
    this.mValue = this.Helper.getInt(mDefValue);
    int i = this.mValue;
    if (this.Helper.isValidateKey()) {
      z = false;
    }
    onColorChanged(i, z);
  }

  protected Object onGetDefaultValue(TypedArray a, int index) {
    String mHexDefaultValue = a.getString(index);
    if (mHexDefaultValue == null || !mHexDefaultValue.startsWith("#")) {
      return Integer.valueOf(a.getColor(index, -16777216));
    }
    int mDefValue1 = convertToColorInt(mHexDefaultValue);
    if (EmperorPreferenceHelper.isValidateKey(getContext(), getKey())) {
      mDefValue = SettingsHelper.getIntofSettings(getKey());
    } else {
      mDefValue = mDefValue1;
    }
    return Integer.valueOf(mDefValue);
  }

  public void onBindViewHolder(PreferenceViewHolder holder) {
    EmperorColorPickerPreference.super.onBindViewHolder(holder);
    onMyBindViewHolder(holder);
  }

  public void onMyBindViewHolder(Object view) {
    Class claz = view.getClass();
    try {
      Field field = claz.getField("itemView");
      field.setAccessible(true);
      this.mView = (View) field.get(view);
      setPreviewColor();
    } catch (IllegalAccessException | NoSuchFieldException e) {
      Log.d(EmperorPreferenceHelper.TAG, "onMyBindViewHolder: " + e + "   " + claz);
    }
  }

  public View getView() {
    return this.mView;
  }

  private void setPreviewColor() {
    if (this.mView != null) {
      ImageView imageView = new ImageView(getContext());
      LinearLayout linearLayout = (LinearLayout) this.mView.findViewById(android.R.id.widget_frame);
      if (linearLayout != null) {
        linearLayout.setVisibility(View.VISIBLE);
        linearLayout.setPadding(linearLayout.getPaddingLeft(), linearLayout.getPaddingTop(), (int) (this.mDensity * 8.0f), linearLayout.getPaddingBottom());
        int childCount = linearLayout.getChildCount();
        if (childCount > 0) {
          linearLayout.removeViews(0, childCount);
        }
        linearLayout.addView(imageView);
        linearLayout.setMinimumWidth(0);
        imageView.setImageBitmap(getPreviewBitmap());
      }
    }
  }

  private String convertToARGB(int i) {
    String toHexString = Integer.toHexString(Color.alpha(i));
    String toHexString2 = Integer.toHexString(Color.red(i));
    String toHexString3 = Integer.toHexString(Color.green(i));
    String toHexString4 = Integer.toHexString(Color.blue(i));
    if (toHexString.length() == 1) {
      toHexString = "0" + toHexString;
    }
    if (toHexString2.length() == 1) {
      toHexString2 = "0" + toHexString2;
    }
    if (toHexString3.length() == 1) {
      toHexString3 = "0" + toHexString3;
    }
    if (toHexString4.length() == 1) {
      toHexString4 = "0" + toHexString4;
    }
    return "#" + toHexString + toHexString2 + toHexString3 + toHexString4;
  }

  private int convertToColorInt(String str) throws IllegalArgumentException {
    if (!str.startsWith("#")) {
      str = "#" + str;
    }
    return Color.parseColor(str);
  }

  private String convertToRGB(int i) {
    String toHexString = Integer.toHexString(Color.red(i));
    String toHexString2 = Integer.toHexString(Color.green(i));
    String toHexString3 = Integer.toHexString(Color.blue(i));
    if (toHexString.length() == 1) {
      toHexString = "0" + toHexString;
    }
    if (toHexString2.length() == 1) {
      toHexString2 = "0" + toHexString2;
    }
    if (toHexString3.length() == 1) {
      toHexString3 = "0" + toHexString3;
    }
    return "#" + toHexString + toHexString2 + toHexString3;
  }

  private Bitmap getPreviewBitmap() {
    boolean isNight;
    if ((getContext().getResources().getConfiguration().uiMode & 48) == 32) {
      isNight = true;
    } else {
      isNight = false;
    }
    int i = (int) (this.mDensity * 31.0f);
    Drawable drawable = new ColorPickerAlphaPatternDrawable((int) (5.0f * this.mDensity));
    Bitmap createBitmap = Bitmap.createBitmap(i + 2, i + 2, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(createBitmap);
    Paint paint = new Paint();
    paint.setAntiAlias(true);
    paint.setColor(!isNight ? -9539986 : -1);
    canvas.drawRoundRect(new RectF(new Rect(0, 0, i + 2, i + 2)), 25.0f, 25.0f, paint);
    drawable.setBounds(1, 1, i + 1, i + 1);
    drawable.draw(canvas);
    paint.setColor(this.mValue);
    canvas.drawRoundRect(new RectF(new Rect(1, 1, i + 1, i + 1)), 25.0f, 25.0f, paint);
    return createBitmap;
  }

  public void onColorChanged(int color) {
    if (this.mValue != color) {
      this.mValue = color;
      this.Helper.setInt(this.mValue);
      this.Helper.sendIntent();
      setPreviewColor();
      setSummary(convertToARGB(this.mValue));
    }
  }

  private void onColorChanged(int color, boolean bool) {
    if (bool || this.mValue != color) {
      this.mValue = color;
      this.Helper.setInt(this.mValue);
      this.Helper.sendIntent();
      setPreviewColor();
      setSummary(convertToARGB(this.mValue));
    }
  }

  protected void onClick() {
    EmperorColorPickerPreference.super.onClick();
    new ColorPickerDialog(getContext(), this.mValue).setAlphaSliderVisible(this.mAlphaEnabled).setHexValueEnabled(this.mHexEnabled).setPresetColorEnable(this.mPresetEnabled).setOnColorChangedListener(this).show();
  }
}
