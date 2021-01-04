//
// Created by poney.ma on 2020/10/28.
//

#ifndef BLOGDEMO_CONTRAST_IMAGE_FILTER_H
#define BLOGDEMO_CONTRAST_IMAGE_FILTER_H


#include "../base/image_filter.h"

class ContrastImageFilter : public ImageFilter {
private:
    int m_contrastLocation;
    float m_contrast = 1.2f;
public:

    virtual void OnInit();

    virtual const GLchar *GetFragmentShader();

    void setValue(float contrast);
};


#endif //BLOGDEMO_CONTRAST_IMAGE_FILTER_H
