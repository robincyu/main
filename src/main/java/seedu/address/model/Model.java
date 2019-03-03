package seedu.address.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.person.Person;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' card collection file path.
     */
    Path getCardCollectionFilePath();

    /**
     * Sets the user prefs' card collection file path.
     */
    void setCardCollectionFilePath(Path cardCollectionFilePath);

    /**
     * Replaces card collection data with the data in {@code cardCollection}.
     */
    void setCardCollection(ReadOnlyCardCollection cardCollection);

    /** Returns the CardCollection */
    ReadOnlyCardCollection getCardCollection();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the card collection.
     */
    boolean hasPerson(Person person);

    /**
     * Deletes the given person.
     * The person must exist in the card collection.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the card collection.
     */
    void addPerson(Person person);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the card collection.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the card
     * collection.
     */
    void setPerson(Person target, Person editedPerson);

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Returns true if the model has previous card collection states to restore.
     */
    boolean canUndoCardCollection();

    /**
     * Returns true if the model has undone card collection states to restore.
     */
    boolean canRedoCardCollection();

    /**
     * Restores the model's card collection to its previous state.
     */
    void undoCardCollection();

    /**
     * Restores the model's card collection to its previously undone state.
     */
    void redoCardCollection();

    /**
     * Saves the current card collection state for undo/redo.
     */
    void commitCardCollection();

    /**
     * Selected person in the filtered person list.
     * null if no person is selected.
     */
    ReadOnlyProperty<Person> selectedPersonProperty();

    /**
     * Returns the selected person in the filtered person list.
     * null if no person is selected.
     */
    Person getSelectedPerson();

    /**
     * Sets the selected person in the filtered person list.
     */
    void setSelectedPerson(Person person);
}
