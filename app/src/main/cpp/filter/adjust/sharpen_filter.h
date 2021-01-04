//
// Created by poney.ma on 2020/11/3.
//

#ifndef BLOGDEMO_SHARPEN_FILTER_H
#define BLOGDEMO_SHARPEN_FILTER_H


#include "../base/image_filter.h"

class SharpenFilter : public ImageFilter {
private:
    int m_sharpnessLocation;
    float m_sharpness = 0.0f;
    int imageWidthFactorLocation;
    int imageHeightFactorLocation;

public:
    virtual void OnInit();

    virtual const GLchar *GetFragmentShader();

    void setValue(float saturation);

    virtual const GLchar *GetVertexShader();

    virtual void onOutputSizeChanged(int width, int height);


};


#endif //BLOGDEMO_SHARPEN_FILTER_H
