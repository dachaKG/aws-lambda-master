package com.lambda.inspector.persist;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.inspector.AmazonInspector;
import com.amazonaws.services.inspector.AmazonInspectorClientBuilder;
import com.amazonaws.services.inspector.model.*;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.lambda.inspector.dto.TargetDTO;
import com.lambda.inspector.model.*;

import java.util.ArrayList;
import java.util.List;

import static com.lambda.inspector.util.Utils.ARN;

public class TargetPersist implements RequestHandler<TargetDTO, InspectorModel> {

    static AmazonDynamoDB client;

    AmazonInspector clientBuilder;

    @Override
    public InspectorModel handleRequest(TargetDTO input, Context context) {

        Regions usEast2 = Regions.US_EAST_2;
        client = AmazonDynamoDBClientBuilder.standard().withRegion(usEast2).build();

        DynamoDBMapper mapper = new DynamoDBMapper(client);


//        String arnTarget = ARN + modelRetrieved.getRegion() + modelRetrieved.getUserId()
//                + ":target/" + input.getTargetId();

        clientBuilder = AmazonInspectorClientBuilder.standard().build();
        InspectorModel modelRetrieved = mapper.load(InspectorModel.class, input.getUserId());
        DescribeAssessmentTargetsRequest request = new DescribeAssessmentTargetsRequest()
                .withAssessmentTargetArns(ARN + modelRetrieved.getRegion() + ":" + modelRetrieved.getUserId()
                        + ":target/" + input.getTargetId());
        DescribeAssessmentTargetsResult target = clientBuilder.describeAssessmentTargets(request);

        List<AssessmentTarget> targetList = target.getAssessmentTargets();
        List<TargetModel> targets = new ArrayList<>();

        targetList.forEach(assessmentTarget -> {
            TargetModel targetModel = new TargetModel();
            targetModel.setName(assessmentTarget.getName());
            targetModel.setArn(assessmentTarget.getArn());
            targetModel.setResourceGroupArn(assessmentTarget.getResourceGroupArn());
            targetModel.setCreatedAt(assessmentTarget.getCreatedAt());
            targetModel.setUpdatedAt(assessmentTarget.getUpdatedAt());
            targets.add(targetModel);
        });

        modelRetrieved.getTargets().addAll(targets);
        System.out.println("region " + modelRetrieved.getRegion());
        System.out.println("userId " + modelRetrieved.getUserId());
        List<TemplateModel> templateModelTmp = new ArrayList<>();
        if (targets.size() > 0) {
            for (TargetModel targetModel : targets) {

                ListAssessmentTemplatesRequest templateRequestList = new ListAssessmentTemplatesRequest().
                        withAssessmentTargetArns(targetModel.getArn()).withMaxResults(123);
                ListAssessmentTemplatesResult responseTemplate = clientBuilder.listAssessmentTemplates(templateRequestList);

                List<String> templateArns = responseTemplate.getAssessmentTemplateArns();
                for (String templateArn : templateArns) {
                    DescribeAssessmentTemplatesRequest templateRequest = new DescribeAssessmentTemplatesRequest()
                            .withAssessmentTemplateArns(templateArn);
                    DescribeAssessmentTemplatesResult template = clientBuilder.describeAssessmentTemplates(templateRequest);
                    List<AssessmentTemplate> assessmentTemplates = template.getAssessmentTemplates();
                    List<TemplateModel> assessmentTemplateList = new ArrayList<>();
                    assessmentTemplates.forEach(assessmentTemplate -> {
                        TemplateModel templateModel = new TemplateModel();
                        templateModel.setArn(assessmentTemplate.getArn());

                        String assessmentTargetName = returnDescribeAssessmentTargetName(assessmentTemplate.getAssessmentTargetArn());

                        templateModel.setAssessmentTargetName(assessmentTargetName);
                        templateModel.setLastAssessmentRunArn(assessmentTemplate.getLastAssessmentRunArn());
                        templateModel.setName(assessmentTemplate.getName());
                        templateModel.setAssessmentRunCount(assessmentTemplate.getAssessmentRunCount());
                        templateModel.setCreatedAt(assessmentTemplate.getCreatedAt());
                        templateModel.setDurationInSeconds(assessmentTemplate.getDurationInSeconds());
                        List<String> rulesPackage = returnRulesPackagesNames(assessmentTemplate.getRulesPackageArns());
                        templateModel.setRulesPackagesArns(rulesPackage);
                        templateModel.setUserAttributesForFindings(assessmentTemplate.getUserAttributesForFindings());
                        assessmentTemplateList.add(templateModel);
                    });
                    templateModelTmp.addAll(assessmentTemplateList);

                }
                modelRetrieved.getTemplates().addAll(templateModelTmp);
            }
        }

        List<RunModel> runModelsTmp = new ArrayList<>();
        if (templateModelTmp.size() > 0) {

            for (TemplateModel templateModel : templateModelTmp) {
                System.out.println("line 100");
                ListAssessmentRunsRequest runsRequest = new ListAssessmentRunsRequest().withAssessmentTemplateArns(
                        templateModel.getArn()).withMaxResults(123);

                System.out.println("line 104");
                ListAssessmentRunsResult responseRun = clientBuilder.listAssessmentRuns(runsRequest);

                System.out.println("line 107");
                DescribeAssessmentRunsRequest runRequest = new DescribeAssessmentRunsRequest()
                        .withAssessmentRunArns(responseRun.getAssessmentRunArns());

                System.out.println("line 111");
                DescribeAssessmentRunsResult runsResult = clientBuilder.describeAssessmentRuns(runRequest);
                System.out.println("line 113");
                List<RunModel> runModels = new ArrayList<>();
                List<AssessmentRun> assessmentRuns = runsResult.getAssessmentRuns();
                assessmentRuns.forEach(run -> {
                    RunModel runModel = new RunModel();
                    runModel.setArn(run.getArn());
                    //other set methods
                    runModel.setAssessmentTemplateRun(run.getAssessmentTemplateArn());
                    runModel.setName(run.getName());
                    runModel.setState(run.getState());
                    runModel.setCompletedAt(run.getCompletedAt());
                    runModel.setCreatedAt(run.getCreatedAt());
                    runModel.setDataCollected(run.getDataCollected());
                    runModel.setDurationInSeconds(run.getDurationInSeconds());
                    runModel.setFindingCounts(run.getFindingCounts());
//                    runModel.setNotifications(run.getNotifications().get(0).get);
                    List<String> rulesPackages = returnRulesPackagesNames(run.getRulesPackageArns());
                    runModel.setRulesPackagesArns(rulesPackages);
                    runModel.setStartedAt(run.getStartedAt());
                    runModel.setStateChangedAt(run.getStateChangedAt());
//                    runModel.setStateChanges(run.getStateChanges().get(0).getS);
//                    runModel.setUserAttributeForFindings(run.getUserAttributesForFindings().get(0).g);
                    runModel.setStartedAt(run.getStartedAt());
                    runModels.add(runModel);
                });

                runModelsTmp.addAll(runModels);

            }
            modelRetrieved.getRuns().addAll(runModelsTmp);
        }

        if (runModelsTmp.size() > 0) {
            List<FindingModel> findingModelTmp = new ArrayList<>();
            for (RunModel runModel : runModelsTmp) {
                System.out.println("run arn: " + runModel.getArn());
                ListFindingsRequest findingsRequest = new ListFindingsRequest().withAssessmentRunArns(
                        runModel.getArn()).withMaxResults(123);
                ListFindingsResult responseFinding = clientBuilder.listFindings(findingsRequest);

                if (responseFinding.getFindingArns().size() > 0) {
                    DescribeFindingsRequest findingsRequest1 = new DescribeFindingsRequest()
                            .withFindingArns(responseFinding.getFindingArns());

//                    System.out.println("finding arns" + responseFinding.getFindingArns());
//                    System.out.println("findingsRequest1" + findingsRequest1);

                    DescribeFindingsResult findingsResult = clientBuilder.describeFindings(findingsRequest1);
                    List<FindingModel> findingModels = new ArrayList<>();
                    System.out.println("findings size: " + findingsResult.getFindings().size());
                    //                        findingModel.setAssetAttributes(finding.getAssetAttributes());
                    //                        findingModel.setAttributes(finding.getAttributes());
                    //                        findingModel.setServiceAttributes(finding.getServiceAttributes());
                    findingsResult.getFindings().forEach(finding -> {
                        FindingModel findingModel = new FindingModel();
                        findingModel.setArn(finding.getArn());
                        findingModel.setAssetType(finding.getAssetType());
                        findingModel.setConfidence(finding.getConfidence());
                        findingModel.setCreatedAt(finding.getCreatedAt());
                        findingModel.setDescription(finding.getDescription());
                        findingModel.setId(finding.getId());
                        findingModel.setIndicatorOfCompromise(finding.getIndicatorOfCompromise());
                        findingModel.setNumericSeverity(finding.getNumericSeverity());
                        findingModel.setRecommendation(finding.getRecommendation());
                        findingModel.setSchemaVersion(finding.getSchemaVersion());
                        findingModel.setService(finding.getService());
                        findingModel.setSeverity(finding.getSeverity());
                        findingModel.setTitle(finding.getTitle());
                        findingModel.setUpdatedAt(finding.getUpdatedAt());
                        DescribeRulesPackagesRequest describeRulesPackagesRequest = new DescribeRulesPackagesRequest()
                                .withRulesPackageArns(finding.getServiceAttributes().getRulesPackageArn());
                        DescribeRulesPackagesResult describeRulesPackagesResult = clientBuilder.describeRulesPackages(describeRulesPackagesRequest);
                        List<String> rulesName = new ArrayList<>();
                        describeRulesPackagesResult.getRulesPackages().forEach(rule -> rulesName.add(rule.getName()));
                        findingModel.setRulesName(rulesName);
                        findingModels.add(findingModel);
                    });

                    findingModelTmp.addAll(findingModels);
                }

            }

            modelRetrieved.getFindings().addAll(findingModelTmp);
        }

        mapper.save(modelRetrieved);

        return modelRetrieved;
    }

    private List<String> returnRulesPackagesNames(List<String> rulesPackageArn){
        DescribeRulesPackagesRequest describeRulesPackagesRequest = new DescribeRulesPackagesRequest()
                .withRulesPackageArns(rulesPackageArn);
        DescribeRulesPackagesResult describeRulesPackagesResult = clientBuilder.describeRulesPackages(describeRulesPackagesRequest);
        List<String> rulesName = new ArrayList<>();
        describeRulesPackagesResult.getRulesPackages().forEach(rule -> rulesName.add(rule.getName()));

        return rulesName;
    }

    private String returnDescribeAssessmentTargetName (String assessmentTarget) {

        DescribeAssessmentTargetsRequest request = new DescribeAssessmentTargetsRequest()
                .withAssessmentTargetArns(assessmentTarget);
        DescribeAssessmentTargetsResult target = clientBuilder.describeAssessmentTargets(request);
        return target.getAssessmentTargets().get(0).getName();
    }

}
