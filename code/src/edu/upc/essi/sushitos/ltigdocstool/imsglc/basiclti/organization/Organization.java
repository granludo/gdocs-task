package edu.upc.essi.sushitos.ltigdocstool.imsglc.basiclti.organization;

/**
 * Organization class
 * It represents an organization registered into the platform.
 * 
 * @author ngalanis
 * @author jpiguillem
 *
 */
public class Organization {

	private String orgId;
	private String secret;

	public Organization(String newOrgId, String newSecret){
		// @TODO: Implement this method
		orgId = newOrgId;
		secret = newSecret;
	}
	
	public String getOrgId() {
		return orgId;
	}

	public String getSecret() {
		return secret;
	}
	
	
}
