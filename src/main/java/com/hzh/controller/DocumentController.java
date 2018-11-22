package com.hzh.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.csource.common.MyException;
import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.StorageClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.hzh.dao.DocumentMapper;
import com.hzh.exception.NotFoundException;
import com.hzh.pojo.Document;
import com.hzh.service.DocumentService;
import com.hzh.util.FileUtil;

@Controller
@RequestMapping("/document")
public class DocumentController extends BaseController{

	@Autowired
	private StorageClient storageClient;
	@Inject
	private DocumentService documentService;

	@RequestMapping(method = RequestMethod.GET)
	private String list(Model model, @RequestParam(required = false, defaultValue = "0") Integer fid) {
		model.addAttribute("documents", documentService.findByFid(fid));
		model.addAttribute("fid", fid);
		fid = documentService.findFid(fid);
		model.addAttribute("url", "/document?fid=" + fid);
		return "document/list";
	}

	@RequestMapping(value = "/dir/new", method = RequestMethod.POST)
	public String saveDir(String name, Integer fid) {
		documentService.saveDir(name, fid);
		return "redirect:/document?fid=" + fid;
	}

	/**
	 * 下载文件简单实例
	 * 
	 * @param id 文档的
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{id:\\d+}/download", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadFile(@PathVariable Integer id, HttpServletResponse response)
			throws Exception {

		//查找文件
		Document document = documentService.findByid(id);
		if (document == null) {
			throw new NotFoundException();
		}

		byte[] bytes = storageClient.download_file(document.getGroupName(), document.getFilename());
		String fileName = new String(document.getName().getBytes("UTF-8"), "ISO8859-1");

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(document.getContexttype()))
				.contentLength(bytes.length)
				.header("Content-Disposition", "attachment;filename=\"" + fileName + "\"")
				.body(bytes);

	}


	@RequestMapping(value = "/{id:\\d+}/viewPdf", method = RequestMethod.GET)
	@ResponseBody
	public void viewPdfContractSign(HttpServletResponse response,@PathVariable Integer id) throws IOException, MyException {
		response.reset();
		response.setContentType("application/pdf");
		Document document = documentService.findByid(id);
		if (document == null) {
			throw new NotFoundException();
		}

		byte[] bytes = storageClient.download_file(document.getGroupName(), document.getFilename());

		String fileName = new String(document.getName().getBytes("UTF-8"), "ISO8859-1");


		try (OutputStream out = response.getOutputStream()) {



				response.setHeader("Content-Disposition",
						"inline; filename=\"" + fileName + "\"");
				out.write(bytes);
				out.flush();

		} catch (Throwable e) {

		}

	}


	@RequestMapping(value = "/del/{fid:\\d+}/{id:\\d+}", method = RequestMethod.GET)
	public String delFile(@PathVariable Integer id, @PathVariable Integer fid) throws Exception {

		 Document document = documentService.findByid(id);
		 if (document != null) {
			 documentService.delFileById(id);
			 storageClient.delete_file(document.getGroupName(), document.getFilename());
		 }
		return "redirect:/document?fid=" + fid;
	}

	/**
	 * 保存上传的文件
	 * 
	 * @param file
	 * @param fid
	 * @return
	 * @throws IOException
	 * @throws MyException
	 */
	@RequestMapping(value = "/savefile", method = RequestMethod.POST)
	@ResponseBody
	public String saveFile(MultipartFile file, Integer fid) throws IOException, MyException {

		if (file.isEmpty()) {
			throw new NotFoundException();
		} else {
			String originalFilename=file.getOriginalFilename();
			InputStream inputStream = file.getInputStream();
			byte[] bytes = IOUtils.toByteArray(file.getInputStream());
			
			String[] strings = storageClient.upload_file(bytes,  originalFilename.substring(originalFilename.lastIndexOf(".") + 1), null);
			for (String string : strings) {
				System.out.println(string);
			}
			inputStream.close();

			Document document = new Document();
			document.setName(originalFilename);
			document.setType(Document.TYPE_DOC);
			document.setContexttype(file.getContentType());
			document.setSize(FileUtils.byteCountToDisplaySize(file.getSize()));
			document.setGroupName(strings[0]);
			document.setFilename(strings[1]);
			document.setFid(fid);
			documentService.saveDocument(document);

		}
		return SUCCESS;
	}

}
