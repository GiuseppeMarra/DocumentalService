package org.gm.docdrive.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRight {
	
	public static enum Right{
		READ("READ"), WRITE("WRITE");
		
		private Right(String value){
			this.value = value;
		}
		
		private String value;
		
		public String getValue(){
			return this.value;
		}
	}

	private String idUser;
	private Right right;
	
	
	public String getIdUser() {
		return idUser;
	}
	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}
	public Right getRight() {
		return right;
	}
	public void setRight(Right right) {
		this.right = right;
	}
}
