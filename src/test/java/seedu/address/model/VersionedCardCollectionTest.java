package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.testutil.TypicalFlashcards.AMY;
import static seedu.address.testutil.TypicalFlashcards.BOB;
import static seedu.address.testutil.TypicalFlashcards.CARL;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import seedu.address.testutil.CardCollectionBuilder;

public class VersionedCardCollectionTest {

    private final ReadOnlyCardCollection cardCollectionWithAmy = new CardCollectionBuilder().withFlashcard(AMY).build();
    private final ReadOnlyCardCollection cardCollectionWithBob = new CardCollectionBuilder().withFlashcard(BOB).build();
    private final ReadOnlyCardCollection cardCollectionWithCarl =
        new CardCollectionBuilder().withFlashcard(CARL).build();
    private final ReadOnlyCardCollection emptyCardCollection = new CardCollectionBuilder().build();

    @Test
    public void commit_singleCardCollection_noStatesRemovedCurrentStateSaved() {
        VersionedCardCollection versionedCardCollection = prepareCardCollectionList(emptyCardCollection);

        versionedCardCollection.commit();
        assertCardCollectionListStatus(versionedCardCollection,
            Collections.singletonList(emptyCardCollection),
            emptyCardCollection,
            Collections.emptyList());
    }

    @Test
    public void commit_multipleCardCollectionPointerAtEndOfStateList_noStatesRemovedCurrentStateSaved() {
        VersionedCardCollection versionedCardCollection = prepareCardCollectionList(
            emptyCardCollection, cardCollectionWithAmy, cardCollectionWithBob);

        versionedCardCollection.commit();
        assertCardCollectionListStatus(versionedCardCollection,
            Arrays.asList(emptyCardCollection, cardCollectionWithAmy, cardCollectionWithBob),
            cardCollectionWithBob,
            Collections.emptyList());
    }

    @Test
    public void commit_multipleCardCollectionPointerNotAtEndOfStateList_statesAfterPointerRemovedCurrentStateSaved() {
        VersionedCardCollection versionedCardCollection = prepareCardCollectionList(
            emptyCardCollection, cardCollectionWithAmy, cardCollectionWithBob);
        shiftCurrentStatePointerLeftwards(versionedCardCollection, 2);

        versionedCardCollection.commit();
        assertCardCollectionListStatus(versionedCardCollection,
            Collections.singletonList(emptyCardCollection),
            emptyCardCollection,
            Collections.emptyList());
    }

    @Test
    public void canUndo_multipleCardCollectionPointerAtEndOfStateList_returnsTrue() {
        VersionedCardCollection versionedCardCollection = prepareCardCollectionList(
            emptyCardCollection, cardCollectionWithAmy, cardCollectionWithBob);

        assertTrue(versionedCardCollection.canUndo());
    }

    @Test
    public void canUndo_multipleCardCollectionPointerAtStartOfStateList_returnsTrue() {
        VersionedCardCollection versionedCardCollection = prepareCardCollectionList(
            emptyCardCollection, cardCollectionWithAmy, cardCollectionWithBob);
        shiftCurrentStatePointerLeftwards(versionedCardCollection, 1);

        assertTrue(versionedCardCollection.canUndo());
    }

    @Test
    public void canUndo_singleCardCollection_returnsFalse() {
        VersionedCardCollection versionedCardCollection = prepareCardCollectionList(emptyCardCollection);

        assertFalse(versionedCardCollection.canUndo());
    }

    @Test
    public void canUndo_multipleCardCollectionPointerAtStartOfStateList_returnsFalse() {
        VersionedCardCollection versionedCardCollection = prepareCardCollectionList(
            emptyCardCollection, cardCollectionWithAmy, cardCollectionWithBob);
        shiftCurrentStatePointerLeftwards(versionedCardCollection, 2);

        assertFalse(versionedCardCollection.canUndo());
    }

    @Test
    public void canRedo_multipleCardCollectionPointerNotAtEndOfStateList_returnsTrue() {
        VersionedCardCollection versionedCardCollection = prepareCardCollectionList(
            emptyCardCollection, cardCollectionWithAmy, cardCollectionWithBob);
        shiftCurrentStatePointerLeftwards(versionedCardCollection, 1);

        assertTrue(versionedCardCollection.canRedo());
    }

