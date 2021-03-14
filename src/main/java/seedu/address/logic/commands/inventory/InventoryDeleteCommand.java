package seedu.address.logic.commands.inventory;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ingredient.IngredientStub;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class InventoryDeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD + "[Insert Usage Here]";

    public static final String MESSAGE_DELETE_INGREDIENT_SUCCESS = "Deleted ingredient: %1$s";

    private final Index targetIndex;

    public InventoryDeleteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        IngredientStub ingredientToDelete = new IngredientStub();

        // Delete dish here from model
        return new CommandResult(String.format(MESSAGE_DELETE_INGREDIENT_SUCCESS, ingredientToDelete));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof InventoryDeleteCommand // instanceof handles nulls
                && targetIndex.equals(((InventoryDeleteCommand) other).targetIndex)); // state check
    }
}
