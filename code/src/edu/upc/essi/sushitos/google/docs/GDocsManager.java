package edu.upc.essi.sushitos.google.docs;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;

import com.google.common.collect.Iterables;
import com.google.gdata.client.DocumentQuery;
import com.google.gdata.client.authn.oauth.GoogleOAuthHelper;
import com.google.gdata.client.authn.oauth.GoogleOAuthParameters;
import com.google.gdata.client.authn.oauth.OAuthException;
import com.google.gdata.client.authn.oauth.OAuthRsaSha1Signer;
import com.google.gdata.client.docs.DocsService;
import com.google.gdata.client.media.MediaService;
import com.google.gdata.client.media.ResumableGDataFileUploader;
import com.google.gdata.data.Link;
import com.google.gdata.data.MediaContent;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.acl.AclEntry;
import com.google.gdata.data.acl.AclRole;
import com.google.gdata.data.acl.AclScope;
import com.google.gdata.data.docs.DocumentEntry;
import com.google.gdata.data.docs.DocumentListEntry;
import com.google.gdata.data.docs.DocumentListFeed;
import com.google.gdata.data.docs.FolderEntry;
import com.google.gdata.data.docs.PdfEntry;
import com.google.gdata.data.docs.PresentationEntry;
import com.google.gdata.data.docs.SpreadsheetEntry;
import com.google.gdata.data.media.MediaFileSource;
import com.google.gdata.data.media.MediaSource;
import com.google.gdata.util.ServiceException;
import com.google.gdata.util.common.util.Base64;
import com.google.gdata.util.common.util.Base64DecoderException;

import edu.upc.essi.sushitos.configuration.ToolConfiguration;
import edu.upc.essi.sushitos.ltigdocstool.document.Document;
import edu.upc.essi.sushitos.ltigdocstool.document.FileUploadProgressListener;

/**
 * GDocsManager class
 * 
 * It implements the connection with Google Docs.
 * 
 * @author ngalanis
 * @author jpiguillem
 * 
 */
public class GDocsManager {

    private DocsService docsClient = new DocsService("GDocs_LTI_Tool");
    private String accessToken;
    private String userId;
    private String consumerKey;
    private String scope;
    private String callback;
    private String path;
    private String privKeyPath;
    PrivateKey privKey;
    private GoogleOAuthHelper oauthHelper;

    /** Maximum number of concurrent uploads */
    private static final int MAX_CONCURRENT_UPLOADS = 10;

    /** Time interval at which upload task will notify about the progress */
    private static final int PROGRESS_UPDATE_INTERVAL = 1000;

    /** Max size for each upload chunk */
    private static final int DEFAULT_CHUNK_SIZE = 10000000;

    /** Default create-media-url for uploading documents */
    private static final String DEFAULT_RESUMABLE_UPLOAD_URL =
            "https://docs.google.com/feeds/upload/create-session/default/private/full";

    public GDocsManager(String userIdOrToken, String type)
            throws IOException, OAuthException {

        ToolConfiguration tc = ToolConfiguration.getToolConfiguration();
        consumerKey = tc.getProperty("google", "consumerkey");
        scope = tc.getProperty("google", "scope");
        callback = tc.getProperty("google", "callback");
        path = tc.getProperty("system", "path");
        privKeyPath = tc.getProperty("google", "privkeypath");
        privKey = getPrivateKey(path + privKeyPath);
        oauthHelper = new GoogleOAuthHelper(new OAuthRsaSha1Signer(privKey));
        if (type.equals("userId"))
            userId = userIdOrToken;
        else
            accessToken = userIdOrToken;
    }

    public String getAuthorizationLink() throws IOException, OAuthException {

        callback += "?userId=" + userId;
        GoogleOAuthParameters oauthParameters = new GoogleOAuthParameters();
        oauthParameters.setOAuthConsumerKey(consumerKey);
        // oauthParameters.setOAuthConsumerSecret(consumerSecret);
        oauthParameters.setScope(scope);
        oauthParameters.setOAuthCallback(callback);

        oauthHelper.getUnauthorizedRequestToken(oauthParameters);

        return oauthHelper.createUserAuthorizationUrl(oauthParameters);
    }

