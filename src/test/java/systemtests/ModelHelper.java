package systemtests;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import seedu.address.model.Model;
import seedu.address.model.flashcard.Flashcard;

/**
 * Contains helper methods to set up {@code Model} for testing.
 */
public class ModelHelper {
    private static final Predicate<Flashcard> PREDICATE_MATCHING_NO_FLASHCARDS = unused -> false;

    /**
     * Updates {@code model}'s filtered list to display only {@code toDisplay}.
     */
    public static void setFilteredList(Model model, List<Flashcard> toDisplay) {
        Optional<Predicate<Flashcard>> predicate =
            toDisplay.stream().map(ModelHelper::getPredicateMatching).reduce(Predicate::or);
        model.updateFilteredFlashcardList(predicate.orElse(PREDICATE_MATCHING_NO_FLASHCARDS));
    }

    /**
     * @see ModelHelper#setFilteredList(Model, List)
     */
    public static void setFilteredList(Model model, Flashcard... toDisplay) {
        setFilteredList(model, Arrays.asList(toDisplay));
    }

    /**
     * Returns a predicate that evaluates to true if this {@code Flashcard} equals to {@code other}.
     */
    private static Predicate<Flashcard> getPredicateMatching(Flashcard other) {
        return flashcard -> flashcard.equals(other);
    }
}
