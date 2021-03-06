package cn.eakay.commonservice.biz.common.fastdfspool;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

import java.util.UUID;

/**
 * PoolableTrackerClientFactory 用于管理池化对象的产生、激活、挂起、校验和销毁
 * Created by xugang on 16/4/14.
 */
@Slf4j
public class PoolableTrackerClientFactory implements PoolableObjectFactory {

    protected volatile ObjectPool pool = null;

    public PoolableTrackerClientFactory(ObjectPool pool) {
        this.pool = pool;
        pool.setFactory(this);
    }

    /**
     * pool.addObject时调
     * 属于池子内部回调工厂方法来创建并将对象放入池队列
     *
     * @return
     * @throws Exception
     */
    @Override
    public Object makeObject() throws Exception {
        //trackerClient含有一个TrackerGroup(static,可被多个trackerClient重复使用 使用时不需要考虑线程安全问题)
        TrackerClient trackerClient = new TrackerClient();
        //getConnection是线程安全的
        TrackerServer trackerServer = trackerClient.getConnection(); //use ClientGlobal and new TrackerServer and Socket
        StorageServer storageServer = null;
        StorageClient storageClient = new StorageClient(trackerServer, storageServer);
        PoolableFastDFSSource sourceDO = new PoolableFastDFSSource(pool, trackerClient, trackerServer, storageClient, storageServer);

        initializeSource(sourceDO);

        log.info("创建FastDFS连接对象准备放入池内:sourceDO={},numActive={}", sourceDO.getSourceId(), pool.getNumActive());
        return sourceDO;
    }

    /**
     * 初始化fast DSF 设置
     *
     * @param source
     * @throws Exception
     */
    protected void initializeSource(PoolableFastDFSSource source) throws Exception {
        //创建资源本地唯一ID 用于跟踪池对象重用情况 暂无业务使用
        source.setSourceId(UUID.randomUUID().toString());
    }

    /**
     * 销毁
     * returnObject -》addObjectToPool if(testOnReturn && !(_factory.validateObject(obj))) 时 并且注册了factory的 回调销毁方法
     *
     * @param obj
     * @throws Exception 抛出的任何异常 被commons-pool捕获
     */
    @Override
    public void destroyObject(Object obj) throws Exception {
        if (obj instanceof PoolableFastDFSSource) {
            //将资源close 否则回收失败下次还会掉
            ((PoolableFastDFSSource) obj).setClosed(true);
        }
        log.info("销毁池对象,sourceId={}", ((PoolableFastDFSSource) obj).getSourceId());
        //commons-pool _numActive 减一 并重新分配queue 此处不对obj做内存处理
    }

    /**
     * 验证有效性
     *
     * testOnBorrow=true:borrowObject调本方法验证这个对象是否有效,如果失败则这个对象会被丢弃
     *      java.util.NoSuchElementException: Could not create a validated object, cause: ValidateObject failed
     *
     * testOnReturn=ture:returnObject调本方法验证这个对象是否有效,如果失败则这个对象会被丢弃
     *
     * @param obj
     * @return
     */
    @Override
    public boolean validateObject(Object obj) {
        if (obj instanceof PoolableFastDFSSource) {
            try {
                validateConn((PoolableFastDFSSource) obj);
                return true;
            } catch (Exception e) {
                log.info("丢弃池对象,sourceId={}", ((PoolableFastDFSSource) obj).getSourceId());
                return false;
            }
        } else {
            return false;
        }
    }

    public void validateConn(PoolableFastDFSSource source) throws Exception {
        if (source.isClosed()) {
            throw new RuntimeException("validateConn error:PoolableFastDFSSource is closed");
        }
        source.validate();
    }

    /**
     * 钝化 将不用的pool对象进行钝化
     * returnObject回调此方法
     *
     * @param obj
     * @throws Exception
     */
    @Override
    public void passivateObject(Object obj) throws Exception {
        if (obj instanceof PoolableFastDFSSource) {
            final PoolableFastDFSSource source = ((PoolableFastDFSSource) obj);
            source.passivate();
        }
    }

    /**
     * 激活/初始化对象 对要使用的pool对象进行激活
     * borrowObject回调此方法
     *
     * @param obj
     * @throws Exception
     */
    @Override
    public void activateObject(Object obj) throws Exception {
        if (obj instanceof PoolableFastDFSSource) {
            final PoolableFastDFSSource source = ((PoolableFastDFSSource) obj);
            source.activate();
        }
    }

}
