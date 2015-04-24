package com.supermy.rule.pojo;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;

public class Vip implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name="";
	private String password="";
	
	public Vip() {
		super();
	}

	public Vip(String name, String password) {
		super();
		this.name = name;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	     public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            this.name    = (String)in.readObject();
            this.password  = (String)in.readObject();
        }
    
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeObject(this.name);
            out.writeObject(this.password);
    
        }
	

}
