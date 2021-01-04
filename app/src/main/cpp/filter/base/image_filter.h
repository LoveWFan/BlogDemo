//
// Created by poney.ma on 2020/10/28.
//

#ifndef BLOGDEMO_IMAGE_FILTER_H
#define BLOGDEMO_IMAGE_FILTER_H


#include "../../drawer/base_drawer.h"

class ImageFilter : public BaseDrawer {
private :
    bool isInitialized;
protected:
    void setFloat(int location, float floatValue);

public:
    virtual void OnInit();

    virtual void Release();

    virtual void DoDraw(int textureId, void *vertexPos, void *texturePos);

    virtual const GLchar *GetVertexShader();

    virtual const GLchar *GetFragmentShader();
};


#endif //BLOGDEMO_IMAGE_FILTER_H
