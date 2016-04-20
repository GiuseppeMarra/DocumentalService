package org.gm.docdrive.dao.interfaces;

public class Response<T> {
	
	public T value;
	public Message message;
	
	
	public T getValue() {
		return value;
	}
	public void setValue(T value) {
		this.value = value;
	}
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}	

}
