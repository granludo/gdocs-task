package edu.upc.essi.sushitos.ltigdocstool.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import edu.upc.essi.sushitos.log.SystemLogger;
import edu.upc.essi.sushitos.ltigdocstool.database.DatabaseManager;
import edu.upc.essi.sushitos.ltigdocstool.database.NeedCreateException;
import edu.upc.essi.sushitos.ltigdocstool.database.NeedUpgradeException;


/**
 * SetupController class
 * Spring controller for setup related requests
 * 
 * @author ngalanis
 * @author jpiguillem
 *
 */
@Controller
public class SetupController {
    protected final SystemLogger logger = SystemLogger.getSystemLogger();


    @RequestMapping(value = "/admin/setup.html", method = RequestMethod.GET)
    public ModelAndView setup(){
        // TODO: ADD CONTROL ACCESS AND LOGGING
        logger.info("/admin/setup.html");
        ModelAndView view = new ModelAndView("/admin/setup");
        DatabaseManager dbManager;
        try {
             dbManager = new DatabaseManager();
             if (!dbManager.isDatabaseCreated()){
                 dbManager.createDatabase();
                 view.addObject("success", true);
             } else {
                 view.addObject("upgrade", true);
             }
        } catch (NeedCreateException e) {
                view.addObject("createDatabase", true);
        } catch (NeedUpgradeException e) {
                view.addObject("updateDatabase", true);
        } catch (Exception e) {
            logger.severe("/admin/setup.html");//TODO: Change logger call to log the errors
            return new ModelAndView("/error");
        }
        return view;
    }

}
