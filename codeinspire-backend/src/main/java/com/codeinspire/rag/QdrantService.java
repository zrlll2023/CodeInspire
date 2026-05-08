package com.codeinspire.rag;

import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import io.qdrant.client.grpc.Points;
import io.qdrant.client.grpc.WithPayloadSelector;
import io.qdrant.client.grpc.WithVectorsSelector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class QdrantService {

    private final QdrantClient client;
    private final String collectionName;

    public QdrantService(
            @Value("${qdrant.host:localhost}") String host,
            @Value("${qdrant.port:6333}") int port,
            @Value("${qdrant.collection-name:codeinspire-knowledge}") String collectionName) {
        this.collectionName = collectionName;
        QdrantGrpcClient grpcClient = new QdrantGrpcClient(host, port);
        this.client = new QdrantClient(grpcClient);

        try {
            initCollection();
        } catch (Exception e) {
            log.warn("Qdrant 初始化失败，RAG功能将降级: {}", e.getMessage());
        }
    }

    private void initCollection() {
        boolean exists = client.listCollectionsAsync().join().getCollectionsList().stream()
                .anyMatch(c -> c.getName().equals(collectionName));

        if (!exists) {
            client.createCollectionAsync(io.qdrant.client.grpc.Collections.CreateCollection.newBuilder()
                    .setCollectionName(collectionName)
                    .setVectorsConfig(io.qdrant.client.grpc.Collections.VectorsConfig.newBuilder()
                            .setParams(io.qdrant.client.grpc.Collections.VectorParams.newBuilder()
                                    .setSize(1536)
                                    .setDistance(io.qdrant.client.grpc.Distance.Cosine)))
                    .build()).join();
            log.info("Qdrant 集合 {} 创建成功", collectionName);
        }
    }

    public List<SearchResult> search(float[] queryVector, int topK) {
        List<SearchResult> results = new ArrayList<>();
        try {
            var response = client.queryAsync(Points.Query.newBuilder()
                            .setCollectionName(collectionName)
                            .setQuery(Points.Query.QueryOneof.Vectors(
                                    Points.Vectors.newBuilder()
                                            .addVector(queryVector)
                                            .build()))
                            .setLimit(topK)
                            .setWithPayload(WithPayloadSelector.enable(true))
                            .build())
                    .join();

            for (var point : response.getResultList()) {
                SearchResult result = new SearchResult();
                result.setId(point.getId().getUuid());
                result.setScore(point.getScore());

                var payload = point.getPayloadMap();
                if (payload.containsKey("content")) {
                    result.setContent(payload.get("content").getStringValue());
                }
                if (payload.containsKey("category")) {
                    result.setCategory(payload.get("category").getStringValue());
                }
                if (payload.containsKey("source")) {
                    result.setSource(payload.get("source").getStringValue());
                }
                results.add(result);
            }
        } catch (Exception e) {
            log.error("Qdrant 搜索失败: {}", e.getMessage());
        }
        return results;
    }

    public void upsert(String id, float[] vector, String content, String category, String source) {
        try {
            var payload = Points.Struct.newBuilder();
            payload.putPayload("content", Points.Value.newBuilder().setStringValue(content).build());
            payload.putPayload("category", Points.Value.newBuilder().setStringValue(category).build());
            payload.putPayload("source", Points.Value.newBuilder().setStringValue(source).build());

            client.upsertAsync(Points.UpsertPoints.newBuilder()
                    .setCollectionName(collectionName)
                    .addPoints(Points.PointStruct.newBuilder()
                            .setId(Points.Id.newBuilder().setUuid(id).build())
                            .setVectors(Points.Vectors.newBuilder().addVector(vector))
                            .setPayload(payload))
                    .build()).join();
        } catch (Exception e) {
            log.error("Qdrant 数据写入失败: {}", e.getMessage());
        }
    }

    @lombok.Data
    public static class SearchResult {
        private String id;
        private float score;
        private String content;
        private String category;
        private String source;
    }
}
