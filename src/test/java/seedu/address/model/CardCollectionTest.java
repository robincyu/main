package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalCardCollection;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.flashcard.Flashcard;
import seedu.address.model.flashcard.exceptions.DuplicateFlashcardException;
import seedu.address.testutil.PersonBuilder;

public class CardCollectionTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final CardCollection cardCollection = new CardCollection();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), cardCollection.getPersonList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        cardCollection.resetData(null);
    }

    @Test
    public void resetData_withValidReadOnlyCardCollection_replacesData() {
        CardCollection newData = getTypicalCardCollection();
        cardCollection.resetData(newData);
        assertEquals(newData, cardCollection);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        // Two flashcards with the same identity fields
        Flashcard editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
            .build();
        List<Flashcard> newFlashcards = Arrays.asList(ALICE, editedAlice);
        CardCollectionStub newData = new CardCollectionStub(newFlashcards);

        thrown.expect(DuplicateFlashcardException.class);
        cardCollection.resetData(newData);
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        cardCollection.hasPerson(null);
    }

    @Test
    public void hasPerson_personNotInCardCollection_returnsFalse() {
        assertFalse(cardCollection.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInCardCollection_returnsTrue() {
        cardCollection.addPerson(ALICE);
        assertTrue(cardCollection.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personWithSameIdentityFieldsInCardCollection_returnsTrue() {
        cardCollection.addPerson(ALICE);
        Flashcard editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
            .build();
        assertTrue(cardCollection.hasPerson(editedAlice));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        cardCollection.getPersonList().remove(0);
    }

    @Test
    public void addListener_withInvalidationListener_listenerAdded() {
        SimpleIntegerProperty counter = new SimpleIntegerProperty();
        InvalidationListener listener = observable -> counter.set(counter.get() + 1);
        cardCollection.addListener(listener);
        cardCollection.addPerson(ALICE);
        assertEquals(1, counter.get());
    }

    @Test
    public void removeListener_withInvalidationListener_listenerRemoved() {
        SimpleIntegerProperty counter = new SimpleIntegerProperty();
        InvalidationListener listener = observable -> counter.set(counter.get() + 1);
        cardCollection.addListener(listener);
        cardCollection.removeListener(listener);
        cardCollection.addPerson(ALICE);
        assertEquals(0, counter.get());
    }

    /**
     * A stub ReadOnlyCardCollection whose flashcards list can violate interface constraints.
     */
    private static class CardCollectionStub implements ReadOnlyCardCollection {
        private final ObservableList<Flashcard> flashcards = FXCollections.observableArrayList();

        CardCollectionStub(Collection<Flashcard> flashcards) {
            this.flashcards.setAll(flashcards);
        }

        @Override
        public ObservableList<Flashcard> getPersonList() {
            return flashcards;
        }

        @Override
        public void addListener(InvalidationListener listener) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void removeListener(InvalidationListener listener) {
            throw new AssertionError("This method should not be called.");
        }
    }

}
