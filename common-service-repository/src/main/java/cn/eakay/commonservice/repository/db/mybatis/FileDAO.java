package cn.eakay.commonservice.repository.db.mybatis;

import cn.eakay.commonservice.client.dataobject.FileDO;
import org.apache.ibatis.annotations.Param;

/**
 * @created by xugang on 16/4/7.
 */
public interface FileDAO {
    void insertOne(FileDO fileDO);

    FileDO queryByBizAndKeyAndKeyId(@Param("biz") Integer biz, @Param("key") Integer key, @Param("keyId") Long keyId);

    FileDO queryById(Long id);

    void deleteById(Long id);

    void updateOne(FileDO fileDO);
}