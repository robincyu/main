package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyCardCollection;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;

/**
 * Manages storage of CardCollection data in local storage.
 */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private CardCollectionStorage cardCollectionStorage;
    private UserPrefsStorage userPrefsStorage;


    public StorageManager(CardCollectionStorage cardCollectionStorage, UserPrefsStorage userPrefsStorage) {
        super();
        this.cardCollectionStorage = cardCollectionStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ CardCollection methods ==============================

    @Override
    public Path getCardCollectionFilePath() {
        return cardCollectionStorage.getCardCollectionFilePath();
    }

    @Override
    public Optional<ReadOnlyCardCollection> readCardCollection() throws DataConversionException, IOException {
        return readCardCollection(cardCollectionStorage.getCardCollectionFilePath());
    }

    @Override
    public Optional<ReadOnlyCardCollection> readCardCollection(Path filePath) throws DataConversionException,
        IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return cardCollectionStorage.readCardCollection(filePath);
    }

    @Override
    public void saveCardCollection(ReadOnlyCardCollection cardCollection) throws IOException {
        saveCardCollection(cardCollection, cardCollectionStorage.getCardCollectionFilePath());
    }

    @Override
    public void saveCardCollection(ReadOnlyCardCollection cardCollection, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        cardCollectionStorage.saveCardCollection(cardCollection, filePath);
    }

}
