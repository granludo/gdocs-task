package edu.upc.essi.sushitos.ltigdocstool.html;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.upc.essi.sushitos.imsglc.basiclti.httpserver.BLTILaunchRequest;

/**
 * @deprecated
 * TODO: TOBEDELETED
 *
 */
public abstract class UserInterface {
    
	public abstract void printScreen(BLTILaunchRequest launchRequest,
			HttpServletRequest request, HttpServletResponse response) throws IOException;
	
}
