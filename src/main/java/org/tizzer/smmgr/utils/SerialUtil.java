package org.tizzer.smmgr.utils;

import org.tizzer.smmgr.common.SerialCallable;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SerialUtil {

    private static ExecutorService pool = Executors.newSingleThreadExecutor();
    private static SerialCallable serialCallable = new SerialCallable();

    public static List<Object> getIncomeSerialNo() throws ExecutionException, InterruptedException {
        Future<List<Object>> future = pool.submit(serialCallable.createIncomeCallable());
        return future.get();
    }

    public static List<Object> getRefundSerialNo() throws ExecutionException, InterruptedException {
        Future<List<Object>> future = pool.submit(serialCallable.createRefundCallable());
        return future.get();
    }

    public static List<Object> getLossSerialNo() throws ExecutionException, InterruptedException {
        Future<List<Object>> future = pool.submit(serialCallable.createLossCallable());
        return future.get();
    }
}
