#include <jni.h>
#include <cpu-features.h>
#include <string>

extern "C" JNIEXPORT jstring

JNICALL
Java_com_lskycity_androidtools_DeviceFragment_getCpuFeature(
        JNIEnv *env,
        jobject /* this */) {
    AndroidCpuFamily cupFamily = android_getCpuFamily();
    std::string cpu = "";

    if(cupFamily == ANDROID_CPU_FAMILY_ARM) {
        cpu += "ARM 32bit: ";

        uint64_t cpuFeature = android_getCpuFeatures();
        if(cpuFeature & ANDROID_CPU_ARM_FEATURE_ARMv7) {
            cpu += "ARMv7";
            cpu += ", ";
        }

        if(cpuFeature & ANDROID_CPU_ARM_FEATURE_VFPv3) {
            cpu += "VFPv3";
            cpu += ", ";
        }

        if(cpuFeature & ANDROID_CPU_ARM_FEATURE_NEON) {
            cpu += "NEON";
            cpu += ", ";
        }

        if(cpuFeature & ANDROID_CPU_ARM_FEATURE_LDREX_STREX) {
            cpu += "LDREX_STREX";
            cpu += ", ";
        }

        if(cpuFeature & ANDROID_CPU_ARM_FEATURE_VFPv2) {
            cpu += "VFPv2";
            cpu += ", ";
        }

        if(cpuFeature & ANDROID_CPU_ARM_FEATURE_VFP_D32) {
            cpu += "VFP_D32";
            cpu += ", ";
        }

        if(cpuFeature & ANDROID_CPU_ARM_FEATURE_VFP_FP16) {
            cpu += "VFP_FP16";
            cpu += ", ";
        }

        if(cpuFeature & ANDROID_CPU_ARM_FEATURE_VFP_FMA) {
            cpu += "VFP_FMA";
            cpu += ", ";
        }

        if(cpuFeature & ANDROID_CPU_ARM_FEATURE_NEON_FMA) {
            cpu += "NEON_FMA";
            cpu += ", ";
        }

        if(cpuFeature & ANDROID_CPU_ARM_FEATURE_IDIV_ARM) {
            cpu += "IDIV_ARM";
            cpu += ", ";
        }

        if(cpuFeature & ANDROID_CPU_ARM_FEATURE_IDIV_THUMB2) {
            cpu += "IDIV_THUMB2";
            cpu += ", ";
        }

        if(cpuFeature & ANDROID_CPU_ARM_FEATURE_iWMMXt) {
            cpu += "iWMMXt";
            cpu += ", ";
        }

        if(cpuFeature & ANDROID_CPU_ARM_FEATURE_AES) {
            cpu += "AES";
            cpu += ", ";
        }

        if(cpuFeature & ANDROID_CPU_ARM_FEATURE_PMULL) {
            cpu += "PMULL";
            cpu += ", ";
        }

        if(cpuFeature & ANDROID_CPU_ARM_FEATURE_SHA1) {
            cpu += "SHA1";
            cpu += ", ";
        }

        if(cpuFeature & ANDROID_CPU_ARM_FEATURE_SHA2) {
            cpu += "SHA2";
            cpu += ", ";
        }

        if(cpuFeature & ANDROID_CPU_ARM_FEATURE_CRC32) {
            cpu += "CRC32";
            cpu += ", ";
        }
    } else if(cupFamily == ANDROID_CPU_FAMILY_ARM64) {
        cpu += "ARM 64bit: ";

        uint64_t cpuFeature = android_getCpuFeatures();
        if(cpuFeature & ANDROID_CPU_ARM64_FEATURE_FP) {
            cpu += "FP";
            cpu += ", ";
        }

        if(cpuFeature & ANDROID_CPU_ARM64_FEATURE_ASIMD) {
            cpu += "ASIMD";
            cpu += ", ";
        }

        if(cpuFeature & ANDROID_CPU_ARM64_FEATURE_AES) {
            cpu += "AES";
            cpu += ", ";
        }

        if(cpuFeature & ANDROID_CPU_ARM64_FEATURE_PMULL) {
            cpu += "PMLL";
            cpu += ", ";
        }

        if(cpuFeature & ANDROID_CPU_ARM64_FEATURE_SHA1) {
            cpu += "SHA1";
            cpu += ", ";
        }

        if(cpuFeature & ANDROID_CPU_ARM64_FEATURE_SHA2) {
            cpu += "SHA2";
            cpu += ", ";
        }
        if(cpuFeature & ANDROID_CPU_ARM64_FEATURE_CRC32) {
            cpu += "CRC32";
            cpu += ", ";
        }
    } else if(cupFamily == ANDROID_CPU_FAMILY_X86
              || cupFamily == ANDROID_CPU_FAMILY_X86_64) {
        cpu += "X86 64bit: ";

        uint64_t cpuFeature = android_getCpuFeatures();
        if(cpuFeature & ANDROID_CPU_X86_FEATURE_SSSE3) {
            cpu += "SSSE3";
            cpu += ", ";
        }

        if(cpuFeature & ANDROID_CPU_X86_FEATURE_POPCNT) {
            cpu += "POPCNT";
            cpu += ", ";
        }

        if(cpuFeature & ANDROID_CPU_X86_FEATURE_MOVBE) {
            cpu += "MOVBE";
            cpu += ", ";
        }

        if(cpuFeature & ANDROID_CPU_X86_FEATURE_SSE4_1) {
            cpu += "SSE4_1";
            cpu += ", ";
        }

        if(cpuFeature & ANDROID_CPU_X86_FEATURE_SSE4_2) {
            cpu += "SSE4_2";
            cpu += ", ";
        }

        if(cpuFeature & ANDROID_CPU_X86_FEATURE_AES_NI) {
            cpu += "AES_NI";
            cpu += ", ";
        }
        if(cpuFeature & ANDROID_CPU_X86_FEATURE_AVX) {
            cpu += "AVX";
            cpu += ", ";
        }
        if(cpuFeature & ANDROID_CPU_X86_FEATURE_RDRAND) {
            cpu += "RDRAND";
            cpu += ", ";
        }
        if(cpuFeature & ANDROID_CPU_X86_FEATURE_AVX2) {
            cpu += "AVX2";
            cpu += ", ";
        }
        if(cpuFeature & ANDROID_CPU_X86_FEATURE_SHA_NI) {
            cpu += "SHA_NI";
            cpu += ", ";
        }

    } else {
        cpu += "Unknown";
    }


    return env->NewStringUTF(cpu.c_str());

}
