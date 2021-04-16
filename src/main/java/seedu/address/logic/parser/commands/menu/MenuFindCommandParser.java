package seedu.address.logic.parser.commands.menu;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INGREDIENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import seedu.address.logic.commands.menu.MenuFindCommand;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.commands.Parser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.dish.Dish;
import seedu.address.model.dish.DishContainsIngredientNamePredicate;
import seedu.address.model.dish.DishNameContainsWordsPredicate;

public class MenuFindCommandParser implements Parser<MenuFindCommand> {

    @Override
    public MenuFindCommand parse(String userInput) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(userInput, PREFIX_NAME, PREFIX_INGREDIENT);

        boolean namePresent = ParserUtil.arePrefixesPresent(argMultimap, PREFIX_NAME);
        boolean ingredientPresent = ParserUtil.arePrefixesPresent(argMultimap, PREFIX_INGREDIENT);

        if (!namePresent && !ingredientPresent) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    MenuFindCommand.MESSAGE_USAGE));
        }

        List<Predicate<Dish>> predicates = getPredicates(argMultimap);
        return new MenuFindCommand(predicates);
    }

    private List<Predicate<Dish>> getPredicates(ArgumentMultimap argMultimap) throws ParseException {
        List<Predicate<Dish>> predicates = new ArrayList<>();

        Optional<String> nameArgs = argMultimap.getValue(PREFIX_NAME);
        Optional<String> ingredientArgs = argMultimap.getValue(PREFIX_INGREDIENT);

        if (nameArgs.isPresent()) {
            List<String> keywords = getKeywords(nameArgs.get());
            predicates.add(new DishNameContainsWordsPredicate(keywords));
        }

        if (ingredientArgs.isPresent()) {
            List<String> keywords = getKeywords(ingredientArgs.get());
            predicates.add(new DishContainsIngredientNamePredicate(keywords));
        }
        return predicates;
    }

    private List<String> getKeywords(String input) throws ParseException {
        List<String> keywords;
        try {
            keywords = ParserUtil.parseKeywords(input);
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    MenuFindCommand.MESSAGE_USAGE), pe);
        }
        return keywords;
    }
}
