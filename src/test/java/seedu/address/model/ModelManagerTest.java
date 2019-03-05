package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_FLASHCARDS;
import static seedu.address.testutil.TypicalFlashcards.ALICE;
import static seedu.address.testutil.TypicalFlashcards.BENSON;
import static seedu.address.testutil.TypicalFlashcards.BOB;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.flashcard.Flashcard;
import seedu.address.model.flashcard.NameContainsKeywordsPredicate;
import seedu.address.model.flashcard.exceptions.FlashcardNotFoundException;
import seedu.address.testutil.CardCollectionBuilder;
import seedu.address.testutil.FlashcardBuilder;

public class ModelManagerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new CardCollection(), new CardCollection(modelManager.getCardCollection()));
        assertEquals(null, modelManager.getSelectedFlashcard());
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        modelManager.setUserPrefs(null);
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setCardCollectionFilePath(Paths.get("address/book/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setCardCollectionFilePath(Paths.get("new/address/book/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        modelManager.setGuiSettings(null);
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setCardCollectionFilePath_nullPath_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        modelManager.setCardCollectionFilePath(null);
    }

    @Test
    public void setCardCollectionFilePath_validPath_setsCardCollectionFilePath() {
        Path path = Paths.get("address/book/file/path");
        modelManager.setCardCollectionFilePath(path);
        assertEquals(path, modelManager.getCardCollectionFilePath());
    }

    @Test
    public void hasFlashcard_nullFlashcard_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        modelManager.hasFlashcard(null);
    }

    @Test
    public void hasFlashcard_flashcardNotInCardCollection_returnsFalse() {
        assertFalse(modelManager.hasFlashcard(ALICE));
    }

    @Test
    public void hasFlashcard_flashcardInCardCollection_returnsTrue() {
        modelManager.addFlashcard(ALICE);
        assertTrue(modelManager.hasFlashcard(ALICE));
    }

    @Test
    public void deleteFlashcard_flashcardIsSelectedAndFirstFlashcardInFilteredFlashcardList_selectionCleared() {
        modelManager.addFlashcard(ALICE);
        modelManager.setSelectedFlashcard(ALICE);
        modelManager.deleteFlashcard(ALICE);
        assertEquals(null, modelManager.getSelectedFlashcard());
    }

    @Test
    public void deleteFlashcard_flashcardIsSelectedAndSecondFlashcardInFilteredFlashcardList_firstFlashcardSelected() {
        modelManager.addFlashcard(ALICE);
        modelManager.addFlashcard(BOB);
        assertEquals(Arrays.asList(ALICE, BOB), modelManager.getFilteredFlashcardList());
        modelManager.setSelectedFlashcard(BOB);
        modelManager.deleteFlashcard(BOB);
        assertEquals(ALICE, modelManager.getSelectedFlashcard());
    }

    @Test
    public void setFlashcard_flashcardIsSelected_selectedFlashcardUpdated() {
        modelManager.addFlashcard(ALICE);
        modelManager.setSelectedFlashcard(ALICE);
        Flashcard updatedAlice = new FlashcardBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        modelManager.setFlashcard(ALICE, updatedAlice);
        assertEquals(updatedAlice, modelManager.getSelectedFlashcard());
    }

    @Test
    public void getFilteredFlashcardList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        modelManager.getFilteredFlashcardList().remove(0);
    }

    @Test
    public void setSelectedFlashcard_flashcardNotInFilteredFlashcardList_throwsFlashcardNotFoundException() {
        thrown.expect(FlashcardNotFoundException.class);
        modelManager.setSelectedFlashcard(ALICE);
    }

    @Test
    public void setSelectedFlashcard_flashcardInFilteredFlashcardList_setsSelectedFlashcard() {
        modelManager.addFlashcard(ALICE);
        assertEquals(Collections.singletonList(ALICE), modelManager.getFilteredFlashcardList());
        modelManager.setSelectedFlashcard(ALICE);
        assertEquals(ALICE, modelManager.getSelectedFlashcard());
    }

    @Test
    public void equals() {
        CardCollection cardCollection = new CardCollectionBuilder().withFlashcard(ALICE).withFlashcard(BENSON).build();
        CardCollection differentCardCollection = new CardCollection();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(cardCollection, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(cardCollection, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different cardCollection -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentCardCollection, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredFlashcardList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(cardCollection, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredFlashcardList(PREDICATE_SHOW_ALL_FLASHCARDS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setCardCollectionFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(cardCollection, differentUserPrefs)));
    }
}
