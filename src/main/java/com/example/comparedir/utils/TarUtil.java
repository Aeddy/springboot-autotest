package com.example.comparedir.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;

import java.io.*;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @description: tar解压工具类
 * @author: zhenqinl
 * @date: 2023/9/19 15:26
 */
@Slf4j
public class TarUtil {

    public static void main(String[] args) throws IOException {
        //解压tar文件
        unTar();
    }

    public static void unTar() throws IOException {
        String tarFilePath = "D:\\work code\\tar-files\\tar\\rarlinux-x64-5.5.0.tgz";
        String destinationFolderPath = "D:\\work code\\tar-files\\un-tar";

        unTarGz(tarFilePath, destinationFolderPath);
        System.out.println("un tar success");
    }


    /**
     * 解压.tar.gz文件
     *
     * @param sourceFile 需解压文件
     * @param outputDir  输出目录
     * @throws IOException
     */
    public static void unTarGz(String sourceFile, String outputDir) throws IOException {
        TarInputStream tarIn = null;
        File file = new File(sourceFile);
        try {
            tarIn = new TarInputStream(new GZIPInputStream(
                    new BufferedInputStream(new FileInputStream(file))),
                    1024 * 2);

            createDirectory(outputDir, null);//创建输出目录

            TarEntry entry = null;
            while ((entry = tarIn.getNextEntry()) != null) {

                if (entry.isDirectory()) {//是目录
                    entry.getName();
                    createDirectory(outputDir, entry.getName());//创建空目录
                } else {//是文件
                    File tmpFile = new File(outputDir + "/" + entry.getName());
                    createDirectory(tmpFile.getParent() + "/", null);//创建输出目录
                    OutputStream out = null;
                    try {
                        out = new FileOutputStream(tmpFile);
                        int length = 0;

                        byte[] b = new byte[2048];

                        while ((length = tarIn.read(b)) != -1) {
                            out.write(b, 0, length);
                        }

                    } catch (IOException ex) {
                        throw ex;
                    } finally {

                        if (out != null) {
                            out.close();
                        }
                    }
                }
            }
        } catch (IOException ex) {
            throw new IOException("解压归档文件出现异常", ex);
        } finally {
            try {
                if (tarIn != null) {
                    tarIn.close();
                }
            } catch (IOException ex) {
                throw new IOException("关闭tarFile出现异常", ex);
            }
        }
    }

    /**
     * 构建目录
     *
     * @param outputDir
     * @param subDir
     */
    public static void createDirectory(String outputDir, String subDir) {
        File file = new File(outputDir);
        if (!(subDir == null || subDir.trim().equals(""))) {//子目录不为空
            file = new File(outputDir + "/" + subDir);
        }
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.mkdirs();
        }
    }

    /**
     * 压缩文件
     *
     * @param sourceFolder 指定打包的源目录
     * @param tarGzPath    指定目标 tar 包的位置
     */
    public static void compress(String sourceFolder, String tarGzPath) {

        TarArchiveOutputStream tarOs = null;
        try {
            // 创建一个 FileOutputStream 到输出文件（.tar.gz）
            FileOutputStream fos = new FileOutputStream(tarGzPath);
            // 创建一个 GZIPOutputStream，用来包装 FileOutputStream 对象
            GZIPOutputStream gos = new GZIPOutputStream(new BufferedOutputStream(fos));
            // 创建一个 TarArchiveOutputStream，用来包装 GZIPOutputStream 对象
            tarOs = new TarArchiveOutputStream(gos);
            // 使文件名支持超过 100 个字节
            tarOs.setLongFileMode(TarArchiveOutputStream.LONGFILE_POSIX);
            File sourceFile = new File(sourceFolder);
            //遍历源目录的文件，将所有文件迁移到新的目录tarGzPath下
            File[] sources = sourceFile.listFiles();
            for (File oneFile : sources) {
                addFilesToTarGZ(oneFile.getPath(), "", tarOs);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                tarOs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param sourcePath 源文件
     * @param parent     源目录
     * @param tarArchive 压缩输出流
     * @throws IOException
     */
    public static void addFilesToTarGZ(String sourcePath, String parent, TarArchiveOutputStream tarArchive) throws IOException {
        File sourceFile = new File(sourcePath);
        // 获取新目录下的文件名称
        String fileName = parent.concat(sourceFile.getName());
        //打包压缩该文件
        tarArchive.putArchiveEntry(new TarArchiveEntry(sourceFile, fileName));
        if (sourceFile.isFile()) {
            FileInputStream fis = new FileInputStream(sourceFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            // 写入文件
            IOUtils.copy(bis, tarArchive);
            tarArchive.closeArchiveEntry();
            bis.close();
        } else if (sourceFile.isDirectory()) {
            // 因为是个文件夹，无需写入内容，关闭即可
            tarArchive.closeArchiveEntry();
            // 遍历文件夹下的文件
            for (File f : sourceFile.listFiles()) {
                // 递归遍历文件目录树
                addFilesToTarGZ(f.getAbsolutePath(), fileName + File.separator, tarArchive);
            }
        }
    }


    public static void changeFileName(String oldFileName, String newFileName) throws IOException {
        // 旧的文件或目录
        File oldName = new File(oldFileName);
        // 新的文件或目录
        File newName = new File(newFileName);
        if (newName.exists()) {  //  确保新的文件名不存在
            throw new java.io.IOException("file exists");
        }
        if (oldName.renameTo(newName)) {
            System.out.println("已重命名");
        } else {
            System.out.println("Error");
        }
    }
}
