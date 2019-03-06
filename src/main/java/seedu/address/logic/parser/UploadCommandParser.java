package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.io.File;

import seedu.address.logic.commands.UploadCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new UploadCommand object
 */
public class UploadCommandParser implements Parser<UploadCommand> {

    public static final String PATH_MESSAGE_CONSTRAINT = "Argument should be a single path associated with a file";
    public static final String FILE_MESSAGE_CONSTRAINT = "File should be a .txt file";

    /**
     * Parses the given {@code String} of arguments in the context of the UploadCommand
     * and returns an UploadCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public UploadCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, UploadCommand.MESSAGE_USAGE));
        }

        File file = new File(trimmedArgs);

        if (!file.exists() || file.isDirectory()) {
            throw new ParseException(PATH_MESSAGE_CONSTRAINT);
        } else if (!trimmedArgs.endsWith(".txt")) {
            throw new ParseException(FILE_MESSAGE_CONSTRAINT);
        }

        return new UploadCommand(file);
    }

}
