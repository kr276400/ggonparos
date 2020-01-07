package org.parosproxy.paros.core.scanner;

class Util {
    static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {}
    }
}
