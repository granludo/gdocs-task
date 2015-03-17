package edu.upc.essi.sushitos.imsglc.lis.rolemanager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import edu.upc.essi.sushitos.ltigdocstool.database.DatabaseManager;
import edu.upc.essi.sushitos.ltigdocstool.database.NeedCreateException;

/**
 * ProfessorManager class contains all professor definition and setup related
 * functions
 * 
 * @author ngalanis
 * @author jpiguillem
 * 
 */
public class ProfessorManager {

    public ProfessorManager() {

    }

    public List<Professor> getProfessors(String resourceId) {
        List<Professor> professors = new LinkedList<Professor>();

        try {
            DatabaseManager dm;
            dm = new DatabaseManager();
            professors = dm.getActivityProfessors(resourceId);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NeedCreateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return professors;
    }

    public Boolean newProfessor(String resourceId, String email) throws DuplicatedProfessorException {
        Boolean success = false;
        try {
            DatabaseManager dm;
            dm = new DatabaseManager();
            dm.setActivityProfessor(resourceId, email);
            success = true;
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NeedCreateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return success;
    }

    public Boolean removeProfessor(String resourceId, String email) {
        Boolean success = false;

        DatabaseManager dm;
        try {
            dm = new DatabaseManager();
            success = dm.removeActivityProfessor(resourceId, email);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NeedCreateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return success;
    }
}
