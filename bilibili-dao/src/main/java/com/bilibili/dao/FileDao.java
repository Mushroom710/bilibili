package com.bilibili.dao;

import com.bilibili.domain.File;
import org.apache.ibatis.annotations.Mapper;


// @date 2023/5/3
// @time 17:12
// @author zhangzhi
// @description
@Mapper
public interface FileDao {
    File getFileByMD5(String fileMD5);

    Integer addFile(File dbFileMD5);
}
