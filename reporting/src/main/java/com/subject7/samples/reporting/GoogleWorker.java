package com.subject7.samples.reporting;

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
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GoogleWorker {
    private static final String APPLICATION_NAME = "Subject 7 Google Sheet Sample";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_DEFAULT_PATH = "tokens";
    private static final List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS, DriveScopes.DRIVE);

    private final static String FOLDER_MIME_TYPE = "application/vnd.google-apps.folder";
    private final static String FILE_QUERY_TEMPLATE = "mimeType='%s' and name = '%s' and trashed=false";
    private final static String CHILD_FILE_QUERY_TEMPLATE = "mimeType='%s' and name = '%s' and '%s' in parents and trashed=false";

    private String credentialsFilePath;
    private String tokensDirectoryPath;
    private String sheetId;
    private Sheets sheetService;
    private Drive driveService;

    public GoogleWorker(String credentialsFilePath, String tokens, String sheetId, String sheetPath, String sheetTitle) throws Exception {
        this.credentialsFilePath = credentialsFilePath;
        this.tokensDirectoryPath = StringUtils.isBlank(tokens) ? TOKENS_DIRECTORY_DEFAULT_PATH : tokens;
        this.sheetId = sheetId;

        if (StringUtils.isBlank(sheetId) && (StringUtils.isBlank(sheetPath) || StringUtils.isBlank(sheetTitle))) {
            throw new Exception("Existed sheet ID or path with title for new sheet should be provided");
        }

        if (StringUtils.isNotBlank(sheetId) && StringUtils.isNotBlank(sheetPath)) {
            throw new Exception("Only one parameter should be provided: existed sheet ID or path for new sheet");
        }

        // Build a new authorized API client service for Google Sheets
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        this.sheetService = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        // Build a new authorized API client service for Google Drive (Create sheet file)
        this.driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        if (StringUtils.isBlank(sheetId)) {
            sheetId = createSheet(sheetPath, sheetTitle);
        }
        this.sheetId = sheetId;
    }

    private String createSheet(String sheetPath, String sheetTitle) throws Exception {
        String sheetFolder = createOrGetSheetPath(sheetPath);
        String sheetId = createSpreadsheet(sheetTitle);

        // Retrieve the existing parents to remove
        File file = driveService.files().get(sheetId)
                .setFields("parents")
                .execute();
        StringBuilder previousParents = new StringBuilder();
        for (String parent : file.getParents()) {
            previousParents.append(parent);
            previousParents.append(',');
        }
        // Move the file to the new folder
        file = driveService.files().update(sheetId, null)
                .setAddParents(sheetFolder)
                .setRemoveParents(previousParents.toString())
                .setFields("id, parents")
                .execute();
        return file.getId();
    }

    private String createSpreadsheet(String sheetTitle) throws IOException {
        Spreadsheet spreadsheet = new Spreadsheet()
                .setProperties(new SpreadsheetProperties()
                        .setTitle(sheetTitle));
        spreadsheet = sheetService.spreadsheets().create(spreadsheet)
                .setFields("spreadsheetId")
                .execute();
        return spreadsheet.getSpreadsheetId();
    }

    private String createOrGetSheetPath(String path) throws Exception {
        //Normalize path
        if (StringUtils.isNotBlank(path)) {
            path = FilenameUtils.normalizeNoEndSeparator(path, true);
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
        }
        //Create folders path
        String[] folderNames = path.split("/");
        String parentFolderId = null;
        for (String folderName : folderNames) {
            String folderId = getFolderId(folderName, parentFolderId);
            if (folderId == null) {
                parentFolderId = createFolder(folderName, parentFolderId);
            } else {
                parentFolderId = folderId;
            }
        }
        //Return folder ID to use as parent folder for sheet
        return parentFolderId;
    }

    private String getFolderId(String folderName, String parentFolderId) throws IOException {
        String folderQueryString;
        if (parentFolderId == null) {
            folderQueryString = String.format(FILE_QUERY_TEMPLATE, FOLDER_MIME_TYPE, folderName);
        } else {
            folderQueryString = String.format(CHILD_FILE_QUERY_TEMPLATE, FOLDER_MIME_TYPE, folderName, parentFolderId);
        }

        Drive.Files.List folderQuery = driveService.files().list().setQ(folderQueryString);
        List<File> folderQueryResults = folderQuery.execute().getFiles();
        if (!folderQueryResults.isEmpty()) {
            return folderQueryResults.get(0).getId();
        } else {
            return null;
        }
    }

    private String createFolder(String folderName, String parentFolderId) throws IOException {
        File folder = new File();
        folder.setName(folderName);
        if (parentFolderId != null) {
            folder.setParents(Collections.singletonList(parentFolderId));
        }
        folder.setMimeType(FOLDER_MIME_TYPE);
        return driveService.files().create(folder).setFields("id").execute().getId();
    }

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        try (InputStream in = new FileInputStream(new java.io.File(credentialsFilePath));
             InputStreamReader streamReader = new InputStreamReader(in)) {
            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, streamReader);

            // Build flow and trigger user authorization request.
            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                    .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(tokensDirectoryPath)))
                    .setAccessType("offline")
                    .build();

            LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
            return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        }
    }

    public void append(List<List<Object>> rowsValues) throws IOException {
        final String replacementRowRange = "A1";
        ValueRange valueRange = new ValueRange();
        valueRange.setMajorDimension("ROWS");
        valueRange.setRange(replacementRowRange);
        valueRange.setValues(rowsValues);

        //Create append request
        Sheets.Spreadsheets.Values.Append request =
                sheetService.spreadsheets().values().append(sheetId, replacementRowRange, valueRange);
        request.setValueInputOption("RAW");
        request.setInsertDataOption("OVERWRITE");
        request.execute();
    }
}
