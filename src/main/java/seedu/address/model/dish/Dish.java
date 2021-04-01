package seedu.address.model.dish;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.core.Pair;
import seedu.address.commons.util.StringUtil;
import seedu.address.model.Aggregator;
import seedu.address.model.Item;
import seedu.address.model.ingredient.Ingredient;

public class Dish implements Item, Aggregator<Ingredient> {
    private String name;
    private double price;
    private List<Pair<Ingredient, Integer>> ingredientQuantityList;

    /**
     * Dish constructor
     * @param name
     * @param price
     * @param ingredientQuantityList
     */
    @JsonCreator
    public Dish(@JsonProperty("dish") String name, @JsonProperty("price") double price,
                @JsonProperty("ingredientQuantityList") List<Pair<Ingredient, Integer>> ingredientQuantityList) {
        this.name = name;
        this.price = price;
        this.ingredientQuantityList = ingredientQuantityList;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    //@@author kangtinglee
    /** Checks if a particular ingredient is contained within this dish */
    @Override
    public boolean contains(Ingredient ingredient) {
        for (Pair<Ingredient, Integer> pair : getIngredientQuantityList()) {
            if (pair.getKey().equals(ingredient)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if dish has an ingredient with keyword, case ignored
     */
    public boolean containsIngredientKeyword(String keyword) {
        for (Pair<Ingredient, Integer> pair : getIngredientQuantityList()) {
            if (StringUtil.containsWordIgnoreCase(pair.getKey().getName(), keyword)) {
                return true;
            }
        }
        return false;
    }

    public List<Pair<Ingredient, Integer>> getIngredientQuantityList() {
        return ingredientQuantityList;
    }

    @Override
    public boolean isSame(Item other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Dish)) {
            return false;
        }

        Dish otherDish = (Dish) other;
        return otherDish != null
                && this.getName().equals(otherDish.getName());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Dish)) {
            return false;
        }

        Dish otherDish = (Dish) other;
        return otherDish.name.equals(name)
                && otherDish.price == price
                && otherDish.getIngredientQuantityList().equals(ingredientQuantityList);
    }
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Name: ")
                .append(getName())
                .append("; Price: ")
                .append(getPrice());

        if (!ingredientQuantityList.isEmpty()) {
            builder.append("; Ingredients: ");
            for (Pair<Ingredient, Integer> pair : ingredientQuantityList) {
                builder.append(pair.getKey());
                builder.append(" x");
                builder.append(pair.getValue());
            }
        }
        return builder.toString();
    }

}
