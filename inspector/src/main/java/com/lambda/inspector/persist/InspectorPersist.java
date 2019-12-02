package com.lambda.inspector.persist;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.lambda.inspector.dto.InspectorDTO;
import com.lambda.inspector.model.InspectorModel;

public class InspectorPersist implements RequestHandler<InspectorDTO, InspectorModel> {


    @Override
    public InspectorModel handleRequest(InspectorDTO inspector, Context context) {

        final AmazonDynamoDBClient client = new AmazonDynamoDBClient(new EnvironmentVariableCredentialsProvider());
        client.withRegion(Regions.US_EAST_2); // specify the region you created the table in.
        final DynamoDB dynamoDB = new DynamoDB(client);

        System.out.println("inspector = " + inspector); // Pure for testing. Do not use System.out in production code
//        final Table table = dynamoDB.getTable("Inspector");

        final InspectorModel inspectorModel = new InspectorModel();
        inspectorModel.setRegion(inspector.getRegion());
        inspectorModel.setUserId(inspector.getUserId());
        inspectorModel.setUsername(inspector.getUsername());


        DynamoDBMapper mapper = new DynamoDBMapper(client);
        mapper.save(inspectorModel);
//
//        final Item item = new Item()
//                .withPrimaryKey("username", inspector.getUsername()) // Every item gets a unique id
//                .withString("userId", inspector.getUserId())
//                .withString("", inspector.getRegion());
//
//        table.putItem(item);
//        return null;
        return inspectorModel;
    }
}
