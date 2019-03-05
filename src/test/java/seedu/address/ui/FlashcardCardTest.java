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
        PersonCard personCard = new PersonCard(flashcardWithNoTags, 1);
        uiPartRule.setUiPart(personCard);
        assertCardDisplay(personCard, flashcardWithNoTags, 1);

        // with tags
        Flashcard flashcardWithTags = new FlashcardBuilder().build();
        personCard = new PersonCard(flashcardWithTags, 2);
        uiPartRule.setUiPart(personCard);
        assertCardDisplay(personCard, flashcardWithTags, 2);
    }

    @Test
    public void equals() {
        Flashcard flashcard = new FlashcardBuilder().build();
        PersonCard personCard = new PersonCard(flashcard, 0);

        // same flashcard, same index -> returns true
        PersonCard copy = new PersonCard(flashcard, 0);
        assertTrue(personCard.equals(copy));

        // same object -> returns true
        assertTrue(personCard.equals(personCard));

        // null -> returns false
        assertFalse(personCard.equals(null));

        // different types -> returns false
        assertFalse(personCard.equals(0));

        // different flashcard, same index -> returns false
        Flashcard differentFlashcard = new FlashcardBuilder().withName("differentName").build();
        assertFalse(personCard.equals(new PersonCard(differentFlashcard, 0)));

        // same flashcard, different index -> returns false
        assertFalse(personCard.equals(new PersonCard(flashcard, 1)));
    }

    /**
     * Asserts that {@code personCard} displays the details of {@code expectedFlashcard} correctly and matches
     * {@code expectedId}.
     */
    private void assertCardDisplay(PersonCard personCard, Flashcard expectedFlashcard, int expectedId) {
        guiRobot.pauseForHuman();

        FlashcardCardHandle flashcardCardHandle = new FlashcardCardHandle(personCard.getRoot());

        // verify id is displayed correctly
        assertEquals(expectedId + ". ", flashcardCardHandle.getId());

        // verify flashcard details are displayed correctly
        assertCardDisplaysFlashcard(expectedFlashcard, flashcardCardHandle);
    }
}
