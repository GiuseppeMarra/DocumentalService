package org.gm.docdrive.ws.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.gm.docdrive.model.File.Kind;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileVW {
	

	
		@Override
	public String toString() {
		return "FileVW [kind=" + kind + ", id=" + id + ", name=" + name + ", mimeType=" + mimeType + ", description="
				+ description + ", parent=" + parent + ", properties=" + properties + ", createdTime=" + createdTime
				+ ", modifiedTime=" + modifiedTime + ", fullFileExtension=" + fullFileExtension + ", fileExtension="
				+ fileExtension + ", md5Checksum=" + md5Checksum + ", size=" + size + ", owner=" + owner
				+ ", sharedWith=" + sharedWith + "]";
	}

		private Kind kind; 
		private String id;
		private String name;
		private String mimeType;
		private String description;
		private String parent;
		private Map<String, String> properties;
		
		@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
		private Date createdTime;
		
		@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
		private Date modifiedTime;
		
		private String fullFileExtension;
		private String fileExtension;
		private String md5Checksum;
		private Long size;
		
		private UserVW owner;
		private List<UserVW> sharedWith;

		public UserVW getOwner() {
			return owner;
		}

		public void setOwner(UserVW owner) {
			this.owner = owner;
		}

		public List<UserVW> getSharedWith() {
			return sharedWith;
		}

		public void setSharedWith(List<UserVW> sharedWith) {
			this.sharedWith = sharedWith;
		}

		public FileVW(){
			super();
		}
		
		public FileVW(String id){
			super();
			this.id=id;
			
		}
		
		
		public String getId() {
			return id;
		}

		public void setId(String id){
			this.id=id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getMimeType() {
			return mimeType;
		}
		public void setMimeType(String mimeType) {
			this.mimeType = mimeType;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public Map<String, String> getProperties() {
			return properties;
		}
		public void setProperties(Map<String, String> properties) {
			this.properties = properties;
		}
		public Date getCreatedTime() {
			return createdTime;
		}
		public void setCreatedTime(Date createdTime) {
			this.createdTime = createdTime;
		}
		public Date getModifiedTime() {
			return modifiedTime;
		}
		public void setModifiedTime(Date modifiedTime) {
			this.modifiedTime = modifiedTime;
		}
		
		public String getFullFileExtension() {
			return fullFileExtension;
		}
		public void setFullFileExtension(String fullFileExtension) {
			this.fullFileExtension = fullFileExtension;
		}
		public String getFileExtension() {
			return fileExtension;
		}
		public void setFileExtension(String fileExtension) {
			this.fileExtension = fileExtension;
		}
		public String getMd5Checksum() {
			return md5Checksum;
		}
		public void setMd5Checksum(String md5Checksum) {
			this.md5Checksum = md5Checksum;
		}
		public Long getSize() {
			return size;
		}
		public void setSize(Long size) {
			this.size = size;
		}
		public Kind getKind() {
			return kind;
		}
		public void setKind(Kind kind) {
			this.kind = kind;
		}

		public String getParent() {
			return parent;
		}

		public void setParent(String parent) {
			this.parent = parent;
		}


}
