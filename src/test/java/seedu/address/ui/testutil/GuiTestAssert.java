package seedu.address.ui.testutil;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.stream.Collectors;

import guitests.guihandles.FlashcardCardHandle;
import guitests.guihandles.FlashcardListPanelHandle;
import guitests.guihandles.ResultDisplayHandle;
import seedu.address.model.flashcard.Flashcard;

/**
 * A set of assertion methods useful for writing GUI tests.
 */
public class GuiTestAssert {
    /**
     * Asserts that {@code actualCard} displays the same values as {@code expectedCard}.
     */
    public static void assertCardEquals(FlashcardCardHandle expectedCard, FlashcardCardHandle actualCard) {
        assertEquals(expectedCard.getId(), actualCard.getId());
        assertEquals(expectedCard.getAddress(), actualCard.getAddress());
        assertEquals(expectedCard.getEmail(), actualCard.getEmail());
        assertEquals(expectedCard.getName(), actualCard.getName());
        assertEquals(expectedCard.getPhone(), actualCard.getPhone());
        assertEquals(expectedCard.getTags(), actualCard.getTags());
    }

    /**
     * Asserts that {@code actualCard} displays the details of {@code expectedFlashcard}.
     */
    public static void assertCardDisplaysFlashcard(Flashcard expectedFlashcard, FlashcardCardHandle actualCard) {
        assertEquals(expectedFlashcard.getName().fullName, actualCard.getName());
        assertEquals(expectedFlashcard.getPhone().value, actualCard.getPhone());
        assertEquals(expectedFlashcard.getEmail().value, actualCard.getEmail());
        assertEquals(expectedFlashcard.getAddress().value, actualCard.getAddress());
        assertEquals(expectedFlashcard.getTags().stream().map(tag -> tag.tagName).collect(Collectors.toList()),
            actualCard.getTags());
    }

    /**
     * Asserts that the list in {@code flashcardListPanelHandle} displays the details of {@code flashcards} correctly
     * and
     * in the correct order.
     */
    public static void assertListMatching(FlashcardListPanelHandle flashcardListPanelHandle, Flashcard... flashcards) {
        for (int i = 0; i < flashcards.length; i++) {
            flashcardListPanelHandle.navigateToCard(i);
            assertCardDisplaysFlashcard(flashcards[i], flashcardListPanelHandle.getFlashcardCardHandle(i));
        }
    }

    /**
     * Asserts that the list in {@code flashcardListPanelHandle} displays the details of {@code flashcards} correctly
     * and
     * in the correct order.
     */
    public static void assertListMatching(FlashcardListPanelHandle flashcardListPanelHandle,
                                          List<Flashcard> flashcards) {
        assertListMatching(flashcardListPanelHandle, flashcards.toArray(new Flashcard[0]));
    }

    /**
     * Asserts the size of the list in {@code flashcardListPanelHandle} equals to {@code size}.
     */
    public static void assertListSize(FlashcardListPanelHandle flashcardListPanelHandle, int size) {
        int numberOfPeople = flashcardListPanelHandle.getListSize();
        assertEquals(size, numberOfPeople);
    }

    /**
     * Asserts the message shown in {@code resultDisplayHandle} equals to {@code expected}.
     */
    public static void assertResultMessage(ResultDisplayHandle resultDisplayHandle, String expected) {
        assertEquals(expected, resultDisplayHandle.getText());
    }
}
