package cn.eakay.commonservice.biz.service.impl;

import cn.eakay.commons.base.CommonErrorEnum;
import cn.eakay.commons.base.performance.annotation.Performance;
import cn.eakay.commonservice.biz.manager.FastDFSFileManager;
import cn.eakay.commonservice.biz.manager.FileManager;
import cn.eakay.commonservice.biz.common.errorcode.FileErrorEnum;
import cn.eakay.commonservice.biz.service.FileOptService;
import cn.eakay.commonservice.client.dataobject.FastDFSFileDO;
import cn.eakay.commonservice.client.dataobject.FileDO;
import cn.eakay.commonservice.client.result.FileOptResultDO;
import cn.eakay.commonservice.client.result.FileUploadResultDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.csource.fastdfs.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

/**
 * Created by xugang on 16/4/14.
 */
@Slf4j
@Service
public class FileOptServiceImpl implements FileOptService {


    public String trackerServerAddr;

    @Autowired
    private FastDFSFileManager fastDFSFileManager;
    @Autowired
    private FileManager fileManager;

    @Performance(desc = "根据id获取单个订单")
    @Override
    public FileOptResultDO uploadFile(FastDFSFileDO fastDFSFileDO) {
        FileOptResultDO rs = new FileOptResultDO();

        try {
            //1.上传到fast dfs server
            if (fastDFSFileDO.getContent() == null) {
                log.error("上传文件失败,文件为空:filename={}", fastDFSFileDO.getName());
                FileErrorEnum.FILE_EMPTY_ERROR.fillResult(rs);
                return rs;
            }

            FileUploadResultDO fileUploadResultDO = fastDFSFileManager.uploadFast(fastDFSFileDO);
            if (fileUploadResultDO.isFailure()) {
                rs.setErrorCode(fileUploadResultDO.getErrorCode());
                rs.setErrorMsg(fileUploadResultDO.getErrorMsg());
                return rs;
            }

            //2.入库 filename,biz,dfsfilename
            fastDFSFileDO.setBiz(null);
            fastDFSFileDO.setGroupName(fileUploadResultDO.getGroupName());
            fastDFSFileDO.setRemoteFileName(fileUploadResultDO.getRemoteFileName());
            fileManager.addFileOne(fastDFSFileDO);

            FileDO fileDO = new FileDO();
            fileDO.setGroupName(fileUploadResultDO.getGroupName());
            fileDO.setRemoteFileName(fileUploadResultDO.getRemoteFileName());
            fileDO.setSourceIpAddr(trackerServerAddr);
            rs.setFileDO(fileDO);
        } catch (Exception e) {
            FileErrorEnum.FILE_UPLOAD_ERROR.fillResult(rs);
            log.error("上传文件失败,filename:" + fastDFSFileDO.getName(), e);
        }
        return rs;
    }

    @Override
    public FileOptResultDO getFile(String groupName, String remoteFileName) {
        FileOptResultDO rs = new FileOptResultDO();

        if (StringUtils.isEmpty(groupName) || StringUtils.isEmpty(remoteFileName)) {
            log.error("获取文件失败,参数为空错误:groupName={},remoteFileName", groupName, remoteFileName);
            CommonErrorEnum.PARAM_EMPTY_ERROR.fillResult(rs);
            return rs;
        }

        try {
            //1.先从本地库里查询文件 如果不存在直接返回fail
            FileDO fileDO = fileManager.findFileOne(groupName, remoteFileName);
            if (fileDO == null) {
                log.error("从本地库里查询不到文件:groupName={},remoteFileName", groupName, remoteFileName);
                FileErrorEnum.FILE_DB_NOT_EXISTS_ERROR.fillResult(rs);
                return rs;
            }

            //2.如果文件存在库中 从远程获取文件内容
            FileInfo fileInfo = fastDFSFileManager.getFileFast(groupName, remoteFileName);
            if (fileInfo == null) {
                log.error("在fastDFS上未找到文件内容:groupName={},remoteFileName", groupName, remoteFileName);
                FileErrorEnum.FILE_CONTEXT_NOT_FOUND_ERROR.fillResult(rs);
                return rs;
            }

            rs.setFileDO(fileDO);
        } catch (Exception e) {
            FileErrorEnum.FILE_FETCH_ERROR.fillResult(rs);
            log.error("上传获取失败,groupName={},remoteFileName:", groupName, remoteFileName, e);
        }
        return rs;
    }

    @Override
    public FileOptResultDO deleteFile(Long id, String groupName, String remoteFileName) {
        FileOptResultDO rs = new FileOptResultDO();

        if (id == null || StringUtils.isEmpty(groupName) || StringUtils.isEmpty(remoteFileName)) {
            log.error("删除文件失败,参数为空错误:id={},groupName={},remoteFileName", new Object[]{id, groupName, remoteFileName});
            CommonErrorEnum.PARAM_EMPTY_ERROR.fillResult(rs);
            return rs;
        }

        try {
            //1.先从本地库里查询文件 如果不存在直接返回fail
            FileDO fileDO = fileManager.findFileById(id);
            if (fileDO == null) {
                log.error("从本地库里查询不到要删除的文件:groupName={},remoteFileName", groupName, remoteFileName);
                FileErrorEnum.FILE_DB_NOT_EXISTS_ERROR.fillResult(rs);
                return rs;
            }

            //2.如果文件存在库中 删除库和远程server file
            fastDFSFileManager.deleteFileFast(groupName, remoteFileName);
            fileManager.deleteFileById(id);
            rs.setFileDO(fileDO);
        } catch (Exception e) {
            FileErrorEnum.FILE_FETCH_ERROR.fillResult(rs);
            log.error("上传获取失败,groupName={},remoteFileName:", groupName, remoteFileName, e);
        }
        return rs;
    }
}