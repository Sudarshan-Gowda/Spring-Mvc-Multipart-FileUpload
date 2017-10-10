package com.star.sud.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.star.sud.StarSystemProperties;
import com.star.sud.form.Document;

@Controller
public class StarBasicController {

	@Resource(name = "starSystemProperties")
	protected StarSystemProperties starSystemProperties;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getBasicScreen(Model model) {

		model.addAttribute("document", new Document());

		Map<String, String> mapList = Document.getList();
		if (mapList != null && !mapList.isEmpty()) {

			model.addAttribute("imageList", mapList);
		}

		return "file-upload";

	}

	@RequestMapping(value = "/file-upload", method = RequestMethod.POST)
	public String uploadDoc(Model model, @ModelAttribute("document") Document document) {

		InputStream inputStream = null;
		OutputStream outputStream = null;

		MultipartFile file = document.getDocs().get(0);

		int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
		String fileName = file.getOriginalFilename();
		String path = Calendar.getInstance().get(Calendar.YEAR) + "/" + month + "/"
				+ Calendar.getInstance().get(Calendar.DATE) + "/";

		String fullPath = starSystemProperties.getFileUpload() + path;

		File dir = new File(fullPath);
		dir.mkdirs();

		try {
			inputStream = file.getInputStream();
			File newFile = new File(fullPath + fileName);
			outputStream = new FileOutputStream(newFile);
			int readBytes = 0;
			byte[] buffer = new byte[8192];
			while ((readBytes = inputStream.read(buffer, 0, 8192)) != -1) {
				outputStream.write(buffer, 0, readBytes);
			}
			outputStream.close();
			inputStream.close();

			String temp = path.replace("/", "-");

			Document.list.put(temp + fileName, fullPath + fileName);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return "redirect:";

	}

	@RequestMapping(value = "/{id}/view", method = RequestMethod.GET)
	public String viewDocument(Model model, @PathVariable("id") String id, HttpServletResponse response) {

		Map<String, String> mapList = Document.getList();

		InputStream fileIn = null;
		String path = mapList.get(id);
		File file = new File(path);
		String mimeType = "application/octet-stream";
		response.setContentType(mimeType);
		response.setContentLength((int) file.length());
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", file.getName());
		response.setHeader(headerKey, headerValue);

		try {
			fileIn = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (fileIn != null) {
			ServletOutputStream out = null;
			try {
				out = response.getOutputStream();

				byte[] outputByte = new byte[8192];
				while (fileIn.read(outputByte, 0, 8192) != -1) {
					out.write(outputByte, 0, 8192);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					fileIn.close();
					if (out != null) {
						out.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "denied";

	}

}
