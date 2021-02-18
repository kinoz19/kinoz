package androidx.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import java.lang.reflect.Field;

public class EmperorDropDownPreference extends DropDownPreference {
  private EmperorPreferenceHelper Helper;
  private boolean mEnableSummary;
  private CharSequence[] mEntries;
  private String mLastState;
  private boolean mValueSet = false;

  public EmperorDropDownPreference(Context context, AttributeSet attributeSet) {
    super(context, attributeSet);
    setPersistent(false);
    this.Helper = new EmperorPreferenceHelper(context, attributeSet);
    init();
  }

  private void init() {
    Log.d(EmperorPreferenceHelper.TAG, "init: " + this.mValueSet);
    if (!this.mValueSet) {
      Log.d(EmperorPreferenceHelper.TAG, "init: " + this.Helper.isValidateKey());
      if (this.Helper.isValidateKey()) {
        this.mLastState = this.Helper.getStr();
        Log.d(EmperorPreferenceHelper.TAG, "init: " + this.mLastState);
      } else {
        this.mLastState = (String) getEntryValues()[0];
      }
      setValue(this.mLastState);
      setValueIndex(findIndexOfValue(this.mLastState));
      checkIconsForEntries();
      setSummary(getEntry());
    }
  }

  private void checkIconsForEntries() {
    boolean z = true;
    if (this.mEntries == null) {
      this.mEntries = getEntries();
      Field[] fields = getClass().getSuperclass().getDeclaredFields();
      Field drawable = null;
      int length = fields.length;
      int i = 0;
      while (true) {
        if (i >= length) {
          break;
        }
        Field field = fields[i];
        if (field.getType().equals(Drawable[].class)) {
          drawable = field;
          break;
        }
        i++;
      }
      Object o = null;
      if (drawable != null) {
        drawable.setAccessible(true);
        try {
          o = drawable.get(this);
        } catch (IllegalAccessException e) {
        }
        if (o == null) {
          z = false;
        }
        this.mEnableSummary = z;
        if (z) {
          CharSequence[] arr = new CharSequence[this.mEntries.length];
          for (int i2 = 0; i2 < this.mEntries.length; i2++) {
            arr[i2] = "";
          }
          setEntries(arr);
        }
      }
    }
  }

  private boolean getDependents(String s) {
    try {
      return Integer.parseInt(s) <= 0;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  protected Object onGetDefaultValue(TypedArray typedArray, int n) {
    return typedArray.getString(n);
  }

  protected void onSetInitialValue(Object o) {
    this.mValueSet = true;
    Log.d(EmperorPreferenceHelper.TAG, "onSetInitialValue: " + this.mLastState + "    " + o + "  " + this.Helper.isValidateKey());
    if (this.Helper.isValidateKey()) {
      this.mLastState = this.Helper.getStr();
    } else if (o != null) {
      this.mLastState = (String) o;
      this.Helper.putStr(this.mLastState);
      this.Helper.sendIntent();
    }
    checkIconsForEntries();
    setValue(this.mLastState);
    setValueIndex(findIndexOfValue(this.mLastState));
    setSummary(getEntry());
  }

  public String getValue() {
    return this.mLastState;
  }

  public boolean callChangeListener(Object newValue) {
    String value = newValue.toString();
    Log.d(EmperorPreferenceHelper.TAG, "callChangeListener: " + value + "   " + this.mLastState);
    if (this.mLastState == null || this.mLastState.equals(value)) {
      return EmperorDropDownPreference.super.callChangeListener(newValue);
    }
    this.Helper.putStr(value);
    this.Helper.sendIntent();
    this.mLastState = value;
    setValue(value);
    setSummary(getEntry());
    notifyDependencyChange(getDependents(getValue()));
    return true;
  }

  public boolean shouldDisableDependents() {
    try {
      return Integer.parseInt(this.Helper.getStr()) <= 0;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  public CharSequence getEntry() {
    int valueIndex = getValueIndex();
    if (valueIndex < 0 || this.mEntries == null) {
      return null;
    }
    return this.mEntries[valueIndex];
  }

  public CharSequence getSummary() {
    CharSequence[] entries = this.mEntries;
    int valueIndex = getValueIndex();
    String str = null;
    CharSequence charSequence = (entries == null || valueIndex >= entries.length || valueIndex < 0) ? null : entries[valueIndex];
    CharSequence summary = EmperorDropDownPreference.super.getSummary();
    if (summary != null) {
      String charSequence2 = summary.toString();
      Object[] objArr = new Object[1];
      if (charSequence == null) {
        charSequence = "";
      }
      objArr[0] = charSequence;
      str = String.format(charSequence2, objArr);
    }
    if (TextUtils.equals(str, summary)) {
      return summary;
    }
    return str;
  }

  public int getValueIndex() {
    return findIndexOfValue(getValue());
  }

  public boolean shouldPersist() {
    return false;
  }

  public void setSummary(CharSequence summary) {
    if (this.mEnableSummary) {
      EmperorDropDownPreference.super.setSummary(summary);
    }
  }
}
