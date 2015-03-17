package edu.upc.essi.sushitos.ltigdocstool.document;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import com.google.gdata.client.authn.oauth.OAuthException;
import com.google.gdata.data.docs.DocumentListEntry;
import com.google.gdata.util.ServiceException;

import edu.upc.essi.sushitos.configuration.ToolConfiguration;
import edu.upc.essi.sushitos.google.docs.DuplicatedFolderException;
import edu.upc.essi.sushitos.google.docs.GDocsManager;
import edu.upc.essi.sushitos.imsglc.basiclti.extensions.OutcomesService;
import edu.upc.essi.sushitos.imsglc.basiclti.extensions.serviceresponses.AttributeNotFoundException;
import edu.upc.essi.sushitos.imsglc.basiclti.extensions.serviceresponses.DeleteResultServiceResponse;
import edu.upc.essi.sushitos.imsglc.basiclti.extensions.serviceresponses.UpdateResultServiceResponse;
import edu.upc.essi.sushitos.imsglc.basiclti.httpserver.BLTILaunchRequest;
import edu.upc.essi.sushitos.imsglc.lis.rolemanager.NoProfessorException;
import edu.upc.essi.sushitos.imsglc.lis.rolemanager.Professor;
import edu.upc.essi.sushitos.imsglc.lis.rolemanager.ProfessorManager;
import edu.upc.essi.sushitos.ltigdocstool.activity.Activity;
import edu.upc.essi.sushitos.ltigdocstool.activity.UnexistantActivityException;
import edu.upc.essi.sushitos.ltigdocstool.database.DatabaseManager;
import edu.upc.essi.sushitos.ltigdocstool.database.NeedCreateException;
import edu.upc.essi.sushitos.ltigdocstool.session.AuthManager;
import edu.upc.essi.sushitos.ltigdocstool.session.UnexistantUserException;

/**
 * DocumentManager class It implements all document related functions
 * 
 * @author ngalanis
 * @author jpiguillem
 * 
 */
public class DocumentManager {

    private String userId;
    private String userToken;

    public DocumentManager(String userId, String userToken) {

        this.userId = userId;
        this.userToken = userToken;

    }

