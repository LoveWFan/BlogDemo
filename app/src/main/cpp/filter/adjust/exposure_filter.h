//
// Created by poney.ma on 2020/10/28.
//

#ifndef BLOGDEMO_EXPOSURE_FILTER_H
#define BLOGDEMO_EXPOSURE_FILTER_H


#include "../base/image_filter.h"

class ExposureFilter : public ImageFilter {
private:
    int m_exposureLocation;
    float m_exposure = 1.0f;

public:

    virtual void OnInit();

    virtual const GLchar *GetFragmentShader();

    void setValue(float exposure);
};


#endif //BLOGDEMO_EXPOSURE_FILTER_H
