package org.parosproxy.paros.common;

import org.apache.commons.configuration.FileConfiguration;
import org.apache.commons.configuration.XMLConfiguration;

abstract public class AbstractParam {

    private FileConfiguration config = null;

    public void load(FileConfiguration config) {
        this.config = config;
        parse();
    }
    
    public void load(String fileName) {
        try {
            config = new XMLConfiguration(fileName);
            load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void load() throws Exception {
        config.load();
        parse();
    }
    
    public FileConfiguration getConfig() {
        return config;
    } 

    /*
     * config 파일은 분석하기 위한 하위 클래스를 실행하는거
     */
    abstract protected void parse();
}
