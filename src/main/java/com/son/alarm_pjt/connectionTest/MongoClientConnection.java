package com.son.alarm_pjt.connectionTest;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MongoClientConnection {
    public static void main(String[] args) {
        String connectionString = "mongodb+srv://xzxz7003:ZUPlcHsd4FbDm1Ir@atlascluster.yderc0l.mongodb.net/?retryWrites=true&w=majority&appName=AtlasCluster";
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();
        // Create a new client and connect to the server
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            try {
                // Send a ping to confirm a successful connection
                MongoDatabase database = mongoClient.getDatabase("itnj"); //
                MongoCollection<Document> collection = database.getCollection("member"); // 컬렉션 이름 "member"로 유지

                // Create a document to insert
                Document document = new Document("name", "준식");

                collection.insertOne(document);

                System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
            } catch (MongoException e) {
                e.printStackTrace();
            }
        }
    }
}