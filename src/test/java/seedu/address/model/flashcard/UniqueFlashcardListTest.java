package seedu.address.model.flashcard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.TypicalFlashcards.ALICE;
import static seedu.address.testutil.TypicalFlashcards.BOB;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.flashcard.exceptions.DuplicateFlashcardException;
import seedu.address.model.flashcard.exceptions.FlashcardNotFoundException;
import seedu.address.testutil.FlashcardBuilder;

public class UniqueFlashcardListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final UniqueFlashcardList uniqueFlashcardList = new UniqueFlashcardList();

    @Test
    public void contains_nullFlashcard_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueFlashcardList.contains(null);
    }

    @Test
    public void contains_flashcardNotInList_returnsFalse() {
        assertFalse(uniqueFlashcardList.contains(ALICE));
    }

    @Test
    public void contains_flashcardInList_returnsTrue() {
        uniqueFlashcardList.add(ALICE);
        assertTrue(uniqueFlashcardList.contains(ALICE));
    }

    @Test
    public void contains_flashcardWithSameIdentityFieldsInList_returnsTrue() {
        uniqueFlashcardList.add(ALICE);
        Flashcard editedAlice = new FlashcardBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
            .build();
        assertTrue(uniqueFlashcardList.contains(editedAlice));
    }

    @Test
    public void add_nullFlashcard_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueFlashcardList.add(null);
    }

    @Test
    public void add_duplicateFlashcard_throwsDuplicateFlashcardException() {
        uniqueFlashcardList.add(ALICE);
        thrown.expect(DuplicateFlashcardException.class);
        uniqueFlashcardList.add(ALICE);
    }

    @Test
    public void setFlashcard_nullTargetFlashcard_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueFlashcardList.setFlashcard(null, ALICE);
    }

    @Test
    public void setFlashcard_nullEditedFlashcard_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueFlashcardList.setFlashcard(ALICE, null);
    }

    @Test
    public void setFlashcard_targetFlashcardNotInList_throwsFlashcardNotFoundException() {
        thrown.expect(FlashcardNotFoundException.class);
        uniqueFlashcardList.setFlashcard(ALICE, ALICE);
    }

    @Test
    public void setFlashcard_editedFlashcardIsSameFlashcard_success() {
        uniqueFlashcardList.add(ALICE);
        uniqueFlashcardList.setFlashcard(ALICE, ALICE);
        UniqueFlashcardList expectedUniqueFlashcardList = new UniqueFlashcardList();
        expectedUniqueFlashcardList.add(ALICE);
        assertEquals(expectedUniqueFlashcardList, uniqueFlashcardList);
    }

    @Test
    public void setFlashcard_editedFlashcardHasSameIdentity_success() {
        uniqueFlashcardList.add(ALICE);
        Flashcard editedAlice = new FlashcardBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
            .build();
        uniqueFlashcardList.setFlashcard(ALICE, editedAlice);
        UniqueFlashcardList expectedUniqueFlashcardList = new UniqueFlashcardList();
        expectedUniqueFlashcardList.add(editedAlice);
        assertEquals(expectedUniqueFlashcardList, uniqueFlashcardList);
    }

    @Test
    public void setFlashcard_editedFlashcardHasDifferentIdentity_success() {
        uniqueFlashcardList.add(ALICE);
        uniqueFlashcardList.setFlashcard(ALICE, BOB);
        UniqueFlashcardList expectedUniqueFlashcardList = new UniqueFlashcardList();
        expectedUniqueFlashcardList.add(BOB);
        assertEquals(expectedUniqueFlashcardList, uniqueFlashcardList);
    }

    @Test
    public void setFlashcard_editedFlashcardHasNonUniqueIdentity_throwsDuplicateFlashcardException() {
        uniqueFlashcardList.add(ALICE);
        uniqueFlashcardList.add(BOB);
        thrown.expect(DuplicateFlashcardException.class);
        uniqueFlashcardList.setFlashcard(ALICE, BOB);
    }

    @Test
    public void remove_nullFlashcard_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueFlashcardList.remove(null);
    }

    @Test
    public void remove_flashcardDoesNotExist_throwsFlashcardNotFoundException() {
        thrown.expect(FlashcardNotFoundException.class);
        uniqueFlashcardList.remove(ALICE);
    }

    @Test
    public void remove_existingFlashcard_removesFlashcard() {
        uniqueFlashcardList.add(ALICE);
        uniqueFlashcardList.remove(ALICE);
        UniqueFlashcardList expectedUniqueFlashcardList = new UniqueFlashcardList();
        assertEquals(expectedUniqueFlashcardList, uniqueFlashcardList);
    }

    @Test
    public void setFlashcards_nullUniqueFlashcardList_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueFlashcardList.setFlashcards((UniqueFlashcardList) null);
    }

    @Test
    public void setFlashcards_uniqueFlashcardList_replacesOwnListWithProvidedUniqueFlashcardList() {
        uniqueFlashcardList.add(ALICE);
        UniqueFlashcardList expectedUniqueFlashcardList = new UniqueFlashcardList();
        expectedUniqueFlashcardList.add(BOB);
        uniqueFlashcardList.setFlashcards(expectedUniqueFlashcardList);
        assertEquals(expectedUniqueFlashcardList, uniqueFlashcardList);
    }

    @Test
    public void setFlashcards_nullList_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueFlashcardList.setFlashcards((List<Flashcard>) null);
    }

    @Test
    public void setFlashcards_list_replacesOwnListWithProvidedList() {
        uniqueFlashcardList.add(ALICE);
        List<Flashcard> flashcardList = Collections.singletonList(BOB);
        uniqueFlashcardList.setFlashcards(flashcardList);
        UniqueFlashcardList expectedUniqueFlashcardList = new UniqueFlashcardList();
        expectedUniqueFlashcardList.add(BOB);
        assertEquals(expectedUniqueFlashcardList, uniqueFlashcardList);
    }

    @Test
    public void setFlashcards_listWithDuplicateFlashcards_throwsDuplicateFlashcardException() {
        List<Flashcard> listWithDuplicateFlashcards = Arrays.asList(ALICE, ALICE);
        thrown.expect(DuplicateFlashcardException.class);
        uniqueFlashcardList.setFlashcards(listWithDuplicateFlashcards);
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        uniqueFlashcardList.asUnmodifiableObservableList().remove(0);
    }
}
