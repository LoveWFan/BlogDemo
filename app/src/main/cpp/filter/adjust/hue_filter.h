//
// Created by poney.ma on 2020/10/30.
//

#ifndef BLOGDEMO_HUE_FILTER_H
#define BLOGDEMO_HUE_FILTER_H


#include "../base/image_filter.h"

class HueFilter : public ImageFilter {
private:
    int m_hue_location;
    float m_hue = 90.0f;

public:

    virtual void OnInit();

    virtual const GLchar *GetFragmentShader();

    void setValue(float hue);
};


#endif //BLOGDEMO_HUE_FILTER_H
