package cn.eakay.commonservice.client.dataobject;

import cn.eakay.commons.base.BaseDO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * 通用文件数据对象
 *
 * @author xugang
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true, value = {"createTime", "updateTime"})
public class FileDO extends BaseDO {
    private static final long serialVersionUID = 3721716059117679633L;

    protected String sourceIpAddr;
    protected long fileSize;
    protected int crc32;
    private String name;
    private String groupName;
    private String remoteFileName;
    private Integer biz;
    private Integer key;
    private Long keyId;
}