    public String retrieveToken(HttpServletRequest request)
            throws OAuthException {

        GoogleOAuthParameters oauthParameters = new GoogleOAuthParameters();
        oauthParameters.setOAuthConsumerKey(consumerKey);

        oauthHelper.getOAuthParametersFromCallback(request.getQueryString(),
                oauthParameters);

        if (oauthParameters.getOAuthVerifier() != "") {
            accessToken = oauthHelper.getAccessToken(oauthParameters);
        }

        return accessToken;
    }

    public Document refreshDocumentData(Document doc) throws OAuthException, IOException, ServiceException {

        URL feedUri = new URL(
                "https://docs.google.com/feeds/default/private/full/" + doc.getDocId());
        GoogleOAuthParameters oauthParameters = new GoogleOAuthParameters();
        oauthParameters.setOAuthConsumerKey(consumerKey);
        oauthParameters.setOAuthToken(accessToken);
        docsClient.setOAuthCredentials(oauthParameters, new OAuthRsaSha1Signer(
                privKey));
        DocumentEntry document = docsClient.getEntry(feedUri, DocumentEntry.class);

        doc.setTitle(document.getTitle().getPlainText());
        Link l = document.getDocumentLink();
        doc.setLink(new URL(l.getHref()));
        doc.setTimeCreated(document.getPublished().toStringRfc822());
        doc.setTimeModified(document.getUpdated().toStringRfc822());
        return doc;
    }

    public URL getDocumentLinkById(String docId) throws IOException,
            ServiceException, OAuthException {

        System.out.println(docId);

        URL feedUri = new URL(
                "https://docs.google.com/feeds/default/private/full/");
        GoogleOAuthParameters oauthParameters = new GoogleOAuthParameters();
        oauthParameters.setOAuthConsumerKey(consumerKey);
        oauthParameters.setOAuthToken(accessToken);
        docsClient.setOAuthCredentials(oauthParameters, new OAuthRsaSha1Signer(
                privKey));
        DocumentListFeed feed = docsClient.getFeed(feedUri,
                DocumentListFeed.class);

        for (DocumentListEntry entry : feed.getEntries()) {
            if (entry.getDocId().equals(docId)) {
                System.out.println("Found entry");
                Link l = entry.getDocumentLink();
                return new URL(l.getHref());
            }
        }
        return null;
    }

    public String getDocumentTitleById(String docId) throws IOException,
            ServiceException, OAuthException {

        URL feedUri = new URL(
                "https://docs.google.com/feeds/default/private/full/");
        GoogleOAuthParameters oauthParameters = new GoogleOAuthParameters();
        oauthParameters.setOAuthConsumerKey(consumerKey);
        oauthParameters.setOAuthToken(accessToken);
        docsClient.setOAuthCredentials(oauthParameters, new OAuthRsaSha1Signer(
                privKey));
        DocumentListFeed feed = docsClient.getFeed(feedUri,
                DocumentListFeed.class);

        for (DocumentListEntry entry : feed.getEntries()) {
            if (entry.getDocId().equals(docId)) {
                return entry.getTitle().getPlainText();
            }
        }
        return null;
    }

    public DocumentListEntry getDocumentById(String docId) throws IOException,
            ServiceException, OAuthException {

        URL feedUri = new URL(
                "https://docs.google.com/feeds/default/private/full/");
        GoogleOAuthParameters oauthParameters = new GoogleOAuthParameters();
        oauthParameters.setOAuthConsumerKey(consumerKey);
        oauthParameters.setOAuthToken(accessToken);
        docsClient.setOAuthCredentials(oauthParameters, new OAuthRsaSha1Signer(
                privKey));
        DocumentListFeed feed = docsClient.getFeed(feedUri,
                DocumentListFeed.class);

        for (DocumentListEntry entry : feed.getEntries()) {
            if (entry.getDocId().equals(docId)) {
                return entry;
            }
        }
        return null;
    }

