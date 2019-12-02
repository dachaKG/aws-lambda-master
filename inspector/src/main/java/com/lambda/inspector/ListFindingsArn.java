package com.lambda.inspector;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.inspector.AmazonInspector;
import com.amazonaws.services.inspector.AmazonInspectorClientBuilder;
import com.amazonaws.services.inspector.model.*;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.lambda.inspector.model.FindingModel;
import com.lambda.inspector.model.InspectorModel;
import com.lambda.inspector.model.RunModel;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ListFindingsArn implements RequestHandler<SNSEvent, Void> {

    static AmazonDynamoDB client;

    AmazonInspector clientBuilder;

    @Override
    public Void handleRequest(SNSEvent input, Context context) {

        Regions usEast2 = Regions.US_EAST_2;
        client = AmazonDynamoDBClientBuilder.standard().withRegion(usEast2).build();

        DynamoDBMapper mapper = new DynamoDBMapper(client);
        clientBuilder = AmazonInspectorClientBuilder.standard().build();


        InspectorModel modelRetrieved = mapper.load(InspectorModel.class, "681206978735");


        input.getRecords().forEach(snsRecord -> {
            String message = snsRecord.getSNS().getMessage();
            JSONParser parser = new JSONParser();
            try {
                JSONObject json = (JSONObject) parser.parse(message);
                if (json.get("event").equals("ASSESSMENT_RUN_STARTED")) {
                    String title = "ASSESSMENT_RUN_STARTED";
                    DescribeAssessmentTemplatesRequest templateRequest = new DescribeAssessmentTemplatesRequest()
                            .withAssessmentTemplateArns((String) json.get("template"));
                    DescribeAssessmentTemplatesResult template = clientBuilder.describeAssessmentTemplates(templateRequest);
                    String body = "Assessment run is started: "
                            + " for template " + template.getAssessmentTemplates().get(0).getName();
                    String runArn = (String) json.get("run");
                    DescribeAssessmentRunsRequest runRequest = new DescribeAssessmentRunsRequest()
                            .withAssessmentRunArns((String) json.get("run"));

                    DescribeAssessmentRunsResult runsResult = clientBuilder.describeAssessmentRuns(runRequest);

                    List<RunModel> runModels = new ArrayList<>();

                    runsResult.getAssessmentRuns().forEach(run -> {
                        RunModel runModel = new RunModel();
                        runModel.setArn(run.getArn());
                        runModel.setAssessmentTemplateRun(run.getAssessmentTemplateArn());
                        runModel.setName(run.getName());
                        runModel.setState(run.getState());
                        runModel.setCompletedAt(run.getCompletedAt());
                        runModel.setCreatedAt(run.getCreatedAt());
                        runModel.setDataCollected(run.getDataCollected());
                        runModel.setDurationInSeconds(run.getDurationInSeconds());
                        runModel.setFindingCounts(run.getFindingCounts());
                        List<String> rulesPackages = returnRulesPackagesNames(run.getRulesPackageArns());
                        runModel.setRulesPackagesArns(rulesPackages);
                        runModel.setStartedAt(run.getStartedAt());
                        runModel.setStateChangedAt(run.getStateChangedAt());
                        runModel.setStartedAt(run.getStartedAt());
                        runModels.add(runModel);
                    });

                    modelRetrieved.getRuns().addAll(runModels);

                    SendPushNotification sendPushNotification = new SendPushNotification();
                    sendPushNotification.execute(title, body, runArn);

                    mapper.save(modelRetrieved);

                } else if (json.get("event").equals("ASSESSMENT_RUN_COMPLETED")) {
                    String title = "ASSESSMENT_RUN_COMPLETED";
                    DescribeAssessmentTemplatesRequest templateRequest = new DescribeAssessmentTemplatesRequest()
                            .withAssessmentTemplateArns((String) json.get("template"));
                    DescribeAssessmentTemplatesResult template = clientBuilder.describeAssessmentTemplates(templateRequest);
                    String body = "Assessment run is completed"
                            + " for template " + template.getAssessmentTemplates().get(0).getName();
                    String runArn = (String) json.get("run");
                    DescribeAssessmentRunsRequest runRequest = new DescribeAssessmentRunsRequest()
                            .withAssessmentRunArns((String) json.get("run"));

                    DescribeAssessmentRunsResult runsResult = clientBuilder.describeAssessmentRuns(runRequest);

                    List<RunModel> sortedRunModel = modelRetrieved.getRuns().stream()
                            .sorted(Comparator.comparing(RunModel::getCreatedAt).reversed()).collect(Collectors.toList());
                    System.out.println("sorted " + sortedRunModel.size());
                    sortedRunModel.forEach(runModel -> {
//                                List<RunModel> runModels = new ArrayList<>();
                        if (runModel.getArn().equals(runArn)) {
                            int index = modelRetrieved.getRuns().indexOf(runModel);
                            System.out.println("index " + index);
                            runsResult.getAssessmentRuns().forEach(run -> {
                                RunModel runModelTmp = new RunModel();
                                runModelTmp.setArn(run.getArn());
                                runModelTmp.setAssessmentTemplateRun(run.getAssessmentTemplateArn());
                                runModelTmp.setName(run.getName());
                                runModelTmp.setState(run.getState());
                                runModelTmp.setCompletedAt(run.getCompletedAt());
                                runModelTmp.setCreatedAt(run.getCreatedAt());
                                runModelTmp.setDataCollected(run.getDataCollected());
                                runModelTmp.setDurationInSeconds(run.getDurationInSeconds());
                                runModelTmp.setFindingCounts(run.getFindingCounts());
                                List<String> rulesPackages = returnRulesPackagesNames(run.getRulesPackageArns());
                                runModelTmp.setRulesPackagesArns(rulesPackages);
                                runModelTmp.setStartedAt(run.getStartedAt());
                                runModelTmp.setStateChangedAt(run.getStateChangedAt());
                                runModelTmp.setStartedAt(run.getStartedAt());
                                modelRetrieved.getRuns().set(index, runModelTmp);
                            });
                            return;
                        }
                    });

                    mapper.save(modelRetrieved);

                    SendPushNotification sendPushNotification = new SendPushNotification();

                    sendPushNotification.execute(title, body, runArn);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        });
//
//        mapper.save(modelRetrieved);


//        AmazonInspector clientBuilder = AmazonInspectorClientBuilder.standard().build();
//
//        ListFindingsRequest findingsRequest = new ListFindingsRequest().withAssessmentRunArns(
//                "arn:aws:inspector:us-east-2:681206978735:target/0-369NQaTU/template/0-ZiyscpEQ/run/0-1PKq3sAx");
//        ListFindingsResult responseFinding = clientBuilder.listFindings(findingsRequest);
//
//        //arn:aws:inspector:us-east-2:681206978735:target/0-L2iik4Jo/template/0-4ah69EGe/run/0-Lzr0TmKl/finding/0-2OlaMojN
//
//        DescribeFindingsRequest findingsRequest1 = new DescribeFindingsRequest()
//                .withFindingArns("arn:aws:inspector:us-east-2:681206978735:target/0-L2iik4Jo/template/0-4ah69EGe/run/0-Lzr0TmKl/finding/0-2OlaMojN");
//
//        DescribeFindingsResult findingsResult = clientBuilder.describeFindings(findingsRequest1);
////        findingsResult.getFindings().get(0).getServiceAttributes().getRulesPackageArn()
//        DescribeRulesPackagesRequest describeRulesPackagesRequest = new DescribeRulesPackagesRequest()
//                .withRulesPackageArns("arn:aws:inspector:us-east-2:646659390643:rulespackage/0-AxKmMHPX");
//        DescribeRulesPackagesResult describeRulesPackagesResult = clientBuilder.describeRulesPackages(describeRulesPackagesRequest);
//
//        System.out.println("describe package rules: " + describeRulesPackagesResult);
//
//        System.out.println("describe finding: " + findingsResult);
//
//        System.out.println("findings: " + responseFinding);
        return null;
    }

    private List<String> returnRulesPackagesNames(List<String> rulesPackageArn) {
        DescribeRulesPackagesRequest describeRulesPackagesRequest = new DescribeRulesPackagesRequest()
                .withRulesPackageArns(rulesPackageArn);
        DescribeRulesPackagesResult describeRulesPackagesResult = clientBuilder.describeRulesPackages(describeRulesPackagesRequest);
        List<String> rulesName = new ArrayList<>();
        describeRulesPackagesResult.getRulesPackages().forEach(rule -> rulesName.add(rule.getName()));

        return rulesName;
    }
}
