package seedu.address.logic.parser.commands.customer;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.address.logic.commands.customer.CustomerFindCommand;
import seedu.address.logic.parser.commands.Parser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.PersonNameContainsWordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class CustomerFindCommandParser implements Parser<CustomerFindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public CustomerFindCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, CustomerFindCommand.MESSAGE_USAGE));
        }

        String[] nameKeywords = trimmedArgs.split("\\s+");

        return new CustomerFindCommand(new PersonNameContainsWordsPredicate(Arrays.asList(nameKeywords)));
    }

}
