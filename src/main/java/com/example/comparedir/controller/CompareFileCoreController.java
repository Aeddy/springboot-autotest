package com.example.comparedir.controller;

import com.example.comparedir.utils.DiffHandleUtils;
import com.example.comparedir.utils.TestFolderCompare;
import com.example.comparedir.vo.ContentDiffVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.io.IOUtils.toByteArray;

/**
 * @description: Myers Diff 差分算法
 * @author: zhenqinl
 * @date: 2023/9/20 9:44
 */
@RestController
@Slf4j
@CrossOrigin
public class CompareFileCoreController {

    @Resource
    private TestFolderCompare testFolderCompare;

    @GetMapping("/diff")
    public void diff(HttpServletResponse response, @RequestParam(required = false) String serviceName, @RequestParam(required = false) String sourceFileRouter) throws IOException {

        sourceFileRouter = sourceFileRouter.replace("/", "\\");
        String fileOriginal = "D:\\work code\\ackage\\" + serviceName + "\\source" + sourceFileRouter;
        String fileRevised = "D:\\work code\\ackage\\" + serviceName + "\\target" + sourceFileRouter;

        List<String> diffString = DiffHandleUtils.diffString(fileOriginal, fileRevised);
        //在 D:\diff\ 目录下生成一个 diff.html 文件，打开便可看到两个文件的对比
        String droducDirPath = "D:\\work code\\ackage\\" + serviceName + "\\content-diff\\";
        DiffHandleUtils.generateDiffHtml(diffString, droducDirPath);

        //把所需的 js和 css 从 resource 资源目录复制到 droducDirPath 目录下
        //FileCoypUtils.copyfile(droducDirPath);
        System.out.println("对比完成，请打开 " + droducDirPath + "\\contentDiff.html 查看");

        //response.setContentType("multipart/form-data");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        // 2.设置文件头：最后一个参数是设置下载文件名
        response.setHeader("Content-Disposition", "attachment;fileName=" + "contentDiff.html");
        FileInputStream fis = new FileInputStream(new File(droducDirPath + "\\contentDiff.html"));
        response.getOutputStream().write(toByteArray(fis));
        response.getOutputStream().flush();
        response.getOutputStream().close();
        fis.close();//使用流之后一定要记得close，避免资源占用。
        //return "对比完成，请打开 " + droducDirPath + "\\diff.html 查看";
    }


    @RequestMapping("/contentDiffList")
    public Map<String, Object> userList(@RequestParam Integer pageNum, @RequestParam Integer pageSize) throws IOException {
        Map<String, Object> map = new HashMap<>(2);
        List<ContentDiffVo> contentDiffAllList = testFolderCompare.contentDiffList();
        List<ContentDiffVo> contentDiffList = new ArrayList<>();
        int start = pageNum * pageSize;
        int end = start + pageSize;
        int totalNum = 0;
        if (!CollectionUtils.isEmpty(contentDiffAllList)) {
            totalNum = contentDiffAllList.size();
            if (contentDiffAllList.size() > start) {
                if (contentDiffAllList.size() >= end) {
                    contentDiffList = contentDiffAllList.subList(start, end);
                } else {
                    contentDiffList = contentDiffAllList.subList(start, totalNum);
                }
            }
        }
        contentDiffList.forEach(c ->{
            c.setFilename(c.getFilename().replace("\\", "/"));
        });
        map.put("contentDiffList", contentDiffList);
        map.put("TotalNum", totalNum);
        return map;
    }
}
