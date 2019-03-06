package guitests.guihandles;

import guitests.GuiRobot;
import javafx.concurrent.Worker;
import javafx.scene.Node;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;

public class CardViewHandle extends NodeHandle<Node> {

    public static final String CARD_VIEW_ID = "#cardPlaceholder";

    public CardViewHandle(Node cardViewPaneNode) {
        super(cardViewPaneNode);
    }

}