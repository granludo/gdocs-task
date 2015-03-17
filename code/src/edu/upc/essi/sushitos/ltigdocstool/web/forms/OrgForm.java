package edu.upc.essi.sushitos.ltigdocstool.web.forms;

/**
 * OrgForm class
 * Spring form for organization registration
 * 
 * @author ngalanis
 * @author jpiguillem
 *
 */
public class OrgForm {

    private String orgId;
    private String password;
    private String passwordConfirm;
    
    public void setOrgId(String orgId) {

        this.orgId = orgId;
    }
    public String getOrgId() {

        return orgId;
    }
    public void setPassword(String password) {

        this.password = password;
    }
    public String getPassword() {

        return password;
    }
    public void setPasswordConfirm(String passwordConfirm) {

        this.passwordConfirm = passwordConfirm;
    }
    public String getPasswordConfirm() {

        return passwordConfirm;
    }

}
