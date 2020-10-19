package com.poney.gpuimage.filter.base;

public class GPUImageAdjustFilter extends GPUImageFilter {
    protected GPUImageFilterType gpuImageFilterType;
    //0-100
    protected int progress;

    public GPUImageAdjustFilter(final String vertexShader, final String fragmentShader, GPUImageFilterType gpuImageFilterType) {
        super(vertexShader, fragmentShader);
        this.gpuImageFilterType = gpuImageFilterType;
    }

    public GPUImageAdjustFilter() {

    }

    public GPUImageFilterType getGpuImageFilterType() {
        return gpuImageFilterType;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
