package androidx.preference;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;

public class EmperorHidePreference extends PreferenceGroup {
  private boolean mEnable = false;
  
  private ArrayList<PrefClass> mPrefs = new ArrayList<>();

  public EmperorHidePreference(Context context, AttributeSet attrs) {
    super(context, attrs);
    boolean z = false;
    if (getKey() != null) {
      this.mEnable = !Build.DEVICE.equals(getKey()) ? true : z;
    }
  }


  private PrefClass contais(String key) {
    Iterator<PrefClass> it = this.mPrefs.iterator();
    while (it.hasNext()) {
      PrefClass claz = it.next();
      if (claz.key.equals(key)) {
        return claz;
      }
    }
    return null;
  }

  public <T extends Preference> T findPreference(CharSequence key) {
    PrefClass prefClass = contais(key.toString());
    Object result = null;
    if (prefClass != null) {
      try {
        result = prefClass.claz.getConstructor(new Class[]{Context.class}).newInstance(new Object[]{getContext()});
      } catch (Exception e) {
        Log.d(getClass().getSimpleName(), "findPreference: Error one = " + e);
        try {
          result = prefClass.claz.getConstructor(new Class[]{Context.class, AttributeSet.class}).newInstance(new Object[]{getContext(), null});
        } catch (Exception e2) {
          Log.d(getClass().getSimpleName(), "findPreference: Error two = " + e2);
        }
      }
    }
    if (result != null) {
      return (T) result;
    }
    return null;
  }
  
  public void onBindViewHolder(PreferenceViewHolder paramPreferenceViewHolder) {
    super.onBindViewHolder(paramPreferenceViewHolder);
    onMyBindViewHolder(paramPreferenceViewHolder);
  }

  public void onMyBindViewHolder(Object view) {
    PreferenceGroup group = getParent();
    if (group != null) {
      group.removePreference(this);
    }
  }
  
  protected boolean onPrepareAddPreference(Preference paramPreference) {
    if (this.mEnable) {
      PreferenceGroup preferenceGroup = getParent();
      if (preferenceGroup != null)
        preferenceGroup.addPreference(paramPreference); 
      return false;
    } 
    String str = paramPreference.getKey();
    if (str != null) {
      PrefClass prefClass = new PrefClass(str, paramPreference.getClass());
      this.mPrefs.add(prefClass);
    } 
    return super.onPrepareAddPreference(paramPreference);
  }
  
  private class PrefClass {
    Class claz;
    String key;
    
    PrefClass(String param1String, Class param1Class) {
      this.key = param1String;
      this.claz = param1Class;
    }
  }
}
