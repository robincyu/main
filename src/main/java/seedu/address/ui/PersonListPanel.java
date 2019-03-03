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
import seedu.address.model.flashcard.FlashCard;

/**
 * Panel containing the list of persons.
 */
public class PersonListPanel extends UiPart<Region> {
    private static final String FXML = "PersonListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(PersonListPanel.class);

    @FXML
    private ListView<FlashCard> personListView;

    public PersonListPanel(ObservableList<FlashCard> flashCardList, ObservableValue<FlashCard> selectedPerson,
                           Consumer<FlashCard> onSelectedPersonChange) {
        super(FXML);
        personListView.setItems(flashCardList);
        personListView.setCellFactory(listView -> new PersonListViewCell());
        personListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            logger.fine("Selection in flashCard list panel changed to : '" + newValue + "'");
            onSelectedPersonChange.accept(newValue);
        });
        selectedPerson.addListener((observable, oldValue, newValue) -> {
            logger.fine("Selected flashCard changed to: " + newValue);

            // Don't modify selection if we are already selecting the selected flashCard,
            // otherwise we would have an infinite loop.
            if (Objects.equals(personListView.getSelectionModel().getSelectedItem(), newValue)) {
                return;
            }

            if (newValue == null) {
                personListView.getSelectionModel().clearSelection();
            } else {
                int index = personListView.getItems().indexOf(newValue);
                personListView.scrollTo(index);
                personListView.getSelectionModel().clearAndSelect(index);
            }
        });
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code FlashCard} using a {@code PersonCard}.
     */
    class PersonListViewCell extends ListCell<FlashCard> {
        @Override
        protected void updateItem(FlashCard flashCard, boolean empty) {
            super.updateItem(flashCard, empty);

            if (empty || flashCard == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new PersonCard(flashCard, getIndex() + 1).getRoot());
            }
        }
    }

}
