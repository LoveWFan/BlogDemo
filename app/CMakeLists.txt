cmake_minimum_required(VERSION 3.4.1)

# 支持gnu++11
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")

set(NATIVE_FILTER_DIR ${CMAKE_SOURCE_DIR}/src/main/cpp)
# 查找代码中使用到的系统库
find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

# 配置目标so库编译信息
add_library( # Sets the name of the library.
        native-lib

        # Sets the library as a shared library.
        SHARED
        ${NATIVE_FILTER_DIR}/utils/logger.h
        ${NATIVE_FILTER_DIR}/utils/opengl_utils.h
        ${NATIVE_FILTER_DIR}/utils/stb_image.h

        ${NATIVE_FILTER_DIR}/drawer/base_drawer.h
        ${NATIVE_FILTER_DIR}/drawer/base_drawer.cpp
        ${NATIVE_FILTER_DIR}/drawer/triangle_drawer.h
        ${NATIVE_FILTER_DIR}/drawer/triangle_drawer.cpp
        ${NATIVE_FILTER_DIR}/render/image_render.h
        ${NATIVE_FILTER_DIR}/render/image_render.cpp

        ${NATIVE_FILTER_DIR}/filter/adjust/color_invert_filter.cpp
        ${NATIVE_FILTER_DIR}/filter/adjust/color_invert_filter.h
        ${NATIVE_FILTER_DIR}/filter/adjust/contrast_image_filter.cpp
        ${NATIVE_FILTER_DIR}/filter/adjust/contrast_image_filter.h
        ${NATIVE_FILTER_DIR}/filter/adjust/exposure_filter.h
        ${NATIVE_FILTER_DIR}/filter/adjust/exposure_filter.cpp
        ${NATIVE_FILTER_DIR}/filter/adjust/brightness_filter.h
        ${NATIVE_FILTER_DIR}/filter/adjust/brightness_filter.cpp
        ${NATIVE_FILTER_DIR}/filter/adjust/saturation_filter.h
        ${NATIVE_FILTER_DIR}/filter/adjust/saturation_filter.cpp
        ${NATIVE_FILTER_DIR}/filter/adjust/hue_filter.h
        ${NATIVE_FILTER_DIR}/filter/adjust/hue_filter.cpp
        ${NATIVE_FILTER_DIR}/filter/adjust/sharpen_filter.h
        ${NATIVE_FILTER_DIR}/filter/adjust/sharpen_filter.cpp

        ${NATIVE_FILTER_DIR}/filter/base/image_filter.h
        ${NATIVE_FILTER_DIR}/filter/base/image_filter.cpp

        #EGL
        ${NATIVE_FILTER_DIR}/egl/egl_core.cpp
        ${NATIVE_FILTER_DIR}/egl/egl_core.h
        ${NATIVE_FILTER_DIR}/egl/egl_surface.h
        ${NATIVE_FILTER_DIR}/egl/egl_surface.cpp
        ${NATIVE_FILTER_DIR}/egl/gl_render.h
        ${NATIVE_FILTER_DIR}/egl/gl_render.cpp

        # Provides a relative path to your source file(s).
        ${NATIVE_FILTER_DIR}/native-lib.cpp
        )

# 指定编译目标库时，cmake要链接的库
target_link_libraries(

        # 指定目标库，native-lib 是在上面 add_library 中配置的目标库
        native-lib
        jnigraphics
        -landroid
        -lEGL
        -lGLESv2
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})