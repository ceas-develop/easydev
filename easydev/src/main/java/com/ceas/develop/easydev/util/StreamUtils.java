package com.ceas.develop.easydev.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class StreamUtils {
    public static final int BUFFER_SIZE = 4096;

    public static void write(String text, OutputStream outputStream) throws IOException {
        write(text.getBytes(), outputStream);
    }

    public static void write(byte[] bytes, OutputStream outputStream) throws IOException {
        if (bytes == null) {
            throw new IllegalArgumentException("bytes cannot be null");
        }
        if (outputStream == null) {
            throw new IllegalArgumentException("outputStream cannot be null");
        }
        outputStream.write(bytes);
        outputStream.flush();
    }

    public static void write(InputStream inputStream, OutputStream outputStream) throws IOException {
        write(inputStream, outputStream, 0);
    }

    public static void write(InputStream inputStream, OutputStream outputStream, int skipBytes) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(outputStream);
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        bis.skip(skipBytes);
        int readByte;
        final byte[] BUFFER = new byte[BUFFER_SIZE];
        while ((readByte = bis.read(BUFFER)) != -1) {
            bos.write(BUFFER, 0, readByte);
        }
        bos.flush();
    }

    public static String toString(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    public static byte[] toBytes(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException("inputStream cannot be null");
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int byteRead;
        while ((byteRead = inputStream.read()) != -1) {
            baos.write(byteRead);
        }
        baos.flush();
        byte[] bytes = baos.toByteArray();
        close(baos);
        return bytes;
    }

    public static byte[] toBytes(InputStream inputStream, int length) throws IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException("inputStream cannot be null");
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream(length);
        for (int i = 0; i < length; i++) {
            baos.write(inputStream.read());
        }
        baos.flush();
        byte[] bytes = baos.toByteArray();
        close(baos);
        return bytes;
    }

    public static FileInputStream openInputStream(String path) throws FileNotFoundException {
        return openInputStream(new File(path));
    }

    public static FileOutputStream openOutputStream(String path) throws FileNotFoundException {
        return openOutputStream(new File(path));
    }

    public static ZipInputStream openZipInputStream(String path) throws FileNotFoundException {
        return new ZipInputStream(openInputStream(path));
    }

    public static ZipOutputStream openZipOutputStream(String path) throws FileNotFoundException {
        return new ZipOutputStream(openOutputStream(path));
    }

    public static ZipInputStream openZipInputStream(File file) throws FileNotFoundException {
        return new ZipInputStream(openInputStream(file));
    }

    public static ZipOutputStream openZipOutputStream(File file) throws FileNotFoundException {
        return new ZipOutputStream(openOutputStream(file));
    }

    public static FileInputStream openInputStream(File file) throws FileNotFoundException {
        return new FileInputStream(file);
    }

    public static FileOutputStream openOutputStream(File file) throws FileNotFoundException {
        return new FileOutputStream(file);
    }

    public static void close(Closeable... closeables) {
        if (closeables == null) return;
        try {
            for (Closeable closeable : closeables) {
                if (closeable != null) closeable.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
