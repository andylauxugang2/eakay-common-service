package cn.eakay.commonservice.biz.manager.impl;


import cn.eakay.commonservice.biz.common.errorcode.FileErrorEnum;
import cn.eakay.commonservice.biz.common.exception.FileOptException;
import cn.eakay.commonservice.biz.common.fastdfspool.FastDFSSource;
import cn.eakay.commonservice.biz.manager.FastDFSFileManager;
import cn.eakay.commonservice.biz.common.fastdfspool.PoolableFastDFSSource;
import cn.eakay.commonservice.client.common.constant.BizEnum;
import cn.eakay.commonservice.client.common.constant.CommonServiceConstant;
import cn.eakay.commonservice.client.dataobject.FastDFSFileDO;
import cn.eakay.commonservice.client.result.FileUploadResultDO;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by xugang on 16/4/13.
 */
@Slf4j
@Component
public class FastDFSFileManagerImpl implements FastDFSFileManager {
    public static final String PROTOCOL = "http://";
    public static final String SEPARATOR = "/";
    public static final String TRACKER_NGNIX_PORT = "";

    @Autowired
    @Setter
    private FastDFSSource fastDFSSource;

    @Override
    public FileUploadResultDO uploadFast(FastDFSFileDO file) {

        PoolableFastDFSSource poolableFastDFSSource = null;
        FileUploadResultDO resultDO = new FileUploadResultDO();
        String group = BizEnum.getEnumByCode(file.getBiz()).getGroup();
        try {
            handleFile(file);
            //getFastDFSSource调用一次会addObject一次到pool
            poolableFastDFSSource = fastDFSSource.getFastDFSSource();
            log.info("从fast对象池中获取一个Source:id={}", poolableFastDFSSource.getSourceId());
            StorageClient storageClient = poolableFastDFSSource.getStorageClient();
            TrackerServer trackerServer = poolableFastDFSSource.getTrackerServer();

            log.info("begin Upload File Name:{},File Length:{},Group:{}", file.getName(), file.getContent().length, group);

            NameValuePair[] meta_list = new NameValuePair[3];
            meta_list[0] = new NameValuePair("width", file.getWidth());
            meta_list[1] = new NameValuePair("heigth", file.getHeight());
            meta_list[2] = new NameValuePair("author", file.getAuthor());

            long startTime = System.currentTimeMillis();

            String[] uploadResults = storageClient.upload_file(group, file.getContent(), file.getExt(), meta_list);

            log.info("upload_file time used:" + (System.currentTimeMillis() - startTime) + " ms");

            if (uploadResults == null) {
                log.error("upload file fail, error code: " + storageClient.getErrorCode());
                FileErrorEnum.FILE_UPLOAD_ERROR.fillResult(resultDO);
                return resultDO;
            }

            String groupName = uploadResults[0];
            String remoteFileName = uploadResults[1];

            resultDO.setGroupName(uploadResults[0]);
            resultDO.setRemoteFileName(uploadResults[1]);
            resultDO.setSourceIpAddr(CommonServiceConstant.IMG_SERVER_ADDRESS);

            log.info("upload file successfully," + "group_name: " + groupName + ", remoteFileName:"
                    + " " + remoteFileName);
            if (log.isDebugEnabled()) {
                String fileAbsolutePath = PROTOCOL + trackerServer.getInetSocketAddress().getHostName()
                        + SEPARATOR
                        + TRACKER_NGNIX_PORT
                        + SEPARATOR
                        + groupName
                        + SEPARATOR
                        + remoteFileName;
                log.info("upload file remote url:", fileAbsolutePath);
            }
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                e.printStackTrace();
            }
            FileErrorEnum.FILE_UPLOAD_ERROR.fillResult(resultDO);
            log.error("occur Exception when uploadind the file: " + file.getName() + ",group=" + group, e);
        } finally {
            try {
                if (poolableFastDFSSource != null) poolableFastDFSSource.close();
            } catch (Exception e) {
                resultDO.setSuccess(false);
                resultDO.setErrorMsg("poolableFastDFSSource object pool close failed");
                log.error("poolableFastDFSSource object pool close failed,filename={}", file.getName());
            }
        }
        return resultDO;
    }

    private void handleFile(FastDFSFileDO file) {
        if (file == null) {
            throw new IllegalStateException("文件file不能为空");
        }
        String fileName = file.getName();

        if (StringUtils.isEmpty(fileName)) {
            throw new IllegalStateException("文件fileName获取不到");
        }

        if (StringUtils.isEmpty(file.getExt())) {
            file.setExt(fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()));
        }
    }

    @Override
    public FileInfo getFileFast(String groupName, String remoteFileName) throws FileOptException {

        PoolableFastDFSSource poolableFastDFSSource = null;

        try {
            poolableFastDFSSource = fastDFSSource.getFastDFSSource();
            StorageClient storageClient = poolableFastDFSSource.getStorageClient();

            FileInfo fileInfo = storageClient.get_file_info(groupName, remoteFileName);
            return fileInfo;
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileOptException(e);
        } finally {
            try {
                if (poolableFastDFSSource != null) poolableFastDFSSource.close();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void deleteFileFast(String groupName, String remoteFileName) throws Exception {
        PoolableFastDFSSource poolableFastDFSSource = null;

        try {
            poolableFastDFSSource = fastDFSSource.getFastDFSSource();
            StorageClient storageClient = poolableFastDFSSource.getStorageClient();

            storageClient.delete_file(groupName, remoteFileName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileOptException(e);
        } finally {
            try {
                if (poolableFastDFSSource != null) poolableFastDFSSource.close();
            } catch (Exception e) {
            }
        }
    }
}
