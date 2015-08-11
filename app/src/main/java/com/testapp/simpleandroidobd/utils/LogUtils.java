package com.testapp.simpleandroidobd.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;

/**
 * Created by PIERRE-LOUIS Antonio on 11/08/2015.
 */
public class LogUtils {

    private static final String LOG_TAG = LogUtils.class.getSimpleName();
    private static final String LOG_FOLDER = Environment.getExternalStorageDirectory() + File.separator + "OBDCrashes" + File.separator;

    public static void logError(Exception p_exception) {
        String logPath = LOG_FOLDER + "log.txt";
        Log.d(LOG_TAG, "Wrote log to : " + logPath);
        try {
            FileOutputStream out = new FileOutputStream(logPath, Boolean.TRUE);
            OutputStreamWriter writer = new OutputStreamWriter(out);
            writer.write("Exception\n");
            writer.write("Date : " + new Date(System.currentTimeMillis()) + '\n' + '\n');
            writer.write(p_exception.getLocalizedMessage() + '\n');
            for (StackTraceElement elem : p_exception.getStackTrace()) {
                writer.write(elem.toString() + "\n");
            }
            writer.write("\n\n\n");
            writer.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logResult(String result) {
        String logPath = LOG_FOLDER + "result.txt";
        Log.d(LOG_TAG, "Wrote log to : " + logPath);
        try {
            FileOutputStream out = new FileOutputStream(logPath, Boolean.TRUE);
            OutputStreamWriter writer = new OutputStreamWriter(out);
            writer.write("Result\n");
            writer.write("Date : " + new Date(System.currentTimeMillis()) + '\n' + '\n');
            writer.write(result);
            writer.write("\n\n\n");
            writer.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
