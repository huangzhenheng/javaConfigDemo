package com.hzh.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.hzh.dao.DocumentMapper;
import com.hzh.pojo.Document; 

@Named
public class DocumentServiceImpl implements DocumentService {
	@Inject
	private DocumentMapper documentMapper;

	@Override
	public List<Document> findByFid(Integer fid) {
		return documentMapper.findByFileFid(fid);
	}

	@Override
	public void saveDocument(Document document) {
		documentMapper.saveDocument(document);
	}

	@Override
	public Integer findFid(Integer fid) {
		Document document = documentMapper.findByid(fid);
		if (document != null) {
			return document.getFid();
		} else {
			return 0;
		}
	}

	@Override
	public void saveDir(String name, Integer fid) {
		Document document = new Document();
        document.setName(name);
        document.setFid(fid);
 
        document.setType(Document.TYPE_DIR);
        documentMapper.saveDocument(document);
		
	}

	@Override
	public Document findByid(Integer id) {
		return documentMapper.findByid(id);
	}

	@Override
	public void delFileById(Integer id) {
		documentMapper.delFileById(id);
		
	}

}
