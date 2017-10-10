package com.star.sud;

import org.springframework.stereotype.Service;

@Service("starSystemProperties")
public class StarSystemProperties {

	protected String commonPath;

	protected String fileUpload;

	public String getCommonPath() {
		return commonPath;
	}

	public void setCommonPath(String commonPath) {
		this.commonPath = commonPath;
	}

	public String getFileUpload() {
		return commonPath + fileUpload;
	}

	public void setFileUpload(String fileUpload) {
		this.fileUpload = fileUpload;
	}

}
