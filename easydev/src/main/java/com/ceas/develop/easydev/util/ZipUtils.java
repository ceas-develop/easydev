package com.ceas.develop.easydev.util;

import com.ceas.develop.easydev.worker.Worker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

    public static void compress(File srcFile, File zipFile) throws Exception {
        if (zipFile == null) {
            throw new IllegalArgumentException("zipFile cannot be null");
        }
        if (srcFile == null) {
            throw new IllegalArgumentException("srcFile cannot be null");
        }
        if (zipFile.isDirectory()) {
            throw new IllegalArgumentException("zipFile cannot be a Directory");
        }

        ZipOutputStream zos = StreamUtils.openZipOutputStream(zipFile);

        if (srcFile.isDirectory()) {
            compressFolder(srcFile, zos, srcFile.getName());
        } else {
            compressFile(srcFile, zos);
        }
    }

    public static void decompress(File srcFile, File destFile) throws Exception {
        if (destFile == null) {
            throw new IllegalArgumentException("destFile cannot be null");
        }
        if (srcFile == null) {
            throw new IllegalArgumentException("srcFile cannot be null");
        }
        if (destFile.isFile()) {
            throw new IllegalArgumentException("destFile cannot be a File");
        }
        decompressFile(srcFile, destFile);
    }

    private static void compressFile(File srcFile, ZipOutputStream zos) throws IOException {
        FileInputStream fis = StreamUtils.openInputStream(srcFile);
        zos.putNextEntry(new ZipEntry(srcFile.getName()));
        zipping(fis, zos);
    }

    private static void compressFolder(File srcFile, ZipOutputStream zipOutputStream, String parentFolder) throws Exception {
        File[] listFiles = FileUtils.listFiles(srcFile);
        if (listFiles.length == 0) {
            zipOutputStream.putNextEntry(
                    new ZipEntry(parentFolder)
            );
        }
        for (File file : listFiles) {
            final String parent = new File(parentFolder, file.getName()).getAbsolutePath();

            if (file.isDirectory()) {
                compressFolder(file, zipOutputStream, parent);
                continue;
            }
            zipOutputStream.putNextEntry(new ZipEntry(parent));
            zipping(StreamUtils.openInputStream(file), zipOutputStream);
        }
    }

    private static void zipping(FileInputStream fis, ZipOutputStream zos) throws IOException {
        StreamUtils.write(fis, zos);
        zos.flush();
        zos.closeEntry();
        StreamUtils.close(fis, zos);
    }


    private static void unZipping(ZipInputStream zis, File destFile) throws IOException {
        FileOutputStream fos = StreamUtils.openOutputStream(destFile);
        StreamUtils.write(zis, fos);
        StreamUtils.close(fos);
    }


    private static void decompressFile(File fileZip, File destFile) throws Exception {
        if (!destFile.exists()) {
            destFile.mkdir();
        }
        ZipInputStream zipInputStream = StreamUtils.openZipInputStream(fileZip);
        ZipEntry zipEntry;

        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            String filePath = destFile.getAbsolutePath()
                    + File.separator + zipEntry.getName();

            if (zipEntry.isDirectory()) {
                new File(filePath).mkdirs();
            } else {
                FileUtils.createFile(new File(filePath));
                unZipping(zipInputStream, new File(filePath));
            }
            zipInputStream.closeEntry();
        }
        zipInputStream.close();
    }
}
