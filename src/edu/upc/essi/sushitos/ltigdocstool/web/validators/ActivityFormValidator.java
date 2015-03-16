package edu.upc.essi.sushitos.ltigdocstool.web.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import edu.upc.essi.sushitos.ltigdocstool.web.forms.ActivityForm;

/**
 * ActivityFormValidator class
 * Spring validator for activity registration forms
 * 
 * @author ngalanis
 * @author jpiguillem
 *
 */
public class ActivityFormValidator implements Validator{

    @Override
    public boolean supports(Class<?> clazz) {
        return ActivityForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        // TODO : IMPLEMENT THIS METHOD
    }

}
