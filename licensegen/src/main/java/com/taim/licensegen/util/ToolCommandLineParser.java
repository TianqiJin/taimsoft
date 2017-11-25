package com.taim.licensegen.util;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tjin on 2016-10-24.
 */
public class ToolCommandLineParser extends BaseCommandLineParser {
    // Set up the options
    public static final String HELP_OPT = "help";
    public static final String LICENSE_REQUEST = "request";
    public static final String PRIVATE_KEY_FILE = "key";
    public static final String SECURITY_FILE = "security-file";
    public static final String TRIAL_SECURITY_FILE = "trial-security-file";

    private static Options options = new FullLongOptions(); // see comment in parent class.

    static {
        options.addOption(
                Option.builder().longOpt(LICENSE_REQUEST).hasArg().argName("license-request")
                        .desc("License request string in JSON format")
                        .required()
                        .build());
        options.addOption(
                Option.builder().longOpt(PRIVATE_KEY_FILE).hasArg().argName("private-key")
                        .desc("private key used for license decryption")
                        .required()
                        .build());
        options.addOption(
                Option.builder().longOpt(SECURITY_FILE).hasArg().argName("security-file")
                        .desc("file name for license generation")
                        .required()
                        .build());
        options.addOption(
                Option.builder().longOpt(TRIAL_SECURITY_FILE).hasArg().argName("trial-security-file")
                        .desc("file name for trial license generation")
                        .build());
        options.addOption(
                Option.builder().longOpt(HELP_OPT)
                        .desc("Display this help message and exit.")
                        .build());
    }

    // The map where results are stored.
    private Map<String, Object> optionValues = new HashMap<>();

    @Override
    protected Options getOptions() {
        return options;
    }

    @Override
    public Object getValue(String name) {
        return optionValues.get(name);
    }

    @Override
    protected void interpret(CommandLine commandLine) throws OptionException {
        // Interpret what was parsed.
        String securityFile = commandLine.getOptionValue(SECURITY_FILE);
        String trialSecurityFile = commandLine.getOptionValue(TRIAL_SECURITY_FILE);
        String privateKeyFile = commandLine.getOptionValue(PRIVATE_KEY_FILE);
        String licenseRequest = commandLine.getOptionValue(LICENSE_REQUEST);

        optionValues.put(LICENSE_REQUEST, licenseRequest);
        optionValues.put(SECURITY_FILE, securityFile);
        optionValues.put(TRIAL_SECURITY_FILE, trialSecurityFile);
        optionValues.put(PRIVATE_KEY_FILE, privateKeyFile);
    }

    @Override
    protected void usage(){
        new HelpFormatter().printHelp(new PrintWriter(System.err, true), 150,
                "LicenseGeneration", null, options, 4, 2, null, true);
    }
}
