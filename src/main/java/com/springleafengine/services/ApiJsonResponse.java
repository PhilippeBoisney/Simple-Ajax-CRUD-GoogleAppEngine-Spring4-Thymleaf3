package com.springleafengine.services;

import java.util.List;
import com.springleafengine.interfaces.IApiObject;

public class ApiJsonResponse {
	
	private int status;
	private String styleClass;
	private String message;
	private IApiObject obj;
	private List<IApiObject> listObjs;
	private String oldId;
	
	public ApiJsonResponse() {
		
	}

	public ApiJsonResponse(int status, String styleClass, String message, IApiObject obj) {
		super();
		this.status = status;
		this.styleClass = styleClass;
		this.message = message;
		this.obj = obj;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public IApiObject getObj() {
		return obj;
	}

	public void setObj(IApiObject obj) {
		this.obj = obj;
	}

	public List<IApiObject> getListObjs() {
		return listObjs;
	}

	public void setListObjs(List<IApiObject> listObjs) {
		this.listObjs = listObjs;
	}

	public String getOldId() {
		return oldId;
	}

	public void setOldId(String oldId) {
		this.oldId = oldId;
	}
	
}