    public DocumentListEntry getFolderByLink(String link) throws IOException,
            ServiceException, OAuthException {

        URL feedUri = new URL(
                "https://docs.google.com/feeds/default/private/full/-/folder");
        GoogleOAuthParameters oauthParameters = new GoogleOAuthParameters();
        oauthParameters.setOAuthConsumerKey(consumerKey);
        oauthParameters.setOAuthToken(accessToken);
        docsClient.setOAuthCredentials(oauthParameters, new OAuthRsaSha1Signer(
                privKey));
        DocumentListFeed feed = docsClient.getFeed(feedUri,
                DocumentListFeed.class);

        for (DocumentListEntry entry : feed.getEntries()) {
            if (((MediaContent) (entry.getContent())).getUri().equals(link)) {
                return entry;
            }
        }
        return null;
    }

    /**
     * Function used for debugging
     * 
     * @param feedUri
     * @throws OAuthException
     * @throws IOException
     * @throws ServiceException
     */
    private void debugList(URL feedUri) throws OAuthException, IOException,
            ServiceException {
        GoogleOAuthParameters oauthParameters = new GoogleOAuthParameters();
        oauthParameters.setOAuthConsumerKey(consumerKey);
        oauthParameters.setOAuthToken(accessToken);
        docsClient.setOAuthCredentials(oauthParameters, new OAuthRsaSha1Signer(
                privKey));
        DocumentListFeed feed = docsClient.getFeed(feedUri,
                DocumentListFeed.class);
        int i = 0;
        for (DocumentListEntry entry : feed.getEntries()) {
            System.out.println("Entry no." + i + ": "
                    + entry.getTitle().getPlainText());
            i++;
        }

    }

    // public void test (){
    //
    // GoogleOAuthParameters oauthParameters = new GoogleOAuthParameters();
    // oauthParameters.setOAuthConsumerKey(CONSUMER_KEY);
    // //oauthParameters.setOAuthConsumerSecret(CONSUMER_SECRET);
    // oauthParameters.setOAuthToken(accessToken);i
    //
    // DocsService client = new DocsService("yourCompany-YourAppName-v1");
    // try {
    // client.setOAuthCredentials(oauthParameters, new
    // OAuthRsaSha1Signer(privKey));
    // URL feedUrl = new
    // URL("https://docs.google.com/feeds/default/private/full/");
    // DocumentListFeed resultFeed = client.getFeed(feedUrl,
    // DocumentListFeed.class);
    // for (DocumentListEntry entry : resultFeed.getEntries()) {
    // System.out.println(entry.getTitle().getPlainText());
    // }
    // } catch (OAuthException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // } catch (ServiceException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // } catch (MalformedURLException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // } catch (IOException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }

    public DocumentListEntry createDocument(String org, String contextLabel,
            String resourceLinkTitle, String resourceLinkId, String familyName,
            String givenName, String userId, String docType, String activityType) throws IOException,
            ServiceException, OAuthException, DuplicatedFolderException {

        String toolFolder[] = {org, contextLabel, resourceLinkId + "-" + resourceLinkTitle};

        String fileName;

        if (activityType.equals("personal")) { // Personal document
            if ((familyName != null) && (givenName != null)) {
                fileName = familyName + "_" + givenName + "-" + resourceLinkTitle;
            } else {
                fileName = userId + "-" + resourceLinkTitle;
            }
        } else { // Collective document
            fileName = resourceLinkTitle + "-common_document";
        }

        System.out.println(fileName);
        DocumentListEntry folderEntry = createFolderHierarchy(toolFolder);
        String destFolderUrl = ((MediaContent) folderEntry.getContent())
                .getUri();
        URL folderUrl = new URL(destFolderUrl);
        DocumentListEntry documentEntry = createNewDocument(fileName, docType,
                folderUrl);

        System.out.println("Document now online in folder '"
                + folderEntry.getTitle().getPlainText() + "' @ :"
                + documentEntry.getDocumentLink().getHref());

        return documentEntry;
    }

    private DocumentListEntry createNewDocument(String title, String type,
            URL uri) throws IOException, ServiceException, OAuthException {

        DocumentListEntry newEntry = null;
        if (type.equals("document")) {
            newEntry = new DocumentEntry();
        } else if (type.equals("presentation")) {
            newEntry = new PresentationEntry();
        } else if (type.equals("spreadsheet")) {
            newEntry = new SpreadsheetEntry();
        }
        newEntry.setTitle(new PlainTextConstruct(title));

        // Prevent collaborators from sharing the document with others.
        newEntry.setWritersCanInvite(false);

        // You can also hide the document on creation
        // newEntry.setHidden(true);
        GoogleOAuthParameters oauthParameters = new GoogleOAuthParameters();
        oauthParameters.setOAuthConsumerKey(consumerKey);
        oauthParameters.setOAuthToken(accessToken);
        docsClient.setOAuthCredentials(oauthParameters, new OAuthRsaSha1Signer(
                privKey));
        return docsClient.insert(uri, newEntry);
    }

