package org.gm.docdrive.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

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
		private boolean starred;
		private boolean trashed;
		private File parent;
		private Map<String, String> properties;
		private long version;
		private String webContentLink;
		private Date viewedByMeTime; //"yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
		private Date createdTime;
		private Date modifiedTime;
		private Date modifiedByMeTime;
		private Date sharedWithMeTime;
		private User sharingUser;
		private List<User> owners;
		private User lastModifyingUser;
		private boolean shared;
		private boolean ownedByMe;
		private List<Capability> capabilities;
		private List<Permission> permissions;
		private String fullFileExtension;
		private String fileExtension;
		private String md5Checksum;
		private Long size;
		
		
		
		public String getId() {
			return id;
		}

		protected void setId(String id){
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
		public boolean isStarred() {
			return starred;
		}
		public void setStarred(boolean starred) {
			this.starred = starred;
		}
		public boolean isTrashed() {
			return trashed;
		}
		public void setTrashed(boolean trashed) {
			this.trashed = trashed;
		}

		public Map<String, String> getProperties() {
			return properties;
		}
		public void setProperties(Map<String, String> properties) {
			this.properties = properties;
		}
		public long getVersion() {
			return version;
		}
		public void setVersion(long version) {
			this.version = version;
		}
		public String getWebContentLink() {
			return webContentLink;
		}
		public void setWebContentLink(String webContentLink) {
			this.webContentLink = webContentLink;
		}
		public Date getViewedByMeTime() {
			return viewedByMeTime;
		}
		public void setViewedByMeTime(Date viewedByMeTime) {
			this.viewedByMeTime = viewedByMeTime;
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
		public Date getModifiedByMeTime() {
			return modifiedByMeTime;
		}
		public void setModifiedByMeTime(Date modifiedByMeTime) {
			this.modifiedByMeTime = modifiedByMeTime;
		}
		public Date getSharedWithMeTime() {
			return sharedWithMeTime;
		}
		public void setSharedWithMeTime(Date sharedWithMeTime) {
			this.sharedWithMeTime = sharedWithMeTime;
		}
		public User getSharingUser() {
			return sharingUser;
		}
		public void setSharingUser(User sharingUser) {
			this.sharingUser = sharingUser;
		}
		public List<User> getOwners() {
			return owners;
		}
		public void setOwners(List<User> owners) {
			this.owners = owners;
		}
		public User getLastModifyingUser() {
			return lastModifyingUser;
		}
		public void setLastModifyingUser(User lastModifyingUser) {
			this.lastModifyingUser = lastModifyingUser;
		}
		public boolean isShared() {
			return shared;
		}
		public void setShared(boolean shared) {
			this.shared = shared;
		}
		public boolean isOwnedByMe() {
			return ownedByMe;
		}
		public void setOwnedByMe(boolean ownedByMe) {
			this.ownedByMe = ownedByMe;
		}
		public List<Capability> getCapabilities() {
			return capabilities;
		}
		public void setCapabilities(List<Capability> capabilities) {
			this.capabilities = capabilities;
		}
		public List<Permission> getPermissions() {
			return permissions;
		}
		public void setPermissions(List<Permission> permissions) {
			this.permissions = permissions;
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

		public File getParent() {
			return parent;
		}

		public void setParent(File parent) {
			this.parent = parent;
		}


}
