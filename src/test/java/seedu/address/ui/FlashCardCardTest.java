package seedu.address.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardDisplaysPerson;

import org.junit.Test;

import guitests.guihandles.PersonCardHandle;
import seedu.address.model.flashcard.FlashCard;
import seedu.address.testutil.PersonBuilder;

public class FlashCardCardTest extends GuiUnitTest {

    @Test
    public void display() {
        // no tags
        FlashCard flashCardWithNoTags = new PersonBuilder().withTags(new String[0]).build();
        PersonCard personCard = new PersonCard(flashCardWithNoTags, 1);
        uiPartRule.setUiPart(personCard);
        assertCardDisplay(personCard, flashCardWithNoTags, 1);

        // with tags
        FlashCard flashCardWithTags = new PersonBuilder().build();
        personCard = new PersonCard(flashCardWithTags, 2);
        uiPartRule.setUiPart(personCard);
        assertCardDisplay(personCard, flashCardWithTags, 2);
    }

    @Test
    public void equals() {
        FlashCard flashCard = new PersonBuilder().build();
        PersonCard personCard = new PersonCard(flashCard, 0);

        // same flashCard, same index -> returns true
        PersonCard copy = new PersonCard(flashCard, 0);
        assertTrue(personCard.equals(copy));

        // same object -> returns true
        assertTrue(personCard.equals(personCard));

        // null -> returns false
        assertFalse(personCard.equals(null));

        // different types -> returns false
        assertFalse(personCard.equals(0));

        // different flashCard, same index -> returns false
        FlashCard differentFlashCard = new PersonBuilder().withName("differentName").build();
        assertFalse(personCard.equals(new PersonCard(differentFlashCard, 0)));

        // same flashCard, different index -> returns false
        assertFalse(personCard.equals(new PersonCard(flashCard, 1)));
    }

    /**
     * Asserts that {@code personCard} displays the details of {@code expectedFlashCard} correctly and matches
     * {@code expectedId}.
     */
    private void assertCardDisplay(PersonCard personCard, FlashCard expectedFlashCard, int expectedId) {
        guiRobot.pauseForHuman();

        PersonCardHandle personCardHandle = new PersonCardHandle(personCard.getRoot());

        // verify id is displayed correctly
        assertEquals(Integer.toString(expectedId) + ". ", personCardHandle.getId());

        // verify flashCard details are displayed correctly
        assertCardDisplaysPerson(expectedFlashCard, personCardHandle);
    }
}
