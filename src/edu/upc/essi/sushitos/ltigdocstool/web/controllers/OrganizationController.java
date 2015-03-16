package edu.upc.essi.sushitos.ltigdocstool.web.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import edu.upc.essi.sushitos.log.SystemLogger;
import edu.upc.essi.sushitos.ltigdocstool.imsglc.basiclti.organization.DuplicatedOrganizationException;
import edu.upc.essi.sushitos.ltigdocstool.imsglc.basiclti.organization.OrganizationManager;
import edu.upc.essi.sushitos.ltigdocstool.web.forms.OrgForm;


/**
 * OrganizationController class
 * Spring controller for organization related requests
 * 
 * @author ngalanis
 * @author jpiguillem
 *
 */
@Controller
public class OrganizationController {

    protected final SystemLogger logger = SystemLogger.getSystemLogger();

    protected OrganizationManager orgMan;

    private Validator validator;

    @Autowired
    @Qualifier("orgManager")
    public void setOrganizationManager(OrganizationManager newOrgMan) {

        orgMan = newOrgMan;
    }
    
    @Autowired
    @Qualifier("orgValidator")
    public void setValidator(Validator validator) {

        this.validator = validator;
    }

    @RequestMapping(value = "/organization/newOrganization.html", method = RequestMethod.GET)
    public ModelAndView newOrgForm() throws ServletException, IOException {

        logger.info("/organization/newOrganization.html");
        // An initial MessageForm bean needs to be provided with the initial
        // values to show in the form:
        return new ModelAndView("/organization/newOrganization").addObject("newOrg", new OrgForm());
    }

    @RequestMapping(value = "/organization/newOrganization.html", method = RequestMethod.POST)
    public ModelAndView processNewOrgForm(@ModelAttribute("newOrg") OrgForm newOrg,
            BindingResult result, HttpServletRequest request, HttpServletResponse response) {

        logger.info("/organization/newOrganization.html");
        ModelAndView view = new ModelAndView("/organization/newOrganization").addObject("newOrg",
                newOrg);
        validator.validate(newOrg, result);
        if (result.hasGlobalErrors()){
            logger.severe("/organization/newOrganization.html");//TODO: Change logger call to log the errors
            return new ModelAndView("/error");
        }
        if (result.hasErrors()) {
            return view;
        }

        try {
            orgMan.createNewOrganization(newOrg.getOrgId(), newOrg.getPassword());
            view.addObject("success", true);
        } catch (DuplicatedOrganizationException doe) {
            result.addError(new FieldError("newOrg", "orgId", "orgId.Dupicated"));
        } catch (Exception e) {
            logger.severe("/organization/newOrganization.html");//TODO: Change logger call to log the errors
            return new ModelAndView("/error"); 
        }
        return view;
    }
}
