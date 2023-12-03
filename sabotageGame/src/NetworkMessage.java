import Values.MessageValue;

import java.io.Serializable;

/**
 * Message over a network indicating something to either the server or client
 *
 * @param identifier what type of message this is
 * @param dataA      additional information, such as chat text
 * @param dataB      in the case where two pieces of information are necessary, this is used
 * @param dataC      in the case where three pieces of information are necessary, this is used
 * @see MessageValue
 */
public record NetworkMessage(MessageValue identifier, String dataA, String dataB, String dataC) implements Serializable {
}
