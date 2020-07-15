/*
 * This file is part of Tornado: A heterogeneous programming framework: 
 * https://github.com/beehive-lab/tornadovm
 *
 * Copyright (c) 2013-2020, APT Group, Department of Computer Science,
 * The University of Manchester. All rights reserved.
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
 * Authors: James Clarkson
 *
 */
package uk.ac.manchester.tornado.drivers.opencl;

import org.graalvm.compiler.options.OptionValues;

import jdk.vm.ci.hotspot.HotSpotJVMCIRuntime;
import uk.ac.manchester.tornado.runtime.TornadoAcceleratorDriver;
import uk.ac.manchester.tornado.runtime.TornadoDriverProvider;
import uk.ac.manchester.tornado.runtime.TornadoVMConfig;

public class OCLTornadoDriverProvider implements TornadoDriverProvider {

    /**
     * Check {@link TornadoDriverProvider} for documentation on priority.
     */
    private final int priority = 0;

    @Override
    public String getName() {
        return "OpenCL Driver";
    }

    @Override
    public TornadoAcceleratorDriver createDriver(OptionValues options, HotSpotJVMCIRuntime vmRuntime, TornadoVMConfig vmConfig) {
        return new OCLDriver(options, vmRuntime, vmConfig);
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(TornadoDriverProvider o) {
        return o.getPriority() - priority;
    }
}
