package tornado.drivers.opencl.graal.nodes.vector;

import tornado.common.exceptions.TornadoInternalError;
import tornado.drivers.opencl.graal.asm.OCLAssembler.OCLBinaryIntrinsic;
import tornado.drivers.opencl.graal.asm.OCLAssembler.OCLOp2;
import tornado.drivers.opencl.graal.asm.OCLAssembler.OCLOp3;
import tornado.drivers.opencl.graal.asm.OCLAssembler.OCLOp4;
import tornado.drivers.opencl.graal.asm.OCLAssembler.OCLOp8;
import tornado.drivers.opencl.graal.asm.OCLAssembler.OCLTernaryIntrinsic;
import tornado.drivers.opencl.graal.asm.OCLAssembler.OCLUnaryOp;
import tornado.drivers.opencl.graal.lir.OCLKind;

import static tornado.drivers.opencl.graal.asm.OCLAssembler.OCLBinaryIntrinsic.*;
import static tornado.drivers.opencl.graal.asm.OCLAssembler.OCLOp2.*;
import static tornado.drivers.opencl.graal.asm.OCLAssembler.OCLOp3.*;
import static tornado.drivers.opencl.graal.asm.OCLAssembler.OCLOp4.*;
import static tornado.drivers.opencl.graal.asm.OCLAssembler.OCLOp8.*;
import static tornado.drivers.opencl.graal.asm.OCLAssembler.OCLTernaryIntrinsic.*;
import static tornado.drivers.opencl.graal.asm.OCLAssembler.OCLUnaryOp.*;

public final class VectorUtil {

    private static final OCLBinaryIntrinsic[] loadTable = new OCLBinaryIntrinsic[]{VLOAD2,
        VLOAD3, VLOAD4, VLOAD8, VLOAD16};

    private static final OCLTernaryIntrinsic[] storeTable = new OCLTernaryIntrinsic[]{VSTORE2,
        VSTORE3, VSTORE4, VSTORE8, VSTORE16};

    private static final OCLUnaryOp[] pointerTable = new OCLUnaryOp[]{
        CAST_TO_SHORT_PTR, CAST_TO_INT_PTR, CAST_TO_FLOAT_PTR, CAST_TO_BYTE_PTR
    };

    private static final OCLOp2[] assignOp2Table = new OCLOp2[]{VMOV_SHORT2, VMOV_INT2, VMOV_FLOAT2, VMOV_BYTE2};
    private static final OCLOp3[] assignOp3Table = new OCLOp3[]{VMOV_SHORT3, VMOV_INT3, VMOV_FLOAT3, VMOV_BYTE3};
    private static final OCLOp4[] assignOp4Table = new OCLOp4[]{VMOV_SHORT4, VMOV_INT4, VMOV_FLOAT4, VMOV_BYTE4};
    private static final OCLOp8[] assignOp8Table = new OCLOp8[]{VMOV_SHORT8, VMOV_INT8, VMOV_FLOAT8, VMOV_BYTE8};

    private static final <T> T lookupValueByLength(T[] array, OCLKind vectorKind) {
        final int index = vectorKind.lookupLengthIndex();
        if (index != -1) {
            return array[index];
        } else {
            throw TornadoInternalError.shouldNotReachHere("Unsupported vector type: "
                    + vectorKind.toString());
        }
    }

    private static final <T> T lookupValueByType(T[] array, OCLKind vectorKind) {
        final int index = vectorKind.lookupTypeIndex();
        if (index != -1) {
            return array[index];
        } else {
            throw TornadoInternalError.shouldNotReachHere("Unsupported vector type: "
                    + vectorKind.toString());
        }
    }

    public static final OCLOp2 resolveAssignOp2(OCLKind vectorKind) {
        return lookupValueByType(assignOp2Table, vectorKind);
    }

    public static final OCLOp3 resolveAssignOp3(OCLKind vectorKind) {
        return lookupValueByType(assignOp3Table, vectorKind);
    }

    public static final OCLOp4 resolveAssignOp4(OCLKind vectorKind) {
        return lookupValueByType(assignOp4Table, vectorKind);
    }

    public static final OCLOp8 resolveAssignOp8(OCLKind vectorKind) {
        return lookupValueByType(assignOp8Table, vectorKind);
    }

    public static final OCLTernaryIntrinsic resolveStoreIntrinsic(OCLKind vectorKind) {
        return lookupValueByLength(storeTable, vectorKind);
    }

    public static final OCLBinaryIntrinsic resolveLoadIntrinsic(OCLKind vectorKind) {
        return lookupValueByLength(loadTable, vectorKind);
    }

    public static final OCLUnaryOp resolvePointerCast(OCLKind vectorKind) {
        return lookupValueByType(pointerTable, vectorKind);
    }

}
