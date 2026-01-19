/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

public class ServerProtocol {
    // Commands
    public static final String CMD_AUTH = "AUTH";
    public static final String CMD_VOTE = "VOTE";

    // Responses
    public static final String MSG_WELCOME = "WELCOME: iVote Server Ready";
    public static final String MSG_OK = "OK: Authenticated";
    public static final String MSG_ERROR = "ERROR";
    public static final String MSG_SUCCESS = "SUCCESS: Vote Counted";
}