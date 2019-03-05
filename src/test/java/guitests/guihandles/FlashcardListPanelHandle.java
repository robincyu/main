package guitests.guihandles;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javafx.scene.Node;
import javafx.scene.control.ListView;
import seedu.address.model.flashcard.Flashcard;

/**
 * Provides a handle for {@code FlashcardListPanel} containing the list of {@code FlashcardCard}.
 */
public class FlashcardListPanelHandle extends NodeHandle<ListView<Flashcard>> {
    public static final String FLASHCARD_LIST_VIEW_ID = "#flashcardListView";

    private static final String CARD_PANE_ID = "#cardPane";

    private Optional<Flashcard> lastRememberedSelectedFlashcardCard;

    public FlashcardListPanelHandle(ListView<Flashcard> flashcardListPanelNode) {
        super(flashcardListPanelNode);
    }

    /**
     * Returns a handle to the selected {@code FlashcardCardHandle}.
     * A maximum of 1 item can be selected at any time.
     *
     * @throws AssertionError        if no card is selected, or more than 1 card is selected.
     * @throws IllegalStateException if the selected card is currently not in the scene graph.
     */
    public FlashcardCardHandle getHandleToSelectedCard() {
        List<Flashcard> selectedFlashcardList = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedFlashcardList.size() != 1) {
            throw new AssertionError("Flashcard list size expected 1.");
        }

        return getAllCardNodes().stream()
            .map(FlashcardCardHandle::new)
            .filter(handle -> handle.equals(selectedFlashcardList.get(0)))
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
        List<Flashcard> selectedCardsList = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedCardsList.size() > 1) {
            throw new AssertionError("Card list size expected 0 or 1.");
        }

        return !selectedCardsList.isEmpty();
    }

    /**
     * Navigates the listview to display {@code flashcard}.
     */
    public void navigateToCard(Flashcard flashcard) {
        if (!getRootNode().getItems().contains(flashcard)) {
            throw new IllegalArgumentException("Flashcard does not exist.");
        }

        guiRobot.interact(() -> {
            getRootNode().scrollTo(flashcard);
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
     * Selects the {@code FlashcardCard} at {@code index} in the list.
     */
    public void select(int index) {
        getRootNode().getSelectionModel().select(index);
    }

    /**
     * Returns the flashcard card handle of a flashcard associated with the {@code index} in the list.
     *
     * @throws IllegalStateException if the selected card is currently not in the scene graph.
     */
    public FlashcardCardHandle getFlashcardCardHandle(int index) {
        return getAllCardNodes().stream()
            .map(FlashcardCardHandle::new)
            .filter(handle -> handle.equals(getFlashcard(index)))
            .findFirst()
            .orElseThrow(IllegalStateException::new);
    }

    private Flashcard getFlashcard(int index) {
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
     * Remembers the selected {@code FlashcardCard} in the list.
     */
    public void rememberSelectedFlashcardCard() {
        List<Flashcard> selectedItems = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedItems.size() == 0) {
            lastRememberedSelectedFlashcardCard = Optional.empty();
        } else {
            lastRememberedSelectedFlashcardCard = Optional.of(selectedItems.get(0));
        }
    }

    /**
     * Returns true if the selected {@code FlashcardCard} is different from the value remembered by the most recent
     * {@code rememberSelectedFlashcardCard()} call.
     */
    public boolean isSelectedFlashcardCardChanged() {
        List<Flashcard> selectedItems = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedItems.size() == 0) {
            return lastRememberedSelectedFlashcardCard.isPresent();
        } else {
            return !lastRememberedSelectedFlashcardCard.isPresent()
                || !lastRememberedSelectedFlashcardCard.get().equals(selectedItems.get(0));
        }
    }

    /**
     * Returns the size of the list.
     */
    public int getListSize() {
        return getRootNode().getItems().size();
    }
}
