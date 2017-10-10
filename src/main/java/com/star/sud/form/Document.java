package com.star.sud.form;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public class Document {

	private List<MultipartFile> docs;

	public static final Map<String, String> list = new HashMap<String, String>();

	public List<MultipartFile> getDocs() {
		return docs;
	}

	public void setDocs(List<MultipartFile> docs) {
		this.docs = docs;
	}

	public static Map<String, String> getList() {
		return list;
	}

}
