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
import org.perf4j.aop.Profiled;
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

    @Autowired
    private FastDFSFileManager fastDFSFileManager;
    @Autowired
    private FileManager fileManager;

    @Profiled(tag = "FileOptService#uploadFile({$0.name},{$0.biz},{$0.key})") // logger default org.perf4j.TimingLogger
    @Override
    public FileOptResultDO uploadFile(FastDFSFileDO fastDFSFileDO) {
        FileOptResultDO rs = new FileOptResultDO();

        try {
            //1.上传到fast dfs server
            if (fastDFSFileDO.getContent() == null || fastDFSFileDO.getContent().length == 0) {
                log.error("上传文件失败,文件为空:filename={}", fastDFSFileDO.getName());
                FileErrorEnum.FILE_EMPTY_ERROR.fillResult(rs);
                return rs;
            }

            FileUploadResultDO fileUploadResultDO = fastDFSFileManager.uploadFast(fastDFSFileDO);
            if (!fileUploadResultDO.isSuccess()) {
                rs.setErrorCode(fileUploadResultDO.getErrorCode());
                rs.setErrorMsg(fileUploadResultDO.getErrorMsg());
                return rs;
            }

            fastDFSFileDO.setGroupName(fileUploadResultDO.getGroupName());
            fastDFSFileDO.setRemoteFileName(fileUploadResultDO.getRemoteFileName());
            fastDFSFileDO.setSourceIpAddr(fileUploadResultDO.getSourceIpAddr());

            //2.入库 filename,biz,dfsfilename
            //查询是否存在 如果存在则更新 数据库唯一性索引保证 并发问题直接抛异常 插入失败
            /*FileDO fileDO = fileManager.findFileOne(fastDFSFileDO.getBiz(), fastDFSFileDO.getKey(), fastDFSFileDO.getKeyId());
            if (fileDO == null) {
                fileManager.addFileOne(fastDFSFileDO);
            } else {
                fastDFSFileDO.setId(fileDO.getId());
                fileManager.updateFileOne(fastDFSFileDO);
            }*/

            FileDO fileDO = buildFileDO(fastDFSFileDO);
            rs.setFileDO(fileDO);
        } catch (Exception e) {
            FileErrorEnum.FILE_UPLOAD_ERROR.fillResult(rs);
            log.error("上传文件失败,filename:" + fastDFSFileDO.getName(), e);
        }
        return rs;
    }

    //build FileDO
    private FileDO buildFileDO(FastDFSFileDO fastDFSFileDO) {
        FileDO fileDO = new FileDO();
        fileDO.setId(fastDFSFileDO.getId());
        fileDO.setName(fastDFSFileDO.getName());
        fileDO.setGroupName(fastDFSFileDO.getGroupName());
        fileDO.setRemoteFileName(fastDFSFileDO.getRemoteFileName());
        fileDO.setFileSize(fastDFSFileDO.getFileSize());
        fileDO.setBiz(fastDFSFileDO.getBiz());
        fileDO.setKey(fastDFSFileDO.getKey());
        fileDO.setKeyId(fastDFSFileDO.getKeyId());
        fileDO.setSourceIpAddr(fastDFSFileDO.getSourceIpAddr());
        return fileDO;
    }

    @Profiled(tag = "FileOptService#getFile({$0.biz},{$0.key},{$0.keyId})")
    @Override
    public FileOptResultDO getFile(FileDO fileDO) {
        FileOptResultDO rs = new FileOptResultDO();

        if (fileDO == null) {
            log.error("获取文件失败,参数为空错误:fileDO");
            CommonErrorEnum.PARAM_EMPTY_ERROR.fillResult(rs);
            return rs;
        }

        Integer biz = fileDO.getBiz();
        Integer key = fileDO.getKey();
        Long keyId = fileDO.getKeyId();

        try {
            //1.先从本地库里查询文件 如果不存在直接返回fail
            fileDO = fileManager.findFileOne(biz, key, keyId);
            if (fileDO == null) {
                log.error("从本地库里查询不到文件:biz={},key={},keyId={}", new Object[]{biz, key, keyId});
                FileErrorEnum.FILE_DB_NOT_EXISTS_ERROR.fillResult(rs);
                return rs;
            }

            //2.如果文件存在库中 从远程获取文件信息
            String groupName = fileDO.getGroupName();
            String remoteFileName = fileDO.getRemoteFileName();
            FileInfo fileInfo = fastDFSFileManager.getFileFast(groupName, remoteFileName);
            if (fileInfo == null) {
                log.error("在fastDFS上未找到文件内容:groupName={},remoteFileName", groupName, remoteFileName);
                FileErrorEnum.FILE_CONTEXT_NOT_FOUND_ERROR.fillResult(rs);
                return rs;
            }

            fileDO.setSourceIpAddr(fileInfo.getSourceIpAddr());
            rs.setFileDO(fileDO);
        } catch (Exception e) {
            FileErrorEnum.FILE_FETCH_ERROR.fillResult(rs);
            log.error("获取文件失败,biz={},key={},keyId={}", new Object[]{biz, key, keyId}, e);
        }
        return rs;
    }

    @Profiled(tag = "FileOptService#deleteFile({$0})")
    @Override
    public FileOptResultDO deleteFile(Long id) {
        FileOptResultDO rs = new FileOptResultDO();

        if (id == null) {
            log.error("删除文件失败,参数为空错误:id");
            CommonErrorEnum.PARAM_EMPTY_ERROR.fillResult(rs);
            return rs;
        }

        try {
            //1.先从本地库里查询文件 如果不存在直接返回fail
            FileDO fileDO = fileManager.findFileById(id);
            if (fileDO == null) {
                log.error("从本地库里查询不到要删除的文件:id", id);
                FileErrorEnum.FILE_DB_NOT_EXISTS_ERROR.fillResult(rs);
                return rs;
            }

            //2.如果文件存在库中 删除库和远程server file
            String groupName = fileDO.getGroupName();
            String remoteFileName = fileDO.getRemoteFileName();
            //先删db 再删远程
            fileManager.deleteFileById(id);
            fastDFSFileManager.deleteFileFast(groupName, remoteFileName);
            rs.setFileDO(fileDO);
            log.info("删除文件成功,groupName={},remoteFileName={},name={}", new Object[]{fileDO.getGroupName(), fileDO.getRemoteFileName(), fileDO.getName()});
        } catch (Exception e) {
            FileErrorEnum.FILE_FETCH_ERROR.fillResult(rs);
            log.error("删除文件失败,id={}", id, e);
        }
        return rs;
    }
}