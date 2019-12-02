package com.lambda.inspector.persist;

import com.amazonaws.services.inspector.AmazonInspector;
import com.amazonaws.services.inspector.AmazonInspectorClientBuilder;
import com.amazonaws.services.inspector.model.DescribeAssessmentRunsRequest;
import com.amazonaws.services.inspector.model.DescribeAssessmentRunsResult;
import com.amazonaws.services.inspector.model.ListAssessmentRunsRequest;
import com.amazonaws.services.inspector.model.ListAssessmentRunsResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.lambda.inspector.model.InspectorModel;
import com.lambda.inspector.model.RunModel;

import java.util.List;

public class RunPersister implements RequestHandler<InspectorModel, Void> {



    @Override
    public Void handleRequest(InspectorModel input, Context context) {

        AmazonInspector client = AmazonInspectorClientBuilder.standard().build();
        ListAssessmentRunsRequest request = new ListAssessmentRunsRequest().withAssessmentTemplateArns(
                "arn:aws:inspector:us-east-2:681206978735:target/0-L2iik4Jo/template/0-4ah69EGe").withMaxResults(123);

        ListAssessmentRunsResult response = client.listAssessmentRuns(request);
        List<String> runs = response.getAssessmentRunArns();

        for(int i = 0 ; i < runs.size(); i++) {
            DescribeAssessmentRunsRequest requestDesc = new DescribeAssessmentRunsRequest()
                    .withAssessmentRunArns(runs.get(i));
            DescribeAssessmentRunsResult run = client.describeAssessmentRuns(requestDesc);
            for(int j = 0 ; j < run.getAssessmentRuns().size(); j++) {
                RunModel runModel = new RunModel();
                String arn = run.getAssessmentRuns().get(j).getArn();
                String templateArn = run.getAssessmentRuns().get(j).getAssessmentTemplateArn();
                String name = run.getAssessmentRuns().get(j).getName();
                String startedDate = run.getAssessmentRuns().get(j).getStartedAt().toString();
                String completedDate = run.getAssessmentRuns().get(j).getCompletedAt().toString();
                String duration = run.getAssessmentRuns().get(j).getDurationInSeconds().toString();
                List<String> rulesPackage = run.getAssessmentRuns().get(j).getRulesPackageArns();
                String state = run.getAssessmentRuns().get(j).getState();
//
//                runModel.setArn(arn);
//                runModel.setDuration(duration);
//                runModel.setEndTime(completedDate);
//                runModel.setRulesPackages(rulesPackage);
//                runModel.setStatus(state);
//                runModel.setStartTime(startedDate);
//                runModel.setTargetName(name);
//                runModel.setTemplateArn(templateArn);
            }
            System.out.println("Desc " + i + ": " + run);

        }

        return null;
    }
}
