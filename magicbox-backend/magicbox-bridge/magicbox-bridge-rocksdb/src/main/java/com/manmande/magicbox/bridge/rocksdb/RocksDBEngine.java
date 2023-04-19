package com.manmande.magicbox.support.rocksdb;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.rocksdb.ColumnFamilyDescriptor;
import org.rocksdb.ColumnFamilyHandle;
import org.rocksdb.ColumnFamilyOptions;
import org.rocksdb.DBOptions;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;
import org.rocksdb.WriteBatch;
import org.rocksdb.WriteOptions;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@Slf4j
public class RocksDBEngine<T> {
    private final String DEFAULT_DB_PATH = new StringBuffer(System.getProperty("user.home"))
            .append(System.getProperty("file.separator"))
            .append(".rocksdb")
            .toString();
    private final String DEFAULT_TENANT = "@T@";
    private final String DEFAULT_GROUP = "@G@";
    private final String JOIN_CHAR = "-";
    private RocksDB rocksDB;
    private String dbPath;
    private boolean isReady;

    /**
     * 存放列族处理类
     */
    private ConcurrentHashMap<String, ColumnFamilyHandle> cfhMap = new ConcurrentHashMap<>();

    /**
     * 使用默认DB路径,进行初始化
     */
    public void init() {
        this.init(DEFAULT_DB_PATH);
    }

    /**
     * 指定DB路径,进行初始化
     * @param dbPath
     */
    public void init(String dbPath) {
        if (rocksDB != null) {
            return;
        }

        this.dbPath = dbPath;
        DBOptions options = new DBOptions().setCreateIfMissing(true);
        List<ColumnFamilyDescriptor> cfdList = this.getAllCfdList();
        List<ColumnFamilyHandle> cfhList = new ArrayList<>();

        try {
            if (cfdList.isEmpty()) {
                this.rocksDB = RocksDB.open(new Options().setCreateIfMissing(true), dbPath);
            } else {
                this.rocksDB = RocksDB.open(options, dbPath, cfdList, cfhList);
                for (ColumnFamilyHandle cfh : cfhList) {
                    cfhMap.put(new String(cfh.getName(), StandardCharsets.UTF_8), cfh);
                }
            }
        } catch (RocksDBException e) {
            log.error("RocksDB初始化失败,path={},error={}",dbPath, e.getMessage());
            this.rocksDB = null;
        }

        if (rocksDB != null) {
            log.info("RocksDB初始化成功");
            this.isReady = true;
        }
    }

    /**
     * 关闭
     */
    public void terminate() {
        this.rocksDB.close();
        this.isReady = false;
    }

    /**
     * 保存
     * @param tenant 租户
     * @param group 组
     * @param key
     * @param value
     * @return
     */
    public boolean save(String tenant, String group, String key, T value) {
        if (!this.isReady) {
            return false;
        }

        // 获取列族处理器
        ColumnFamilyHandle cfh = getColumnFamilyHandle(tenant, group);
        try {
            byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
            byte[] valueBytes = this.serialize(value);
            rocksDB.put(cfh, keyBytes, valueBytes);
        } catch (IOException | RocksDBException e) {
            log.error("保存数据失败, tenant={}, group={}, key={}, value={}", tenant, group, key, value);
            return false;
        }
        return true;
    }

    /**
     * 批量保存
     * @param tenant
     * @param group
     * @param map
     * @return
     */
    public boolean save(String tenant, String group, LinkedHashMap<String, Object> map) {
        if (!this.isReady) {
            return false;
        }
        // 获取列族处理器
        ColumnFamilyHandle cfh = getColumnFamilyHandle(tenant, group);
        WriteBatch writeBatch = new WriteBatch();
        try {
            for (String key : map.keySet()) {
                byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
                byte[] valueBytes = this.serialize(map.get(key));
                writeBatch.put(cfh, keyBytes, valueBytes);
            }
            rocksDB.write(new WriteOptions(), writeBatch);
        } catch (RocksDBException | IOException e) {
            log.error("批量保存数据失败, tenant={}, group={}, map={}", tenant, group, map);
            return false;
        }
        return true;
    }

    /**
     * 根据key获取缓存
     * @param tenant
     * @param group
     * @param keyList
     * @return
     */
    public List load(String tenant, String group, List<String> keyList) {
        List itemList = new ArrayList<>();

        // 获取列族处理器
        ColumnFamilyHandle cfh = getColumnFamilyHandle(tenant, group);
        List<byte[]> keyBytesList = keyList.stream().map(item -> item.getBytes(StandardCharsets.UTF_8)).collect(Collectors.toList());
        ColumnFamilyHandle[] cfhArray = new ColumnFamilyHandle[keyList.size()];
        Arrays.fill(cfhArray, cfh);
        try {
            List<byte[]> valueBytesList = rocksDB.multiGetAsList(Arrays.asList(cfhArray), keyBytesList);
            for (byte[] valueBytes : valueBytesList) {
                if (valueBytes != null) {
                    itemList.add(this.deserialize(valueBytes));
                }
            }
        } catch (RocksDBException | IOException | ClassNotFoundException e) {
            log.error("获取数据失败, tenant={}, group={}, keyList={}", tenant, group, keyList);
        }
        return itemList;
    }

    /**
     * 获取整个列族的缓存
     * @param tenant
     * @param group
     * @return
     */
    public List load(String tenant, String group) {
        List itemList = new ArrayList<>();
        // 获取列族处理器
        ColumnFamilyHandle cfh = getColumnFamilyHandle(tenant, group);

        try (final RocksIterator iterator = rocksDB.newIterator(cfh)) {
            for (iterator.seekToFirst(); iterator.isValid(); iterator.next()) {
                byte[] value = iterator.value();
                itemList.add(this.deserialize(value));
            }
        } catch (IOException | ClassNotFoundException e) {
            log.error("获取数据失败, tenant={}, group={}", tenant, group);
        }
        return itemList;
    }

