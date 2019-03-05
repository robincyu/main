package seedu.address.ui;

import static java.time.Duration.ofMillis;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static seedu.address.testutil.TypicalFlashcards.getTypicalFlashcards;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_FLASHCARD;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardDisplaysFlashcard;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardEquals;

import java.util.Collections;

import org.junit.Test;

import guitests.guihandles.FlashcardCardHandle;
import guitests.guihandles.FlashcardListPanelHandle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.flashcard.Address;
import seedu.address.model.flashcard.Email;
import seedu.address.model.flashcard.Flashcard;
import seedu.address.model.flashcard.Name;
import seedu.address.model.flashcard.Phone;

public class FlashcardListPanelTest extends GuiUnitTest {
    private static final ObservableList<Flashcard> TYPICAL_FLASHCARDS =
        FXCollections.observableList(getTypicalFlashcards());

    private static final long CARD_CREATION_AND_DELETION_TIMEOUT = 2500;

    private final SimpleObjectProperty<Flashcard> selectedFlashcard = new SimpleObjectProperty<>();
    private FlashcardListPanelHandle flashcardListPanelHandle;

    @Test
    public void display() {
        initUi(TYPICAL_FLASHCARDS);

        for (int i = 0; i < TYPICAL_FLASHCARDS.size(); i++) {
            flashcardListPanelHandle.navigateToCard(TYPICAL_FLASHCARDS.get(i));
            Flashcard expectedFlashcard = TYPICAL_FLASHCARDS.get(i);
            FlashcardCardHandle actualCard = flashcardListPanelHandle.getFlashcardCardHandle(i);

            assertCardDisplaysFlashcard(expectedFlashcard, actualCard);
            assertEquals((i + 1) + ". ", actualCard.getId());
        }
    }

    @Test
    public void selection_modelSelectedFlashcardChanged_selectionChanges() {
        initUi(TYPICAL_FLASHCARDS);
        Flashcard secondFlashcard = TYPICAL_FLASHCARDS.get(INDEX_SECOND_FLASHCARD.getZeroBased());
        guiRobot.interact(() -> selectedFlashcard.set(secondFlashcard));
        guiRobot.pauseForHuman();

        FlashcardCardHandle expectedFlashcard =
            flashcardListPanelHandle.getFlashcardCardHandle(INDEX_SECOND_FLASHCARD.getZeroBased());
        FlashcardCardHandle selectedFlashcard = flashcardListPanelHandle.getHandleToSelectedCard();
        assertCardEquals(expectedFlashcard, selectedFlashcard);
    }

    /**
     * Verifies that creating and deleting large number of flashcards in {@code FlashcardListPanel} requires lesser than
     * {@code CARD_CREATION_AND_DELETION_TIMEOUT} milliseconds to execute.
     */
    @Test
    public void performanceTest() {
        ObservableList<Flashcard> backingList = createBackingList(10000);

        assertTimeoutPreemptively(ofMillis(CARD_CREATION_AND_DELETION_TIMEOUT), () -> {
            initUi(backingList);
            guiRobot.interact(backingList::clear);
        }, "Creation and deletion of flashcard cards exceeded time limit");
    }

    /**
     * Returns a list of flashcards containing {@code flashcardCount} flashcards that is used to populate the
     * {@code FlashcardListPanel}.
     */
    private ObservableList<Flashcard> createBackingList(int flashcardCount) {
        ObservableList<Flashcard> backingList = FXCollections.observableArrayList();
        for (int i = 0; i < flashcardCount; i++) {
            Name name = new Name(i + "a");
            Phone phone = new Phone("000");
            Email email = new Email("a@aa");
            Address address = new Address("a");
            Flashcard flashcard = new Flashcard(name, phone, email, address, Collections.emptySet());
            backingList.add(flashcard);
        }
        return backingList;
    }

    /**
     * Initializes {@code flashcardListPanelHandle} with a {@code FlashcardListPanel} backed by {@code backingList}.
     * Also shows the {@code Stage} that displays only {@code FlashcardListPanel}.
     */
    private void initUi(ObservableList<Flashcard> backingList) {
        FlashcardListPanel flashcardListPanel =
            new FlashcardListPanel(backingList, selectedFlashcard, selectedFlashcard::set);
        uiPartRule.setUiPart(flashcardListPanel);

        flashcardListPanelHandle = new FlashcardListPanelHandle(getChildNode(flashcardListPanel.getRoot(),
            FlashcardListPanelHandle.FLASHCARD_LIST_VIEW_ID));
    }
}
