package com.bilibili.api;

import com.bilibili.domain.JsonResponse;
import com.bilibili.service.FileService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

// @date 2023/5/3
// @time 17:09
// @author zhangzhi
// @description 文件上传

@RestController
public class FileApi {
    @Resource
    private FileService fileService;

    // 获取文件的唯一标识
    @PostMapping("/md5files")
    public JsonResponse<String> getFileMD5(MultipartFile file) throws Exception{
        String fileMD5 = fileService.getFileMD5(file);
        return new JsonResponse<>(fileMD5);
    }

    // 分片文件上传 - 断点续传
    @PutMapping("/file-slices")
    public JsonResponse<String> uploadFileBySlices(MultipartFile slice,
                                                   String fileMd5,
                                                   Integer sliceNo,
                                                   Integer totalSliceNo) throws Exception{
        String filePath = fileService.uploadFileBySlices(slice, fileMd5, sliceNo, totalSliceNo);
        return new JsonResponse<>(filePath);
    }

    // TODO 文件删除 -- 删除文件的同时，还需要删除表 t_file 对应的记录
    @DeleteMapping("/delete-files")
    public JsonResponse<String> deleteFile(@RequestParam String filePath){
        fileService.deleteFile(filePath);
        return JsonResponse.success();
    }
}
