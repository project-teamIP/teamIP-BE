package com.teamip.heyhello.domain.socketio.socket;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SocketProperty {

    public static final String MESSAGE_KEY = "message";
    public static final String MATCH_KEY = "match";
    public static final String CANCEL_KEY = "cancel";
    public static final String OFFER_KEY = "offer";
    public static final String ANSWER_KEY = "answer";
    public static final String ICE_KEY = "ice";
    public static final String ERROR_KEY = "error";
    public static final String ENDCALL_KEY = "end";
}