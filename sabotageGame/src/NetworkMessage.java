/**
 * Message over a network indicating something to either the server or client
 * @param identifier what type of message this is
 * @param information additional information, such as chat text
 * @param additional in the case where two pieces of information are necessary, this is used
 * @see MessageValues
 */
public record NetworkMessage(MessageValues identifier, String information, String additional){
}
