package com.ceas.develop.easydev.worker;

import android.graphics.Bitmap;

import java.io.File;

public class WorkerFactory {
    private static <Params, Result> Worker<Params, Result> create(
            Class<Params> paramType, Class<Result> resultType,
            Worker.Work<Params, Result> work
    ) {
        return Worker.get(work);
    }

    public static Worker<File, File> forFile(Worker.Work<File, File> work) {
        return create(File.class, File.class, work);
    }

    public static Worker<File, Void> forFileVoid(Worker.Work<File, Void> work) {
        return create(File.class, Void.class, work);
    }

    public static Worker<Void, File> forVoidFile(Worker.Work<Void, File> work) {
        return create(Void.class, File.class, work);
    }

    public static Worker<Void, Void> forVoid(Worker.Work<Void, Void> work) {
        return create(Void.class, Void.class, work);
    }

    public static Worker<String, String> forString(Worker.Work<String, String> work) {
        return create(String.class, String.class, work);
    }

    public static Worker<String, Void> forStringVoid(Worker.Work<String, Void> work) {
        return create(String.class, Void.class, work);
    }

    public static Worker<Void, String> forVoidString(Worker.Work<Void, String> work) {
        return create(Void.class, String.class, work);
    }

    public static Worker<File, Bitmap> forFileBitmap(Worker.Work<File, Bitmap> work) {
        return create(File.class, Bitmap.class, work);
    }

    public static Worker<Bitmap, File> forBitmapFile(Worker.Work<Bitmap, File> work) {
        return create(Bitmap.class, File.class, work);
    }
}