package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.dish.Dish;
import seedu.address.model.dish.DishBook;
import seedu.address.model.dish.ReadOnlyDishBook;
import seedu.address.model.ingredient.Ingredient;
import seedu.address.model.ingredient.IngredientBook;
import seedu.address.model.ingredient.ReadOnlyIngredientBook;
import seedu.address.model.order.Order;
import seedu.address.model.order.OrderBook;
import seedu.address.model.order.ReadOnlyOrderBook;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBook;
import seedu.address.model.person.ReadOnlyPersonBook;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final PersonBook personBook;
    private final DishBook dishBook;
    private final IngredientBook ingredientBook;
    private final OrderBook orderBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    private final FilteredList<Dish> filteredDishes;
    private final FilteredList<Ingredient> filteredIngredients;
    private FilteredList<Order> filteredOrders;

    /**
     * Initializes a ModelManager with the given books and userPrefs.
     */
    public ModelManager(ReadOnlyPersonBook personBook, ReadOnlyDishBook dishBook,
                        ReadOnlyIngredientBook ingredientBook, ReadOnlyOrderBook orderBook,
                        ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(personBook, userPrefs);

        logger.fine("Initializing with address book: " + personBook + " and user prefs " + userPrefs);

        this.personBook = new PersonBook(personBook);
        this.dishBook = new DishBook(dishBook);
        this.ingredientBook = new IngredientBook(ingredientBook);
        this.orderBook = new OrderBook(orderBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.personBook.getPersonList());
        filteredDishes = new FilteredList<>(this.dishBook.getDishList());
        filteredIngredients = new FilteredList<>(this.ingredientBook.getIngredientList());
        filteredOrders = new FilteredList<>(this.orderBook.getOrderList());
    }

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyPersonBook personBook, ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(personBook, userPrefs);

        logger.fine("Initializing with address book: " + personBook + " and user prefs " + userPrefs);

        this.personBook = new PersonBook(personBook);
        this.dishBook = null;
        this.ingredientBook = null;
        this.orderBook = new OrderBook();
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.personBook.getPersonList());
        filteredDishes = null;
        filteredIngredients = null;
        filteredOrders = new FilteredList<>(this.orderBook.getOrderList());
    }

    public ModelManager() {
        this(new PersonBook(), new DishBook(), new IngredientBook(), new OrderBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getPersonBookFilePath() {
        return userPrefs.getPersonBookFilePath();
    }

    @Override
    public void setPersonBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setPersonBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyPersonBook addressBook) {
        this.personBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyPersonBook getAddressBook() {
        return personBook;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return personBook.hasPerson(person);
    }

    //@@author kangtinglee
    public Person getPersonByIndex(int i) {
        return getFilteredPersonList().get(i);
    }

    //@@author kangtinglee
    @Override
    public void deletePerson(Person target) {
        personBook.removePerson(target);
        List<Order> deletionList = getOrdersFromPerson(target);
        deleteOrders(deletionList);

        assert !personBook.hasPerson(target);

        for (Order o : deletionList) {
            assert !hasOrder(o);
            logger.fine(String.format("Order %s belonging to %s deleted by cascade", o, target));
        }
    }

    @Override
    public void addPerson(Person person) {
        personBook.addPerson(person);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        personBook.setPerson(target, editedPerson);
    }

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<? super Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    //=========== Dishes ================================================================================
    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    @Override
    public void setDishBook(ReadOnlyDishBook dishBook) {
        this.dishBook.resetData(dishBook);
    }

    /** Returns the AddressBook */
    @Override
    public ReadOnlyDishBook getDishBook() {
        return dishBook;
    }

    /**
     * Returns true if a dish with the same name as {@code dish} exists in the address book.
     */
    @Override
    public boolean hasDish(Dish dish) {
        requireNonNull(dish);
        return dishBook.hasDish(dish);
    }

    /**
     * Returns the {@code Person} object at the specified index on the UI
     */
    public Dish getDishByIndex(int i) {
        return getFilteredDishList().get(i);
    }

    /**
     * Deletes the given dish.
     * The dish must exist.
     */
    public void deleteDish(Dish target) {
        dishBook.removeDish(target);
    }

    /**
     * Adds the given dish.
     * {@code dish} must not already exist
     */
    public void addDish(Dish dish) {
        dishBook.addDish(dish);
    }

    /**
     * Replaces the given dish {@code target} with {@code editedDish}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedDish} must not be the same as another existing dish in the address book.
     */
    public void setDish(Dish target, Dish editedDish) {
        requireAllNonNull(target, editedDish);

        dishBook.setDish(target, editedDish);
    }

    /** Returns an unmodifiable view of the filtered person list */
    public ObservableList<Dish> getFilteredDishList() {
        return filteredDishes;
    }

    @Override
    public void updateFilteredDishList(Predicate<? super Dish> predicate) {
        requireNonNull(predicate);
        filteredDishes.setPredicate(predicate);
    }

    //=========== Ingredients ================================================================================
    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    @Override
    public void setIngredientBook(ReadOnlyIngredientBook ingredientBook) {
        this.ingredientBook.resetData(ingredientBook);
    }

    /** Returns the AddressBook */
    @Override
    public ReadOnlyIngredientBook getIngredientBook() {
        return ingredientBook;
    }

    /**
     * Returns true if a ingredient with the same name as {@code ingredient} exists in the address book.
     */
    @Override
    public boolean hasIngredient(Ingredient ingredient) {
        requireNonNull(ingredient);
        return ingredientBook.hasIngredient(ingredient);
    }

    /**
     * Returns the {@code Ingredient} object at the specified index on the UI
     */
    public Ingredient getIngredientByIndex(int i) {
        return getFilteredIngredientList().get(i);
    }

    /**
     * Deletes the given ingredient.
     * The ingredient must exist.
     */
    public void deleteIngredient(Ingredient target) {
        ingredientBook.removeIngredient(target);
    }

    /**
     * Adds the given ingredient.
     * {@code ingredient} must not already exist
     */
    public void addIngredient(Ingredient ingredient) {
        ingredientBook.addIngredient(ingredient);
    }

    /**
     * Replaces the given ingredient {@code target} with {@code editedIngredient}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedIngredient} must not be the same as another existing ingredient.
     */
    public void setIngredient(Ingredient target, Ingredient editedIngredient) {
        requireAllNonNull(target, editedIngredient);

        ingredientBook.setIngredient(target, editedIngredient);
    }

    /** Returns an unmodifiable view of the filtered person list */
    public ObservableList<Ingredient> getFilteredIngredientList() {
        return filteredIngredients;
    }

    @Override
    public void updateFilteredIngredientList(Predicate<? super Ingredient> predicate) {
        requireNonNull(predicate);
        filteredIngredients.setPredicate(predicate);
    }


    //@@author kangtinglee
    /** Returns a list of dishes that use a particular ingredient */
    public List<Dish> getDishesByIngredients(Ingredient ingredient) {
        List<Dish> result = new ArrayList<>();
        List<Dish> dishes = getDishBook().getDishList();
        for (Dish d : dishes) {
            if (d.contains(ingredient)) {
                result.add(d);
            }
        }
        return result;
    }

    //=========== Orders ================================================================================
    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    @Override
    public void setOrderBook(ReadOnlyOrderBook orderBook) {
        this.orderBook.resetData(orderBook);
    }

    /** Returns the OrderBook */
    @Override
    public ReadOnlyOrderBook getOrderBook() {
        return orderBook;
    }

    /**
     * Returns true if a order with the same name as {@code order} exists in the address book.
     */
    @Override
    public boolean hasOrder(Order order) {
        requireNonNull(order);
        return orderBook.hasOrder(order);
    }

    /**
     * Deletes the given order.
     * The order must exist.
     */
    public void deleteOrder(Order target) {
        orderBook.removeOrder(target);
    }

    /**
     * Deletes a list of orders.
     * The orders must exist.
     */
    public void deleteOrders(List<Order> orders) {
        for (Order o : orders) {
            deleteOrder(o);
        }
    }


    /**
     * Adds the given order.
     * {@code order} must not already exist
     */
    public void addOrder(Order order) {
        orderBook.addOrder(order);
    }

    /**
     * Replaces the given order {@code target} with {@code editedOrder}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedOrder} must not be the same as another existing order in the address book.
     */
    public void setOrder(Order target, Order editedOrder) {
        requireAllNonNull(target, editedOrder);

        orderBook.setOrder(target, editedOrder);
    }

    /**
     * Sets the state of the order to complete
     */
    public void completeOrder(Order target) {
        orderBook.completeOrder(target);
    }

    /**
     * Sets the state of the order to cancelled
     */
    public void cancelOrder(Order target) {
        orderBook.completeOrder(target);
    }

    /**
     * Returns an unmodifiable view of the filtered order list with one predicate
     */
    @Override
    public ObservableList<Order> getFilteredOrderList(Order.State state) {
        return filteredOrders.filtered(order -> order.getState() == state);
    }

    /**
     * Returns an unmodifiable view of the filtered order list with two predicates
     */
    @Override
    public ObservableList<Order> getFilteredOrderList(Order.State firstState, Order.State secState) {
        return filteredOrders.filtered(order -> order.getState() == firstState || order.getState() == secState);
    }

    /** Returns an sorted view of the filtered order list */
    public ObservableList<Order> getFilteredOrderList(Comparator<Order> comparator, Order.State state) {
        sortOrder(comparator);
        return getFilteredOrderList(state);
    }

    /**
     *  Returns a sorted view of the filtered Order List
     * @param comparator
     * @return
     */
    public void sortOrder(Comparator<Order> comparator) {
        orderBook.sortItemsByDateTime(comparator);
    }

    @Override
    public void updateFilteredOrderList(Predicate<? super Order> predicate) {
        requireNonNull(predicate);
        filteredOrders.setPredicate(predicate);
    }

    //@@author kangtinglee
    /** Returns an unmodifiable view of the orders belonging to a particular customer */
    public List<Order> getOrdersFromPerson(Person target) {
        List<Order> result = new ArrayList<>();
        ObservableList<Order> orders = getOrderBook().getOrderList();
        for (Order o : orders) {
            if (o.isFromCustomer(target)) {
                result.add(o);
            }
        }
        return result;
    }

    //=========== Filtered Person List Accessors =============================================================



    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return personBook.equals(other.personBook)
                && userPrefs.equals(other.userPrefs)
                && filteredPersons.equals(other.filteredPersons);
    }

}
