package edu.upc.essi.sushitos.imsglc.lis.rolemanager;

/**
 * Professor Class It represent a Professor inside GDocs4Learning Activity
 * 
 * @author ngalanis
 * @author jpiguillem
 * 
 */
public class Professor {

    private Integer id;
    private String activityId;
    private String email;

    public Professor(Integer id, String activityId, String email) {

        this.id = id;
        this.activityId = activityId;
        this.email = email;
    }

    public Integer getId() {

        return id;
    }

    public String getActivityId() {

        return activityId;
    }

    public void setActivityId(String activityId) {

        this.activityId = activityId;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }
}
