/**
 * @deprecated
 * TODO: TOBEDELETED
 */
//package edu.upc.essi.sushitos.googlegdata;
//
//import java.net.URL;
//
//import com.google.gdata.client.docs.DocsService;
//import com.google.gdata.data.docs.DocumentListEntry;
//import com.google.gdata.data.docs.DocumentListFeed;
//
//import edu.upc.essi.sushitos.googledatainterface.GDataIface;
//import edu.upc.essi.sushitos.lis.Lis;
//
//public class CommonFunctions {
//	
//	public void openDocument(String email, int toolID) {
//		document = GDataIface.GetDocument(email, toolID);
//		
//		if (document) { // Document already exists
//			// Open the document in an iframe for editing
//			gDocs_OpenDocumentInIframe(document);
//		}
//		else { // Document does not exist; create it
//			Person learner = Lis.get_learner();
//			String documentID = GDataIface.CreateDocument(learner.email, toolID);
//			
//			// Open the document in an iframe for editing
//			gDocs_OpenDocumentInIframe(documentEntry);
//		}
//	}
//	
//	
//	// TODO: Modify to use OAuth
//	public String getDocsList(String name, String pass) {
//		try	{
//			DocsService service = new DocsService("Document List");
//			service.setUserCredentials(name, pass);
//			
//			
//			URL documentListFeedURL = new
//				URL("https://docs.google.com/feeds/default/private/full");
//	
//			DocumentListFeed feed = service.getFeed(documentListFeedURL, 
//										DocumentListFeed.class);
//	
//			String returns = "";
//			for (DocumentListEntry entry : feed.getEntries()) {
//				returns += entry.getTitle().getPlainText() + "<br>";
//			}
//	
//			return returns;
//		}
//		catch(Exception ex) {
//			String returns = "Exception: " + ex.getMessage();
//			return returns;
//		}
//	}
//}