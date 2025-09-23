package com.project.excepcions;

public class IOFitxerExcepcio extends Exception {
    public IOFitxerExcepcio(String message) {
        super(message);
    }

    public IOFitxerExcepcio(String message, Throwable cause) {
        super(message, cause);
    }
}
