package com.lambda.inspector.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedJson;
import com.amazonaws.services.inspector.model.AssessmentRunNotification;
import com.amazonaws.services.inspector.model.AssessmentRunStateChange;
import com.amazonaws.services.inspector.model.Attribute;

import java.util.Date;
import java.util.List;
import java.util.Map;

@DynamoDBDocument
public class RunModel {

    public RunModel() {
        super();
    }

    private String arn;

    private String assessmentTemplateRun;

    private String name;

    private String state;

    private Date completedAt;

    private Date createdAt;

    private Boolean dataCollected;

    private Integer durationInSeconds;

    private Map<String, Integer> findingCounts;

//    private List<AssessmentRunNotification> notifications;

    private List<String> rulesPackagesArns;

    private Date startedAt;

    private Date stateChangedAt;

//    private List<AssessmentRunStateChange> stateChanges;

//    private List<Attribute> userAttributeForFindings;

    public String getArn() {
        return arn;
    }

    public void setArn(String arn) {
        this.arn = arn;
    }

    public String getAssessmentTemplateRun() {
        return assessmentTemplateRun;
    }

    public void setAssessmentTemplateRun(String assessmentTemplateRun) {
        this.assessmentTemplateRun = assessmentTemplateRun;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getDataCollected() {
        return dataCollected;
    }

    public void setDataCollected(Boolean dataCollected) {
        this.dataCollected = dataCollected;
    }

    public Integer getDurationInSeconds() {
        return durationInSeconds;
    }

    public void setDurationInSeconds(Integer durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }

    public Map<String, Integer> getFindingCounts() {
        return findingCounts;
    }

    public void setFindingCounts(Map<String, Integer> findingCounts) {
        this.findingCounts = findingCounts;
    }

//    public List<AssessmentRunNotification> getNotifications() {
//        return notifications;
//    }
//
//    public void setNotifications(List<AssessmentRunNotification> notifications) {
//        this.notifications = notifications;
//    }

    public List<String> getRulesPackagesArns() {
        return rulesPackagesArns;
    }

    public void setRulesPackagesArns(List<String> rulesPackagesArns) {
        this.rulesPackagesArns = rulesPackagesArns;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public Date getStateChangedAt() {
        return stateChangedAt;
    }

    public void setStateChangedAt(Date stateChangedAt) {
        this.stateChangedAt = stateChangedAt;
    }

//    @DynamoDBTypeConvertedJson
//    public List<AssessmentRunStateChange> getStateChanges() {
//        return stateChanges;
//    }
//
//    @DynamoDBTypeConvertedJson
//    public void setStateChanges(List<AssessmentRunStateChange> stateChanges) {
//        this.stateChanges = stateChanges;
//    }

//    public List<Attribute> getUserAttributeForFindings() {
//        return userAttributeForFindings;
//    }
//
//    public void setUserAttributeForFindings(List<Attribute> userAttributeForFindings) {
//        this.userAttributeForFindings = userAttributeForFindings;
//    }
}
