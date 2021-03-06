package services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import controllers.DocumentController;
import entityClasses.Document;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.*;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class GoogleService {
    private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String CREDENTIALS_FOLDER = "./resources"; // Directory to store user credentials.
    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved credentials/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
    private static final String CLIENT_SECRET_DIR = "/client_secret.json";
    private static boolean finished = false;
    private static ArrayList<Document> documents = new ArrayList<Document>();
    @Inject
    private DocumentController documentController;

    public static String download(File file, Drive service) throws IOException {
        String encoding = "UTF-8";

        String fileId = file.getId();
        OutputStream result = new ByteArrayOutputStream();
        service.files().get(fileId)
                .executeMediaAndDownloadTo(result);
//        URL url = new URL(file.getWebContentLink());
//        URLConnection urlCon = url.openConnection();
//        InputStream is = urlCon.getInputStream();
//
//        ByteArrayOutputStream result = new ByteArrayOutputStream();
//
//        byte[] buffer = new byte[1024];
//        int length;
//        while ((length = is.read(buffer)) != -1) {
//            result.write(buffer, 0, length);
//        }
//
//        is.close();
        return result.toString();
    }

    public static boolean isFinished() {
        return (finished && documents.isEmpty());
    }

    public static void setFinishedFalse() {
        finished = false;
    }

    public static Document getNext() {
        return documents.size() != 0 ? documents.remove(documents.size() - 1) : null;
    }

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If there is no client_secret.
     */
    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {

        // Load client secrets.
        InputStream in = GoogleService.class.getResourceAsStream(CLIENT_SECRET_DIR);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(CREDENTIALS_FOLDER)))
                .setAccessType("offline")
                .build();
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    public void downloadFiles(String path) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        // Print the names and IDs for up to 10 files.
        FileList result = service.files().list().setQ("'" + path + "' in parents") //0B_R7SeoAotsmUUtYendIX04zRjA
                .setPageSize(600)
                .setFields("nextPageToken, files(id, name, webContentLink, webViewLink)")
                .execute();
        List<File> files = result.getFiles();

        Document temp;
        for (File f : files) {
            if (!documentController.existDocumentsUrl(f.getWebViewLink())) {
                temp = new Document();
                temp.setUrl(f.getWebContentLink());
                temp.setvUrl(f.getWebViewLink());
                temp.setDocName(f.getName());
                temp.setFile(download(f, service));
                documents.add(temp);
            }
        }
        finished = true;
    }
}