    /**
     * 计算列族数量(键值对个数)
     * @param tenant
     * @param group
     * @return
     */
    public long count(String tenant, String group) {
        // 获取列族处理器
        ColumnFamilyHandle cfh = getColumnFamilyHandle(tenant, group);
        long size = 0;
        try {
            size = rocksDB.getLongProperty(cfh, "rocksdb.estimate-num-keys");
        } catch (RocksDBException e) {
            log.error("获取数量失败, tenant={}, group={}", tenant, group);
        }
        return size;
    }

    /**
     * 计算列族容量(字节)
     * @param tenant
     * @param group
     * @return
     */
    public long countVolume(String tenant, String group) {
        // 获取列族处理器
        ColumnFamilyHandle cfh = getColumnFamilyHandle(tenant, group);
        long size = rocksDB.getColumnFamilyMetaData(cfh).size();
        return size;
    }

    /**
     * 根据key删除
     * @param tenant
     * @param group
     * @param key
     * @return
     */
    public void remove(String tenant, String group, String key) {
        // 获取列族处理器
        ColumnFamilyHandle cfh = getColumnFamilyHandle(tenant, group);
        try {
            rocksDB.delete(cfh, key.getBytes(StandardCharsets.UTF_8) );
        } catch (RocksDBException e) {
            log.error("删除失败, tenant={}, group={}, key={}", tenant, group, key);
        }
    }

    /**
     * 删除某个列族的缓存
     * @param tenant
     * @param group
     * @return
     */
    public void remove(String tenant, String group) {
        // 获取列族处理器
        ColumnFamilyHandle cfh = getColumnFamilyHandle(tenant, group);
        try {
            cfhMap.remove(new String(cfh.getName(), StandardCharsets.UTF_8));
            rocksDB.dropColumnFamily(cfh);
        } catch (RocksDBException e) {
            log.error("删除列族失败, tenant={}, group={}", tenant, group);
        }
    }

    /**
     * 删除某个租户的缓存
     * @param tenant
     * @return
     */
    public void remove(String tenant) {
        for (String cfn : cfhMap.keySet()) {
            if (cfn.startsWith(tenant.concat(JOIN_CHAR))) {
                // 获取列族处理器
                ColumnFamilyHandle cfh = cfhMap.get(cfn);
                try {
                    cfhMap.remove(cfn);
                    rocksDB.dropColumnFamily(cfh);
                } catch (RocksDBException e) {
                    log.error("删除列族失败, cfn", cfn);
                }
            }
        }
    }

    /**
     * 删除所有租户的缓存
     * @return
     */
    public void removeAll() {
        for (String cfn : cfhMap.keySet()) {
            // 获取列族处理器
            ColumnFamilyHandle cfh = cfhMap.get(cfn);
            try {
                cfhMap.remove(cfn);
                rocksDB.dropColumnFamily(cfh);
            } catch (RocksDBException e) {
                log.error("删除列族失败, cfn", cfn);
            }
        }
    }

    /**
     * 获取所有列族Descriptor
     * @return
     */
    public List<ColumnFamilyDescriptor> getAllCfdList() {
        List<ColumnFamilyDescriptor> cfdList = new ArrayList<>();
        Options options = new Options().setCreateIfMissing(true);
        try {
            List<byte[]> cfnList = RocksDB.listColumnFamilies(options, this.dbPath);
            for (byte[] name : cfnList) {
                cfdList.add(new ColumnFamilyDescriptor(name, new ColumnFamilyOptions()));
            }
        } catch (RocksDBException e) {
            log.error("RocksDB获取全部ColumnFamilyDescriptor失败,path={},error={}",this.dbPath, e.getMessage());
        }
        return cfdList;
    }

    /**
     * 获取所有列族名称
     * @param tenant
     * @param group
     * @return
     */
    public String getColumnFamilyName(String tenant, String group) {
        String opTenant = StrUtil.isBlank(tenant) ? DEFAULT_TENANT : tenant;
        String opGroup = StrUtil.isBlank(group) ? DEFAULT_GROUP : group;
        // 确定列族名
        String cfn = opTenant.concat(JOIN_CHAR).concat(opGroup);
        return cfn;
    }

    /**
     * 获取列族处理器
     * @param tenant 租户
     * @param group 组
     * @return
     */
    public ColumnFamilyHandle getColumnFamilyHandle(String tenant, String group) {
        ColumnFamilyHandle cfh = null;
        String name = this.getColumnFamilyName(tenant, group);
        if (this.cfhMap.containsKey(name)) {
            cfh = this.cfhMap.get(name);
        } else {
            // 新建列族
            ColumnFamilyDescriptor cfd = new ColumnFamilyDescriptor(name.getBytes(StandardCharsets.UTF_8), new ColumnFamilyOptions());
            try {
                cfh = this.rocksDB.createColumnFamily(cfd);
                this.cfhMap.put(name, cfh);
            } catch (RocksDBException e) {
                log.error("列族={}, 创建失败, message={}", name, e.getMessage());
            }
        }
        return cfh;
    }

    /**
     * 序列化
     * @param obj
     * @return
     * @throws IOException
     */
    public byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(obj);
        return out.toByteArray();
    }

    /**
     * 反序列化
     * @param bytes
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(in);
        return ois.readObject();
    }
}
