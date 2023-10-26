package com.example.comparedir.core;

import com.example.comparedir.utils.DownloadUtil;
import com.example.comparedir.utils.TarUtil;

import java.io.IOException;

/**
 * @description:
 * @author: zhenqinl
 * @date: 2023/9/19 15:48
 */
public class TarCoreService {


    public static void unTarCore() throws IOException {

        //下载文件
        DownloadUtil.downTar();

        //解压文件
        TarUtil.unTar();
    }
}
