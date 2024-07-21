package com.ceas.develop.easydev.worker;

import com.ceas.develop.easydev.util.StreamUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Works {

    public static Worker.Work<String, Void> downloadWork() {
        return (controller, strings) -> {
            URL url = new URL(strings[0]);
            URLConnection conexao = url.openConnection();
            InputStream inputStream = conexao.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(strings[1]);

            controller.setMaxProgress(conexao.getContentLength());
            byte[] buffer = new byte[1024];
            int tamanho;
            while ((tamanho = inputStream.read(buffer)) != -1) {
                controller.setMaxProgress(tamanho);
                //controller.notifyProgress();
                outputStream.write(buffer, 0, tamanho);
            }
            StreamUtils.close(inputStream, outputStream);
            return null;
        };
    }
}
