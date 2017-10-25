/*
 * This file is part of Tornado: A heterogeneous programming framework: 
 * https://github.com/beehive-lab/tornado
 *
 * Copyright (c) 2013-2017 APT Group, School of Computer Science, 
 * The University of Manchester
 *
 * This work is partially supported by EPSRC grants:
 * Anyscale EP/L000725/1 and PAMELA EP/K008730/1.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors: James Clarkson
 *
 */
package tornado.benchmarks.sgemv;

import tornado.benchmarks.BenchmarkDriver;
import tornado.benchmarks.BenchmarkRunner;

public class Benchmark extends BenchmarkRunner {

    private int width;
    private int height;

    @Override
    public void parseArgs(String[] args) {
        if (args.length == 3) {
            iterations = Integer.parseInt(args[0]);
            width = Integer.parseInt(args[1]);
            height = Integer.parseInt(args[2]);

        } else {
            iterations = 20;
            width = 4096;
            height = 4096;
        }
    }

    @Override
    protected String getName() {
        return "sgemv";
    }

    @Override
    protected String getIdString() {
        return String.format("%s-%d-%d-%d", getName(), iterations, width, height);
    }

    @Override
    protected String getConfigString() {
        return String.format("width=%d, height=%d", width, height);
    }

    @Override
    protected BenchmarkDriver getJavaDriver() {
        return new SgemvJava(iterations, width, height);
    }

    @Override
    protected BenchmarkDriver getTornadoDriver() {
        return new SgemvTornado(iterations, width, height);
    }

}