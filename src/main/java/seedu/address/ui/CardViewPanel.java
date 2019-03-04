package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.net.URL;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import seedu.address.MainApp;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;

/**
 * The Browser Panel of the App.
 */
public class CardViewPanel extends UiPart<Region> {

    private static final String FXML = "CardViewPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    @FXML
    private StackPane cardPlaceholder;

    public CardViewPanel(ObservableValue<Person> selectedPerson) {
        super(FXML);

        // Load person page when selected person changes.
        selectedPerson.addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                loadDefaultPage();
                return;
            }
            loadCardPage(newValue);
        });

        loadDefaultPage();
    }

    private void loadCardPage(Person person) {
        loadPage(new PersonCard(person, 0));
    }

    public void loadPage(UiPart<Region> uiPart) {
        cardPlaceholder.getChildren().clear();
        if (uiPart != null) cardPlaceholder.getChildren().add(uiPart.getRoot());
    }

    /**
     * Loads a default HTML file with a background that matches the general theme.
     */
    private void loadDefaultPage() {
        loadPage(null);
    }

}
