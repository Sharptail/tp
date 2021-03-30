package seedu.address.logic.commands.order;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.model.Model;

/**
 * Lists all orders to the user.
 */
public class OrderHistoryCommand extends Command {

    public static final String COMMAND_WORD = "history";

    public static final String MESSAGE_SUCCESS = "Listed all history";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_SUCCESS, CommandResult.CRtype.ORDERHISTORY);
    }
}
