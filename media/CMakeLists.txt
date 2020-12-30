cmake_minimum_required(VERSION 3.4.1)

# 支持gnu++11
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")

# 1. 定义so库和头文件所在目录，方面后面使用
set(ffmpeg_lib_dir ${CMAKE_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI})
set(ffmpeg_head_dir ${CMAKE_SOURCE_DIR}/src/main/cpp/ffmpeg)
set(native_head_dir ${CMAKE_SOURCE_DIR}/src/main/cpp)


# 2. 添加头文件目录
include_directories(${ffmpeg_head_dir}/include)

# 3. 添加ffmpeg相关的so库
add_library(avutil
        SHARED
        IMPORTED)
set_target_properties(avutil
        PROPERTIES IMPORTED_LOCATION
        ${ffmpeg_lib_dir}/libavutil.so)

add_library(swresample
        SHARED
        IMPORTED)
set_target_properties(swresample
        PROPERTIES IMPORTED_LOCATION
        ${ffmpeg_lib_dir}/libswresample.so)

add_library(avcodec
        SHARED
        IMPORTED)
set_target_properties(avcodec
        PROPERTIES IMPORTED_LOCATION
        ${ffmpeg_lib_dir}/libavcodec.so)

add_library(avfilter
        SHARED
        IMPORTED)
set_target_properties(avfilter
        PROPERTIES IMPORTED_LOCATION
        ${ffmpeg_lib_dir}/libavfilter.so)

add_library(swscale
        SHARED
        IMPORTED)
set_target_properties(swscale
        PROPERTIES IMPORTED_LOCATION
        ${ffmpeg_lib_dir}/libswscale.so)

add_library(avformat
        SHARED
        IMPORTED)
set_target_properties(avformat
        PROPERTIES IMPORTED_LOCATION
        ${ffmpeg_lib_dir}/libavformat.so)

add_library(avdevice
        SHARED
        IMPORTED)
set_target_properties(avdevice
        PROPERTIES IMPORTED_LOCATION
        ${ffmpeg_lib_dir}/libavdevice.so)


#libyuv
get_filename_component(PARENT_DIR ${CMAKE_SOURCE_DIR} PATH)

#使用单个yuv库
set(LIBYUV_DIR ${PARENT_DIR}/libyuv-single)


add_library(libyuv
        SHARED
        IMPORTED)

set_target_properties(libyuv
        PROPERTIES IMPORTED_LOCATION
        ${LIBYUV_DIR}/libs/${ANDROID_ABI}/libyuv.so
        )

# libyuv库头文件
include_directories(${LIBYUV_DIR}/src/main/cpp/include)


#sdl2
get_filename_component(PARENT_DIR ${CMAKE_SOURCE_DIR} PATH)

#使用单个sdl2库
set(SDL2_DIR ${PARENT_DIR}/sdl2-hello)


add_library(SDL2
        SHARED
        IMPORTED)

set_target_properties(SDL2
        PROPERTIES IMPORTED_LOCATION
        ${SDL2_DIR}/libs/${ANDROID_ABI}/libSDL2.so
        )

# sdl2库头文件
include_directories(${SDL2_DIR}/src/main/cpp/include)


# 查找代码中使用到的系统库
find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)


add_library(ffmpeg-lib
        SHARED

        ${native_head_dir}/encode/native_code.h
        ${native_head_dir}/encode/native_code.cpp
        ${native_head_dir}/encode/encode_video.h
        ${native_head_dir}/encode/encode_video.cpp
        ${native_head_dir}/encode/encode_mp4.h
        ${native_head_dir}/encode/encode_mp4.cpp
        ${native_head_dir}/encode/encode_jpeg.h
        ${native_head_dir}/encode/encode_jpeg.cpp

        ${native_head_dir}/ffmpeg/ffplay_demo.cpp
        )

# 配置目标so库编译信息
add_library( # Sets the name of the library.
        # Sets the library as a shared library.
        utils

        SHARED


        #工具
        ${native_head_dir}/utils/const.h
        ${native_head_dir}/utils/logger.h
        ${native_head_dir}/utils/timer.c
        )

# 指定编译目标库时，cmake要链接的库
target_link_libraries(

        # 指定目标库，native-encode 是在上面 add_library 中配置的目标库
        ffmpeg-lib
        utils
        libyuv
        SDL2

        # 4. 连接 FFmpeg 相关的库
        avutil
        swresample
        avcodec
        avfilter
        swscale
        avformat
        avdevice
        -landroid
        OpenSLES
        -lEGL
        -lGLESv2
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})