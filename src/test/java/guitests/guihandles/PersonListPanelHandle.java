package guitests.guihandles;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javafx.scene.Node;
import javafx.scene.control.ListView;
import seedu.address.model.flashcard.FlashCard;

/**
 * Provides a handle for {@code PersonListPanel} containing the list of {@code PersonCard}.
 */
public class PersonListPanelHandle extends NodeHandle<ListView<FlashCard>> {
    public static final String PERSON_LIST_VIEW_ID = "#personListView";

    private static final String CARD_PANE_ID = "#cardPane";

    private Optional<FlashCard> lastRememberedSelectedPersonCard;

    public PersonListPanelHandle(ListView<FlashCard> personListPanelNode) {
        super(personListPanelNode);
    }

    /**
     * Returns a handle to the selected {@code PersonCardHandle}.
     * A maximum of 1 item can be selected at any time.
     * @throws AssertionError if no card is selected, or more than 1 card is selected.
     * @throws IllegalStateException if the selected card is currently not in the scene graph.
     */
    public PersonCardHandle getHandleToSelectedCard() {
        List<FlashCard> selectedFlashCardList = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedFlashCardList.size() != 1) {
            throw new AssertionError("FlashCard list size expected 1.");
        }

        return getAllCardNodes().stream()
                .map(PersonCardHandle::new)
                .filter(handle -> handle.equals(selectedFlashCardList.get(0)))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    /**
     * Returns the index of the selected card.
     */
    public int getSelectedCardIndex() {
        return getRootNode().getSelectionModel().getSelectedIndex();
    }

    /**
     * Returns true if a card is currently selected.
     */
    public boolean isAnyCardSelected() {
        List<FlashCard> selectedCardsList = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedCardsList.size() > 1) {
            throw new AssertionError("Card list size expected 0 or 1.");
        }

        return !selectedCardsList.isEmpty();
    }

    /**
     * Navigates the listview to display {@code flashCard}.
     */
    public void navigateToCard(FlashCard flashCard) {
        if (!getRootNode().getItems().contains(flashCard)) {
            throw new IllegalArgumentException("FlashCard does not exist.");
        }

        guiRobot.interact(() -> {
            getRootNode().scrollTo(flashCard);
        });
        guiRobot.pauseForHuman();
    }

    /**
     * Navigates the listview to {@code index}.
     */
    public void navigateToCard(int index) {
        if (index < 0 || index >= getRootNode().getItems().size()) {
            throw new IllegalArgumentException("Index is out of bounds.");
        }

        guiRobot.interact(() -> {
            getRootNode().scrollTo(index);
        });
        guiRobot.pauseForHuman();
    }

    /**
     * Selects the {@code PersonCard} at {@code index} in the list.
     */
    public void select(int index) {
        getRootNode().getSelectionModel().select(index);
    }

    /**
     * Returns the flashCard card handle of a flashCard associated with the {@code index} in the list.
     * @throws IllegalStateException if the selected card is currently not in the scene graph.
     */
    public PersonCardHandle getPersonCardHandle(int index) {
        return getAllCardNodes().stream()
                .map(PersonCardHandle::new)
                .filter(handle -> handle.equals(getPerson(index)))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    private FlashCard getPerson(int index) {
        return getRootNode().getItems().get(index);
    }

    /**
     * Returns all card nodes in the scene graph.
     * Card nodes that are visible in the listview are definitely in the scene graph, while some nodes that are not
     * visible in the listview may also be in the scene graph.
     */
    private Set<Node> getAllCardNodes() {
        return guiRobot.lookup(CARD_PANE_ID).queryAll();
    }

    /**
     * Remembers the selected {@code PersonCard} in the list.
     */
    public void rememberSelectedPersonCard() {
        List<FlashCard> selectedItems = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedItems.size() == 0) {
            lastRememberedSelectedPersonCard = Optional.empty();
        } else {
            lastRememberedSelectedPersonCard = Optional.of(selectedItems.get(0));
        }
    }

    /**
     * Returns true if the selected {@code PersonCard} is different from the value remembered by the most recent
     * {@code rememberSelectedPersonCard()} call.
     */
    public boolean isSelectedPersonCardChanged() {
        List<FlashCard> selectedItems = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedItems.size() == 0) {
            return lastRememberedSelectedPersonCard.isPresent();
        } else {
            return !lastRememberedSelectedPersonCard.isPresent()
                    || !lastRememberedSelectedPersonCard.get().equals(selectedItems.get(0));
        }
    }

    /**
     * Returns the size of the list.
     */
    public int getListSize() {
        return getRootNode().getItems().size();
    }
}
