import java.io.Serializable;

/**
 * Values for the integer identifiers used in a NetworkMessage
 */
public enum MessageValues implements Serializable {
    TERMINATE, VOTE, STEAL, FORGE, INVESTIGATE, PASS, MESSAGE, WIN, LOSS, CHAT
}
