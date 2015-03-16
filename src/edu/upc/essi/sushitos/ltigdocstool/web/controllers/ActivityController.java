package edu.upc.essi.sushitos.ltigdocstool.web.controllers;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import edu.upc.essi.sushitos.imsglc.basiclti.extensions.OutcomesService;
import edu.upc.essi.sushitos.imsglc.basiclti.extensions.serviceresponses.ReadResultServiceResponse;
import edu.upc.essi.sushitos.imsglc.basiclti.httpserver.BLTILaunchRequest;
import edu.upc.essi.sushitos.imsglc.lis.rolemanager.DuplicatedProfessorException;
import edu.upc.essi.sushitos.imsglc.lis.rolemanager.Professor;
import edu.upc.essi.sushitos.imsglc.lis.rolemanager.ProfessorManager;
import edu.upc.essi.sushitos.imsglc.lis.rolemanager.RoleManager;
import edu.upc.essi.sushitos.log.SystemLogger;
import edu.upc.essi.sushitos.ltigdocstool.activity.ActivityManager;
import edu.upc.essi.sushitos.ltigdocstool.document.Document;
import edu.upc.essi.sushitos.ltigdocstool.document.DocumentManager;
import edu.upc.essi.sushitos.ltigdocstool.document.UnexistantDocument;
import edu.upc.essi.sushitos.ltigdocstool.session.AuthManager;
import edu.upc.essi.sushitos.ltigdocstool.web.forms.ActivityForm;
import edu.upc.essi.sushitos.ltigdocstool.web.forms.DefineProfessorsForm;
import edu.upc.essi.sushitos.ltigdocstool.web.forms.GradeForm;
import edu.upc.essi.sushitos.ltigdocstool.web.forms.RemoveProfessorForm;
import edu.upc.essi.sushitos.ltigdocstool.web.forms.SubmitDocumentForm;

/**
 * ActivityController class Spring controller for activity related requests
 * 
 * @author ngalanis
 * @author jpiguillem
 * 
 */
@Controller
public class ActivityController {

    protected final SystemLogger logger = SystemLogger.getSystemLogger();

    private Validator actValidator;
    private Validator gradeValidator;

    @Autowired
    @Qualifier("activityValidator")
    public void setActValidator(Validator validator) {

        this.actValidator = validator;
    }

    @Autowired
    @Qualifier("gradeValidator")
    public void setGradeValidator(Validator validator) {

        this.gradeValidator = validator;
    }

    @RequestMapping(value = "/activity/newActivity.html", method = RequestMethod.GET)
    public ModelAndView configureActivity(HttpServletRequest request,
            HttpServletResponse response) {
        logger.info("/activity/newActivity.html");
        ModelAndView view = null;

        BLTILaunchRequest launchRequest = (BLTILaunchRequest) request
                .getSession().getAttribute("bltiRequest");
        if (RoleManager.isLearner(launchRequest.getRoles())) {
            view = new ModelAndView("/activity/notConfigured");
        } else if (RoleManager.isTeacher(launchRequest.getRoles())) {
            view = new ModelAndView("/activity/newActivity");
            view.addObject("newActivity", new ActivityForm());

        } else {
            view = new ModelAndView("/error");
        }

        return view;
    }

