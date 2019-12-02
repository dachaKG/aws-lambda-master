package com.lambda.inspector.persist;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.lambda.inspector.model.InspectorModel;
import com.lambda.inspector.model.TargetModel;
import com.lambda.inspector.model.TemplateModel;

import java.util.List;

public class TemplatePersist implements RequestHandler<InspectorModel, Void> {

    static AmazonDynamoDB client;

    @Override
    public Void handleRequest(InspectorModel input, Context context) {

        Regions usEast2 = Regions.US_EAST_2;
        client = AmazonDynamoDBClientBuilder.standard().withRegion(usEast2).build();

        DynamoDBMapper mapper = new DynamoDBMapper(client);

        InspectorModel modelRetrieved = mapper.load(InspectorModel.class, input.getUsername());

        System.out.println("region " + modelRetrieved.getRegion());
        System.out.println("userId " + modelRetrieved.getUserId());

        List<TemplateModel> targets = input.getTemplates();

        modelRetrieved.setTemplates(targets);

        mapper.save(modelRetrieved);
        return null;
    }
}
