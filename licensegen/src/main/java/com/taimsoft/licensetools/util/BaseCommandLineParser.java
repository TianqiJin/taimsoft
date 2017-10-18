package com.taimsoft.licensetools.util;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tjin on 2016-10-24.
 */
public abstract class BaseCommandLineParser {
    public enum ParseResult {
        success,        // execute the application
        failFriendly,   // exit with a zero status
        failBadly       // exit with nonzero status
    }

    public class OptionException extends Exception {
        public OptionException(String message) {
            super(message);
        }
    }

    // Abstract methods
    protected abstract Options getOptions();
    public abstract Object getValue(String name);
    protected abstract void interpret(CommandLine commandLine) throws OptionException;
    protected abstract void usage();

    /*
     * The Options class, when used by the DefaultParser, matches long options even if the argument
     * starts with the option's string.  We want a full match for the entire option string.  Tail
     * patch the option matcher to make it exclude matches where the option name doesn't fully
     * match the argument.
     */
    static public class FullLongOptions extends Options {
        @Override
        public List<String> getMatchingOptions(String opt) {
            List<String> found = super.getMatchingOptions(opt);
            return super.getOption(opt) == null ? new ArrayList<>() : found;
        }
    }

    /**
     * Parse an argument list.
     * @param args The args to parse.
     * @return The result of parsing.
     */
    public ParseResult parse(String[] args){
        // Parse the command line
        CommandLine commandLine;
        try {
            commandLine = new DefaultParser().parse(getOptions(), args);
        } catch(ParseException e) {
            // Parsing may have failed due to missing options, but --help has priority over them;
            // if --help is present, it doesn't matter what's missing or incorrect.  So find
            // whether --help is present.
            boolean needHelp = Arrays.stream(args)
                    .anyMatch("--help"::equals);
            if(needHelp) {
                usage();
                return ParseResult.failFriendly;
            } else {
                System.err.println(e.getMessage());
                return ParseResult.failBadly;
            }
        }

        // If --help is present, everything else is ignored.
        if(commandLine.hasOption("help")){
            usage();
            return ParseResult.failFriendly;
        }
        try{
            interpret(commandLine);
            return ParseResult.success;
        } catch(OptionException e) {
            System.err.println(e.getMessage());
            return ParseResult.failBadly;
        }
    }

    public String getStringValue(String name) {
        return (String) getValue(name);
    }

}
