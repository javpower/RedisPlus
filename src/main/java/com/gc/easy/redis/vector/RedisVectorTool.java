package com.gc.easy.redis.vector;

import org.redisson.api.RMap;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RedisVectorTool {

    private final RedissonClient redisson;
    private final RScript script;

    public RedisVectorTool(RedissonClient redisson) {
        this.redisson = redisson;
        this.script = redisson.getScript();
    }

    // 创建向量索引
    public void createVectorIndex(String indexName, List<FieldSchema> fields) {
        String schema = fields.stream()
                .map(field -> field.getName() + " " + field.getType().name() +
                        (field.getDataType() != null ? (" " + field.getDataType().name() + " " + field.getDimension() + " DIM ") : ""))
                .collect(Collectors.joining(", "));

        String luaScript = "return redis.call('FT.CREATE', '" + indexName + "', 'ON', 'HASH', 'SCHEMA', '" + schema + "')";
        // 执行Lua脚本

        script.eval(RScript.Mode.READ_WRITE,luaScript,RScript.ReturnType.STATUS);
    }

    // 向索引中添加文档
    public void addDocumentToIndex(String indexName, String docId, Map<String, Object> fields) {
        String hashKey = indexName + ":" + docId; // 构建哈希键名
        RMap<String, Object> map = redisson.getMap(hashKey);
        fields.forEach(map::put);
    }

    // 搜索向量
    public List<String> searchVector(String indexName, float[] queryVector, int limit) {
        String vectorAsString = floatArrayToString(queryVector);
        String searchScript = "return redis.call('FT.SEARCH', '" + indexName + "', '$vector:[" + vectorAsString + "]@vector', 'LIMIT', 0, " + limit + ")";
        @SuppressWarnings("unchecked")
        List<String> results = (List<String>) script.eval(RScript.Mode.READ_ONLY, searchScript,RScript.ReturnType.MULTI);
        return results;
    }

    // 将浮点数数组转换为逗号分隔的字符串
    private String floatArrayToString(float[] array) {
        return Arrays.asList(array).stream().map(String::valueOf).collect(Collectors.joining(","));
    }

}