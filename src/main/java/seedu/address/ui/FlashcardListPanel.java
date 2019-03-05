package seedu.address.ui;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Logger;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.flashcard.Flashcard;

/**
 * Panel containing the list of flashcards.
 */
public class FlashcardListPanel extends UiPart<Region> {
    private static final String FXML = "FlashcardListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(FlashcardListPanel.class);

    @FXML
    private ListView<Flashcard> flashcardListView;

    public FlashcardListPanel(ObservableList<Flashcard> flashcardList, ObservableValue<Flashcard> selectedFlashcard,
                              Consumer<Flashcard> onSelectedFlashcardChange) {
        super(FXML);
        flashcardListView.setItems(flashcardList);
        flashcardListView.setCellFactory(listView -> new FlashcardListViewCell());
        flashcardListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            logger.fine("Selection in flashcard list panel changed to : '" + newValue + "'");
            onSelectedFlashcardChange.accept(newValue);
        });
        selectedFlashcard.addListener((observable, oldValue, newValue) -> {
            logger.fine("Selected flashcard changed to: " + newValue);

            // Don't modify selection if we are already selecting the selected flashcard,
            // otherwise we would have an infinite loop.
            if (Objects.equals(flashcardListView.getSelectionModel().getSelectedItem(), newValue)) {
                return;
            }

            if (newValue == null) {
                flashcardListView.getSelectionModel().clearSelection();
            } else {
                int index = flashcardListView.getItems().indexOf(newValue);
                flashcardListView.scrollTo(index);
                flashcardListView.getSelectionModel().clearAndSelect(index);
            }
        });
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Flashcard} using a {@code FlashcardCard}.
     */
    class FlashcardListViewCell extends ListCell<Flashcard> {
        @Override
        protected void updateItem(Flashcard flashcard, boolean empty) {
            super.updateItem(flashcard, empty);

            if (empty || flashcard == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new FlashcardCard(flashcard, getIndex() + 1).getRoot());
            }
        }
    }

}