    @Test
    public void canRedo_multipleCardCollectionPointerAtStartOfStateList_returnsTrue() {
        VersionedCardCollection versionedCardCollection = prepareCardCollectionList(
            emptyCardCollection, cardCollectionWithAmy, cardCollectionWithBob);
        shiftCurrentStatePointerLeftwards(versionedCardCollection, 2);

        assertTrue(versionedCardCollection.canRedo());
    }

    @Test
    public void canRedo_singleCardCollection_returnsFalse() {
        VersionedCardCollection versionedCardCollection = prepareCardCollectionList(emptyCardCollection);

        assertFalse(versionedCardCollection.canRedo());
    }

    @Test
    public void canRedo_multipleCardCollectionPointerAtEndOfStateList_returnsFalse() {
        VersionedCardCollection versionedCardCollection = prepareCardCollectionList(
            emptyCardCollection, cardCollectionWithAmy, cardCollectionWithBob);

        assertFalse(versionedCardCollection.canRedo());
    }

    @Test
    public void undo_multipleCardCollectionPointerAtEndOfStateList_success() {
        VersionedCardCollection versionedCardCollection = prepareCardCollectionList(
            emptyCardCollection, cardCollectionWithAmy, cardCollectionWithBob);

        versionedCardCollection.undo();
        assertCardCollectionListStatus(versionedCardCollection,
            Collections.singletonList(emptyCardCollection),
            cardCollectionWithAmy,
            Collections.singletonList(cardCollectionWithBob));
    }

    @Test
    public void undo_multipleCardCollectionPointerNotAtStartOfStateList_success() {
        VersionedCardCollection versionedCardCollection = prepareCardCollectionList(
            emptyCardCollection, cardCollectionWithAmy, cardCollectionWithBob);
        shiftCurrentStatePointerLeftwards(versionedCardCollection, 1);

        versionedCardCollection.undo();
        assertCardCollectionListStatus(versionedCardCollection,
            Collections.emptyList(),
            emptyCardCollection,
            Arrays.asList(cardCollectionWithAmy, cardCollectionWithBob));
    }

    @Test
    public void undo_singleCardCollection_throwsNoUndoableStateException() {
        VersionedCardCollection versionedCardCollection = prepareCardCollectionList(emptyCardCollection);

        assertThrows(VersionedCardCollection.NoUndoableStateException.class, versionedCardCollection::undo);
    }

    @Test
    public void undo_multipleCardCollectionPointerAtStartOfStateList_throwsNoUndoableStateException() {
        VersionedCardCollection versionedCardCollection = prepareCardCollectionList(
            emptyCardCollection, cardCollectionWithAmy, cardCollectionWithBob);
        shiftCurrentStatePointerLeftwards(versionedCardCollection, 2);

        assertThrows(VersionedCardCollection.NoUndoableStateException.class, versionedCardCollection::undo);
    }

    @Test
    public void redo_multipleCardCollectionPointerNotAtEndOfStateList_success() {
        VersionedCardCollection versionedCardCollection = prepareCardCollectionList(
            emptyCardCollection, cardCollectionWithAmy, cardCollectionWithBob);
        shiftCurrentStatePointerLeftwards(versionedCardCollection, 1);

        versionedCardCollection.redo();
        assertCardCollectionListStatus(versionedCardCollection,
            Arrays.asList(emptyCardCollection, cardCollectionWithAmy),
            cardCollectionWithBob,
            Collections.emptyList());
    }

    @Test
    public void redo_multipleCardCollectionPointerAtStartOfStateList_success() {
        VersionedCardCollection versionedCardCollection = prepareCardCollectionList(
            emptyCardCollection, cardCollectionWithAmy, cardCollectionWithBob);
        shiftCurrentStatePointerLeftwards(versionedCardCollection, 2);

        versionedCardCollection.redo();
        assertCardCollectionListStatus(versionedCardCollection,
            Collections.singletonList(emptyCardCollection),
            cardCollectionWithAmy,
            Collections.singletonList(cardCollectionWithBob));
    }

    @Test
    public void redo_singleCardCollection_throwsNoRedoableStateException() {
        VersionedCardCollection versionedCardCollection = prepareCardCollectionList(emptyCardCollection);

        assertThrows(VersionedCardCollection.NoRedoableStateException.class, versionedCardCollection::redo);
    }

