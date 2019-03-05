package seedu.address.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardDisplaysFlashcard;

import org.junit.Test;

import guitests.guihandles.FlashcardCardHandle;
import seedu.address.model.flashcard.Flashcard;
import seedu.address.testutil.FlashcardBuilder;

public class FlashcardCardTest extends GuiUnitTest {

    @Test
    public void display() {
        // no tags
        Flashcard flashcardWithNoTags = new FlashcardBuilder().withTags().build();
        FlashcardCard flashcardCard = new FlashcardCard(flashcardWithNoTags, 1);
        uiPartRule.setUiPart(flashcardCard);
        assertCardDisplay(flashcardCard, flashcardWithNoTags, 1);

        // with tags
        Flashcard flashcardWithTags = new FlashcardBuilder().build();
        flashcardCard = new FlashcardCard(flashcardWithTags, 2);
        uiPartRule.setUiPart(flashcardCard);
        assertCardDisplay(flashcardCard, flashcardWithTags, 2);
    }

    @Test
    public void equals() {
        Flashcard flashcard = new FlashcardBuilder().build();
        FlashcardCard flashcardCard = new FlashcardCard(flashcard, 0);

        // same flashcard, same index -> returns true
        FlashcardCard copy = new FlashcardCard(flashcard, 0);
        assertTrue(flashcardCard.equals(copy));

        // same object -> returns true
        assertTrue(flashcardCard.equals(flashcardCard));

        // null -> returns false
        assertFalse(flashcardCard.equals(null));

        // different types -> returns false
        assertFalse(flashcardCard.equals(0));

        // different flashcard, same index -> returns false
        Flashcard differentFlashcard = new FlashcardBuilder().withName("differentName").build();
        assertFalse(flashcardCard.equals(new FlashcardCard(differentFlashcard, 0)));

        // same flashcard, different index -> returns false
        assertFalse(flashcardCard.equals(new FlashcardCard(flashcard, 1)));
    }

    /**
     * Asserts that {@code flashcardCard} displays the details of {@code expectedFlashcard} correctly and matches
     * {@code expectedId}.
     */
    private void assertCardDisplay(FlashcardCard flashcardCard, Flashcard expectedFlashcard, int expectedId) {
        guiRobot.pauseForHuman();

        FlashcardCardHandle flashcardCardHandle = new FlashcardCardHandle(flashcardCard.getRoot());

        // verify id is displayed correctly
        assertEquals(expectedId + ". ", flashcardCardHandle.getId());

        // verify flashcard details are displayed correctly
        assertCardDisplaysFlashcard(expectedFlashcard, flashcardCardHandle);
    }
}
