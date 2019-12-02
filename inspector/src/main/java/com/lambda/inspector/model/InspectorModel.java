package com.lambda.inspector.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.ArrayList;
import java.util.List;

@DynamoDBTable(tableName = "InspectorId")
public class InspectorModel {

    public InspectorModel() {
        super();
    }

    private String userId;

    private String username;

    private String region;

    private List<TargetModel> targets;

    private List<TemplateModel> templates;

    private List<RunModel> runs;

    private List<FindingModel> findings;

    private List<String> amessage;

    @DynamoDBHashKey(attributeName = "userId")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String id) {
        this.userId = id;
    }

    @DynamoDBAttribute(attributeName = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    @DynamoDBAttribute(attributeName = "region")
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

//    @DynamoDBTypeConverted(converter = TargetModelConverter.class)
    @DynamoDBAttribute(attributeName = "targets")
    public List<TargetModel> getTargets() {
        if(targets == null){
            targets = new ArrayList<>();
        }
        return targets;
    }

    @DynamoDBAttribute(attributeName = "targets")
    public void setTargets(List<TargetModel> targets) {
        this.targets = targets;
    }

//    @DynamoDBTypeConverted(converter = TargetModelConverter.class)
    @DynamoDBAttribute(attributeName = "templates")
    public List<TemplateModel> getTemplates() {
        if(templates == null) {
            templates = new ArrayList<>();
        }
        return templates;
    }

    @DynamoDBAttribute(attributeName = "templates")
    public void setTemplates(List<TemplateModel> templates) {
        this.templates = templates;
    }

    @DynamoDBAttribute(attributeName = "runs")
    public List<RunModel> getRuns() {
        if(runs == null) {
            runs = new ArrayList<>();
        }
        return runs;
    }

    @DynamoDBAttribute(attributeName = "runs")
    public void setRuns(List<RunModel> runs) {
        this.runs = runs;
    }

    @DynamoDBAttribute(attributeName = "findings")
    public List<FindingModel> getFindings() {
        if(findings == null) {
            findings = new ArrayList<>();
        }
        return findings;
    }

    @DynamoDBAttribute(attributeName = "findings")
    public void setFindings(List<FindingModel> findings) {
        this.findings = findings;
    }


    //    @DynamoDBTypeConverted(converter = TargetModel.class)
//    @DynamoDBAttribute(attributeName = "runs")
//    public List<RunModel> getRuns() {
//        return runs;
//    }
//
//    @DynamoDBAttribute(attributeName = "runs")
//    public void setRuns(List<RunModel> runs) {
//        this.runs = runs;
//    }
//
//    @DynamoDBTypeConverted(converter = TargetModel.class)
//    @DynamoDBAttribute(attributeName = "findings")
//    public List<FindingModel> getFindings() {
//        return findings;
//    }
//
//    @DynamoDBAttribute(attributeName = "findings")
//    public void setFindings(List<FindingModel> findings) {
//        this.findings = findings;
//    }

    @DynamoDBAttribute(attributeName = "amessage")
    public List<String> getAmessage() {
        if(amessage == null) {
            amessage = new ArrayList<>();
        }
        return amessage;
    }

    @DynamoDBAttribute(attributeName = "amessage")
    public void setAmessage(List<String> amessage) {
        this.amessage = amessage;
    }
}

