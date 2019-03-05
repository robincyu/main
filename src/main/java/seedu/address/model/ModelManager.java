package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.flashcard.Flashcard;
import seedu.address.model.flashcard.exceptions.FlashcardNotFoundException;

/**
 * Represents the in-memory model of the card collection data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final VersionedCardCollection versionedCardCollection;
    private final UserPrefs userPrefs;
    private final FilteredList<Flashcard> filteredFlashcards;
    private final SimpleObjectProperty<Flashcard> selectedFlashcard = new SimpleObjectProperty<>();

    /**
     * Initializes a ModelManager with the given cardCollection and userPrefs.
     */
    public ModelManager(ReadOnlyCardCollection cardCollection, ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(cardCollection, userPrefs);

        logger.fine("Initializing with card collection: " + cardCollection + " and user prefs " + userPrefs);

        versionedCardCollection = new VersionedCardCollection(cardCollection);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredFlashcards = new FilteredList<>(versionedCardCollection.getFlashcardList());
        filteredFlashcards.addListener(this::ensureSelectedFlashcardIsValid);
    }

    public ModelManager() {
        this(new CardCollection(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getCardCollectionFilePath() {
        return userPrefs.getCardCollectionFilePath();
    }

    @Override
    public void setCardCollectionFilePath(Path cardCollectionFilePath) {
        requireNonNull(cardCollectionFilePath);
        userPrefs.setCardCollectionFilePath(cardCollectionFilePath);
    }

    //=========== CardCollection ================================================================================

    @Override
    public void setCardCollection(ReadOnlyCardCollection cardCollection) {
        versionedCardCollection.resetData(cardCollection);
    }

    @Override
    public ReadOnlyCardCollection getCardCollection() {
        return versionedCardCollection;
    }

    @Override
    public boolean hasFlashcard(Flashcard flashcard) {
        requireNonNull(flashcard);
        return versionedCardCollection.hasFlashcard(flashcard);
    }

    @Override
    public void deleteFlashcard(Flashcard target) {
        versionedCardCollection.removeFlashcard(target);
    }

    @Override
    public void addFlashcard(Flashcard flashcard) {
        versionedCardCollection.addFlashcard(flashcard);
        updateFilteredFlashcardList(PREDICATE_SHOW_ALL_FLASHCARDS);
    }

    @Override
    public void setFlashcard(Flashcard target, Flashcard editedFlashcard) {
        requireAllNonNull(target, editedFlashcard);

        versionedCardCollection.setFlashcard(target, editedFlashcard);
    }

    //=========== Filtered Flashcard List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Flashcard} backed by the internal list of
     * {@code versionedCardCollection}
     */
    @Override
    public ObservableList<Flashcard> getFilteredFlashcardList() {
        return filteredFlashcards;
    }

    @Override
    public void updateFilteredFlashcardList(Predicate<Flashcard> predicate) {
        requireNonNull(predicate);
        filteredFlashcards.setPredicate(predicate);
    }

    //=========== Undo/Redo =================================================================================

    @Override
    public boolean canUndoCardCollection() {
        return versionedCardCollection.canUndo();
    }

    @Override
    public boolean canRedoCardCollection() {
        return versionedCardCollection.canRedo();
    }

    @Override
    public void undoCardCollection() {
        versionedCardCollection.undo();
    }

    @Override
    public void redoCardCollection() {
        versionedCardCollection.redo();
    }

    @Override
    public void commitCardCollection() {
        versionedCardCollection.commit();
    }

    //=========== Selected flashcard ===========================================================================

    @Override
    public ReadOnlyProperty<Flashcard> selectedFlashcardProperty() {
        return selectedFlashcard;
    }

    @Override
    public Flashcard getSelectedFlashcard() {
        return selectedFlashcard.getValue();
    }

    @Override
    public void setSelectedFlashcard(Flashcard flashcard) {
        if (flashcard != null && !filteredFlashcards.contains(flashcard)) {
            throw new FlashcardNotFoundException();
        }
        selectedFlashcard.setValue(flashcard);
    }

    /**
     * Ensures {@code selectedFlashcard} is a valid flashcard in {@code filteredFlashcards}.
     */
    private void ensureSelectedFlashcardIsValid(ListChangeListener.Change<? extends Flashcard> change) {
        while (change.next()) {
            if (selectedFlashcard.getValue() == null) {
                // null is always a valid selected flashcard, so we do not need to check that it is valid anymore.
                return;
            }

            boolean wasSelectedFlashcardReplaced =
                change.wasReplaced() && change.getAddedSize() == change.getRemovedSize()
                && change.getRemoved().contains(selectedFlashcard.getValue());
            if (wasSelectedFlashcardReplaced) {
                // Update selectedFlashcard to its new value.
                int index = change.getRemoved().indexOf(selectedFlashcard.getValue());
                selectedFlashcard.setValue(change.getAddedSubList().get(index));
                continue;
            }

            boolean wasSelectedFlashcardRemoved = change.getRemoved().stream()
                .anyMatch(removedFlashcard -> selectedFlashcard.getValue().isSameFlashcard(removedFlashcard));
            if (wasSelectedFlashcardRemoved) {
                // Select the flashcard that came before it in the list,
                // or clear the selection if there is no such flashcard.
                selectedFlashcard.setValue(change.getFrom() > 0 ? change.getList().get(change.getFrom() - 1) : null);
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return versionedCardCollection.equals(other.versionedCardCollection)
            && userPrefs.equals(other.userPrefs)
            && filteredFlashcards.equals(other.filteredFlashcards)
            && Objects.equals(selectedFlashcard.get(), other.selectedFlashcard.get());
    }

}
