package org.parosproxy.paros.core.scanner;

abstract public class AbstractHostPlugin extends AbstractPlugin {

    public void notifyPluginCompleted(HostProcess parent) {
        parent.pluginCompleted(this);
    }

}