    /**
     * Create a folder hierarchy
     */
    private DocumentListEntry createFolderHierarchy(String pathElements[])
            throws IOException, ServiceException, OAuthException,
            DuplicatedFolderException {

        URL feedUrl = new URL(
                "https://docs.google.com/feeds/default/private/full/-/folder");

        int i;
        DocumentListEntry folderEntry = null;
        for (i = 0; i < pathElements.length; i++) {
            System.out.println("SEARCHING: " + pathElements[i]);
            folderEntry = findFolder(feedUrl, pathElements[i]);
            if (folderEntry == null) {
                if (i == 0)
                    feedUrl = new URL("https://docs.google.com/feeds/default/private/full");
                break;
            } else {
                feedUrl = new URL(((MediaContent) folderEntry.getContent()).getUri());
            }
        }

        GoogleOAuthParameters oauthParameters = new GoogleOAuthParameters();
        oauthParameters.setOAuthConsumerKey(consumerKey);
        oauthParameters.setOAuthToken(accessToken);
        docsClient.setOAuthCredentials(oauthParameters,
                new OAuthRsaSha1Signer(privKey));

        DocumentListEntry parentFolder = folderEntry;
        for (; i < pathElements.length; i++) {
            System.out.println("Creating: " + pathElements[i]);
            DocumentListEntry subFolder = new FolderEntry();
            subFolder.setTitle(new PlainTextConstruct(pathElements[i]));

            parentFolder = docsClient.insert(feedUrl, subFolder);
            feedUrl = new URL(((MediaContent) parentFolder.getContent()).getUri());
        }
        return parentFolder;
    }

    private DocumentListEntry findFolder(URL feedUri, String folderName)
            throws OAuthException, IOException, ServiceException,
            DuplicatedFolderException {

        DocumentQuery query = new DocumentQuery(feedUri);
        query.setTitleQuery(folderName);
        query.setTitleExact(true);
        query.setMaxResults(10);
        query.setStringCustomParameter("showdeleted", "false");

        GoogleOAuthParameters oauthParameters = new GoogleOAuthParameters();
        oauthParameters.setOAuthConsumerKey(consumerKey);
        oauthParameters.setOAuthToken(accessToken);
        docsClient.setOAuthCredentials(oauthParameters, new OAuthRsaSha1Signer(
                privKey));
        DocumentListFeed feed = docsClient.getFeed(query,
                DocumentListFeed.class);
        System.out.println(feedUri.toString());
        if (feed.getEntries(FolderEntry.class).isEmpty()) {
            System.out.println("EMPTY");
            return null;
        } else if (feed.getEntries(FolderEntry.class).size() == 1) {
            return feed.getEntries(FolderEntry.class).get(0);
        } else {
            String entriesList = "";
            Integer i = 0;
            for (DocumentListEntry entry : feed.getEntries(FolderEntry.class)) {
                entriesList += "Element" + i + ": "
                        + entry.getTitle() + " ";
                i++;
            }
            System.out.println("Found more than 2 folders with the same name: "
                    + folderName + ". Aborting: " + i + " " + entriesList);
            throw new DuplicatedFolderException();
        }
    }

