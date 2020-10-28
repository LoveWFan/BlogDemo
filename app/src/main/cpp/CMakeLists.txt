cmake_minimum_required(VERSION 3.4.1)

# 支持gnu++11
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")


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
        ${CMAKE_SOURCE_DIR}/utils/logger.h
        ${CMAKE_SOURCE_DIR}/utils/opengl_utils.h
        ${CMAKE_SOURCE_DIR}/utils/stb_image.h

        ${CMAKE_SOURCE_DIR}/drawer/base_drawer.h
        ${CMAKE_SOURCE_DIR}/drawer/base_drawer.cpp
        ${CMAKE_SOURCE_DIR}/drawer/triangle_drawer.h
        ${CMAKE_SOURCE_DIR}/drawer/triangle_drawer.cpp
        ${CMAKE_SOURCE_DIR}/render/image_render.h
        ${CMAKE_SOURCE_DIR}/render/image_render.cpp

        ${CMAKE_SOURCE_DIR}/filter/adjust/color_invert_filter.cpp
        ${CMAKE_SOURCE_DIR}/filter/adjust/color_invert_filter.h

        ${CMAKE_SOURCE_DIR}/filter/base/image_filter.h
        ${CMAKE_SOURCE_DIR}/filter/base/image_filter.cpp
        # Provides a relative path to your source file(s).
        ${CMAKE_SOURCE_DIR}/native-lib.cpp
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