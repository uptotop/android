/**
 * 
 */
package com.project.uptotop.model;

import java.io.Serializable;

/**
 * @author alexey.kvitko
 * 
 */
public class UserModel implements Serializable {

	private static final long serialVersionUID = -4601033686923908013L;

	private Integer userId;
	private String userLogin;
	private String userPassword;
	private String avatar;
	private String location;
	private Integer showStartup;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public boolean isShowStartup() {
		return showStartup == 1;
	}

	public void setShowStartup(Integer showStartup) {
		this.showStartup = showStartup;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("userId: ["+userId+"]")
		   .append("userLogin: ["+userLogin+"]")
		  .append("userPassword: ["+userPassword+"]")
		  .append("avatar: ["+avatar+"]")
		  .append("location: ["+location+"]")
		  .append("showStartup: ["+showStartup+"]");
		return sb.toString();
	}

}
