package com.hzh.service;

import java.util.List;

import com.hzh.pojo.Document;

public interface DocumentService {

	List<Document> findByFid(Integer fid);

	void saveDocument(Document document);

	Integer findFid(Integer fid);

	void saveDir(String name, Integer fid);

	Document findByid(Integer id);

	void delFileById(Integer id);

}
