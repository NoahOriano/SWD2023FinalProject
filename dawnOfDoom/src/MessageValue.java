import java.io.Serializable;

/**
 * Values for the identifiers used in a NetworkMessage and ActionRequest
 */
public enum MessageValue implements Serializable {
    TERMINATE, VOTE, STEAL, FORGE, INVESTIGATE, PASS, MESSAGE, ROUNDOVER, GAMEOVER, CHAT, SIGNIN, EVIDENCE, TIMER, CULTIST, INNOCENT, NEWPLAYER, JOIN
    // Note that message sender is verified by server, field containing sender username is not necessary

    // Terminate message data fields: null, null, null
    // Vote message data fields: player being voted out, null, null
    // Steal message data fields: player to steal from, null, null
    // Forge message data fields: player to forge on, identifier (whether good or bad, or other roles), null
    // Investigate message data fields: player to investigate, null, null
    // Pass message data fields: player's file to retrieve, player to pass to, null

    // Message data fields: text to display on server, null, null
    // Round-over data fields: player name voted out, rounds left, null
    // Game-over data fields: game end identifier such as win or loss, null, null
    // Chat data fields: text to display, recipient group or name of user, null
    // Chat data fields FROM SERVER: text to display, sender of chat, null
    // SignIN data fields: username, null, null *** Only time username is sent, will be saved in subserver

    // Evidence data fields: suspect, identifier, null  *** Inspector is hidden from client

    // Timer data fields: null, null, null, null *** Used only by server side
}