    @RequestMapping(value = "/activity/newActivity.html", method = RequestMethod.POST)
    public ModelAndView newActivity(
            @ModelAttribute("newActivity") ActivityForm newActivity,
            BindingResult result, HttpServletRequest request,
            HttpServletResponse response) {
        logger.info("/activity/newActivity.html");

        ModelAndView view = new ModelAndView("/activity/newActivity")
                .addObject("newActivity", newActivity);
        actValidator.validate(newActivity, result);
        if (result.hasGlobalErrors()) {
            logger.severe("/activity/newActivity.html");// TODO: Change logger
                                                        // call to log the
                                                        // errors
            return new ModelAndView("/error");
        }
        if (result.hasErrors()) {
            return view;
        }
        BLTILaunchRequest launchRequest = (BLTILaunchRequest) request
                .getSession().getAttribute("bltiRequest");

        String orgId = launchRequest.getToolConsumerInstanceGuid();
        String contextLabel = launchRequest.getContextLabel();
        String resourceLabel = launchRequest.getResourceLinkTitle();
        String resourceId = launchRequest.getResourceLinkId();

        try {
            ActivityManager.createActivity(orgId, contextLabel, resourceLabel,
                    resourceId, newActivity.getActivityType());

            // Add current user as the default professor
            /*
             * String email = launchRequest.getLisPersonContactEmailPrimary();
             * if (email != null) { ProfessorManager pm = new
             * ProfessorManager(); pm.newProfessor(resourceId, email); }
             */

            view = new ModelAndView("redirect:/activity/defineProfessors.html?newActivity=true");
        } catch (Exception e) {
            logger.severe("/activity/newActivity.html");// TODO: Change logger
                                                        // call to log the
                                                        // errors
            view = new ModelAndView("/error");
        }

        return view;
    }

    @RequestMapping(value = "/activity/viewActivity.html", method = RequestMethod.GET)
    public ModelAndView viewActivity(HttpServletRequest request,
            HttpServletResponse response) {
        logger.info("/activity/viewActivity.html");
        ModelAndView view = null;

        BLTILaunchRequest launchRequest = (BLTILaunchRequest) request
                .getSession().getAttribute("bltiRequest");

        List<Professor> professors = new LinkedList<Professor>();

        ProfessorManager pm = new ProfessorManager();
        professors = pm.getProfessors(launchRequest.getResourceLinkId());

        try {
            if (!ActivityManager.isConfigured(
                    launchRequest.getToolConsumerInstanceGuid(),
                    launchRequest.getContextLabel(),
                    launchRequest.getResourceLinkTitle(),
                    launchRequest.getResourceLinkId())) {

                view = new ModelAndView("redirect:/activity/newActivity.html");
            } else if (professors.isEmpty()) {
                if (RoleManager.isLearner(launchRequest.getRoles())) {
                    view = new ModelAndView(
                            "redirect:/activity/newActivity.html");
                } else if (RoleManager.isTeacher(launchRequest.getRoles())) {
                    view = new ModelAndView("redirect:/activity/defineProfessors.html?newActivity=true");
                }
            } else {
                if (RoleManager.isLearner(launchRequest.getRoles())) {
                    view = new ModelAndView(
                            "redirect:/activity/studentActivity.html");
                } else if (RoleManager.isTeacher(launchRequest.getRoles())) {
                    view = new ModelAndView(
                            "redirect:/activity/teacherPanel.html");
                } else {
                    System.out.println("xxxxxxxxxxxxxxxxx");
                    view = new ModelAndView("/error");
                }
            }
        } catch (Exception e) {
            logger.severe("/activity/viewActivity.html");// TODO: Change logger
                                                         // call to log the
                                                         // errors
            view = new ModelAndView("/error");
        }
        return view;
    }

    @RequestMapping(value = "/activity/defineProfessors.html", method = RequestMethod.GET)
    public ModelAndView defineProfessors(HttpServletRequest request,
            HttpServletResponse response) {
        logger.info("/activity/defineProfessors.html");

        BLTILaunchRequest launchRequest = (BLTILaunchRequest) request
                .getSession().getAttribute("bltiRequest");

        String newActivity = request.getParameter("newActivity");

        ModelAndView view = new ModelAndView("/activity/defineProfessors");
        view.addObject("newProfessor", new DefineProfessorsForm());
        view.addObject("activity", launchRequest.getResourceLinkTitle());

        if (newActivity != null) {
            view.addObject("newActivity", newActivity);
        }

        String resourceId = launchRequest.getResourceLinkId();

        List<Professor> professors = new LinkedList<Professor>();

        ProfessorManager pm = new ProfessorManager();
        professors = pm.getProfessors(resourceId);
        view.addObject("professors", professors);

        return view;
    }

