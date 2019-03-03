package seedu.address.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.flashcard.FlashCard;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<FlashCard> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

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
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns true if a flashCard with the same identity as {@code flashCard} exists in the address book.
     */
    boolean hasPerson(FlashCard flashCard);

    /**
     * Deletes the given flashCard.
     * The flashCard must exist in the address book.
     */
    void deletePerson(FlashCard target);

    /**
     * Adds the given flashCard.
     * {@code flashCard} must not already exist in the address book.
     */
    void addPerson(FlashCard flashCard);

    /**
     * Replaces the given flashCard {@code target} with {@code editedFlashCard}.
     * {@code target} must exist in the address book.
     * The flashCard identity of {@code editedFlashCard} must not be the same as another existing flashCard in the address book.
     */
    void setPerson(FlashCard target, FlashCard editedFlashCard);

    /** Returns an unmodifiable view of the filtered flashCard list */
    ObservableList<FlashCard> getFilteredPersonList();

    /**
     * Updates the filter of the filtered flashCard list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<FlashCard> predicate);

    /**
     * Returns true if the model has previous address book states to restore.
     */
    boolean canUndoAddressBook();

    /**
     * Returns true if the model has undone address book states to restore.
     */
    boolean canRedoAddressBook();

    /**
     * Restores the model's address book to its previous state.
     */
    void undoAddressBook();

    /**
     * Restores the model's address book to its previously undone state.
     */
    void redoAddressBook();

    /**
     * Saves the current address book state for undo/redo.
     */
    void commitAddressBook();

    /**
     * Selected flashCard in the filtered flashCard list.
     * null if no flashCard is selected.
     */
    ReadOnlyProperty<FlashCard> selectedPersonProperty();

    /**
     * Returns the selected flashCard in the filtered flashCard list.
     * null if no flashCard is selected.
     */
    FlashCard getSelectedPerson();

    /**
     * Sets the selected flashCard in the filtered flashCard list.
     */
    void setSelectedPerson(FlashCard flashCard);
}
