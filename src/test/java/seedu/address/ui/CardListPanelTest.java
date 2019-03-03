package seedu.address.ui;

import static java.time.Duration.ofMillis;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalPersons;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardDisplaysPerson;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardEquals;

import java.util.Collections;

import guitests.guihandles.CardListPanelHandle;
import org.junit.Test;

import guitests.guihandles.CardCardHandle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.flashcard.Address;
import seedu.address.model.flashcard.Card;
import seedu.address.model.flashcard.Email;
import seedu.address.model.flashcard.Name;
import seedu.address.model.flashcard.Phone;

public class CardListPanelTest extends GuiUnitTest {
    private static final ObservableList<Card> TYPICAL_FLASH_CARDS =
            FXCollections.observableList(getTypicalPersons());

    private static final long CARD_CREATION_AND_DELETION_TIMEOUT = 2500;

    private final SimpleObjectProperty<Card> selectedPerson = new SimpleObjectProperty<>();
    private CardListPanelHandle cardListPanelHandle;

    @Test
    public void display() {
        initUi(TYPICAL_FLASH_CARDS);

        for (int i = 0; i < TYPICAL_FLASH_CARDS.size(); i++) {
            cardListPanelHandle.navigateToCard(TYPICAL_FLASH_CARDS.get(i));
            Card expectedCard = TYPICAL_FLASH_CARDS.get(i);
            CardCardHandle actualCard = cardListPanelHandle.getPersonCardHandle(i);

            assertCardDisplaysPerson(expectedCard, actualCard);
            assertEquals(Integer.toString(i + 1) + ". ", actualCard.getId());
        }
    }

    @Test
    public void selection_modelSelectedPersonChanged_selectionChanges() {
        initUi(TYPICAL_FLASH_CARDS);
        Card secondCard = TYPICAL_FLASH_CARDS.get(INDEX_SECOND_PERSON.getZeroBased());
        guiRobot.interact(() -> selectedPerson.set(secondCard));
        guiRobot.pauseForHuman();

        CardCardHandle expectedPerson = cardListPanelHandle.getPersonCardHandle(INDEX_SECOND_PERSON.getZeroBased());
        CardCardHandle selectedPerson = cardListPanelHandle.getHandleToSelectedCard();
        assertCardEquals(expectedPerson, selectedPerson);
    }

    /**
     * Verifies that creating and deleting large number of persons in {@code PersonListPanel} requires lesser than
     * {@code CARD_CREATION_AND_DELETION_TIMEOUT} milliseconds to execute.
     */
    @Test
    public void performanceTest() {
        ObservableList<Card> backingList = createBackingList(10000);

        assertTimeoutPreemptively(ofMillis(CARD_CREATION_AND_DELETION_TIMEOUT), () -> {
            initUi(backingList);
            guiRobot.interact(backingList::clear);
        }, "Creation and deletion of card cards exceeded time limit");
    }

    /**
     * Returns a list of persons containing {@code personCount} persons that is used to populate the
     * {@code PersonListPanel}.
     */
    private ObservableList<Card> createBackingList(int personCount) {
        ObservableList<Card> backingList = FXCollections.observableArrayList();
        for (int i = 0; i < personCount; i++) {
            Name name = new Name(i + "a");
            Phone phone = new Phone("000");
            Email email = new Email("a@aa");
            Address address = new Address("a");
            Card card = new Card(name, phone, email, address, Collections.emptySet());
            backingList.add(card);
        }
        return backingList;
    }

    /**
     * Initializes {@code cardListPanelHandle} with a {@code PersonListPanel} backed by {@code backingList}.
     * Also shows the {@code Stage} that displays only {@code PersonListPanel}.
     */
    private void initUi(ObservableList<Card> backingList) {
        PersonListPanel personListPanel =
                new PersonListPanel(backingList, selectedPerson, selectedPerson::set);
        uiPartRule.setUiPart(personListPanel);

        cardListPanelHandle = new CardListPanelHandle(getChildNode(personListPanel.getRoot(),
                CardListPanelHandle.PERSON_LIST_VIEW_ID));
    }
}