    @RequestMapping(value = "/activity/defineProfessors.html", method = RequestMethod.POST)
    public ModelAndView newProfessor(
            @ModelAttribute("newProfessor") DefineProfessorsForm newProfessor,
            BindingResult result, HttpServletRequest request,
            HttpServletResponse response) {
        logger.info("/activity/defineProfessors.html");

        ModelAndView view = new ModelAndView("/activity/defineProfessors")
                .addObject("newProfessor", newProfessor);
        actValidator.validate(newProfessor, result);
        if (result.hasGlobalErrors()) {
            logger.severe("/activity/defineProfessors.html");// TODO: Change
                                                             // logger
            // call to log the
            // errors
            return new ModelAndView("/error");
        }
        if (result.hasErrors()) {
            return view;
        }
        BLTILaunchRequest launchRequest = (BLTILaunchRequest) request
                .getSession().getAttribute("bltiRequest");

        view.addObject("activity", launchRequest.getResourceLinkTitle());

        String resourceId = launchRequest.getResourceLinkId();

        ProfessorManager pm = new ProfessorManager();
        Boolean success;
        try {
            success = pm.newProfessor(resourceId, newProfessor.getEmail());
        } catch (DuplicatedProfessorException e) {
            view = new ModelAndView("redirect:/activity/defineProfessors.html");
            view.addObject("duplicate", true);
            success = false;
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (success) {
            view = new ModelAndView("redirect:/activity/defineProfessors.html");
            view.addObject("success", true);
        }

        return view;
    }

    @RequestMapping(value = "/activity/removeProfessor.html", method = RequestMethod.GET)
    public ModelAndView removeProfessor(HttpServletRequest request,
            HttpServletResponse response) {
        logger.info("/activity/removeProfessor.html");

        ModelAndView view = new ModelAndView("/activity/removeProfessor");

        String professorEmail = request.getParameter("professorEmail");

        RemoveProfessorForm rpf = new RemoveProfessorForm();
        rpf.setEmail(professorEmail);
        System.out.println("professor e-mail:" + rpf.getEmail());
        view.addObject("removeProfessor", rpf);

        return view;
    }

    @RequestMapping(value = "/activity/removeProfessor.html", params = "remove", method = RequestMethod.POST)
    public ModelAndView removeProfessor(
            @ModelAttribute("removeProfessor") RemoveProfessorForm removeProfessor,
            BindingResult result, HttpServletRequest request,
            HttpServletResponse response) {
        logger.info("/activity/removeProfessor.html");

        ModelAndView view = new ModelAndView("/activity/removeProfessor")
                .addObject("removeProfessor", removeProfessor);
        actValidator.validate(removeProfessor, result);
        if (result.hasGlobalErrors()) {
            logger.severe("/activity/removeProfessor.html");// TODO: Change
                                                            // logger
            // call to log the
            // errors
            return new ModelAndView("/error");
        }
        if (result.hasErrors()) {
            return view;
        }
        BLTILaunchRequest launchRequest = (BLTILaunchRequest) request
                .getSession().getAttribute("bltiRequest");

        String resourceId = launchRequest.getResourceLinkId();

        System.out.println("Trying to remove professor: " + removeProfessor.getEmail() + " From activity: " + resourceId);

        ProfessorManager pm = new ProfessorManager();
        Boolean success = pm.removeProfessor(resourceId, removeProfessor.getEmail());

        if (success) {
            view = new ModelAndView("redirect:/activity/defineProfessors.html");
            view.addObject("success", true);
        }

        return view;
    }

    @RequestMapping(value = "/activity/removeProfessor.html", params = "cancel", method = RequestMethod.POST)
    public ModelAndView cancelRemoveProfessor(
            @ModelAttribute("removeProfessor") RemoveProfessorForm toRemove,
            BindingResult result, HttpServletRequest request,
            HttpServletResponse response) {
        logger.info("/activity/removeProfessor.html");

        ModelAndView view = new ModelAndView("/activity/removeProfessor")
                .addObject("removeProfessor", toRemove);
        actValidator.validate(toRemove, result);
        if (result.hasGlobalErrors()) {
            logger.severe("/activity/removeProfessor.html");// TODO: Change
                                                            // logger
            // call to log the
            // errors
            return new ModelAndView("/error");
        }
        if (result.hasErrors()) {
            return view;
        }

        view = new ModelAndView("redirect:/activity/defineProfessors.html");
        return view;
    }

    @RequestMapping(value = "/activity/studentActivity.html", method = RequestMethod.GET)
    public ModelAndView studentActivity(HttpServletRequest request,
            HttpServletResponse response) {
        logger.info("/activity/studentActivity.html");

        ModelAndView view = new ModelAndView("/activity/studentActivity");

        // out.println("<H1><b> Hey Mr. Learner </b></H1>");
        BLTILaunchRequest launchRequest = (BLTILaunchRequest) request
                .getSession().getAttribute("bltiRequest");
        // Gather document data
        String org = launchRequest.getToolConsumerInstanceGuid();
        String contextLabel = launchRequest.getContextLabel();
        String resourceLabel = launchRequest.getResourceLinkTitle();
        String resourceId = launchRequest.getResourceLinkId();
        String userId = launchRequest.getUserId();
        String familyName = launchRequest.getLisPersonNameFamily();
        String givenName = launchRequest.getLisPersonNameGiven();
        String email = launchRequest.getLisPersonContactEmailPrimary();
        Document doc = null;
        String type = null;
        try {
            type = ActivityManager.getActivityType(org, contextLabel, resourceLabel, resourceId);
            System.out.println("SEARCH DOCUMENT");
            // Search for the document in the local database
            String userToken = AuthManager.getToken(org + userId);
            DocumentManager docMan = new DocumentManager(org + userId,
                    userToken);
            doc = docMan.getDocument(org, contextLabel, resourceLabel,
                    resourceId, org + userId);

            if (doc.getSubmitted()) {
                view.addObject("submitted", true);
            }

            view.addObject("doc", doc);

        } catch (UnexistantDocument e) {
            try {
                type = ActivityManager.getActivityType(org, contextLabel, resourceLabel, resourceId);
                String sourcedId = launchRequest.getLisResultSourcedid();
                String userToken = AuthManager.getToken(org + userId);
                DocumentManager docMan = new DocumentManager(org + userId, userToken);

                if (type.equals("personal")) {
                    System.out.println("CREATE PERSONAL DOCUMENT");
                    // Create new document
                    doc = docMan.createDocument(org, contextLabel, resourceLabel,
                            resourceId, familyName, givenName, userId, sourcedId, type);
                    view.addObject("doc", doc);
                } else if (type.equals("collective")) {
                    // See if the collective document already exists
                    doc = docMan.getCollectiveDocument(org, contextLabel, resourceLabel,
                            resourceId);

                    if (doc != null) {
                        System.out.println("SHARE EXISTING COLLECTIVE DOCUMENT");
                        Boolean share = docMan.shareDocument(doc, email, userId, sourcedId);
                        view.addObject("doc", doc);
                    } else { // Create the document
                        System.out.println("CREATE COLLECTIVE DOCUMENT");
                        doc = docMan.createDocument(org, contextLabel, resourceLabel,
                                resourceId, null, null, userId, sourcedId, type);
                        view.addObject("doc", doc);
                    }

                }
            } catch (Exception e1) {
                e1.printStackTrace();
                view = new ModelAndView("/error");
                return view;
            }

        } catch (Exception e) {
            view = new ModelAndView("/error");
            return view;
        }
        SubmitDocumentForm sdf = new SubmitDocumentForm(doc.getDocId());
        view.addObject("newSubmit", sdf);
        view.addObject("activity", launchRequest.getResourceLinkTitle());

        // Add a "submit" button if the activity is not collective and
        // the document is not already submitted
        if (type.equals("personal")) {
            view.addObject("personal", true);
        }
        return view;
    }

    @RequestMapping(value = "/activity/studentActivity.html", method = RequestMethod.POST)
    public ModelAndView newSubmit(
            @ModelAttribute("newSubmit") SubmitDocumentForm newSubmit,
            BindingResult result, HttpServletRequest request,
            HttpServletResponse response) {

        logger.info("/activity/studentActivity.html");

        ModelAndView view = new ModelAndView("/activity/studentActivity");
        view.addObject("newSubmit", newSubmit);

        BLTILaunchRequest launchRequest = (BLTILaunchRequest) request
                .getSession().getAttribute("bltiRequest");

        String org = launchRequest.getToolConsumerInstanceGuid();
        String contextLabel = launchRequest.getContextLabel();
        String resourceLabel = launchRequest.getResourceLinkTitle();
        String resourceId = launchRequest.getResourceLinkId();
        String userId = launchRequest.getUserId();
        Document doc = null;
        String type = null;
        String userToken;
        try {
            type = ActivityManager.getActivityType(org, contextLabel, resourceLabel, resourceId);
            userToken = AuthManager.getToken(org + userId);
            DocumentManager docMan = new DocumentManager(org + userId,
                    userToken);
            doc = docMan.getDocument(org, contextLabel, resourceLabel,
                    resourceId, org + userId);
            view.addObject("doc", doc);
            doc = docMan.submit(doc, org, userId, resourceId);
            view.addObject("success", true);

        } catch (Exception e) {
            logger.severe("/activity/studentActivity.html");// TODO: Change
                                                            // logger call to
                                                            // log the errors
            e.printStackTrace();
            return new ModelAndView("/error");
        }
        view.addObject("activity", launchRequest.getResourceLinkTitle());
        if (type.equals("personal")) {
            view.addObject("personal", true);
        }
        return view;
    }

    @RequestMapping(value = "/activity/teacherPanel.html", method = RequestMethod.GET)
    public ModelAndView teacherPanel(HttpServletRequest request,
            HttpServletResponse response) {
        logger.info("/activity/teacherPanel.html");

        ModelAndView view = new ModelAndView("/activity/teacherPanel");

        BLTILaunchRequest launchRequest = (BLTILaunchRequest) request
                .getSession().getAttribute("bltiRequest");

        List<Document> courseDocuments = new LinkedList<Document>();

        String org = launchRequest.getToolConsumerInstanceGuid();
        String contextLabel = launchRequest.getContextLabel();
        String resourceLabel = launchRequest.getResourceLinkTitle();
        String resourceId = launchRequest.getResourceLinkId();
        String userId = launchRequest.getUserId();

        String userToken = null;
        try {
            userToken = AuthManager.getToken(org + userId);
            DocumentManager docMan = new DocumentManager(org + userId,
                    userToken);
            courseDocuments = docMan.getDocuments(org, contextLabel,
                    resourceLabel, resourceId);

            String type = ActivityManager.getActivityType(org, contextLabel, resourceLabel, resourceId);
            if (type.equals("personal")) {
                view.addObject("personal", true);
            } else if (type.equals("collective")) {
                view.addObject("collective", true);
            }
        } catch (Exception e) {
            logger.severe("/activity/teacherPanel.html");// TODO: Change logger
                                                         // call to log the
                                                         // errors
            return new ModelAndView("/error");
        }
        view.addObject("documents", courseDocuments);
        view.addObject("activity", launchRequest.getResourceLinkTitle());
        return view;
    }

    @RequestMapping(value = "/activity/gradePanel.html", method = RequestMethod.GET)
    public ModelAndView gradePanel(HttpServletRequest request,
            HttpServletResponse response) {
        logger.info("/activity/gradePanel.html");

        ModelAndView view = new ModelAndView("/activity/gradePanel");

        String docId = request.getParameter("docId");
        String gradeType = request.getParameter("gradeType");
        System.out.println("GRADE TYPE: " + gradeType);
        BLTILaunchRequest launchRequest = (BLTILaunchRequest) request
                .getSession().getAttribute("bltiRequest");

        String org = launchRequest.getToolConsumerInstanceGuid();
        String userId = launchRequest.getUserId();

        String userToken = null;
        GradeForm grade = new GradeForm();
        try {
            userToken = AuthManager.getToken(org + userId);
            DocumentManager docMan = new DocumentManager(org + userId,
                    userToken);
            Document doc = docMan.getDocumentById(Integer.valueOf(docId));
            grade.setUserId(doc.getUserId());
            grade.setDocId(Integer.toString(doc.getId()));

            view.addObject("gradeType", gradeType);
            view.addObject("doc", doc);
            view.addObject("activity", launchRequest.getResourceLinkTitle());

            if ((gradeType.equals("individual")) && (doc.getGraded())) {
                ReadResultServiceResponse readOutcomeResult = OutcomesService.readResult(launchRequest,
                        doc.getSourcedId());
                String gradeValue = readOutcomeResult.getScore();
                grade.setGrade(gradeValue);
                view.addObject("update", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.severe("/activity/gradePanel.html");// TODO: Change logger
                                                       // call to log the errors
            return new ModelAndView("/error");
        }

        view.addObject("newGrade", grade);
        view.addObject("activity", launchRequest.getResourceLinkTitle());
        return view;

    }

    @RequestMapping(value = "/activity/gradePanel.html", method = RequestMethod.POST)
    public ModelAndView submitGrade(
            @ModelAttribute("newGrade") GradeForm newGrade,
            BindingResult result, HttpServletRequest request,
            HttpServletResponse response) {
        logger.info("/activity/gradePanel.html");

        ModelAndView view = new ModelAndView("/activity/gradePanel").addObject(
                "newGrade", newGrade);
        gradeValidator.validate(newGrade, result);
        if (result.hasGlobalErrors()) {
            logger.severe("/activity/gradePanel.html");// TODO: Change logger
                                                       // call to log the errors
            return new ModelAndView("/error");
        }
        if (result.hasErrors()) {
            return view;
        }
        String gradeType = request.getParameter("gradeType");

        BLTILaunchRequest launchRequest = (BLTILaunchRequest) request
                .getSession().getAttribute("bltiRequest");
        String userId = launchRequest.getUserId();

        String grade = newGrade.getGrade();

        String docId = newGrade.getDocId();
        String org = launchRequest.getToolConsumerInstanceGuid();

        // Sending grade back to the LMS
        try {

            String userToken = AuthManager.getToken(org + userId);
            DocumentManager docMan = new DocumentManager(org + userId,
                    userToken);
            Document doc = docMan.getDocumentById(Integer.valueOf(docId));
            if (!doc.getUserId().equals(newGrade.getUserId())) {
                logger.severe("/activity/gradePanel.html");// TODO: Change
                                                           // logger
                // call to log the errors
                view = new ModelAndView("/error");
                return view;
            }

            Boolean gradeSuccess = docMan.grade(doc, grade, launchRequest);

            view.addObject("doc", doc);
            view.addObject("activity", launchRequest.getResourceLinkTitle());

            if (gradeSuccess) {
                view.addObject("success", true);
                view.addObject("update", true);
                view.addObject("gradeType", gradeType);
            } else {
                view.addObject("warning", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.severe("/activity/gradePanel.html");// TODO: Change logger
                                                       // call to log the errors
            view = new ModelAndView("/error");
        }

        return view;
    }

    @RequestMapping(value = "/activity/gradePanel.html", params = "submitCommon", method = RequestMethod.POST)
    public ModelAndView submitCommonGrade(
            @ModelAttribute("newGrade") GradeForm newGrade,
            BindingResult result, HttpServletRequest request,
            HttpServletResponse response) {
        logger.info("/activity/gradePanel.html");
        ModelAndView view = new ModelAndView("/activity/gradePanel").addObject(
                "newGrade", newGrade);
        gradeValidator.validate(newGrade, result);
        if (result.hasGlobalErrors()) {
            logger.severe("/activity/gradePanel.html");// TODO: Change logger
                                                       // call to log the errors
            return new ModelAndView("/error");
        }
        if (result.hasErrors()) {
            return view;
        }
        String gradeType = request.getParameter("gradeType");

        BLTILaunchRequest launchRequest = (BLTILaunchRequest) request
                .getSession().getAttribute("bltiRequest");
        String userId = launchRequest.getUserId();

        String grade = newGrade.getGrade();

        String docId = newGrade.getDocId();
        String org = launchRequest.getToolConsumerInstanceGuid();

        Boolean success = true;
        // Sending grade back to the LMS
        try {
            String userToken = AuthManager.getToken(org + userId);
            DocumentManager docMan = new DocumentManager(org + userId,
                    userToken);

            Document doc = docMan.getDocumentById(Integer.valueOf(docId));
            List<Document> l = new LinkedList<Document>();
            l = docMan.getActivityDocuments(doc.getDocId());
            ListIterator<Document> it = (ListIterator<Document>) l.listIterator();
            while (it.hasNext()) {
                Document newDoc;
                newDoc = it.next();

                System.out.println("Grading document " + newDoc.getDocId() + " for user " + newDoc.getUserId());
                Boolean gradeSuccess = docMan.grade(newDoc, grade, launchRequest);
                success = success && gradeSuccess;

                view.addObject("doc", newDoc);
            }

            if (success) {
                view.addObject("success", true);
                view.addObject("update", true);
                view.addObject("gradeType", gradeType);
            } else {
                view.addObject("warning", true);
            }

            view.addObject("activity", launchRequest.getResourceLinkTitle());

        } catch (Exception e) {
            e.printStackTrace();
            logger.severe("/activity/gradePanel.html");// TODO: Change logger
                                                       // call to log the errors
            view = new ModelAndView("/error");
        }

        return view;
    }

    @RequestMapping(value = "/activity/gradePanel.html", params = "delete", method = RequestMethod.POST)
    public ModelAndView deleteGrade(
            @ModelAttribute("newGrade") GradeForm newGrade,
            BindingResult result, HttpServletRequest request,
            HttpServletResponse response) {
        logger.info("/activity/gradePanel.html");

        ModelAndView view = new ModelAndView("/activity/gradePanel");
        gradeValidator.validate(newGrade, result);
        if (result.hasGlobalErrors()) {
            logger.severe("/activity/gradePanel.html");// TODO: Change logger
                                                       // call to log the errors
            return new ModelAndView("/error");
        }
        if (result.hasErrors()) {
            return view;
        }

        BLTILaunchRequest launchRequest = (BLTILaunchRequest) request
                .getSession().getAttribute("bltiRequest");
        String userId = launchRequest.getUserId();

        String docId = newGrade.getDocId();
        String org = launchRequest.getToolConsumerInstanceGuid();

        GradeForm grade = new GradeForm();

        // Sending grade back to the LMS
        try {
            String userToken = AuthManager.getToken(org + userId);
            DocumentManager docMan = new DocumentManager(org + userId,
                    userToken);
            Document doc = docMan.getDocumentById(Integer.valueOf(docId));
            if (!doc.getUserId().equals(newGrade.getUserId())) {
                logger.severe("/activity/gradePanel.html");// TODO: Change
                                                           // logger
                // call to log the errors
                view = new ModelAndView("/error");
                return view;
            }

            grade.setUserId(doc.getUserId());
            grade.setDocId(Integer.toString(doc.getId()));

            Boolean deleteSuccess = docMan.deleteGrade(doc, launchRequest);

            view.addObject("doc", doc);
            view.addObject("activity", launchRequest.getResourceLinkTitle());

            if (deleteSuccess) {
                view.addObject("success", true);
                view.addObject("delete", true);
            } else {
                view.addObject("warning", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.severe("/activity/gradePanel.html");// TODO: Change logger
                                                       // call to log the errors
            view = new ModelAndView("/error");
        }

        view.addObject("newGrade", grade);
        return view;
    }
}
