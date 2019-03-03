package seedu.address.testutil;

import seedu.address.model.CardCollection;
import seedu.address.model.person.Person;

/**
 * A utility class to help with building CardCollection objects.
 * Example usage: <br>
 *     {@code CardCollection ab = new CardCollectionBuilder().withPerson("John", "Doe").build();}
 */
public class CardCollectionBuilder {

    private CardCollection cardCollection;

    public CardCollectionBuilder() {
        cardCollection = new CardCollection();
    }

    public CardCollectionBuilder(CardCollection cardCollection) {
        this.cardCollection = cardCollection;
    }

    /**
     * Adds a new {@code Person} to the {@code CardCollection} that we are building.
     */
    public CardCollectionBuilder withPerson(Person person) {
        cardCollection.addPerson(person);
        return this;
    }

    public CardCollection build() {
        return cardCollection;
    }
}
