import java.io.Serializable;

/**
 * Values for the integer identifiers used in a NetworkMessage
 */
public enum MessageValues implements Serializable {
    TERMINATE, VOTE, STEAL, FORGE, SEARCH, GIVE, MESSAGE, WIN, LOSS
}
