package org.tizzer.smmgr.common;

import org.tizzer.smmgr.constant.SystemConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

public class SerialCallable {

    public Callable<List<Object>> createIncomeCallable() {
        return new Callable<List<Object>>() {
            @Override
            public List<Object> call() {
                List<Object> list = new ArrayList<>();
                Date currentDate = new Date();
                String date = new SimpleDateFormat("yyyyMMdd").format(currentDate);
                String markId = String.format("%04d", SystemConstants.markId++);
                String random = String.format("%03d", new Random().nextInt(100));
                String time = new SimpleDateFormat("HHmmss").format(currentDate);
                list.add(date + markId + random + time);
                list.add(currentDate);
                list.add(markId);
                return list;
            }
        };
    }


    public Callable<List<Object>> createRefundCallable() {
        return new Callable<List<Object>>() {
            @Override
            public List<Object> call() {
                List<Object> list = new ArrayList<>();
                Date currentDate = new Date();
                String date = new SimpleDateFormat("yyyyMMdd").format(currentDate);
                String markId = String.format("%04d", SystemConstants.refundId++);
                String random = String.format("%03d", new Random().nextInt(100));
                String time = new SimpleDateFormat("HHmmss").format(currentDate);
                list.add(date + markId + random + time);
                list.add(currentDate);
                list.add(markId);
                return list;
            }
        };
    }

    public Callable<List<Object>> createLossCallable() {
        return new Callable<List<Object>>() {
            @Override
            public List<Object> call() {
                List<Object> list = new ArrayList<>();
                Date currentDate = new Date();
                String date = new SimpleDateFormat("yyyyMMdd").format(currentDate);
                String markId = String.format("%04d", SystemConstants.lossId++);
                String random = String.format("%03d", new Random().nextInt(100));
                String time = new SimpleDateFormat("HHmmss").format(currentDate);
                list.add(date + markId + random + time);
                list.add(currentDate);
                list.add(markId);
                return list;
            }
        };
    }

}
