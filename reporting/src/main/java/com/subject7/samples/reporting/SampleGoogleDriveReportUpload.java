package com.subject7.samples.reporting;

import com.subject7.samples.common.rest.Authentication;
import com.subject7.samples.common.rest.AuthenticationType;
import com.subject7.samples.common.rest.RestUtils;
import com.subject7.samples.reporting.domain.ExecutionResponse;
import org.apache.commons.cli.*;

public class SampleGoogleDriveReportUpload {
    private static final String S7_USERNAME_KEY = "s7_username";
    private static final String S7_PASSWORD_KEY = "s7_password";
    private static final String S7_SERVER_URL_KEY = "s7_url";

    private static final String S7_EXECUTIONS_KEY = "executions";

    public static void main(String[] args) {
        Options options = new Options();

        //S7 info
        Option optionS7Username = Option.builder(S7_USERNAME_KEY).argName(S7_USERNAME_KEY).hasArg().desc("Subject 7 Username").required().build();
        options.addOption(optionS7Username);
        Option optionS7Password = Option.builder(S7_PASSWORD_KEY).argName(S7_PASSWORD_KEY).hasArg().desc("Subject 7 Password").required().build();
        options.addOption(optionS7Password);
        Option optionS7Url = Option.builder(S7_SERVER_URL_KEY).argName(S7_SERVER_URL_KEY).hasArg().desc("Subject 7 URL With Protocol and Port").required().build();
        options.addOption(optionS7Url);

        //Google info
        //TODO: Google Drive Credentials, Google Drive Path, Spreadsheet Name (based on Google help)

        //Execution info
        Option optionS7Executions = Option.builder(S7_EXECUTIONS_KEY).argName(S7_EXECUTIONS_KEY).hasArg().desc("Subject 7 Executions IDs").required().build();
        optionS7Executions.setArgs(Option.UNLIMITED_VALUES);
        options.addOption(optionS7Executions);


        //validate options
        CommandLine cmd;
        CommandLineParser parser = new DefaultParser();
        try {
            cmd = parser.parse(options, args);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        String[] executions = cmd.getOptionValues(S7_EXECUTIONS_KEY);
        try {
            String response = RestUtils.executeHttpRequest("GET", cmd.getOptionValue(S7_SERVER_URL_KEY) + "/api/v2/executions/" + executions[0], null, null,
                    new Authentication(cmd.getOptionValue(S7_USERNAME_KEY), cmd.getOptionValue(S7_PASSWORD_KEY), AuthenticationType.BASIC));
            System.out.println(response);
            ExecutionResponse execution = Converter.convertToObjectT(ExecutionResponse.class, response);
            System.out.println(String.format("Execution is extracted successfully, execution id from object: %d", execution.getId()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
