#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring

JNICALL
Java_com_loushao_player_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_loushao_player_Constants_baseUrl(JNIEnv *env, jobject) {

    std::string base = "http://www.loushao.net/api/";

    return env->NewStringUTF(base.c_str());
}