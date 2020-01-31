package com.subject7.samples.reporting;

import com.subject7.samples.common.rest.Authentication;
import com.subject7.samples.common.rest.AuthenticationType;
import com.subject7.samples.common.rest.RestUtils;
import com.subject7.samples.reporting.domain.ExecutionResponse;
import org.apache.commons.cli.*;

import java.util.ArrayList;
import java.util.List;

public class SampleGoogleDriveReportUpload {
    private static final String S7_USERNAME_KEY = "s7_username";
    private static final String S7_PASSWORD_KEY = "s7_password";
    private static final String S7_SERVER_URL_KEY = "s7_url";
    private static final String S7_EXECUTIONS_KEY = "executions";

    private static final String GOOGLE_CREDENTIALS_FILE_PATH_KEY = "google_credentials_file_path";
    private static final String GOOGLE_TOKENS_DIR_PATH_KEY = "google_tokens_dir_path";
    private static final String GOOGLE_SHEET_ID_KEY = "google_sheet_id";
    private static final String GOOGLE_NEW_SHEET_PATH_KEY = "google_new_sheet_path";
    private static final String GOOGLE_NEW_SHEET_TITLE_KEY = "google_new_sheet_title";

    public static void main(String[] args) {
        Options options = new Options();

        //S7 info
        Option optionS7Username = Option.builder(S7_USERNAME_KEY).argName(S7_USERNAME_KEY).hasArg().desc("Subject 7 Username").required().build();
        options.addOption(optionS7Username);
        Option optionS7Password = Option.builder(S7_PASSWORD_KEY).argName(S7_PASSWORD_KEY).hasArg().desc("Subject 7 Password").required().build();
        options.addOption(optionS7Password);
        Option optionS7Url = Option.builder(S7_SERVER_URL_KEY).argName(S7_SERVER_URL_KEY).hasArg().desc("Subject 7 URL With Protocol and Port").required().build();
        options.addOption(optionS7Url);

        //Execution info
        Option optionS7Executions = Option.builder(S7_EXECUTIONS_KEY).argName(S7_EXECUTIONS_KEY).hasArg().desc("Subject 7 Executions IDs").required().build();
        optionS7Executions.setArgs(Option.UNLIMITED_VALUES);
        options.addOption(optionS7Executions);

        //Google info
        Option optionGoogleCredentialsPath = Option.builder(GOOGLE_CREDENTIALS_FILE_PATH_KEY).argName(GOOGLE_CREDENTIALS_FILE_PATH_KEY).
                hasArg().desc("Path to file with Google API credentials").required().build();
        options.addOption(optionGoogleCredentialsPath);
        Option optionGoogleTokensDirPath = Option.builder(GOOGLE_TOKENS_DIR_PATH_KEY).argName(GOOGLE_TOKENS_DIR_PATH_KEY).
                hasArg().desc("Path to directory where application will store Google tokens").optionalArg(true).build();
        options.addOption(optionGoogleTokensDirPath);
        Option optionGoogleSheetId = Option.builder(GOOGLE_SHEET_ID_KEY).argName(GOOGLE_SHEET_ID_KEY).
                hasArg().desc("Google Sheet ID (if file already exists)").optionalArg(true).build();
        options.addOption(optionGoogleSheetId);
        Option optionGoogleNewSheetPath = Option.builder(GOOGLE_NEW_SHEET_PATH_KEY).argName(GOOGLE_NEW_SHEET_PATH_KEY).
                hasArg().desc("Path for new Google Sheet (if file doesn't exist)").optionalArg(true).build();
        options.addOption(optionGoogleNewSheetPath);
        Option optionGoogleNewSheetTitle = Option.builder(GOOGLE_NEW_SHEET_TITLE_KEY).argName(GOOGLE_NEW_SHEET_TITLE_KEY).
                hasArg().desc("Title for new Google Sheet file (if file doesn't exist)").optionalArg(true).build();
        options.addOption(optionGoogleNewSheetTitle);


        //validate options
        CommandLine cmd;
        CommandLineParser parser = new DefaultParser();
        try {
            cmd = parser.parse(options, args);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        String[] executionIds = cmd.getOptionValues(S7_EXECUTIONS_KEY);
        try {
            List<List<Object>> values = new ArrayList<>();
            for (String executionId : executionIds) {
                String response = RestUtils.executeHttpRequest("GET", cmd.getOptionValue(S7_SERVER_URL_KEY) + "/api/v2/executions/" + executionId, null, null,
                        new Authentication(cmd.getOptionValue(S7_USERNAME_KEY), cmd.getOptionValue(S7_PASSWORD_KEY), AuthenticationType.BASIC));
                ExecutionResponse execution = Converter.convertToObjectT(ExecutionResponse.class, response);
                values.add(convertExecutionToRowValues(execution));
            }


            GoogleWorker googleWorker = new GoogleWorker(cmd.getOptionValue(GOOGLE_CREDENTIALS_FILE_PATH_KEY),
                    cmd.getOptionValue(GOOGLE_TOKENS_DIR_PATH_KEY),
                    cmd.getOptionValue(GOOGLE_SHEET_ID_KEY),
                    cmd.getOptionValue(GOOGLE_NEW_SHEET_PATH_KEY),
                    cmd.getOptionValue(GOOGLE_NEW_SHEET_TITLE_KEY));
            googleWorker.append(values);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static List<Object> convertExecutionToRowValues(ExecutionResponse execution) {
        List<Object> executionValues = new ArrayList<>();
        executionValues.add(execution.getId().toString());
        executionValues.add(execution.getExecutionSetName());
        executionValues.add(execution.getExecutionStatus().toString());
        executionValues.add(execution.getExecutionState().toString());
        return executionValues;
    }
}
