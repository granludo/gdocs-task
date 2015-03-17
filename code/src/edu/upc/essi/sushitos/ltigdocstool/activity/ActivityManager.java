package edu.upc.essi.sushitos.ltigdocstool.activity;

import java.io.IOException;
import java.sql.SQLException;

import edu.upc.essi.sushitos.ltigdocstool.database.DatabaseManager;
import edu.upc.essi.sushitos.ltigdocstool.database.NeedCreateException;

/**
 * ActivityManager class It implements all activity related methods
 * 
 * @author ngalanis
 * @author jpiguillem
 * 
 */
public class ActivityManager {

    public static boolean isConfigured(String orgId, String contextLabel, String resourceLabel, String resourceId) throws SQLException, ClassNotFoundException, NeedCreateException, IOException {
        DatabaseManager db;
        try {
            db = new DatabaseManager();
            db.getActivity(orgId, contextLabel, resourceLabel, resourceId);
        } catch (UnexistantActivityException e) {
            return false;
        }

        return true;
    }

    public static Activity createActivity(String orgId, String contextLabel, String resourceLabel, String resourceId, String activityType) throws ClassNotFoundException, SQLException, NeedCreateException, IOException, DuplicatedActivityException, UnexistantActivityException {
        DatabaseManager db = new DatabaseManager();
        return db.addActivity(orgId, contextLabel, resourceLabel, resourceId, activityType);

    }

    public static String getActivityType(String orgId, String contextLabel, String resourceLabel, String resourceId) throws SQLException, ClassNotFoundException, NeedCreateException, IOException {
        DatabaseManager db;
        String type = null;
        try {
            db = new DatabaseManager();
            Activity activity = db.getActivity(orgId, contextLabel, resourceLabel, resourceId);
            type = activity.getActivityType();
        } catch (UnexistantActivityException e) {
            return null;
        }

        return type;
    }
}
