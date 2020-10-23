// Generated code from Butter Knife. Do not modify!
package com.poney.gpuimage.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.poney.gpuimage.R;
import com.poney.gpuimage.view.GPUImageView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class GalleryActivity_ViewBinding implements Unbinder {
  private GalleryActivity target;

  @UiThread
  public GalleryActivity_ViewBinding(GalleryActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public GalleryActivity_ViewBinding(GalleryActivity target, View source) {
    this.target = target;

    target.gpuImageView = Utils.findRequiredViewAsType(source, R.id.gpu_image, "field 'gpuImageView'", GPUImageView.class);
    target.btnCameraFilter = Utils.findRequiredViewAsType(source, R.id.btn_filter, "field 'btnCameraFilter'", ImageView.class);
    target.btnCameraAdjust = Utils.findRequiredViewAsType(source, R.id.btn_adjust, "field 'btnCameraAdjust'", ImageView.class);
    target.filterListView = Utils.findRequiredViewAsType(source, R.id.filter_listView, "field 'filterListView'", RecyclerView.class);
    target.seekBar = Utils.findRequiredViewAsType(source, R.id.seekBar, "field 'seekBar'", SeekBar.class);
    target.fragmentRadioContrast = Utils.findRequiredViewAsType(source, R.id.fragment_radio_contrast, "field 'fragmentRadioContrast'", RadioButton.class);
    target.fragmentRadioExposure = Utils.findRequiredViewAsType(source, R.id.fragment_radio_exposure, "field 'fragmentRadioExposure'", RadioButton.class);
    target.fragmentRadioSaturation = Utils.findRequiredViewAsType(source, R.id.fragment_radio_saturation, "field 'fragmentRadioSaturation'", RadioButton.class);
    target.fragmentRadioSharpness = Utils.findRequiredViewAsType(source, R.id.fragment_radio_sharpness, "field 'fragmentRadioSharpness'", RadioButton.class);
    target.fragmentRadioBright = Utils.findRequiredViewAsType(source, R.id.fragment_radio_bright, "field 'fragmentRadioBright'", RadioButton.class);
    target.fragmentRadioHue = Utils.findRequiredViewAsType(source, R.id.fragment_radio_hue, "field 'fragmentRadioHue'", RadioButton.class);
    target.fragmentAdjustRadiogroup = Utils.findRequiredViewAsType(source, R.id.fragment_adjust_radiogroup, "field 'fragmentAdjustRadiogroup'", RadioGroup.class);
    target.filterAdjust = Utils.findRequiredViewAsType(source, R.id.filter_adjust, "field 'filterAdjust'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    GalleryActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.gpuImageView = null;
    target.btnCameraFilter = null;
    target.btnCameraAdjust = null;
    target.filterListView = null;
    target.seekBar = null;
    target.fragmentRadioContrast = null;
    target.fragmentRadioExposure = null;
    target.fragmentRadioSaturation = null;
    target.fragmentRadioSharpness = null;
    target.fragmentRadioBright = null;
    target.fragmentRadioHue = null;
    target.fragmentAdjustRadiogroup = null;
    target.filterAdjust = null;
  }
}
