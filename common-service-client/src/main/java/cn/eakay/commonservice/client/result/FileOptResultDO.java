package cn.eakay.commonservice.client.result;

import cn.eakay.commons.base.ResultDO;
import cn.eakay.commonservice.client.dataobject.FileDO;
import lombok.Data;

/**
 * 文件操作结果数据对象
 * @author xugang
 */
@Data
public class FileOptResultDO extends ResultDO {
	private static final long serialVersionUID = 339308308497129808L;

	private FileDO fileDO;

}
