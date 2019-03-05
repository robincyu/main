package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static seedu.address.testutil.TypicalFlashcards.ALICE;
import static seedu.address.testutil.TypicalFlashcards.HOON;
import static seedu.address.testutil.TypicalFlashcards.IDA;
import static seedu.address.testutil.TypicalFlashcards.getTypicalCardCollection;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.CardCollection;
import seedu.address.model.ReadOnlyCardCollection;

public class JsonCardCollectionStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonCardCollectionStorageTest");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void readCardCollection_nullFilePath_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        readCardCollection(null);
    }

    private java.util.Optional<ReadOnlyCardCollection> readCardCollection(String filePath) throws Exception {
        return new JsonCardCollectionStorage(Paths.get(filePath))
            .readCardCollection(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
            ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
            : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readCardCollection("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() throws Exception {

        thrown.expect(DataConversionException.class);
        readCardCollection("notJsonFormatCardCollection.json");

        // IMPORTANT: Any code below an exception-throwing line (like the one above) will be ignored.
        // That means you should not have more than one exception test in one method
    }

    @Test
    public void readCardCollection_invalidCardCardCollection_throwDataConversionException() throws Exception {
        thrown.expect(DataConversionException.class);
        readCardCollection("invalidCardCardCollection.json");
    }

    @Test
    public void readCardCollection_invalidAndValidCardCardCollection_throwDataConversionException() throws Exception {
        thrown.expect(DataConversionException.class);
        readCardCollection("invalidAndValidCardCardCollection.json");
    }

    @Test
    public void readAndSaveCardCollection_allInOrder_success() throws Exception {
        Path filePath = testFolder.getRoot().toPath().resolve("TempCardCollection.json");
        CardCollection original = getTypicalCardCollection();
        JsonCardCollectionStorage jsonCardCollectionStorage = new JsonCardCollectionStorage(filePath);

        // Save in new file and read back
        jsonCardCollectionStorage.saveCardCollection(original, filePath);
        ReadOnlyCardCollection readBack = jsonCardCollectionStorage.readCardCollection(filePath).get();
        assertEquals(original, new CardCollection(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addFlashcard(HOON);
        original.removeFlashcard(ALICE);
        jsonCardCollectionStorage.saveCardCollection(original, filePath);
        readBack = jsonCardCollectionStorage.readCardCollection(filePath).get();
        assertEquals(original, new CardCollection(readBack));

        // Save and read without specifying file path
        original.addFlashcard(IDA);
        jsonCardCollectionStorage.saveCardCollection(original); // file path not specified
        readBack = jsonCardCollectionStorage.readCardCollection().get(); // file path not specified
        assertEquals(original, new CardCollection(readBack));

    }

    @Test
    public void saveCardCollection_nullCardCollection_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveCardCollection(null, "SomeFile.json");
    }

    /**
     * Saves {@code cardCollection} at the specified {@code filePath}.
     */
    private void saveCardCollection(ReadOnlyCardCollection cardCollection, String filePath) {
        try {
            new JsonCardCollectionStorage(Paths.get(filePath))
                .saveCardCollection(cardCollection, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveCardCollection_nullFilePath_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveCardCollection(new CardCollection(), null);
    }
}
