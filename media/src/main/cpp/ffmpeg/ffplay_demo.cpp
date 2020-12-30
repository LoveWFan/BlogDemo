#include <jni.h>
#include <android/log.h>
#include <android/native_window.h>
#include <android/native_window_jni.h>
#include <string>
#include <unistd.h>

#define LOG_TAG "FFNative"
#define ALOGV(...) ((void)__android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG, __VA_ARGS__))
#define ALOGD(...) ((void)__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__))
#define ALOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__))
#define ALOGW(...) ((void)__android_log_print(ANDROID_LOG_WARN, LOG_TAG, __VA_ARGS__))
#define ALOGE(...) ((void)__android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__))

extern "C" {

#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
#include <libavfilter/avfilter.h>
#include <libswscale/swscale.h>
#include "libswresample/swresample.h"
#include "libavutil/opt.h"
#include <libavutil/imgutils.h>


JNIEXPORT jstring JNICALL
Java_com_poney_ffmpeg_play_FFUtils_urlProtocolInfo(JNIEnv *env, jclass type) {
    char info[40000] = {0};

    struct URLProtocol *pup = NULL;

    struct URLProtocol **p_temp = (struct URLProtocol **) &pup;
    avio_enum_protocols((void **) p_temp, 0);

    while ((*p_temp) != NULL) {
        sprintf(info, "%sInput: %s\n", info, avio_enum_protocols((void **) p_temp, 0));
    }
    pup = NULL;
    avio_enum_protocols((void **) p_temp, 1);
    while ((*p_temp) != NULL) {
        sprintf(info, "%sInput: %s\n", info, avio_enum_protocols((void **) p_temp, 1));
    }
    ALOGI("%s", info);
    return env->NewStringUTF(info);
}

JNIEXPORT jstring JNICALL
Java_com_poney_ffmpeg_play_FFUtils_avFormatInfo(JNIEnv *env, jclass type) {
    char info[40000] = {0};


    AVInputFormat *if_temp = av_iformat_next(NULL);
    AVOutputFormat *of_temp = av_oformat_next(NULL);
    while (if_temp != NULL) {
        sprintf(info, "%sInput: %s\n", info, if_temp->name);
        if_temp = if_temp->next;
    }
    while (of_temp != NULL) {
        sprintf(info, "%sOutput: %s\n", info, of_temp->name);
        of_temp = of_temp->next;
    }
    ALOGI("%s", info);
    return env->NewStringUTF(info);
}

JNIEXPORT jstring JNICALL
Java_com_poney_ffmpeg_play_FFUtils_avCodecInfo(JNIEnv *env, jclass type) {
    char info[40000] = {0};


    AVCodec *c_temp = av_codec_next(NULL);

    while (c_temp != NULL) {
        if (c_temp->decode != NULL) {
            sprintf(info, "%sdecode:", info);
        } else {
            sprintf(info, "%sencode:", info);
        }
        switch (c_temp->type) {
            case AVMEDIA_TYPE_VIDEO:
                sprintf(info, "%s(video):", info);
                break;
            case AVMEDIA_TYPE_AUDIO:
                sprintf(info, "%s(audio):", info);
                break;
            case AVMEDIA_TYPE_SUBTITLE:
                sprintf(info, "%s(subtitle):", info);
                break;
            default:
                sprintf(info, "%s(other):", info);
                break;
        }
        sprintf(info, "%s[%10s]\n", info, c_temp->name);
        c_temp = c_temp->next;
    }
    ALOGI("%s", info);
    return env->NewStringUTF(info);
}

JNIEXPORT jstring JNICALL
Java_com_poney_ffmpeg_play_FFUtils_avFilterInfo(JNIEnv *env, jclass type) {
    char info[40000] = {0};

    AVFilter *f_temp = (AVFilter *) avfilter_next(NULL);
    while (f_temp != NULL) {
        sprintf(info, "%s%s\n", info, f_temp->name);
        f_temp = f_temp->next;
    }
    ALOGI("%s", info);
    return env->NewStringUTF(info);
}

JNIEXPORT void JNICALL
Java_com_poney_ffmpeg_play_FFUtils_playVideo(JNIEnv *env, jclass type, jstring videoPath_,
                                             jobject surface, jint view_width, jint view_height) {
    const char *videoPath = env->GetStringUTFChars(videoPath_, 0);
    ALOGI("PlayVideo: %s", videoPath);
    if (videoPath == NULL) {
        ALOGE("video path is null");
        return;
    }

    //初始化AVFormatContext

    AVFormatContext *formatContext = avformat_alloc_context();

    //open video file
    if (avformat_open_input(&formatContext, videoPath, NULL, NULL) != 0) {
        ALOGE("Cannot open video file :%s\n", videoPath);
        return;
    }

    //Retrieve stream information
    if (avformat_find_stream_info(formatContext, NULL) < 0) {
        ALOGE("Cannot find stream information.");
        return;
    }

    //Find the first video stream
    int video_stream_index = -1;
    for (int i = 0; i < formatContext->nb_streams; ++i) {
        if (formatContext->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_VIDEO) {
            video_stream_index = i;
        }
    }
    if (video_stream_index == -1) {
        ALOGE("No video stream found!");
        return;
    }

    //Get a pointer to the pCodec context for the video stream
    AVCodecParameters *pCodecParameters = formatContext->streams[video_stream_index]->codecpar;
    AVCodec *pCodec = avcodec_find_decoder(
            pCodecParameters->codec_id);
    if (pCodec == NULL) {
        ALOGE("Codec not found!");
        return;
    }
    AVCodecContext *pCodecContext = avcodec_alloc_context3(pCodec);
    if (pCodecContext == NULL) {
        ALOGE("CodecContext not found!");
        return;
    }

    //fill CodecContext according to CodecParameters
    if (avcodec_parameters_to_context(pCodecContext, pCodecParameters) < 0) {
        ALOGE("Fill CodecContext fail!");
        return;
    }

    //init codec context
    if (avcodec_open2(pCodecContext, pCodec, NULL)) {
        ALOGE("Init CodecContext failed.");
        return;
    }

    ALOGI("pCodecContext width:%d,height:%d", pCodecContext->width, pCodecContext->height);
    //Allocate av packet
    AVPacket *pPacket = av_packet_alloc();
    if (pPacket == NULL) {
        ALOGE("Could not allocate av packet.");
        return;
    }
    //Allocate video frame
    AVFrame *pFrame = av_frame_alloc();
    //Allocate render frame;
    AVFrame *renderFrame = av_frame_alloc();
    if (pFrame == NULL || renderFrame == NULL) {
        ALOGE("Could not allocate video frame.");
        return;
    }
    //Determine required buffer size and allocate buffer
    int size = av_image_get_buffer_size(AV_PIX_FMT_RGBA, pCodecContext->width,
                                        pCodecContext->height, 1);
    uint8_t *buffer = static_cast<uint8_t *>(av_malloc(size * sizeof(uint8_t)));

    av_image_fill_arrays(renderFrame->data, renderFrame->linesize, buffer, AV_PIX_FMT_RGBA,
                         pCodecContext->width, pCodecContext->height, 1);
    //init SwsContext
    SwsContext *pSwsContext = sws_getContext(pCodecContext->width, pCodecContext->height,
                                             pCodecContext->pix_fmt,
                                             pCodecContext->width, pCodecContext->height,
                                             AV_PIX_FMT_RGBA,
                                             SWS_BILINEAR, NULL, NULL, NULL);

    if (pSwsContext == NULL) {
        ALOGE("Init SwsContext failed.");
        return;
    }

    //native window
    ANativeWindow *pWindow = ANativeWindow_fromSurface(env, surface);
    int videoWidth = pCodecContext->width;
    int videoHeight = pCodecContext->height;
    ALOGI("ViewSize: [%d,%d]", view_width, view_height);
    ALOGI("VideoSize: [%d,%d]", videoWidth, videoHeight);
// 设置native window的buffer大小,可自动拉伸
    ALOGI("set native window");
    ANativeWindow_setBuffersGeometry(pWindow, videoWidth, videoHeight,
                                     WINDOW_FORMAT_RGBA_8888);
    ANativeWindow_Buffer windowBuffer;

    //read frame
    while (av_read_frame(formatContext, pPacket) == 0) {
        // Is this a packet from the video stream?
        if (pPacket->stream_index == video_stream_index) {
            //send origin data to the decoder
            int sendPacketState = avcodec_send_packet(pCodecContext, pPacket);
            if (sendPacketState == 0) {
                ALOGD("向解码器-发送数据");

                int receiveFrameState = avcodec_receive_frame(pCodecContext, pFrame);
                if (receiveFrameState == 0) {
                    ALOGD("从解码器-接收数据");
                    //lock native window buffer;
                    ANativeWindow_lock(pWindow, &windowBuffer, NULL);
                    //格式转换
                    sws_scale(pSwsContext, pFrame->data, pFrame->linesize, 0, pCodecContext->height,
                              renderFrame->data, renderFrame->linesize);
                    //获取stride
                    uint8_t *dst = static_cast<uint8_t *>(windowBuffer.bits);
                    uint8_t *src = renderFrame->data[0];
                    int32_t dstStride = windowBuffer.stride * 4;
                    int srcStride = renderFrame->linesize[0];

                    //由于window的stride和帧的stride不同，因此需要逐行复制
                    for (int i = 0; i < videoHeight; ++i) {
                        memcpy(dst + i * dstStride, src + i * srcStride, srcStride);
                    }
                    ANativeWindow_unlockAndPost(pWindow);
                } else if (receiveFrameState == AVERROR(EAGAIN)) {
                    ALOGD("从解码器-接收-数据失败：AVERROR(EAGAIN)");
                } else if (receiveFrameState == AVERROR_EOF) {
                    ALOGD("从解码器-接收-数据失败：AVERROR_EOF");
                } else if (receiveFrameState == AVERROR(EINVAL)) {
                    ALOGD("从解码器-接收-数据失败：AVERROR(EINVAL)");
                } else {
                    ALOGD("从解码器-接收-数据失败：未知");
                }
            } else if (sendPacketState == AVERROR(EAGAIN)) {//发送数据被拒绝，必须尝试先读取数据
                ALOGD("向解码器-发送-数据包失败：AVERROR(EAGAIN)");//解码器已经刷新数据但是没有新的数据包能发送给解码器
            } else if (sendPacketState == AVERROR_EOF) {
                ALOGD("向解码器-发送-数据失败：AVERROR_EOF");
            } else if (sendPacketState == AVERROR(EINVAL)) {//遍解码器没有打开，或者当前是编码器，也或者需要刷新数据
                ALOGD("向解码器-发送-数据失败：AVERROR(EINVAL)");
            } else if (sendPacketState == AVERROR(ENOMEM)) {//数据包无法压如解码器队列，也可能是解码器解码错误
                ALOGD("向解码器-发送-数据失败：AVERROR(ENOMEM)");
            } else {
                ALOGD("向解码器-发送-数据失败：未知");
            }

        }
        av_packet_unref(pPacket);
    }

    //release
    ANativeWindow_release(pWindow);
    av_frame_free(&pFrame);
    av_frame_free(&renderFrame);
    av_packet_free(&pPacket);
    avcodec_close(pCodecContext);
    avcodec_free_context(&pCodecContext);
    avformat_close_input(&formatContext);
    avformat_free_context(formatContext);
    env->ReleaseStringUTFChars(videoPath_, videoPath);
}

}