    // /**
    // * Create a folder hierarchy
    // */
    // private DocumentListEntry createFolderHierarchy(String pathElements[])
    // throws IOException, ServiceException, OAuthException,
    // DuplicatedFolderException {
    //
    // URL feedUrl = new URL(
    // "https://docs.google.com/feeds/default/private/full/-/folder");
    //
    // DocumentListEntry subFolder = new FolderEntry();
    // subFolder.setTitle(new PlainTextConstruct(pathElements[0]));
    // System.out.println("Trying to create folder: " + pathElements[0]);
    // DocumentListEntry parentFolder = new FolderEntry();
    // // Check if folder exists already
    // DocumentListEntry exists = folderExists(feedUrl, pathElements[0]);
    // System.out.println("Exists: " + exists);
    //
    // if (exists == null) {
    // feedUrl = new URL(
    // "https://docs.google.com/feeds/default/private/full/");
    // GoogleOAuthParameters oauthParameters = new GoogleOAuthParameters();
    // oauthParameters.setOAuthConsumerKey(consumerKey);
    // oauthParameters.setOAuthToken(accessToken);
    // docsClient.setOAuthCredentials(oauthParameters,
    // new OAuthRsaSha1Signer(privKey));
    // parentFolder = docsClient.insert(feedUrl, subFolder);
    // System.out.println("Created folder: " + pathElements[0]
    // + " with URL: "
    // + ((MediaContent) parentFolder.getContent()).getUri());
    //
    // } else {
    // parentFolder = exists;
    // }
    //
    // for (int i = 1; i < pathElements.length; i++) {
    // String parentFolderUrl = ((MediaContent) parentFolder.getContent())
    // .getUri();
    // System.out.println("Parent folder URL: " + parentFolderUrl);
    // URL parentUrl = new URL(parentFolderUrl);
    // DocumentListEntry existent = folderExists(parentUrl,
    // pathElements[i]);
    //
    // if (existent == null) {
    // subFolder.setTitle(new PlainTextConstruct(pathElements[i]));
    // GoogleOAuthParameters oauthParameters = new GoogleOAuthParameters();
    // oauthParameters.setOAuthConsumerKey(consumerKey);
    // oauthParameters.setOAuthToken(accessToken);
    // docsClient.setOAuthCredentials(oauthParameters,
    // new OAuthRsaSha1Signer(privKey));
    // parentFolder = docsClient.insert(parentUrl, subFolder);
    // System.out.println("Created folder: " + pathElements[i]);
    // } else {
    // parentFolder = existent;
    // continue;
    // }
    // }
    // return parentFolder;
    // }
    //
    // private DocumentListEntry folderExists(URL feedUri, String folderName)
    // throws OAuthException, IOException, ServiceException,
    // DuplicatedFolderException {
    // System.out.println("Searching for folder '"+ folderName +"' in: " +
    // feedUri);
    // DocumentQuery query = new DocumentQuery(feedUri);
    // query.setTitleQuery(folderName);
    // query.setTitleExact(true);
    // query.setMaxResults(10);
    // query.setStringCustomParameter("showdeleted", "false");
    //
    // GoogleOAuthParameters oauthParameters = new GoogleOAuthParameters();
    // oauthParameters.setOAuthConsumerKey(consumerKey);
    // oauthParameters.setOAuthToken(accessToken);
    // docsClient.setOAuthCredentials(oauthParameters, new OAuthRsaSha1Signer(
    // privKey));
    // DocumentListFeed feed = docsClient.getFeed(query,
    // DocumentListFeed.class);
    // if (feed.getEntries(FolderEntry.class).isEmpty()) {
    // System.out.println("EMPTY");
    // return null;
    // } else if (feed.getEntries(FolderEntry.class).size() == 1) {
    // return feed.getEntries(FolderEntry.class).get(0);
    // } else {
    // String entriesList = "";
    // Integer i = 0;
    // for (DocumentListEntry entry : feed.getEntries(FolderEntry.class)) {
    // entriesList += "Element" + i + ": "
    // + entry.getTitle() + " ";
    // i++;
    // }
    // System.out.println("Found more than 2 folders with the same name: "
    // + folderName + ". Aborting: " + i + " " + entriesList);
    // throw new DuplicatedFolderException();
    // }
    // }

