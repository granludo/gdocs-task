package edu.upc.essi.sushitos.ltigdocstool.web.forms;

/**
 * ActivityForm class
 * Spring form for activity registration
 * 
 * @author ngalanis
 * @author jpiguillem
 *
 */
public class ActivityForm {
   
    private String activityType;

    public void setActivityType(String activityType) {

        this.activityType = activityType;
    }

    public String getActivityType() {

        return activityType;
    }
    
}
