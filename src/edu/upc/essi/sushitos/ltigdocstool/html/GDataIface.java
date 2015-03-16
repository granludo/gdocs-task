package edu.upc.essi.sushitos.ltigdocstool.html;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gdata.client.DocumentQuery;
import com.google.gdata.client.docs.*;
import com.google.gdata.data.Link;
import com.google.gdata.data.MediaContent;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.acl.AclEntry;
import com.google.gdata.data.acl.AclFeed;
import com.google.gdata.data.acl.AclRole;
import com.google.gdata.data.acl.AclScope;
import com.google.gdata.data.docs.DocumentEntry;
import com.google.gdata.data.docs.DocumentListEntry;
import com.google.gdata.data.docs.DocumentListFeed;
import com.google.gdata.data.docs.FolderEntry;
import com.google.gdata.data.docs.PresentationEntry;
import com.google.gdata.data.docs.SpreadsheetEntry;
import com.google.gdata.data.media.MediaSource;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

/**
 * @deprecated
 * TODO: TOBEDELETED
 *
 */
public class GDataIface {

	private DocsService client = new DocsService("GDocs_LTI_Tool");
	
	public GDataIface(String username, String pass) {
		try {
			client.setUserCredentials(username, pass);
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	public DocumentListEntry createDocument(String org, String contextLabel, String resourceLinkTitle,
//										String resourceLinkId, String familyName, String givenName, String type) throws Exception {
//		try {
//			String toolFolder[] = {org,	contextLabel, resourceLinkTitle, resourceLinkId};
//			
//			// TODO Construct the file name
//			String fileName = org + "_" +
//								contextLabel + "_" +
//								resourceLinkTitle + "_" +
//								resourceLinkId + "_" +
//								familyName + "_" + givenName;
//
//			DocumentListEntry folderEntry = createFolderHierarchy(toolFolder);
//			String destFolderUrl = ((MediaContent)folderEntry.getContent()).getUri();
//			URL folderUrl = new URL(destFolderUrl);
//			DocumentListEntry documentEntry = createNewDocument(fileName, type, folderUrl);
//			
//			System.out.println("Document now online in folder '" +
//				    folderEntry.getTitle().getPlainText() + "' @ :" + documentEntry.getDocumentLink().getHref());
//			
//			return documentEntry;
//		}
//		catch(Exception ex) {
//			throw ex;
//		}
//	}
	
//	public DocumentListEntry createNewDocument(String title, String type, URL uri) throws IOException, ServiceException {
//		DocumentListEntry newEntry = null;
//		if (type.equals("document")) {
//		  	newEntry = new DocumentEntry();
//	  	} else if (type.equals("presentation")) {
//	  		newEntry = new PresentationEntry();
//		} else if (type.equals("spreadsheet")) {
//			newEntry = new SpreadsheetEntry();
//		}
//		newEntry.setTitle(new PlainTextConstruct(title));
//	
//		// Prevent collaborators from sharing the document with others.
//		newEntry.setWritersCanInvite(false);
//	
//		// You can also hide the document on creation
//		// newEntry.setHidden(true);
//	
//		return client.insert(uri, newEntry);
//	}
	
//	public Link getDocumentLinkById(String docId) throws IOException, ServiceException {
//		  URL feedUri = new URL("https://docs.google.com/feeds/default/private/full/");
//		  DocumentListFeed feed = client.getFeed(feedUri, DocumentListFeed.class);
//
//		  for (DocumentListEntry entry : feed.getEntries()) {
//		    if (entry.getDocId().equals(docId)) {
//		    	System.out.println("Found entry");
//		    	return entry.getDocumentLink();
//		    }
//		  }
//		  return null;
//		}
//	
//	public String getDocumentTitleById(String docId) throws IOException, ServiceException {
//		  URL feedUri = new URL("https://docs.google.com/feeds/default/private/full/");
//		  DocumentListFeed feed = client.getFeed(feedUri, DocumentListFeed.class);
//
//		  for (DocumentListEntry entry : feed.getEntries()) {
//		    if (entry.getResourceId().equals(docId)) {
//		    	return entry.getTitle().getPlainText();
//		    }
//		  }
//		  return null;
//	}
	
//	public DocumentListEntry getFolderByLink(String link) throws IOException, ServiceException {
//		  URL feedUri = new URL("https://docs.google.com/feeds/default/private/full/-/folder");
//		  DocumentListFeed feed = client.getFeed(feedUri, DocumentListFeed.class);
//
//		  for (DocumentListEntry entry : feed.getEntries()) {
//		    if (((MediaContent)(entry.getContent())).getUri().equals(link)) {
//		    	return entry;
//		    }
//		  }
//		  return null;
//	}
	
	// TODO Rework this function as there can be more than 1 documents
	// with the same title.
	/*
	public Link getDocumentByTitle(String title) throws Exception {
		try	{
			URL feedUri = new URL("https://docs.google.com/feeds/default/private/full/");
			DocumentQuery query = new DocumentQuery(feedUri);
			query.setTitleQuery(title);
			query.setTitleExact(true);
			query.setMaxResults(10);
			DocumentListFeed feed = client.getFeed(query, DocumentListFeed.class);
	
			Link[] documentLinks = {};
			int i = 0;
			for (DocumentListEntry entry : feed.getEntries()) {
				Link documentLink = entry.getDocumentLink();
				documentLinks[i] = documentLink;
				i++;
			}
			
			int noOfDocs = documentLinks.length;
			if (noOfDocs == 1) {
				return documentLinks[0];
			}
			else if (noOfDocs > 1) {
				
			}
		}
		catch(Exception ex) {
			throw ex;
		}
	} */
	
//	/**
//	 * Set a new permission to a document or folder.
//	 * Possible values for roleDescription: "reader", "writer", "owner".
//	 */
//	public void setRole(String email, String roleDescription, DocumentListEntry documentEntry) {
//		AclRole role = new AclRole(roleDescription);
//		AclScope scope = new AclScope(AclScope.Type.USER, email);
//		try {
//			AclEntry aclEntry = addAclRole(role, scope, documentEntry);
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ServiceException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	public AclEntry addAclRole(AclRole role, AclScope scope, DocumentListEntry entry)
//    		throws IOException, MalformedURLException, ServiceException  {
//		AclEntry aclEntry = new AclEntry();
//		aclEntry.setRole(role);
//		aclEntry.setScope(scope);
//
//		return client.insert(new URL(entry.getAclFeedLink().getHref()), aclEntry);
//	}
	
	/*
	 * Remove sharing permissions
	 */
	public void removePermissions(String email, DocumentListEntry documentEntry) throws MalformedURLException, IOException, ServiceException {
		AclFeed aclFeed = client.getFeed(new URL(documentEntry.getAclFeedLink().getHref()), AclFeed.class);
		for (AclEntry entry : aclFeed.getEntries()) {
			if (entry.getScope().getValue().equals(email)) {
				entry.delete();
			}
		}
	}
	
//	private DocumentListEntry folderExists(URL feedUri, String folderName) throws Exception {
//		DocumentQuery query = new DocumentQuery(feedUri);
//		query.setTitleQuery(folderName);
//		query.setTitleExact(true);
//		query.setMaxResults(10);
//		query.setStringCustomParameter("showdeleted", "false");
//		DocumentListFeed feed = client.getFeed(query, DocumentListFeed.class);
//		if (feed.getEntries().isEmpty()) {
//			return null;
//		}
//		else if (feed.getEntries().size() == 1) {
//			return feed.getEntries().get(0);
//		}
//		else {
//			String entriesList = "";
//			Integer i = 0;
//			for (DocumentListEntry entry : feed.getEntries()) {
//				entriesList += "Element" + i +": " + entry.getType().toString() + " ";
//				i++;
//			}
//			System.out.println("Found more than 2 folders with the same name: " + folderName + ". Aborting: " + i + " " + entriesList);
//			throw new Exception("Found more than 2 folders with the same name: " + folderName + ". Aborting: " + i + " " + entriesList);
//		}
//	}
	
	/*
	 * Create a folder hierarchy
	 */
//	private DocumentListEntry createFolderHierarchy(String pathElements[]) throws Exception {
//		URL feedUrl = new URL("https://docs.google.com/feeds/default/private/full/-/folder");
//
//		DocumentListEntry subFolder = new FolderEntry();
//		subFolder.setTitle(new PlainTextConstruct(pathElements[0]));
//
//		DocumentListEntry parentFolder = new FolderEntry();
//		// Check if folder exists already
//		DocumentListEntry exists = folderExists(feedUrl, pathElements[0]);
//		
//		if (exists == null) {
//			parentFolder = client.insert(feedUrl, subFolder);
//			System.out.println("Created folder: " + pathElements[0]);
//		}
//		else {
//			parentFolder = exists;
//		}
//
//		for (int i = 1; i < pathElements.length; i++) {
//			String parentFolderUrl = ((MediaContent)parentFolder.getContent()).getUri();
//			URL parentUrl = new URL(parentFolderUrl);
//			DocumentListEntry existent = folderExists(parentUrl, pathElements[i]);
//
//			if (existent == null) {
//				subFolder.setTitle(new PlainTextConstruct(pathElements[i]));
//				parentFolder = client.insert(parentUrl, subFolder);
//				System.out.println("Created folder: " + pathElements[i]);
//			}
//			else {
//				parentFolder = existent;
//				continue;
//			}
//		}
//		return parentFolder;
//	}
	
	/**
	 * Create a folder named "title"
	 * 
	 * @param title
	 * @return DocumentListEntry
	 * @throws IOException
	 * @throws ServiceException
	 */
//	private DocumentListEntry createFolder(String title) throws IOException, ServiceException {
//		DocumentListEntry newEntry = new FolderEntry();
//		newEntry.setTitle(new PlainTextConstruct(title));
//		URL feedUrl = new URL("https://docs.google.com/feeds/default/private/full/");
//		return client.insert(feedUrl, newEntry);
//	}
	
	/**
	 * Exports a document to a .pdf file in the same location as the original
	 * 
	 * @param docId
	 * @param path
	 * @return DocumentListEntry
	 * @throws IOException
	 * @throws ServiceException
	 */
//	public DocumentListEntry exportToPdf(String docId, String path) throws IOException, ServiceException {
//		URL feedUri = new URL("https://docs.google.com/feeds/default/private/full/");
//		DocumentListFeed feed = client.getFeed(feedUri, DocumentListFeed.class);
//
//		for (DocumentListEntry entry : feed.getEntries()) {
//			if (entry.getDocId().equals(docId)) {
//				String fileExtension = "pdf";
//				String exportUrl = ((MediaContent) entry.getContent()).getUri() + "&exportFormat=" + fileExtension;
//
//				DocumentListEntry parentFolder = getFolderByLink(entry.getParentLinks().get(0).getHref() + "/contents");
//				
//				// Create the pdf file
//				File folder = new File(path + "/temp");
//				if (!folder.exists()) {
//					if (!folder.mkdir()) throw new IOException("Could not create folder " + folder.getPath());
//				}
//				File newFile = new File(folder.getPath() + "/" + entry.getTitle().getPlainText() + "." + fileExtension);
//				Boolean success = newFile.createNewFile();
//				if (!success) {
//					throw new IOException("Failed to create file: " + newFile.getPath());
//				}				
//				
//				// Download the file on the server as a pdf
//				downloadFile(exportUrl, newFile.getPath());
//				
//				// Upload the pdf from the server to Google Docs
//				DocumentListEntry pdfDocument = uploadFile(newFile.getPath(), entry.getTitle().getPlainText(), new URL(((MediaContent)parentFolder.getContent()).getUri()));
//
//				// Attempt to delete the local file
//				if (!newFile.delete()) throw new IOException("Could not delete local copy of the file: " + newFile.getPath());
//				
//				// Return the pdf entry
//				return pdfDocument;
//			}
//		}
//		
//		return null;
//	}
//	
	/**
	 *  Uploads a file to a particular folder specified by the URL argument
	 * @param filepath
	 * @param title
	 * @param uri
	 * @return
	 * @throws IOException
	 * @throws ServiceException
	 */
//	public DocumentListEntry uploadFile(String filepath, String title, URL uri)
//	    throws IOException, ServiceException  {
//		File file = new File(filepath);
//		DocumentListEntry newDocument = new DocumentListEntry();
//		String mimeType = DocumentListEntry.MediaType.fromFileName(file.getName()).getMimeType();
//		newDocument.setFile(new File(filepath), mimeType);
//		newDocument.setTitle(new PlainTextConstruct(title));
//
//		return client.insert(uri, newDocument);
//	}
//
	/**
	 * Downloads a file from Google docs to the "filepath" location
	 * 
	 * @param exportUrl
	 * @param filepath
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws ServiceException
	 */
//	public void downloadFile(String exportUrl, String filepath)
//    		throws IOException, MalformedURLException, ServiceException {
//		
//		System.out.println("Exporting document from: " + exportUrl);
//
//		MediaContent mc = new MediaContent();
//		mc.setUri(exportUrl);
//		MediaSource ms = client.getMedia(mc);
//
//		InputStream inStream = null;
//		FileOutputStream outStream = null;
//
//		try {
//			inStream = ms.getInputStream();
//			outStream = new FileOutputStream(filepath);
//
//			int c;
//			while ((c = inStream.read()) != -1) {
//				outStream.write(c);
//			}
//		} finally {
//			if (inStream != null) {
//				inStream.close();
//			}
//			if (outStream != null) {
//    			outStream.flush();
//      			outStream.close();
//    		}
//  		}
//	}

}
