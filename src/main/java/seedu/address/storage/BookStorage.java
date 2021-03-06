package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.Item;
import seedu.address.model.ReadOnlyBook;

/**
 * Interface to represent storage of a book containing items.
 */
public interface BookStorage<T extends Item> {

    /**
     * Returns the file path of the data file.
     */
    Path getBookFilePath();

    /**
     * Returns book data as a {@link ReadOnlyBook}.
     * Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyBook<T>> readBook() throws DataConversionException, IOException;

    /**
     * @see #getBookFilePath()
     */
    Optional<ReadOnlyBook<T>> readBook(Path filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyBook} to the storage.
     * @param book cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveBook(ReadOnlyBook<T> book) throws IOException;

    /**
     * @see #saveBook(ReadOnlyBook)
     */
    void saveBook(ReadOnlyBook<T> book, Path filePath) throws IOException;
}
