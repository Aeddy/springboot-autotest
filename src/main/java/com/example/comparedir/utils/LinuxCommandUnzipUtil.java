package com.example.comparedir.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @description: linux命令执行服务类
 * @author: zhenqinl
 * @date: 2023/9/25 10:54
 */
@Slf4j
public class LinuxCommandUnzipUtil {

    public static void decomService(String directoryPath, String linuxCommand, String fileName) throws Exception {

        try {
            log.info("执行命令：{}", linuxCommand);
            ProcessBuilder processBuilder = new ProcessBuilder("/bin/sh", "-c",
                    linuxCommand + directoryPath + fileName + "-C" + directoryPath);
            Process process = processBuilder.start();
            int errCode = process.waitFor();
            log.info(String.format("finished tar local driver code2: %s", errCode));
            System.out.println("linux 命令执行成功！");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("linux 命令执行失败！");
        }
    }
}
