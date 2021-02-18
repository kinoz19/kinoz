package androidx.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EmperorSeekBarPreference extends Preference {
  private static final String TAG = EmperorPreferenceHelper.TAG;
  private EmperorPreferenceHelper Helper;
  private boolean isOne;
  private boolean mAdjustable;
  private int mDefValue;
  private int mMax;
  private int mMin;
  private SeekBar mSeekBar;
  private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener;
  private int mSeekBarIncrement;
  private View.OnKeyListener mSeekBarKeyListener;
  private int mSeekBarValue;
  private TextView mSeekBarValueTextView;
  private boolean mShowSeekBarValue;
  private boolean mTrackingTouch;
  private boolean mUpdatesContinuously;
  private int mValue;


  private static class SavedState extends Preference.BaseSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {

      @Override
      public SavedState createFromParcel(Parcel parcel) {
        return new SavedState(parcel);
      }

      @Override
      public SavedState[] newArray(int i) {
        return new SavedState[i];
      }
    };
    int mMax;
    int mMin;
    int mSeekBarValue;

    SavedState(Parcel parcel) {
      super(parcel);
      this.mSeekBarValue = parcel.readInt();
      this.mMin = parcel.readInt();
      this.mMax = parcel.readInt();
    }

    SavedState(Parcelable parcelable) {
      super(parcelable);
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
      super.writeToParcel(parcel, i);
      parcel.writeInt(this.mSeekBarValue);
      parcel.writeInt(this.mMin);
      parcel.writeInt(this.mMax);
    }
  }

  public EmperorSeekBarPreference(Context context) {
    this(context, null);
  }

  public EmperorSeekBarPreference(Context context, AttributeSet attributeSet) {
    this(context, attributeSet, 0);
  }

  public EmperorSeekBarPreference(Context context, AttributeSet attributeSet, int i) {
    this(context, attributeSet, i, 0);
  }

  public EmperorSeekBarPreference(Context context, AttributeSet attributeSet, int i, int i2) {
    super(context, attributeSet, i, i2);
    this.isOne = true;
    this.mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

      @Override
      public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        if (EmperorSeekBarPreference.this.isOne) {
          EmperorSeekBarPreference.this.isOne = false;
        } else {
          EmperorSeekBarPreference.this.syncValueInternal(seekBar);
        }
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {
        EmperorSeekBarPreference.this.mTrackingTouch = true;
      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
        boolean z = false;
        EmperorSeekBarPreference.this.mTrackingTouch = false;
        if (seekBar.getProgress() + EmperorSeekBarPreference.this.mMin != EmperorSeekBarPreference.this.mSeekBarValue) {
          EmperorSeekBarPreference.this.syncValueInternal(seekBar);
        }
        EmperorSeekBarPreference emperorSeekBarPreference = EmperorSeekBarPreference.this;
        if (EmperorSeekBarPreference.this.mValue == EmperorSeekBarPreference.this.mMin) {
          z = true;
        }
        emperorSeekBarPreference.notifyDependencyChange(z);
      }
    };
    this.mSeekBarKeyListener = new View.OnKeyListener() {

      @Override
      public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (keyEvent.getAction() != 0) {
          return false;
        }
        if ((!EmperorSeekBarPreference.this.mAdjustable && (i == 21 || i == 22)) || i == 23 || i == 66) {
          return false;
        }
        if (EmperorSeekBarPreference.this.mSeekBar != null) {
          return EmperorSeekBarPreference.this.mSeekBar.onKeyDown(i, keyEvent);
        }
        Log.e(EmperorSeekBarPreference.TAG, "SeekBar view is null and hence cannot be adjusted.");
        return false;
      }
    };

    this.Helper = new EmperorPreferenceHelper(context, attributeSet);
    this.mMax = this.Helper.getAttributeInt("max", 100);
    this.mMin = this.Helper.getAttributeInt("min", 0);
    setMax(this.mMax);
    setMin(this.mMin);
    setSeekBarIncrement(this.Helper.getAttributeInt("step", 0));
    this.mAdjustable = false;
    this.mShowSeekBarValue = true;
    this.mUpdatesContinuously = true;
    setPersistent(false);
    String attributeAndroidValue = this.Helper.getAttributeAndroidValue("defaultValue");
    if (attributeAndroidValue == null || attributeAndroidValue.equals("")) {
      setDefaultValue(Integer.valueOf(this.mMin));
      if (!this.Helper.isValidateKey()) {
        this.mValue = this.mMin;
      } else {
        this.mValue = this.Helper.getInt();
      }
    } else {
      int attributeAndroidInt = this.Helper.getAttributeAndroidInt("defaultValue", this.mMax);
      this.mDefValue = attributeAndroidInt;
      setDefaultValue(Integer.valueOf(attributeAndroidInt));
      if (!this.Helper.isValidateKey()) {
        this.mValue = this.mDefValue;
      } else {
        this.mValue = this.Helper.getInt();
      }
    }
    setValue(this.mValue);

  }

  private int IdToId(String str) {
    return getContext().getResources().getIdentifier(str, "id", "android");
  }

  private int idToId(String str) {
    return getContext().getResources().getIdentifier(str, "id", getContext().getPackageName());
  }

  private void setMax(int i) {
    if (i < this.mMin) {
      i = this.mMin;
    }
    if (i != this.mMax) {
      this.mMax = i;
      notifyChanged();
    }
  }

  private void setMin(int i) {
    if (i > this.mMax) {
      i = this.mMax;
    }
    if (i != this.mMin) {
      this.mMin = i;
      notifyChanged();
    }
  }

  private void setSeekBarIncrement(int i) {
    if (i != this.mSeekBarIncrement) {
      this.mSeekBarIncrement = Math.min(this.mMax - this.mMin, Math.abs(i));
      notifyChanged();
    }
  }

  private void setValue(int i) {
    setValueInternal(i, true);
  }

  private void setValueInternal(int i, boolean z) {
    Log.d(TAG, "setValueInternal: " + i + " " + this.mSeekBarValue);
    if (i < this.mMin) {
      i = this.mMin;
    }
    if (i > this.mMax) {
      i = this.mMax;
    }
    if (i != this.mSeekBarValue) {
      this.mSeekBarValue = i;
      this.mValue = this.mSeekBarValue;
      updateLabelValue(this.mSeekBarValue);
      persistInt(i);
      if (z) {
        notifyChanged();
      }
      this.Helper.putInt(this.mValue);
      this.Helper.sendIntent();
    }
  }


  private void syncValueInternal(SeekBar seekBar) {
    int progress = this.mMin + seekBar.getProgress();
    if (progress != this.mSeekBarValue) {
      setValueInternal(progress, false);
    }
  }

  private void updateLabelValue(int i) {
    if (this.mSeekBarValueTextView != null) {
      this.mSeekBarValueTextView.setText(String.valueOf(i));
    }
  }

  public int getMax() {
    return this.mMax;
  }

  public int getMin() {
    return this.mMin;
  }

  public final int getSeekBarIncrement() {
    return this.mSeekBarIncrement;
  }

  public boolean getUpdatesContinuously() {
    return this.mUpdatesContinuously;
  }

  public int getValue() {
    return this.mSeekBarValue;
  }

  public boolean isAdjustable() {
    return this.mAdjustable;
  }

  public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
    super.onBindViewHolder(preferenceViewHolder);
    onMyBindViewHolder(preferenceViewHolder);
  }


  @Override
  protected Object onGetDefaultValue(TypedArray typedArray, int i) {
    return Integer.valueOf(typedArray.getInt(i, 0));
  }

  public void onMyBindViewHolder(Object obj) {
    Class<?> cls = obj.getClass();
    int idToId = idToId("seekbar");
    int idToId2 = idToId("seekbar_value");
    try {
      Method method = cls.getMethod("findViewById", Integer.TYPE);
      Object invoke = method.invoke(obj, Integer.valueOf(idToId));
      Object invoke2 = method.invoke(obj, Integer.valueOf(idToId2));
      if (invoke != null && invoke2 != null) {
        this.mSeekBar = (SeekBar) invoke;
        this.mSeekBarValueTextView = (TextView) invoke2;
        this.mSeekBarValueTextView.setVisibility(View.VISIBLE);
        if (this.mSeekBar == null) {
          Log.e(TAG, "SeekBar view is null in onBindViewHolder.");
          return;
        }
        this.mSeekBar.setOnSeekBarChangeListener(this.mSeekBarChangeListener);
        this.mSeekBar.setMax(this.mMax - this.mMin);
        if (this.mSeekBarIncrement != 0) {
          this.mSeekBar.setKeyProgressIncrement(this.mSeekBarIncrement);
        } else {
          this.mSeekBarIncrement = this.mSeekBar.getKeyProgressIncrement();
        }
        Log.d(TAG, "onBindViewHolder: " + this.mSeekBarValue);
        this.mSeekBar.setProgress(this.mSeekBarValue - this.mMin);
        updateLabelValue(this.mSeekBarValue);
        this.mSeekBar.setEnabled(isEnabled());
      }
    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
      Log.d(TAG, "onMyBindViewHolder: " + e + "   " + cls);
    }

  }



  @Override
  protected void onRestoreInstanceState(Parcelable parcelable) {
    if (!parcelable.getClass().equals(SavedState.class)) {
      super.onRestoreInstanceState(parcelable);
      return;
    }
    SavedState savedState = (SavedState) parcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    this.mSeekBarValue = savedState.mSeekBarValue;
    this.mMin = savedState.mMin;
    this.mMax = savedState.mMax;
    notifyChanged();
  }


  @Override
  protected Parcelable onSaveInstanceState() {
    Parcelable onSaveInstanceState = super.onSaveInstanceState();
    if (isPersistent()) {
      return onSaveInstanceState;
    }
    SavedState savedState = new SavedState(onSaveInstanceState);
    savedState.mSeekBarValue = this.mSeekBarValue;
    savedState.mMin = this.mMin;
    savedState.mMax = this.mMax;
    return savedState;
  }


  @Override
  protected void onSetInitialValue(Object obj) {
    if (obj == null) {
      obj = Integer.valueOf(this.Helper.getAttributeInt("min", 0));
    }
    this.mValue = this.Helper.getInt(((Integer) obj).intValue());
    Log.d(TAG, "onSetInitialValue: " + this.mValue + "   " + obj);
    this.mDefValue = ((Integer) obj).intValue();
    if (!this.Helper.isValidateKey()) {
      this.Helper.putInt(((Integer) obj).intValue());
      this.mValue = this.mDefValue;
    }
    setValue(this.mValue);
    notifyDependencyChange(this.mValue == this.mMin);
  }

  public void setAdjustable(boolean z) {
    this.mAdjustable = z;
  }

  public void setUpdatesContinuously(boolean z) {
    this.mUpdatesContinuously = z;
  }

  @Override
  public boolean shouldDisableDependents() {
    return this.Helper.getInt(this.mDefValue) == this.mMin;
  }
}