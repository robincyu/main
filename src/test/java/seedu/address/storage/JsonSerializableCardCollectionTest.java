package seedu.address.storage;

import static org.junit.Assert.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.CardCollection;
import seedu.address.testutil.TypicalFlashcards;

public class JsonSerializableCardCollectionTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableCardCollectionTest");
    private static final Path TYPICAL_FLASHCARDS_FILE = TEST_DATA_FOLDER.resolve("typicalCardsCardCollection.json");
    private static final Path INVALID_FLASHCARD_FILE = TEST_DATA_FOLDER.resolve("invalidCardCardCollection.json");
    private static final Path DUPLICATE_FLASHCARD_FILE = TEST_DATA_FOLDER.resolve("duplicateCardCardCollection.json");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void toModelType_typicalFlashcardsFile_success() throws Exception {
        JsonSerializableCardCollection dataFromFile = JsonUtil.readJsonFile(TYPICAL_FLASHCARDS_FILE,
            JsonSerializableCardCollection.class).get();
        CardCollection cardCollectionFromFile = dataFromFile.toModelType();
        CardCollection typicalFlashcardsCardCollection = TypicalFlashcards.getTypicalCardCollection();
        assertEquals(cardCollectionFromFile, typicalFlashcardsCardCollection);
    }

    @Test
    public void toModelType_invalidFlashcardFile_throwsIllegalValueException() throws Exception {
        JsonSerializableCardCollection dataFromFile = JsonUtil.readJsonFile(INVALID_FLASHCARD_FILE,
            JsonSerializableCardCollection.class).get();
        thrown.expect(IllegalValueException.class);
        dataFromFile.toModelType();
    }

    @Test
    public void toModelType_duplicateFlashcards_throwsIllegalValueException() throws Exception {
        JsonSerializableCardCollection dataFromFile = JsonUtil.readJsonFile(DUPLICATE_FLASHCARD_FILE,
            JsonSerializableCardCollection.class).get();
        thrown.expect(IllegalValueException.class);
        thrown.expectMessage(JsonSerializableCardCollection.MESSAGE_DUPLICATE_FLASHCARD);
        dataFromFile.toModelType();
    }

}