    @Test
    public void redo_multipleCardCollectionPointerAtEndOfStateList_throwsNoRedoableStateException() {
        VersionedCardCollection versionedCardCollection = prepareCardCollectionList(
            emptyCardCollection, cardCollectionWithAmy, cardCollectionWithBob);

        assertThrows(VersionedCardCollection.NoRedoableStateException.class, versionedCardCollection::redo);
    }

    @Test
    public void equals() {
        VersionedCardCollection versionedCardCollection = prepareCardCollectionList(cardCollectionWithAmy,
            cardCollectionWithBob);

        // same values -> returns true
        VersionedCardCollection copy = prepareCardCollectionList(cardCollectionWithAmy, cardCollectionWithBob);
        assertTrue(versionedCardCollection.equals(copy));

        // same object -> returns true
        assertTrue(versionedCardCollection.equals(versionedCardCollection));

        // null -> returns false
        assertFalse(versionedCardCollection.equals(null));

        // different types -> returns false
        assertFalse(versionedCardCollection.equals(1));

        // different state list -> returns false
        VersionedCardCollection differentCardCollectionList = prepareCardCollectionList(cardCollectionWithBob,
            cardCollectionWithCarl);
        assertFalse(versionedCardCollection.equals(differentCardCollectionList));

        // different current pointer index -> returns false
        VersionedCardCollection differentCurrentStatePointer = prepareCardCollectionList(
            cardCollectionWithAmy, cardCollectionWithBob);
        shiftCurrentStatePointerLeftwards(versionedCardCollection, 1);
        assertFalse(versionedCardCollection.equals(differentCurrentStatePointer));
    }

    /**
     * Asserts that {@code versionedCardCollection} is currently pointing at {@code expectedCurrentState},
     * states before {@code versionedCardCollection#currentStatePointer} is equal to {@code
     * expectedStatesBeforePointer},
     * and states after {@code versionedCardCollection#currentStatePointer} is equal to {@code
     * expectedStatesAfterPointer}.
     */
    private void assertCardCollectionListStatus(VersionedCardCollection versionedCardCollection,
                                                List<ReadOnlyCardCollection> expectedStatesBeforePointer,
                                                ReadOnlyCardCollection expectedCurrentState,
                                                List<ReadOnlyCardCollection> expectedStatesAfterPointer) {
        // check state currently pointing at is correct
        assertEquals(new CardCollection(versionedCardCollection), expectedCurrentState);

        // shift pointer to start of state list
        while (versionedCardCollection.canUndo()) {
            versionedCardCollection.undo();
        }

        // check states before pointer are correct
        for (ReadOnlyCardCollection expectedCardCollection : expectedStatesBeforePointer) {
            assertEquals(expectedCardCollection, new CardCollection(versionedCardCollection));
            versionedCardCollection.redo();
        }

        // check states after pointer are correct
        for (ReadOnlyCardCollection expectedCardCollection : expectedStatesAfterPointer) {
            versionedCardCollection.redo();
            assertEquals(expectedCardCollection, new CardCollection(versionedCardCollection));
        }

        // check that there are no more states after pointer
        assertFalse(versionedCardCollection.canRedo());

        // revert pointer to original position
        expectedStatesAfterPointer.forEach(unused -> versionedCardCollection.undo());
    }

    /**
     * Creates and returns a {@code VersionedCardCollection} with the {@code cardCollectionStates} added into it, and
     * the
     * {@code VersionedCardCollection#currentStatePointer} at the end of list.
     */
    private VersionedCardCollection prepareCardCollectionList(ReadOnlyCardCollection... cardCollectionStates) {
        assertFalse(cardCollectionStates.length == 0);

        VersionedCardCollection versionedCardCollection = new VersionedCardCollection(cardCollectionStates[0]);
        for (int i = 1; i < cardCollectionStates.length; i++) {
            versionedCardCollection.resetData(cardCollectionStates[i]);
            versionedCardCollection.commit();
        }

        return versionedCardCollection;
    }

    /**
     * Shifts the {@code versionedCardCollection#currentStatePointer} by {@code count} to the left of its list.
     */
    private void shiftCurrentStatePointerLeftwards(VersionedCardCollection versionedCardCollection, int count) {
        for (int i = 0; i < count; i++) {
            versionedCardCollection.undo();
        }
    }
}
