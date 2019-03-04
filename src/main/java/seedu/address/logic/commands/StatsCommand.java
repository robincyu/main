package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.Set;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.tag.Tag;

/**
 * Display the success rate of the past quiz mode.
 * Keyword matching is case insensitive.
 */
public class StatsCommand extends Command {
    public static final String COMMAND_WORD = "stats";

    public static final String MESSAGE_WIP = "Not Yet Implemented";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Display the success rate from quiz mode. "
            + "Parameters: ";

    private final Set<Tag> tags = new HashSet<>();

    /**
     * Creates a StatsCommand to display the success rate
     */
    public StatsCommand() {

    }


    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        throw new CommandException(StatsCommand.MESSAGE_WIP);
        // return new CommandResult(String.format("[WIP] display statistics here"));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof StatsCommand // instanceof handles nulls
                && tags.equals(((StatsCommand) other).tags));
    }
}
