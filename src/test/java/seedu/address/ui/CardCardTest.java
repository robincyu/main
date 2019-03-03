package seedu.address.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardDisplaysPerson;

import guitests.guihandles.CardCardHandle;
import org.junit.Test;

import seedu.address.model.flashcard.Card;
import seedu.address.testutil.PersonBuilder;

public class CardCardTest extends GuiUnitTest {

    @Test
    public void display() {
        // no tags
        Card cardWithNoTags = new PersonBuilder().withTags(new String[0]).build();
        PersonCard personCard = new PersonCard(cardWithNoTags, 1);
        uiPartRule.setUiPart(personCard);
        assertCardDisplay(personCard, cardWithNoTags, 1);

        // with tags
        Card cardWithTags = new PersonBuilder().build();
        personCard = new PersonCard(cardWithTags, 2);
        uiPartRule.setUiPart(personCard);
        assertCardDisplay(personCard, cardWithTags, 2);
    }

    @Test
    public void equals() {
        Card card = new PersonBuilder().build();
        PersonCard personCard = new PersonCard(card, 0);

        // same card, same index -> returns true
        PersonCard copy = new PersonCard(card, 0);
        assertTrue(personCard.equals(copy));

        // same object -> returns true
        assertTrue(personCard.equals(personCard));

        // null -> returns false
        assertFalse(personCard.equals(null));

        // different types -> returns false
        assertFalse(personCard.equals(0));

        // different card, same index -> returns false
        Card differentCard = new PersonBuilder().withName("differentName").build();
        assertFalse(personCard.equals(new PersonCard(differentCard, 0)));

        // same card, different index -> returns false
        assertFalse(personCard.equals(new PersonCard(card, 1)));
    }

    /**
     * Asserts that {@code personCard} displays the details of {@code expectedCard} correctly and matches
     * {@code expectedId}.
     */
    private void assertCardDisplay(PersonCard personCard, Card expectedCard, int expectedId) {
        guiRobot.pauseForHuman();

        CardCardHandle cardCardHandle = new CardCardHandle(personCard.getRoot());

        // verify id is displayed correctly
        assertEquals(Integer.toString(expectedId) + ". ", cardCardHandle.getId());

        // verify card details are displayed correctly
        assertCardDisplaysPerson(expectedCard, cardCardHandle);
    }
}
