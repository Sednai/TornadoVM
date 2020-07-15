/*
 * Copyright (c) 2020, APT Group, Department of Computer Science,
 * School of Engineering, The University of Manchester. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 */
package uk.ac.manchester.tornado.drivers.ptx.graal.lir;

import jdk.vm.ci.meta.Value;
import org.graalvm.compiler.core.common.LIRKind;
import org.graalvm.compiler.lir.LIRInstruction.Use;
import org.graalvm.compiler.lir.Variable;
import uk.ac.manchester.tornado.drivers.ptx.graal.asm.PTXAssembler;
import uk.ac.manchester.tornado.drivers.ptx.graal.compiler.PTXCompilationResultBuilder;

import static uk.ac.manchester.tornado.api.exceptions.TornadoInternalError.shouldNotReachHere;
import static uk.ac.manchester.tornado.drivers.ptx.graal.asm.PTXAssemblerConstants.COMMA;
import static uk.ac.manchester.tornado.drivers.ptx.graal.asm.PTXAssemblerConstants.CONVERT;
import static uk.ac.manchester.tornado.drivers.ptx.graal.asm.PTXAssemblerConstants.CURLY_BRACKETS_CLOSE;
import static uk.ac.manchester.tornado.drivers.ptx.graal.asm.PTXAssemblerConstants.CURLY_BRACKETS_OPEN;
import static uk.ac.manchester.tornado.drivers.ptx.graal.asm.PTXAssemblerConstants.DOT;
import static uk.ac.manchester.tornado.drivers.ptx.graal.asm.PTXAssemblerConstants.MOVE;
import static uk.ac.manchester.tornado.drivers.ptx.graal.asm.PTXAssemblerConstants.SPACE;
import static uk.ac.manchester.tornado.drivers.ptx.graal.asm.PTXAssemblerConstants.TAB;
import static uk.ac.manchester.tornado.drivers.ptx.graal.asm.PTXAssemblerConstants.VECTOR;

public class PTXVectorAssign {

    /**
     * PTX vector assignment expression
     */
    public static class AssignVectorExpr extends PTXLIROp {

        @Use protected Value[] values;

        public AssignVectorExpr(PTXKind ptxKind, Value... values) {
            super(LIRKind.value(ptxKind));
            this.values = values;
        }

        @Override
        public void emit(PTXCompilationResultBuilder crb, PTXAssembler asm, Variable dest) {
            PTXKind destElementKind = ((PTXKind)dest.getPlatformKind()).getElementKind();
            boolean useConvert = destElementKind.is8Bit();
            PTXVectorSplit vectorSplitData = new PTXVectorSplit(dest);
            Value[] intermValues = new Value[vectorSplitData.newKind.getVectorLength()];

            for (int i = 0; i < vectorSplitData.vectorNames.length; i++) {
                if (vectorSplitData.newKind.getVectorLength() >= 0) {
                    System.arraycopy(values, i * vectorSplitData.newKind.getVectorLength(), intermValues, 0, vectorSplitData.newKind.getVectorLength());
                }
                asm.emitSymbol(TAB);
                asm.emitSymbol(useConvert ? CONVERT : MOVE);
                if (!vectorSplitData.fullUnwrapVector) {
                    asm.emitSymbol(DOT);
                    asm.emit(VECTOR + vectorSplitData.newKind.getVectorLength());
                }
                asm.emitSymbol(DOT);
                asm.emit(destElementKind.toString());
                if (useConvert) {
                    asm.emitSymbol(DOT);
                    asm.emit(destElementKind.toString());
                }
                asm.emitSymbol(TAB);

                asm.emitSymbol(vectorSplitData.vectorNames[i]);
                asm.emitSymbol(COMMA);
                asm.emitSymbol(SPACE);
                if (!vectorSplitData.fullUnwrapVector) {
                    asm.emitSymbol(CURLY_BRACKETS_OPEN);
                }
                asm.emitValuesOrOp(crb, intermValues, dest);
                if (!vectorSplitData.fullUnwrapVector) {
                    asm.emitSymbol(CURLY_BRACKETS_CLOSE);
                }
                if (i < vectorSplitData.vectorNames.length - 1) {
                    asm.delimiter();
                    asm.eol();
                }
            }
        }
    }

}
