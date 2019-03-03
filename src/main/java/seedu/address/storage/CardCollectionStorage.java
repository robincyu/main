package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.CardCollection;
import seedu.address.model.ReadOnlyCardCollection;

/**
 * Represents a storage for {@link CardCollection}.
 */
public interface CardCollectionStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getCardCollectionFilePath();

    /**
     * Returns CardCollection data as a {@link ReadOnlyCardCollection}.
     * Returns {@code Optional.empty()} if storage file is not found.
     *
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException             if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyCardCollection> readCardCollection() throws DataConversionException, IOException;

    /**
     * @see #getCardCollectionFilePath()
     */
    Optional<ReadOnlyCardCollection> readCardCollection(Path filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyCardCollection} to the storage.
     *
     * @param cardCollection cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveCardCollection(ReadOnlyCardCollection cardCollection) throws IOException;

    /**
     * @see #saveCardCollection(ReadOnlyCardCollection)
     */
    void saveCardCollection(ReadOnlyCardCollection cardCollection, Path filePath) throws IOException;

}
