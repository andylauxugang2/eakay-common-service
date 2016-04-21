package cn.eakay.commonservice.biz.service;

import cn.eakay.commonservice.client.dataobject.FastDFSFileDO;
import cn.eakay.commonservice.client.dataobject.FileDO;
import cn.eakay.commonservice.client.result.FileOptResultDO;

/**
 * 文件上传和下载等操作接口
 *
 * @author xugang
 */
public interface FileOptService {

    /**
     * 上传文件
     * @param fastDFSFileDO
     * @return
     */
    FileOptResultDO uploadFile(FastDFSFileDO fastDFSFileDO);

    /**
     * 查询文件
     * @param fileDO
     * @return
     */
    FileOptResultDO getFile(FileDO fileDO);

    /**
     * 删除文件
     * @param id file id
     * @return
     */
    FileOptResultDO deleteFile(Long id);

}
