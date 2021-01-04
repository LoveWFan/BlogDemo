//
// Created by poney.ma on 2020/10/29.
//

#ifndef BLOGDEMO_SATURATION_FILTER_H
#define BLOGDEMO_SATURATION_FILTER_H


#include "../base/image_filter.h"

class SaturationFilter : public ImageFilter {
private:
    int m_saturationLocation;
    float m_saturation = 1.0f;

public:
    virtual void OnInit();

    virtual const GLchar *GetFragmentShader();

    void setValue(float saturation);
};


#endif //BLOGDEMO_SATURATION_FILTER_H
