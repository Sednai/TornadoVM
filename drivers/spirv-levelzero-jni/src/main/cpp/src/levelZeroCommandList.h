/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class uk_ac_manchester_tornado_drivers_spirv_levelzero_LevelZeroCommandList */

#ifndef _Included_uk_ac_manchester_tornado_drivers_spirv_levelzero_LevelZeroCommandList
#define _Included_uk_ac_manchester_tornado_drivers_spirv_levelzero_LevelZeroCommandList
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     uk_ac_manchester_tornado_drivers_spirv_levelzero_LevelZeroCommandList
 * Method:    zeCommandListAppendLaunchKernel_native
 * Signature: (JJLuk/ac/manchester/tornado/drivers/spirv/levelzero/ZeGroupDispatch;Ljava/lang/Object;ILjava/lang/Object;)I
 */
JNIEXPORT jint JNICALL Java_uk_ac_manchester_tornado_drivers_spirv_levelzero_LevelZeroCommandList_zeCommandListAppendLaunchKernel_1native
        (JNIEnv *, jobject, jlong, jlong, jobject, jobject, jint, jobject);

/*
 * Class:     uk_ac_manchester_tornado_drivers_spirv_levelzero_LevelZeroCommandList
 * Method:    zeCommandListClose_native
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_uk_ac_manchester_tornado_drivers_spirv_levelzero_LevelZeroCommandList_zeCommandListClose_1native
        (JNIEnv *, jobject, jlong);

/*
 * Class:     uk_ac_manchester_tornado_drivers_spirv_levelzero_LevelZeroCommandList
 * Method:    zeCommandListAppendMemoryCopy_native
 * Signature: (JLuk/ac/manchester/tornado/drivers/spirv/levelzero/LevelZeroByteBuffer;[CILuk/ac/manchester/tornado/drivers/spirv/levelzero/ZeEventHandle;ILuk/ac/manchester/tornado/drivers/spirv/levelzero/ZeEventHandle;)I
 */
JNIEXPORT jint JNICALL Java_uk_ac_manchester_tornado_drivers_spirv_levelzero_LevelZeroCommandList_zeCommandListAppendMemoryCopy_1native
        (JNIEnv *, jobject, jlong, jobject, jcharArray, jint, jobject, jint, jobject);


#ifdef __cplusplus
}
#endif
#endif