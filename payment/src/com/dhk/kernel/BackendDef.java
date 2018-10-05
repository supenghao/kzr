package com.dhk.kernel;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.dhk.kernel.exception.BaseException;

public class BackendDef {

	private boolean enabled;
    private String name;
    private String text;
    private String className;
    private Class serverClass;
    private int port;
    private Properties properties;
    private List requires;

    public BackendDef(){
        enabled = true;
        port = -1;
        properties = new Properties();
        requires = new ArrayList(0);
    }
    public Class getServerClass(){
        if(serverClass == null)
            try{
                serverClass = Thread.currentThread().getContextClassLoader().loadClass(className);
            }catch(ClassNotFoundException enfe){
                throw new BaseException(enfe);
            }
        return serverClass;
    }
    public String getText(){
            return text;
    }
    public void setText(String text){
            this.text = text;
    }
        public String getClassName() {
                return className;
        }
        public void setClassName(String className) {
                this.className = className;
        }
        public boolean isEnabled() {
                return enabled;
        }
        public void setEnabled(boolean enabled) {
                this.enabled = enabled;
        }
        public String getName() {
                return name;
        }
        public void setName(String name) {
                this.name = name;
        }
        public int getPort() {
                return port;
        }
        public void setPort(int port) {
                this.port = port;
        }
        public Properties getProperties() {
                return properties;
        }
        public void setProperties(Properties properties) {
                this.properties = properties;
        }
        public List getRequires() {
                return requires;
        }
        public void setRequires(List requires) {
                this.requires = requires;
        }
        public void setServerClass(Class serverClass) {
                this.serverClass = serverClass;
        }
}

