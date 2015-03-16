package edu.upc.essi.sushitos.ltigdocstool.web.forms;

/**
 * DefineProfessors class Spring form for professor definition
 * 
 * @author ngalanis
 * @author jpiguillem
 * 
 */
public class DefineProfessorsForm {
    private String name;
    private String lastname;
    private String email;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
