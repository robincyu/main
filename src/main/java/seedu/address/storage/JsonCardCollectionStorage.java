package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.ReadOnlyCardCollection;

/**
 * A class to access CardCollection data stored as a json file on the hard disk.
 */
public class JsonCardCollectionStorage implements CardCollectionStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonCardCollectionStorage.class);

    private Path filePath;

    public JsonCardCollectionStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getCardCollectionFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyCardCollection> readCardCollection() throws DataConversionException {
        return readCardCollection(filePath);
    }

    /**
     * Similar to {@link #readCardCollection()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyCardCollection> readCardCollection(Path filePath) throws DataConversionException {
        requireNonNull(filePath);

        Optional<JsonSerializableCardCollection> jsonCardCollection = JsonUtil.readJsonFile(
            filePath, JsonSerializableCardCollection.class);
        if (!jsonCardCollection.isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonCardCollection.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveCardCollection(ReadOnlyCardCollection cardCollection) throws IOException {
        saveCardCollection(cardCollection, filePath);
    }

    /**
     * Similar to {@link #saveCardCollection(ReadOnlyCardCollection)}.
     *
     * @param filePath location of the data. Cannot be null.
     */
    public void saveCardCollection(ReadOnlyCardCollection cardCollection, Path filePath) throws IOException {
        requireNonNull(cardCollection);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableCardCollection(cardCollection), filePath);
    }

}
