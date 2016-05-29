package org.gm.docdrive.ws.model;

import java.util.List;

public class FolderVW {
	
	
	@Override
	public String toString() {
		return "FolderVW [folderId=" + folderId + ", files=" + files + "]";
	}
	private String folderId;
	private List<FileVW> files;
	
	public String getFolderId() {
		return folderId;
	}
	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}
	public List<FileVW> getFiles() {
		return files;
	}
	public void setFiles(List<FileVW> files) {
		this.files = files;
	}

}
