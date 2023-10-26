package com.example.comparedir.controller;

import com.example.comparedir.utils.LinuxCommandUnzipUtil;
import com.example.comparedir.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 文件linux命令解压测试控制器
 * @author: zhenqinl
 * @date: 2023/9/25 10:57
 */
@RestController
@Slf4j
public class DecomFileController {

    @GetMapping("/decom")
    public ResultVo decomFile(@RequestParam String directoryPath, @RequestParam String linuxCommand, @RequestParam String fileName) {

        try {
            LinuxCommandUnzipUtil.decomService(directoryPath, linuxCommand, fileName);
            log.info("linux 命令执行成功！");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("linux 命令执行失败！");
            return ResultVo.failure("linux 命令执行失败！");
        }
        return ResultVo.success("linux 命令执行成功！");
    }
}
