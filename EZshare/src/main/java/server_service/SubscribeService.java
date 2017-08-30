package server_service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import assist.MyException;
import assist.ResourceStorage;
import assist.Response;
import assist.ServerErrorResponse;
import assist.ServerRecords;
import assist.ServerSuccessResponse;
import dao.QueryResource;
import dao.Resource;
import dao.ServerInfo;
import dao.SubscribeResource;

public class SubscribeService extends Service {
	public SubscribeService(ResourceStorage resourceStroage, ServerRecords serverRecords) {
		super(resourceStroage, serverRecords);
		// TODO Auto-generated constructor stub
	}
	
	public void response(Resource resource, DataOutputStream out, String HostnamePort, boolean relay
			, String id){
		Response response = null;
		Gson gson = new GsonBuilder().serializeNulls().create();
		ArrayList<Resource> matchResources = new ArrayList<Resource>();
		int result = 0;
		
		try {
			checkResource(resource);
			if(relay){
				for(ServerInfo server : serverRecords.getServers()){
					try {
						Socket s = new Socket(server.getHostname(),server.getPort());
						SubscribeResource sendResource = new SubscribeResource(false, resource, id);
						DataInputStream otherIn = new DataInputStream(s.getInputStream());
						DataOutputStream otherOut = new DataOutputStream(s.getOutputStream());
						otherOut.writeUTF(sendResource.toJson(gson));
					
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
	
		} catch (MyException e) {
			// TODO Auto-generated catch block
			response = new ServerErrorResponse(e.getMessage());
			try {
				out.writeUTF(response.toJson(gson));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		
		
		
	
	}
	
	public void checkResource(Resource resource) throws MyException {
		if(resource.getUri().contains("\0")){
			throw new MyException("invalid resource");
		}
		if(resource.getChannel().contains("\0")){
			throw new MyException("invalid resource");
		}
		if(resource.getDescription().contains("\0")){
			throw new MyException("invalid resource");
		}
		if(resource.getName().contains("\0")){
			throw new MyException("invalid resource");
		}
		if(resource.getOwner().contains("\0") || resource.getOwner().equals("*")){
			throw new MyException("invalid resource");
		}
		if(resource.getTags().toString().contains("\0")){
			throw new MyException("invalid resource");
		}
		
	}
	
	
}
