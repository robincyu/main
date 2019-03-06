package guitests.guihandles;

import javafx.scene.Node;

/**
 * A handler for the {@code CardView} of the UI.
 */
public class CardViewHandle extends NodeHandle<Node> {

    public static final String CARD_VIEW_ID = "#cardPlaceholder";

    public CardViewHandle(Node cardViewPaneNode) {
        super(cardViewPaneNode);
    }

}
