package seedu.address.logic.commands;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

import static java.util.Objects.requireNonNull;

/**
 * Enters quiz mode
 */
public class QuizCommand extends Command {
  @Override
  public CommandResult execute(Model model, CommandHistory history) throws CommandException {
    requireNonNull(model);
    return null;
  }
}
