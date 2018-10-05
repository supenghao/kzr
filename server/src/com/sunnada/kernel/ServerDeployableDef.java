package com.sunnada.kernel;

import java.util.ArrayList;
import java.util.List;

public class ServerDeployableDef {
	List<ServerDef> servers;
    public ServerDeployableDef(){
       servers = new ArrayList<ServerDef>(0);
	}
	
		
	
	public List<ServerDef> getServers(){
	    return servers;
	}
	
	public void setServers(List<ServerDef> list){
	    servers = list;
	}
}
