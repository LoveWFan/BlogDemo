//
// Created by poney.ma on 2020/10/29.
//

#ifndef BLOGDEMO_BRIGHTNESS_FILTER_H
#define BLOGDEMO_BRIGHTNESS_FILTER_H


#include "../base/image_filter.h"

class BrightnessFilter : public ImageFilter {
private:
    int m_brightnessLocation;
    float m_brightness = 0.0f;
public:
    virtual void OnInit();

    virtual const GLchar *GetFragmentShader();

    void setValue(float value);
};


#endif //BLOGDEMO_BRIGHTNESS_FILTER_H
