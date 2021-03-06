package cn.eakay.commonservice.biz.manager;

import cn.eakay.commons.dao.persistence.exception.DAOException;
import cn.eakay.commonservice.client.dataobject.FileDO;

/**
 * File 业务逻辑组件
 * 入库File
 * Created by xugang on 16/4/14.
 */
public interface FileManager {
    void addFileOne(FileDO fileDO) throws DAOException;

    FileDO findFileOne(Integer biz, Integer key, Long keyId) throws DAOException;

    FileDO findFileById(Long id) throws DAOException;

    void deleteFileById(Long id) throws DAOException;

    //TODO 需要添加 updateOption 参数
    void updateFileOne(FileDO fileDO);
}
