package com.poney.gpuimage.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.poney.gpuimage.R;
import com.poney.gpuimage.R2;
import com.poney.gpuimage.adapter.FilterAdapter;
import com.poney.gpuimage.filter.base.FilterAdjuster;
import com.poney.gpuimage.filter.base.FilterTypeList;
import com.poney.gpuimage.filter.base.GPUImageAdjustFilter;
import com.poney.gpuimage.filter.base.GPUImageFilter;
import com.poney.gpuimage.filter.base.GPUImageFilterType;
import com.poney.gpuimage.filter.base.GPUImageParams;
import com.poney.gpuimage.filter.group.GPUImageFilterGroup;
import com.poney.gpuimage.utils.FilterTypeHelper;
import com.poney.gpuimage.view.GPUImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryActivity extends Activity implements View.OnClickListener {

    private static final int REQUEST_PICK_IMAGE = 1;
    @BindView(R2.id.gpu_image)
    GPUImageView gpuImageView;
    @BindView(R2.id.btn_filter)
    ImageView btnCameraFilter;
    @BindView(R2.id.btn_adjust)
    ImageView btnCameraAdjust;
    @BindView(R2.id.filter_listView)
    RecyclerView filterListView;
    @BindView(R2.id.seekBar)
    SeekBar seekBar;
    @BindView(R2.id.fragment_radio_contrast)
    RadioButton fragmentRadioContrast;
    @BindView(R2.id.fragment_radio_exposure)
    RadioButton fragmentRadioExposure;
    @BindView(R2.id.fragment_radio_saturation)
    RadioButton fragmentRadioSaturation;
    @BindView(R2.id.fragment_radio_sharpness)
    RadioButton fragmentRadioSharpness;
    @BindView(R2.id.fragment_radio_bright)
    RadioButton fragmentRadioBright;
    @BindView(R2.id.fragment_radio_hue)
    RadioButton fragmentRadioHue;
    @BindView(R2.id.fragment_adjust_radiogroup)
    RadioGroup fragmentAdjustRadiogroup;
    @BindView(R2.id.filter_adjust)
    LinearLayout filterAdjust;

    private FilterAdjuster filterAdjuster;

    private GPUImageFilterGroup gpuImageFilterGroup = new GPUImageFilterGroup();
    private GPUImageAdjustFilter imageAdjustFilterBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);
        GPUImageParams.init(this);
        initAction();
        initFilterList();
        startPhotoPicker();
    }

    private void initAction() {
        btnCameraAdjust.setOnClickListener(this);
        btnCameraFilter.setOnClickListener(this);

        fragmentAdjustRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == -1) {
                    seekBar.setVisibility(View.GONE);
                    filterAdjuster = null;
                    return;
                }

                seekBar.setVisibility(View.VISIBLE);
                //image adjust filter
                if (checkedId == R.id.fragment_radio_contrast) {
                    imageAdjustFilterBy = gpuImageFilterGroup.getFilterByType(GPUImageFilterType.CONTRAST);
                } else if (checkedId == R.id.fragment_radio_saturation) {
                    imageAdjustFilterBy = gpuImageFilterGroup.getFilterByType(GPUImageFilterType.SATURATION);
                } else if (checkedId == R.id.fragment_radio_exposure) {
                    imageAdjustFilterBy = gpuImageFilterGroup.getFilterByType(GPUImageFilterType.EXPOSURE);
                } else if (checkedId == R.id.fragment_radio_sharpness) {
                    imageAdjustFilterBy = gpuImageFilterGroup.getFilterByType(GPUImageFilterType.SHARPEN);
                } else if (checkedId == R.id.fragment_radio_bright) {
                    imageAdjustFilterBy = gpuImageFilterGroup.getFilterByType(GPUImageFilterType.BRIGHTNESS);
                } else if (checkedId == R.id.fragment_radio_hue) {
                    imageAdjustFilterBy = gpuImageFilterGroup.getFilterByType(GPUImageFilterType.HUE);
                }

                int progress = imageAdjustFilterBy.getProgress();
                seekBar.setProgress(progress);
                if (filterAdjuster == null)
                    filterAdjuster = new FilterAdjuster(imageAdjustFilterBy);
                gpuImageView.setFilter(gpuImageFilterGroup);
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (filterAdjuster.canAdjust()) {
                            filterAdjuster.adjust(progress);
                            imageAdjustFilterBy.setProgress(progress);
                        }

                        gpuImageView.requestRender();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

            }
        });
    }

    private void initFilterList() {
        filterListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        FilterAdapter filterAdapter = new FilterAdapter(this, FilterTypeList.TYPES);
        filterAdapter.setOnFilterChangeListener(new FilterAdapter.onFilterChangeListener() {
            @Override
            public void onFilterChanged(GPUImageFilterType filterType) {
                switchFilterTo(FilterTypeHelper.createFilterBy(filterType));
            }
        });
        filterListView.setAdapter(filterAdapter);
    }

    private void switchFilterTo(GPUImageFilter filter) {
        fragmentAdjustRadiogroup.clearCheck();
        GPUImageFilter originalFilter = gpuImageView.getFilter();
        if (originalFilter == null || originalFilter.getClass() != filter.getClass()) {
            gpuImageView.setFilter(filter);
        }
    }


    private void startPhotoPicker() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_PICK_IMAGE: {
                if (resultCode == RESULT_OK) {
                    gpuImageView.setImage(data.getData());
                } else {
                    finish();
                }
            }
            break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }


    @Override
    public void onClick(View v) {
        filterListView.setVisibility(View.GONE);
        filterAdjust.setVisibility(View.GONE);
        if (v.getId() == R.id.btn_filter) {
            filterListView.setVisibility(View.VISIBLE);
        } else if (v.getId() == R.id.btn_adjust) {
            filterAdjust.setVisibility(View.VISIBLE);
        }
    }
}