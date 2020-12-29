/**
 * @author feibiao.ma
 * @project BlogDemo
 * @date 2020/12/25
 */

#include <jni.h>
#include <malloc.h>
#include <string.h>
#include <libavformat/avformat.h>
#include "../utils/logger.h"

extern "C"
JNIEXPORT void JNICALL
Java_com_poney_ffmpeg_FFMpegDemoActivity_dumpInfo(JNIEnv *env, jobject thiz, jstring input_path) {
    int err_code;
    char errors[1024];

    AVFormatContext *fmt_ctx = NULL;
    const char *src_filename = env->GetStringUTFChars(input_path, 0);

    /* open input file, and allocate format context */
    if ((err_code = avformat_open_input(&fmt_ctx, src_filename, NULL, NULL)) < 0) {
        av_strerror(err_code, errors, 1024);
        LOGE("MFB", "Could not open source file %s, %d(%s)\n", src_filename, err_code, errors);
        exit(1);
    }

    /* retreive stream information */
    if ((err_code = avformat_find_stream_info(fmt_ctx, NULL)) < 0) {
        av_strerror(err_code, errors, 1024);
        LOGE("MFB", "Could not open source file %s, %d(%s)\n", src_filename, err_code, errors);
        exit(1);
    }

    /* dump input information to stderr */
    av_dump_format(fmt_ctx, 0, src_filename, 0);

    /* close input file */
    avformat_close_input(&fmt_ctx);

    env->ReleaseStringUTFChars(input_path, src_filename);
}

