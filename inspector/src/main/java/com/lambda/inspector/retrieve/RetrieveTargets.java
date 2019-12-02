package com.lambda.inspector.retrieve;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.lambda.inspector.dto.TargetDTO;
import com.lambda.inspector.model.InspectorModel;
import com.lambda.inspector.model.TargetModel;

import java.util.List;

public class RetrieveTargets implements RequestHandler<TargetDTO, List<TargetModel>> {

    static AmazonDynamoDB client;

    @Override
    public List<TargetModel> handleRequest(TargetDTO input, Context context) {

        Regions usEast2 = Regions.US_EAST_2;
        client = AmazonDynamoDBClientBuilder.standard().withRegion(usEast2).build();

        DynamoDBMapper mapper = new DynamoDBMapper(client);

        InspectorModel inspectorModel = mapper.load(InspectorModel.class, input.getUserId());

        return inspectorModel.getTargets();
    }
}
