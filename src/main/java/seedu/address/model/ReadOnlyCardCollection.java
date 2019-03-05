package seedu.address.model;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import seedu.address.model.flashcard.Flashcard;

/**
 * Unmodifiable view of an card collection
 */
public interface ReadOnlyCardCollection extends Observable {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Flashcard> getPersonList();

}
