package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.AddCommandParser;
import seedu.address.model.Model;

/**
 * Appends a data file containing flashcards to the current data file.
 */
public class UploadCommand extends Command {

    public static final String COMMAND_WORD = "upload";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Uploads flashcards from a data file. "
            + "Parameters: "
            + "FILE PATH \n"
            + "Example: " + COMMAND_WORD + " "
            + "C:\\Users\\Alice\\Downloads\\spanishCards.txt";

    public static final String MESSAGE_SUCCESS = "Flashcards successfully uploaded from file: ";
    public static final String MESSAGE_UPLOAD_ERROR = "Unable to upload flashcards from file";

    private final File toAppend;

    /**
     * Creates an UploadCommand to upload the specified flashcards from {@code File}
     */
    public UploadCommand(File file) {
        requireNonNull(file);
        toAppend = file;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        try (BufferedReader br = new BufferedReader(new FileReader(toAppend))) {
            String flashcardToAdd;
            while ((flashcardToAdd = br.readLine()) != null) {
                AddCommandParser parser = new AddCommandParser();
                AddCommand command = parser.parse(flashcardToAdd);
                command.execute(model, history);
            }
        } catch (Exception exception) {
            throw new CommandException(MESSAGE_UPLOAD_ERROR);
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, toAppend.getAbsolutePath()));
    }

}
