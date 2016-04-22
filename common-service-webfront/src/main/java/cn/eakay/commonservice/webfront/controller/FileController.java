package cn.eakay.commonservice.webfront.controller;

import cn.eakay.commonservice.biz.service.FileOptService;
import cn.eakay.commonservice.client.dataobject.FastDFSFileDO;
import cn.eakay.commonservice.client.dataobject.FileDO;
import cn.eakay.commonservice.client.result.FileOptResultDO;
import cn.eakay.commonservice.webfront.base.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xugang on 16/4/7.
 *
 * @link http://websystique.com/springmvc/spring-mvc-4-restful-web-services-crud-example-resttemplate/
 */
@Slf4j
@RestController
@RequestMapping(value = "/file")
public class FileController extends BaseController {
    @Autowired
    private FileOptService fileOptService;

    /**
     * 上传文件
     * 支持批量
     *
     * @param uploadFiles
     * @param biz
     * @param key
     * @param keyId
     * @return
     */
    @RequestMapping(value = "/uploads", method = RequestMethod.POST)
    public ResponseEntity<List<FileOptResultDO>> uploadFileBatch(@RequestParam("uploadfiles") CommonsMultipartFile[] uploadFiles,
                                                                 @RequestParam("biz") Integer biz,
                                                                 @RequestParam("key") Integer key,
                                                                 @RequestParam("keyId") Long keyId) {
        if (uploadFiles.length == 0) {
            log.error("uploads file error:{}", "param uploadfiles is empty");
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("begin uploading files " + uploadFiles.length);

        List<FileOptResultDO> result = new ArrayList<>();
        boolean failedItem = false;
        for (int i = 0; i < uploadFiles.length; i++) {
            CommonsMultipartFile multipartFile = uploadFiles[i];

            FastDFSFileDO fastDFSFileDO = new FastDFSFileDO();
            try {
                InputStream is = multipartFile.getInputStream();
                byte[] fileData = new byte[(int) multipartFile.getSize()];
                is.read(fileData);
                fastDFSFileDO.setName(multipartFile.getOriginalFilename());
                fastDFSFileDO.setContent(fileData);
                fastDFSFileDO.setBiz(biz);
                fastDFSFileDO.setKey(key);
                fastDFSFileDO.setKeyId(keyId);

                //fileOptService.uploadFile不会抛异常
                FileOptResultDO fileOptResultDO = fileOptService.uploadFile(fastDFSFileDO);

                if (!fileOptResultDO.isSuccess()) {
                    failedItem = true;
                    log.error("uploading file failed error:{}", fileOptResultDO.getErrorCode() + fileOptResultDO.getErrorMsg());
                }

                //客户端需要遍历结果集 获取失败的上传item
                result.add(fileOptResultDO);
            } catch (Exception e) {
                if (log.isDebugEnabled()) {
                    e.printStackTrace();
                }
                log.error("upload file failed. Exception:", e);
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        if(failedItem){
            return new ResponseEntity<List<FileOptResultDO>>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<FileOptResultDO>>(result, HttpStatus.CREATED);
    }

    /**
     * 获取文件
     *
     * @param biz
     * @param key
     * @param keyId
     * @return
     */
    @RequestMapping(value = "fetch/{biz}/{key}/{keyId}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<FileOptResultDO> fetchingFile(@PathVariable("biz") Integer biz,
                                                        @PathVariable("key") Integer key,
                                                        @PathVariable("keyId") Long keyId) {

        log.info("Fetching File with biz={},key={},keyId={}", new Object[]{biz, key, keyId});
        FileDO fileDO = new FileDO();
        fileDO.setBiz(biz);
        fileDO.setKey(key);
        fileDO.setKeyId(keyId);
        FileOptResultDO resultDO = fileOptService.getFile(fileDO);

        if (!resultDO.isSuccess()) {
            log.error("Fetching File failed,biz={},key={},keyId={},error={}", new Object[]{biz, key, keyId, resultDO.getErrorCode() + resultDO.getErrorMsg()});
            return new ResponseEntity<FileOptResultDO>(resultDO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<FileOptResultDO>(resultDO, HttpStatus.OK);
    }

    /**
     * 删文件
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<FileOptResultDO> deleteFile(@PathVariable("id") Long id) {

        log.info("Fetching & delete file with fileId={}", id);

        FileOptResultDO resultDO = fileOptService.deleteFile(id);
        if (!resultDO.isSuccess()) {
            log.error("delete file failed:fileId={}, error={}", new Object[]{id, resultDO.getErrorCode() + resultDO.getErrorMsg()});
            return new ResponseEntity<FileOptResultDO>(resultDO, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<FileOptResultDO>(resultDO, HttpStatus.NO_CONTENT);

    }
}