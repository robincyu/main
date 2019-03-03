package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.CommandHistory;
import seedu.address.model.CardCollection;
import seedu.address.model.Model;

/**
 * Clears the card collection.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Address book has been cleared!";


    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        model.setCardCollection(new CardCollection());
        model.commitCardCollection();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