    private PrivateKey getPrivateKey(String privKeyFileName) {

        File privKeyFile = new File(privKeyFileName);
        FileInputStream fis;
        try {
            fis = new FileInputStream(privKeyFile);
            DataInputStream dis = new DataInputStream(fis);

            byte[] privKeyBytes = new byte[(int) privKeyFile.length()];
            dis.read(privKeyBytes);
            dis.close();
            fis.close();

            String BEGIN = "-----BEGIN PRIVATE KEY-----";
            String END = "-----END PRIVATE KEY-----";
            String str = new String(privKeyBytes);
            if (str.contains(BEGIN) && str.contains(END)) {
                str = str.substring(BEGIN.length(), str.lastIndexOf(END));
            }

            KeyFactory fac = KeyFactory.getInstance("RSA");
            EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(
                    Base64.decode(str));
            return fac.generatePrivate(privKeySpec);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Base64DecoderException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Set a new permission to a document or folder. Possible values for
     * roleDescription: "reader", "writer", "owner".
     */
    public void setRole(String email, String roleDescription,
            DocumentListEntry documentEntry) {
        AclRole role = new AclRole(roleDescription);
        AclScope scope = new AclScope(AclScope.Type.USER, email);
        try {
            AclEntry aclEntry = addAclRole(role, scope, documentEntry);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ServiceException e) {
            // TODO Auto-generated catch block
            System.out.println("DANGER");
            e.printStackTrace();
        } catch (OAuthException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private AclEntry addAclRole(AclRole role, AclScope scope,
            DocumentListEntry entry) throws IOException, MalformedURLException,
            ServiceException, OAuthException {
        AclEntry aclEntry = new AclEntry();
        aclEntry.setRole(role);
        aclEntry.setScope(scope);

        GoogleOAuthParameters oauthParameters = new GoogleOAuthParameters();
        oauthParameters.setOAuthConsumerKey(consumerKey);
        oauthParameters.setOAuthToken(accessToken);
        docsClient.setOAuthCredentials(oauthParameters, new OAuthRsaSha1Signer(
                privKey));
        return docsClient.insert(new URL(entry.getAclFeedLink().getHref()),
                aclEntry);
    }

    /**
     * Exports a document to a .pdf file in the same location as the original
     * 
     * @param docId
     * @param path
     * @return DocumentListEntry
     * @throws IOException
     * @throws ServiceException
     * @throws OAuthException
     * @throws InterruptedException
     */
    public DocumentListEntry exportToPdf(String docId, String path)
            throws IOException, ServiceException, OAuthException,
            InterruptedException {
        URL feedUri = new URL(
                "https://docs.google.com/feeds/default/private/full/");
        GoogleOAuthParameters oauthParameters = new GoogleOAuthParameters();
        oauthParameters.setOAuthConsumerKey(consumerKey);
        oauthParameters.setOAuthToken(accessToken);
        docsClient.setOAuthCredentials(oauthParameters, new OAuthRsaSha1Signer(
                privKey));
        DocumentListFeed feed = docsClient.getFeed(feedUri,
                DocumentListFeed.class);

        for (DocumentListEntry entry : feed.getEntries()) {
            if (entry.getDocId().equals(docId)) {
                String fileExtension = "pdf";
                String exportUrl = ((MediaContent) entry.getContent()).getUri()
                        + "&exportFormat=" + fileExtension;

                DocumentListEntry parentFolder = getFolderByLink(entry
                        .getParentLinks().get(0).getHref()
                        + "/contents");

                // Create the pdf file
                File folder = new File(path + "/temp");
                if (!folder.exists()) {
                    if (!folder.mkdir())
                        throw new IOException("Could not create folder "
                                + folder.getPath());
                }
                System.out
                        .println("Creating " + folder.getPath() + "/"
                                + entry.getTitle().getPlainText() + "."
                                + fileExtension);
                File newFile = new File(folder.getPath() + "/"
                        + entry.getTitle().getPlainText() + "." + fileExtension);
                Boolean success = newFile.createNewFile();
                if (!success) {
                    throw new IOException("Failed to create file: "
                            + newFile.getPath());
                }

                // Download the file on the server as a pdf
                downloadFile(exportUrl, newFile.getPath());

                String filePath = newFile.getPath();
                Collection<DocumentListEntry> pdfDocument = uploadFile(
                        filePath,
                        new URL(DEFAULT_RESUMABLE_UPLOAD_URL), DEFAULT_CHUNK_SIZE);

                // Attempt to delete the local file
                if (!newFile.delete())
                    throw new IOException(
                            "Could not delete local copy of the file: "
                                    + newFile.getPath());

                // Move the uploaded pdf file into the correct collection
                DocumentListEntry uploadedFile = Iterables.get(pdfDocument, 0);
                uploadedFile = moveToFolder(uploadedFile, parentFolder);

                // Return the pdf entry
                return uploadedFile;
            }
        }

        return null;
    }

    public Collection<DocumentListEntry> uploadFile(String filePath,
            URL uri, int chunkSize) throws OAuthException, IOException,
            ServiceException, InterruptedException {

        GoogleOAuthParameters oauthParameters = new GoogleOAuthParameters();
        oauthParameters.setOAuthConsumerKey(consumerKey);
        oauthParameters.setOAuthToken(accessToken);
        docsClient.setOAuthCredentials(oauthParameters, new OAuthRsaSha1Signer(
                privKey));

        // Create a listener
        FileUploadProgressListener listener = new FileUploadProgressListener();

        // Pool for handling concurrent upload tasks
        ExecutorService executor = Executors
                .newFixedThreadPool(MAX_CONCURRENT_UPLOADS);

        File file = new File(filePath);
        String contentType = DocumentListEntry.MediaType.fromFileName(
                    file.getName()).getMimeType();
        MediaFileSource mediaFile = new MediaFileSource(file, contentType);
        System.out.println("Uploading file " + mediaFile.getName() + " to: " + uri);
        ResumableGDataFileUploader uploader = new ResumableGDataFileUploader.Builder(
                    (MediaService) docsClient, uri, mediaFile, null)
                    .title(mediaFile.getName())
                    .chunkSize(DEFAULT_CHUNK_SIZE)
                    .executor(executor)
                    .trackProgress(listener, PROGRESS_UPDATE_INTERVAL)
                    .build();

        // attach the listener to list of uploaders
        listener.listenTo(uploader, "", mediaFile);

        // Start the upload
        uploader.start();

        // wait for uploads to complete
        while (!listener.isDone()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                throw ie; // rethrow
            }
        }

        // return list of uploaded entries
        return listener.getUploaded();

    }

    /**
     * Downloads a file from Google docs to the "filepath" location
     * 
     * @param exportUrl
     * @param filepath
     * @throws IOException
     * @throws MalformedURLException
     * @throws ServiceException
     * @throws OAuthException
     */
    public void downloadFile(String exportUrl, String filepath)
            throws IOException, MalformedURLException, ServiceException,
            OAuthException {

        System.out.println("Exporting document from: " + exportUrl);

        MediaContent mc = new MediaContent();
        mc.setUri(exportUrl);
        GoogleOAuthParameters oauthParameters = new GoogleOAuthParameters();
        oauthParameters.setOAuthConsumerKey(consumerKey);
        oauthParameters.setOAuthToken(accessToken);
        docsClient.setOAuthCredentials(oauthParameters, new OAuthRsaSha1Signer(
                privKey));
        MediaSource ms = docsClient.getMedia(mc);

        InputStream inStream = null;
        FileOutputStream outStream = null;

        try {
            inStream = ms.getInputStream();
            outStream = new FileOutputStream(filepath);

            int c;
            while ((c = inStream.read()) != -1) {
                outStream.write(c);
            }
        } finally {
            if (inStream != null) {
                inStream.close();
            }
            if (outStream != null) {
                outStream.flush();
                outStream.close();
            }
        }
    }

    public DocumentListEntry moveToFolder(DocumentListEntry sourceEntry, DocumentListEntry destFolderEntry)
            throws IOException, MalformedURLException, ServiceException {

        DocumentListEntry newEntry = null;

        String docType = sourceEntry.getType();
        if (docType.equals("document")) {
            newEntry = new DocumentEntry();
        } else if (docType.equals("presentation")) {
            newEntry = new PresentationEntry();
        } else if (docType.equals("spreadsheet")) {
            newEntry = new SpreadsheetEntry();
        } else if (docType.equals("folder")) {
            newEntry = new FolderEntry();
        } else if (docType.equals("pdf")) {
            newEntry = new PdfEntry();
        } else {
            newEntry = new DocumentListEntry(); // Unknown type
        }
        newEntry.setId(sourceEntry.getId());

        String destFolderUri = ((MediaContent) destFolderEntry.getContent()).getUri();

        return docsClient.insert(new URL(destFolderUri), newEntry);
    }

}
