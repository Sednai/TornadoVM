package uk.ac.manchester.tornado.drivers.ptx;

import uk.ac.manchester.tornado.api.exceptions.TornadoBailoutRuntimeException;
import uk.ac.manchester.tornado.runtime.tasks.meta.TaskMetaData;

import java.util.Arrays;

import static uk.ac.manchester.tornado.api.exceptions.TornadoInternalError.shouldNotReachHere;
import static uk.ac.manchester.tornado.runtime.common.Tornado.DEBUG;
import static uk.ac.manchester.tornado.runtime.common.Tornado.FULL_DEBUG;
import static uk.ac.manchester.tornado.runtime.common.Tornado.warn;

public class PTXScheduler {

    private final PTXDevice device;

    public PTXScheduler(final PTXDevice device) {
        this.device = device;
    }

    public void calculateGlobalWork(final TaskMetaData meta, long batchThreads) {
        if (meta.isGlobalWorkDefined()) return;

        final long[] globalWork = meta.getGlobalWork();
        for (int i = 0; i < meta.getDims(); i++) {
            long value = (batchThreads <= 0) ? (long) (meta.getDomain().get(i).cardinality()) : batchThreads;
            globalWork[i] = value;
        }
    }

    public int[] calculateBlocks(PTXModule module) {
        if (module.metaData.isLocalWorkDefined()) {
            return Arrays.stream(module.metaData.getLocalWork()).mapToInt(l -> (int) l).toArray();
        }

        int[] defaultBlocks = {1, 1, 1};
        try {
            int maxBlockThreads = module.getMaxBlocks();
            for (int i = 0; i < module.metaData.getDims(); i++) {
                defaultBlocks[i] = calculateBlockSize(calculateEffectiveMaxWorkItemSize(module.metaData, maxBlockThreads), module.metaData.getGlobalWork()[i]);
            }
        }
        catch (Exception e) {
            warn("[CUDA-PTX] Failed to calculate blocks for " + module.javaName);
            warn("[CUDA-PTX] Falling back to blocks: " + Arrays.toString(defaultBlocks));
            if (DEBUG || FULL_DEBUG) {
                e.printStackTrace();
            }
            throw new TornadoBailoutRuntimeException("[Error During Block Size compute] ", e);
        }
        return defaultBlocks;
    }

    private long calculateEffectiveMaxWorkItemSize(TaskMetaData metaData, int threads) {
        if (metaData.getDims() == 0) shouldNotReachHere();
        return (long) Math.pow(threads, (double) 1 / metaData.getDims());
    }

    private int calculateBlockSize(long maxBlockSize, long globalWorkSize) {
        if (maxBlockSize == globalWorkSize) {
            maxBlockSize /= 4;
        }

        int value = (int) Math.min(maxBlockSize, globalWorkSize);
        if (value == 0) {
            return 1;
        }
        while (globalWorkSize % value != 0) {
            value--;
        }
        return value;
    }

    public int[] calculateGrids(PTXModule module, int[] blocks) {
        int[] defaultGrids = {1, 1, 1};

        try {
            int dims = module.metaData.getDims();
            long[] maxGridSizes = device.getDeviceMaxWorkGroupSize();

            for (int i = 0; i < dims; i++) {
                int workSize = (int) module.metaData.getGlobalWork()[i];
                defaultGrids[i] = Math.max(Math.min(workSize / blocks[i], (int) maxGridSizes[i]), 1);
            }
        }
        catch (Exception e) {
            warn("[CUDA-PTX] Failed to calculate grids for " + module.javaName);
            warn("[CUDA-PTX] Falling back to grid: " + Arrays.toString(defaultGrids));
            if (DEBUG || FULL_DEBUG) {
                e.printStackTrace();
            }
            throw new TornadoBailoutRuntimeException("[Error During Grid Size compute] ", e);
        }

        return defaultGrids;
    }
}
