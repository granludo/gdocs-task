package edu.upc.essi.sushitos.ltigdocstool.activity;

/**
 * Activity class It represent a GDocs Activity
 * 
 * @author ngalanis
 * @author jpiguillem
 */
public class Activity {

    private Integer id;
    private String orgId;
    private String contextLabel;
    private String resourceLabel;
    private String resourceId;
    private String activityType;

    public Activity(Integer newId, String newOrgId, String newContextLabel,
            String newResourceLabel, String newResourceId, String newActivityType) {
        setId(newId);
        setOrgId(newOrgId);
        setContextLabel(newContextLabel);
        setResourceLabel(newResourceLabel);
        setResourceId(newResourceId);
        setActivityType(newActivityType);
    }

    private void setId(Integer id) {

        this.id = id;
    }

    public Integer getId() {

        return id;
    }

    private void setOrgId(String orgId) {

        this.orgId = orgId;
    }

    public String getOrgId() {

        return orgId;
    }

    private void setResourceLabel(String resourceLabel) {

        this.resourceLabel = resourceLabel;
    }

    public String getResourceLabel() {

        return resourceLabel;
    }

    private void setResourceId(String resourceId) {

        this.resourceId = resourceId;
    }

    public String getResourceId() {

        return resourceId;
    }

    private void setContextLabel(String contextLabel) {

        this.contextLabel = contextLabel;
    }

    public String getContextLabel() {

        return contextLabel;
    }

    private void setActivityType(String activityType) {

        this.activityType = activityType;
    }

    public String getActivityType() {

        return activityType;
    }
}
