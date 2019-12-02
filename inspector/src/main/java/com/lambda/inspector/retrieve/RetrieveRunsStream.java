package com.lambda.inspector.retrieve;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambda.inspector.model.InspectorModel;
import com.lambda.inspector.model.RunModel;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.List;

public class RetrieveRunsStream implements RequestStreamHandler {

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        int letter;

        while((letter = inputStream.read()) != -1)
        {
            outputStream.write(Character.toUpperCase(letter));
        }
        JSONObject responseJson = new JSONObject();
        JSONObject responseBody = new JSONObject();
//        responseBody.put("input", event.toJSONString());
////        responseBody.put("message", "Output is" + inspector.getRuns());
//
//        JSONObject headerJson = new JSONObject();
//        headerJson.put("x-custom-header", "my custom header value");
//        headerJson.put("Access-Control-Allow-Origin", "*");
//
//        responseJson.put("isBase64Encoded", false);
//        responseJson.put("statusCode", responseCode);
//        responseJson.put("headers", headerJson);
//        responseJson.put("body", responseBody.toString());
//
//
//        outputStream.write(responseJson.toJSONString());
    }
}

//        implements RequestHandler<InputStream, List<RunModel>> {
//
//    static AmazonDynamoDB client;
//
//    JSONParser parser = new JSONParser();
//
//    @Override
//    public List<RunModel> handleRequest(InputStream inputStream, Context context) {
//
//        LambdaLogger logger = context.getLogger();
//        logger.log("Loading Java Lambda handler of ProxyWithStream");
//        System.out.println("Krenulooooo");
//
//        String userId = null;
////        System.out.println(" input stream " + inputStream);
//        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//        // Jackson JSON Provider
////        ObjectMapper mapper = new ObjectMapper();
////        mapper.registerModule(new IdentifiableDeserializableModule());
//        System.out.println(" reader " + reader);
//        JSONObject responseJson = new JSONObject();
//        String responseCode = "200";
//        JSONObject event = null;
//        try {
//            event = (JSONObject)parser.parse(reader);
//            System.out.println("event " + event);
//
//            if (event.get("pathParameters") != null) {
//                JSONObject pps = (JSONObject)event.get("pathParameters");
//                if(pps.get("proxy") != null)
//                    userId = (String) pps.get("proxy");
//            }
//            if (event.get("queryStringParameters") != null)
//            {
//                JSONObject qps = (JSONObject)event.get("queryStringParameters");
//                if ( qps.get("proxy") != null)
//                {
//                    userId = (String)qps.get("proxy");
//                }
//            }
//
//
//        }
//        catch(Exception pex)
//        {
//            responseJson.put("statusCode", "400");
//            responseJson.put("exception", pex);
//        }
//        // Implement your logic here
//        Regions usEast2 = Regions.US_EAST_2;
//        client = AmazonDynamoDBClientBuilder.standard().withRegion(usEast2).build();
//
//
//
//        DynamoDBMapper mapper = new DynamoDBMapper(client);
//
//        InspectorModel inspector = mapper.load(InspectorModel.class, userId);
////
////        JSONObject responseBody = new JSONObject();
////        responseBody.put("input", event.toJSONString());
////        responseBody.put("message", "Output is" + inspector.getRuns());
////
////        JSONObject headerJson = new JSONObject();
////        headerJson.put("x-custom-header", "my custom header value");
////        headerJson.put("Access-Control-Allow-Origin", "*");
////
////        responseJson.put("isBase64Encoded", false);
////        responseJson.put("statusCode", responseCode);
////        responseJson.put("headers", headerJson);
////        responseJson.put("body", responseBody.toString());
//
//        return inspector.getRuns();
//    }
//}
