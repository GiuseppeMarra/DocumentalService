package org.gm.docdrive.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class File {
	
	public enum Kind{
		FILE("docdrive/file"), FOLDER("docdrive/folder");
		
		private String name;
		
		private Kind(String name){
			this.setName(name);
		}
		
		@JsonValue
		public String getName(){
			return this.name;
		}

		public void setName(String name) {
			this.name = name;
		}
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
		
		private UserRight owner;
		private List<UserRight> sharedWith;
		


		public File(){
			super();
		}
		
		public File(String id){
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

		public List<UserRight> getSharedWith() {
			return sharedWith;
		}

		public void setSharedWith(List<UserRight> sharedWith) {
			this.sharedWith = sharedWith;
		}

		public UserRight getOwner() {
			return owner;
		}

		public void setOwner(UserRight owner) {
			this.owner = owner;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			File other = (File) obj;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			return true;
		}


		
		

}
