package com.mk.exsim.gateway.fix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import quickfix.*;

/**
 * Fix adaptor routing application
 */
public class FixAdaptor extends MessageCracker implements Application {
    private final Logger log = LoggerFactory.getLogger(FixAdaptor.class);

    /**
     *
     */
    public FixAdaptor() {
    }

    @Override
    public void onCreate(SessionID sessionId) {
        log.info("onCreate");
    }

    @Override
    public void onLogon(SessionID sessionId) {
        log.info("onLogin");
    }

    @Override
    public void onLogout(SessionID sessionId) {
        log.info("onLogout");
    }

    @Override
    public void toAdmin(Message message, SessionID sessionId) {
        log.info("toAdmin");
    }

    @Override
    public void fromAdmin(Message message, SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
        log.info("fromAdmin");
    }

    @Override
    public void toApp(Message message, SessionID sessionId) throws DoNotSend {
        log.info("toApp");
    }

    @Override
    public void fromApp(Message message, SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
        log.info("fromApp");
    }
}
