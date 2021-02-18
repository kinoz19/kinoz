package androidx.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

public class EmperorCheckBoxPreference extends CheckBoxPreference {
  private EmperorPreferenceHelper Helper;
  private int mDefaultValue;

  public EmperorCheckBoxPreference(Context context, AttributeSet set) {
    super(context, set);
    this.Helper = new EmperorPreferenceHelper(context, set);
    setPersistent(false);
    String s = this.Helper.getAttributeAndroidValue("defaultValue");
    if (s == null || s.equals("")) {
      setDefaultValue(Boolean.valueOf(this.Helper.getAttributeAndroidBool("defaultValue", false)));
    }
    setChecked(this.Helper.getBool(this.mDefaultValue));
  }

  protected Object onGetDefaultValue(TypedArray typedArray, int n) {
    int i = 0;
    if (typedArray.getBoolean(n, false)) {
      i = 1;
    }
    this.mDefaultValue = i;
    return Integer.valueOf(i);
  }

  protected void onSetInitialValue(Object defaultValue) {
    if (defaultValue instanceof Boolean) {
      this.mDefaultValue = ((Boolean) defaultValue).booleanValue() ? 1 : 0;
    }
    setChecked(this.Helper.getBool(this.mDefaultValue));
  }

  public void setChecked(boolean checked) {
    if (checked != isChecked()) {
      EmperorCheckBoxPreference.super.setChecked(checked);
    }
    if (checked != this.Helper.getBool(this.mDefaultValue) || !this.Helper.isValidateKey()) {
      this.Helper.setInt(checked ? 1 : 0);
      this.Helper.sendIntent();
    }
  }
}
