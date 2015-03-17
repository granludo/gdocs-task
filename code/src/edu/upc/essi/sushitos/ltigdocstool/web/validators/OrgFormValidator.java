package edu.upc.essi.sushitos.ltigdocstool.web.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import edu.upc.essi.sushitos.ltigdocstool.imsglc.basiclti.organization.OrganizationManager;
import edu.upc.essi.sushitos.ltigdocstool.imsglc.basiclti.organization.UnexistantOrganizationException;
import edu.upc.essi.sushitos.ltigdocstool.web.forms.OrgForm;


/**
 * OrgFormValidator class
 * Spring validator for organization registration forms
 * 
 * @author ngalanis
 * @author jpiguillem
 *
 */
public class OrgFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return OrgForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "orgId", "org.orgId.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "org.password.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordConfirm", "org.passwordConfirm.empty");
        
        
        OrgForm org = (OrgForm) target;
        if(!org.getPassword().equals(org.getPasswordConfirm())){
            errors.rejectValue("password","org.passwords.match");
        }
        OrganizationManager orgMan = new OrganizationManager();
        try {
            orgMan.getOrganization(org.getOrgId());
            errors.rejectValue("orgId","org.orgId.duplicated");
        } catch (UnexistantOrganizationException e) {
            // OK
        } catch (Exception e){
            errors.reject("exception",e.getMessage());
        }
    }

}