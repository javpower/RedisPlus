package com.gc.easy.redis.vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class VectorTest {
    private final RedisVectorTool redisVectorTool;

    @Autowired
    public VectorTest(RedisVectorTool redisVectorTool) {
        this.redisVectorTool = redisVectorTool;
    }

    public void performVectorOperations() {
        // 定义字段结构来创建索引
        List<FieldSchema> fields = Arrays.asList(
                new FieldSchema("text", FieldType.TEXT, null, null, null),
                new FieldSchema("vector", FieldType.VECTOR, VectorDataType.FLOAT32, 128, DistanceMetric.COSINE)
        );

        // 创建一个名为 "myIndex" 的向量索引
        redisVectorTool.createVectorIndex("testIndex", fields);
        // 添加一个文档到索引，包含文本和向量
        java.util.Map<String, Object> document = new HashMap<>();
        document.put("text", "This is a sample text");
        document.put("vector", new float[]{0.1f, 0.2f, 0.3f, 0.4f});// 示例向量数据
        redisVectorTool.addDocumentToIndex("testIndex", "1", document); // 假设文档ID为 "1"
        // 执行向量搜索，假设我们搜索与上面添加的向量相似的文档
        float[] queryVector = new float[]{0.1f, 0.2f, 0.3f, 0.4f};
        List<String> searchResults = redisVectorTool.searchVector("testIndex", queryVector, 10); // 限制返回结果为10个
        // 打印搜索结果
        searchResults.forEach(System.out::println);
    }
}