    public Document getDocument(String org, String contextLabel,
            String resourceLabel, String resourceId, String userId)
            throws UnexistantDocument, OAuthException, ServiceException {

        // TODO set title and link
        DatabaseManager dm;
        Document doc = null;
        try {
            dm = new DatabaseManager();
            // TODO set title and link
            System.out.println("searching db for: " + org + " " + userId + " "
                    + resourceId);
            doc = dm.getDocument(org, contextLabel, resourceLabel,
                    resourceId, userId);
            GDocsManager gdocs = new GDocsManager(userToken, "userToken");
            doc = gdocs.refreshDocumentData(doc);

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
        return doc;
    }

    public List<Document> getDocuments(String org, String contextLabel,
            String resourceLabel, String resourceId) {

        List<Document> l = new LinkedList<Document>();

        try {
            DatabaseManager dm = new DatabaseManager();
            l = dm.getDocuments(org, contextLabel, resourceLabel, resourceId);
            GDocsManager gdocs = new GDocsManager(userToken, "userToken");
            ListIterator<Document> it = (ListIterator<Document>) l
                    .listIterator();
            while (it.hasNext()) {
                Document doc;
                doc = it.next();
                doc = gdocs.refreshDocumentData(doc);
            }
            // TODO set title and link
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
        } catch (OAuthException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return l;
    }

    public Document getDocumentById(Integer id) {

        Document doc = null;
        try {
            DatabaseManager dm = new DatabaseManager();
            doc = dm.getDocumentById(id);
            GDocsManager gdocs = new GDocsManager(userToken, "userToken");
            // URL link = gdocs.getDocumentLinkById(doc.getDocId());
            // String title = gdocs.getDocumentTitleById(doc.getDocId());
            // doc.setLink(link);
            // doc.setTitle(title);
            doc = gdocs.refreshDocumentData(doc);
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
        } catch (OAuthException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return doc;
    }

    public List<Document> getActivityDocuments(String docId) {
        List<Document> l = new LinkedList<Document>();

        try {
            DatabaseManager dm = new DatabaseManager();
            l = dm.getActivityDocuments(docId);
            GDocsManager gdocs = new GDocsManager(userToken, "userToken");
            // URL link = gdocs.getDocumentLinkById(doc.getDocId());
            // String title = gdocs.getDocumentTitleById(doc.getDocId());
            // doc.setLink(link);
            // doc.setTitle(title);
            ListIterator<Document> it = (ListIterator<Document>) l.listIterator();
            while (it.hasNext()) {
                Document doc;
                doc = it.next();
                doc = gdocs.refreshDocumentData(doc);
            }
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
        } catch (OAuthException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return l;
    }

    public List<Document> getSubmittedDocuments(String org,
            String contextLabel, String resourceLabel, String resourceId) throws OAuthException, ServiceException {

        List<Document> l = new LinkedList<Document>();

        try {
            DatabaseManager dm = new DatabaseManager();
            l = dm.getSubmittedDocuments(org, contextLabel, resourceLabel,
                    resourceId);

            GDocsManager gdocs = new GDocsManager(userToken, "userToken");
            ListIterator<Document> it = (ListIterator<Document>) l
                    .listIterator();
            while (it.hasNext()) {
                Document doc;
                doc = it.next();
                doc = gdocs.refreshDocumentData(doc);
            }
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

        return l;
    }

    public Document getCollectiveDocument(String org, String contextLabel,
            String resourceLabel, String resourceId) {
        Document doc = null;

        try {
            DatabaseManager dm = new DatabaseManager();
            Activity act = dm.getActivity(org, contextLabel, resourceLabel, resourceId);
            Integer actId = act.getId();
            doc = dm.getDocumentByActivityId(actId);
            GDocsManager gdocs = new GDocsManager(userToken, "userToken");
            doc = gdocs.refreshDocumentData(doc);
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
        } catch (UnexistantActivityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnexistantDocument e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OAuthException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return doc;
    }

    public Document createDocument(String org, String contextLabel,
            String resourceLabel, String resourceId, String familyName,
            String givenName, String userId, String sourcedId, String activityType)
            throws IOException, OAuthException, ServiceException,
            DuplicatedFolderException, ClassNotFoundException, SQLException,
            NeedCreateException, DuplicatedDocumentException,
            UnexistantActivityException {

        DocumentListEntry docListEntry;
        GDocsManager gdocsMan = new GDocsManager(userToken, "userToken");

        docListEntry = gdocsMan.createDocument(org, contextLabel,
                resourceLabel, resourceId, familyName, givenName, userId,
                "document", activityType);

        ProfessorManager pm = new ProfessorManager();
        List<Professor> professors = pm.getProfessors(resourceId);
        ListIterator<Professor> pit = professors.listIterator();
        while (pit.hasNext()) {
            Professor p = pit.next();
            gdocsMan.setRole(p.getEmail(), "writer", docListEntry);
        }

        String docId = docListEntry.getDocId();
        DatabaseManager dm = new DatabaseManager();
        Document doc = dm.addDocument(docId, org, contextLabel, resourceLabel,
                resourceId, userId, sourcedId, false, false);

        doc = gdocsMan.refreshDocumentData(doc);

        return doc;
    }

    public Document submit(Document doc, String org, String userId,
            String resourceId) throws IOException, ServiceException {

        try {
            System.out.println("Submitting document for evaluation: "
                    + doc.getId() + "  " + doc.getUserId());
            DatabaseManager dm = new DatabaseManager();

            // Export document to pdf
            String userToken = AuthManager.getToken(org + userId);
            GDocsManager gdocs = new GDocsManager(userToken, "userToken");
            ToolConfiguration tc = ToolConfiguration.getToolConfiguration();
            String path = tc.getProperty("system", "path");
            DocumentListEntry pdfFile = gdocs.exportToPdf(doc.getDocId(), path);

            doc = dm.submitDocument(doc.getId(), pdfFile.getDocId());

            // Share pdfFile with professor
            String[] professorMail = dm.getProfessorMails(resourceId);
            for (int i = 0; i < professorMail.length; i++) {
                gdocs.setRole(professorMail[i], "writer", pdfFile);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NeedCreateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnexistantUserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OAuthException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoProfessorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return doc;
    }

    public Boolean grade(Document doc, String grade,
            BLTILaunchRequest launchRequest) throws IOException,
            ServiceException, AttributeNotFoundException,
            ParserConfigurationException, SAXException, Exception {

        try {
            UpdateResultServiceResponse setOutcomeResult = OutcomesService
                    .updateResult(grade, launchRequest, doc.getSourcedId());

            if (setOutcomeResult.getCodemajor().equals("Success")) {
                DatabaseManager dm = new DatabaseManager();
                dm.gradeDocument(doc.getId());
                doc.setGraded();

                return true;
            }
        } catch (Exception e) {
            throw new Exception("Internal Error");
        }

        return false;
    }

    public Boolean deleteGrade(Document doc, BLTILaunchRequest launchRequest)
            throws IOException, ServiceException, ParserConfigurationException,
            SAXException, XPathExpressionException, AttributeNotFoundException,
            Exception {

        try {
            DeleteResultServiceResponse deleteOutcomeResult = OutcomesService
                    .deleteResult(launchRequest, doc.getSourcedId());

            if (deleteOutcomeResult.getCodemajor().equals("Success")) {
                DatabaseManager dm = new DatabaseManager();
                dm.deleteDocumentGrade(doc.getId());
                doc.unsetGraded();

                return true;
            }
        } catch (Exception e) {
            throw new Exception("Internal Error");
        }

        return false;
    }

    public Boolean shareDocument(Document doc, String email, String userId, String sourcedId) {
        Boolean success = false;

        try {
            DatabaseManager dm = new DatabaseManager();

            // Recover the token of a user already participating in the document
            String token = dm.getUserToken(doc.getUserId());

            GDocsManager gdocsMan = new GDocsManager(token, "userToken");
            // Recover the DocumentListEntry of the file we want shared
            System.out.println("Trying get document " + doc.getDocId());
            DocumentListEntry dle = gdocsMan.getDocumentById(doc.getDocId());

            System.out.println("Trying to share document " + dle.getDocId());
            // Set user as writer
            gdocsMan.setRole(email, "writer", dle);

            // Add document entry for this user
            dm.addDocument(doc.getDocId(), doc.getOrg(), doc.getContextLabel(), doc.getResourceLabel(),
                    doc.getResourceId(), userId, sourcedId, false, false);

            success = true;

            // Use the recovered token to share the file with the current user
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
        } catch (UnexistantUserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OAuthException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DuplicatedDocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnexistantActivityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return success;
    }
}
