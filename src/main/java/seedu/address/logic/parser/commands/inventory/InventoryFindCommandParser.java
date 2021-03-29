package seedu.address.logic.parser.commands.inventory;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.address.logic.commands.inventory.InventoryFindCommand;
import seedu.address.logic.parser.commands.Parser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.ingredient.IngredientNameContainsWordsPredicate;

public class InventoryFindCommandParser implements Parser<InventoryFindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public InventoryFindCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, InventoryFindCommand.MESSAGE_USAGE));
        }

        String[] nameKeywords = trimmedArgs.split("\\s+");

        return new InventoryFindCommand(new IngredientNameContainsWordsPredicate(Arrays.asList(nameKeywords)));
    }


}
