//
// Decompiled by Jadx - 2803ms
//
package androidx.preference;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.preference.SettingsHelper;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;

public class EmperorPreferenceHelper {
  public static final String ANDROID_RESOURCE_TAG = "http://schemas.android.com/apk/res/android";
  public static final String TAG = EmperorPreferenceHelper.class.getSimpleName();
  private AttributeSet mAttr;
  private Context mContext;
  String mIntent = "";
  String mKey = "";

  static {
  }

  EmperorPreferenceHelper(Context context, AttributeSet attributeSet) {
    this.mContext = context.getApplicationContext();
    this.mAttr = attributeSet;
    init();
  }

  private void init() {
    if (this.mAttr != null) {
      this.mKey = getAttributeAndroidValue("key");
      this.mIntent = getAttributeValue("intent");
    }
  }

  public static String getTAG(Class clas) {
    return clas.getSimpleName();
  }

  String getAttributeValue(String value) {
    return this.mAttr != null ? this.mAttr.getAttributeValue((String) null, value) : "";
  }

  String getAttributeAndroidValue(String value) {
    return this.mAttr != null ? this.mAttr.getAttributeValue("http://schemas.android.com/apk/res/android", value) : "";
  }

  int getAttributeInt(String value, int defValue) {
    return this.mAttr != null ? this.mAttr.getAttributeIntValue((String) null, value, defValue) : defValue;
  }

  boolean getAttributeBool(String value, boolean defValue) {
    return this.mAttr != null ? this.mAttr.getAttributeBooleanValue((String) null, value, defValue) : defValue;
  }

  boolean getAttributeAndroidBool(String value, boolean defValue) {
    return this.mAttr != null ? this.mAttr.getAttributeBooleanValue("http://schemas.android.com/apk/res/android", value, defValue) : defValue;
  }

  int getAttributeAndroidInt(String value, int defValue) {
    return this.mAttr != null ? this.mAttr.getAttributeIntValue("http://schemas.android.com/apk/res/android", value, defValue) : defValue;
  }

  void sendIntent() {
    Log.d(TAG, "sendIntent: " + this.mIntent);
    if (this.mIntent != null && !this.mIntent.isEmpty()) {
      this.mContext.sendBroadcast(new Intent(this.mIntent));
    }
  }

  void setInt(int value) {
    SettingsHelper.putIntinSettings(this.mKey, value);
  }

  int getInt() {
    return SettingsHelper.getIntofSettings(this.mKey);
  }

  int getInt(int def) {
    return SettingsHelper.getIntofSettings(this.mKey, def);
  }

  void putInt(int value) {
    SettingsHelper.putIntinSettings(this.mKey, value);
  }

  String getStr() {
    return SettingsHelper.getStringofSettings(this.mKey);
  }

  String getStr(String def) {
    return SettingsHelper.getStringofSettings(this.mKey, def);
  }

  void putStr(String value) {
    SettingsHelper.putStringinSettings(this.mKey, value);
  }

  boolean getBool() {
    return SettingsHelper.getBoolofSettings(this.mKey);
  }

  boolean getBool(boolean def) {
    return SettingsHelper.getBoolofSettings(this.mKey, def ? 1 : 0);
  }

  boolean getBool(int def) {
    return SettingsHelper.getBoolofSettings(this.mKey, def);
  }

  boolean getBool(String value) {
    String s = SettingsHelper.getStringofSettings(this.mKey);
    return s != null && s.equals(value);
  }

  static boolean isValidateKey(Context mContext2, String mKey2) {
    ContentResolver resolver = mContext2.getContentResolver();
    try {
      Settings.System.getInt(resolver, mKey2);
      return true;
    } catch (Settings.SettingNotFoundException e) {
      try {
        Settings.System.getLong(resolver, mKey2);
        return true;
      } catch (Settings.SettingNotFoundException e2) {
        try {
          Settings.System.getFloat(resolver, mKey2);
          return true;
        } catch (Settings.SettingNotFoundException e3) {
          if (Settings.System.getString(resolver, mKey2) == null) {
            return false;
          }
          return true;
        }
      }
    }
  }

  boolean isValidateKey() {
    ContentResolver resolver = this.mContext.getContentResolver();
    try {
      Settings.System.getInt(resolver, this.mKey);
      return true;
    } catch (Settings.SettingNotFoundException e) {
      try {
        Settings.System.getLong(resolver, this.mKey);
        return true;
      } catch (Settings.SettingNotFoundException e2) {
        try {
          Settings.System.getFloat(resolver, this.mKey);
          return true;
        } catch (Settings.SettingNotFoundException e3) {
          if (Settings.System.getString(resolver, this.mKey) == null) {
            return false;
          }
          return true;
        }
      }
    }
  }
}
