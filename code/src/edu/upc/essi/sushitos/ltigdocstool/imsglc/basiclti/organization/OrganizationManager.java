package edu.upc.essi.sushitos.ltigdocstool.imsglc.basiclti.organization;

import java.io.IOException;
import java.sql.SQLException;

import edu.upc.essi.sushitos.ltigdocstool.database.DatabaseManager;
import edu.upc.essi.sushitos.ltigdocstool.database.NeedCreateException;

/**
 * OrganizationManager class
 * It manages all organization related functions
 * 
 * @author ngalanis
 * @author jpiguillem
 *
 */
public class OrganizationManager {

	public OrganizationManager(){
		
	}
	
	public Organization getOrganization(String orgId) throws ClassNotFoundException, SQLException, NeedCreateException, IOException, UnexistantOrganizationException{
		
	    DatabaseManager db = new DatabaseManager();
	    Organization org = db.getOrganization(orgId);
	   
		return org;
	}
	
	public Organization createNewOrganization(String orgId, String password) throws ClassNotFoundException, SQLException, NeedCreateException, DuplicatedOrganizationException, IOException{
		
		DatabaseManager db = new DatabaseManager();
		
		Organization org = new Organization(orgId, password);
		db.addOrganization(org);
		
		return org;
		
	}
	
}
