package com.share.case_jr.api.exception;

import lombok.extern.java.Log;

@Log
public enum Level {

    ERROR() {
        @Override
        public void gravarLog(String mensagem) {
            log.severe(mensagem);
        }

        @Override
        public String addLevel(String mensagem) {
            return "ERROR | " + mensagem;
        }
    },
    WARNING {
        @Override
        public void gravarLog(String mensagem) {
            log.warning(mensagem);
        }

        @Override
        public String addLevel(String mensagem) {
            return "WARNING | " + mensagem;
        }
    };

    public abstract void gravarLog(String mensagem);
    public abstract String addLevel(String mensagem);
}
