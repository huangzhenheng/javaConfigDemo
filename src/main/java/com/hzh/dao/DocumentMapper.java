package com.hzh.dao;

import java.util.List;

import com.hzh.pojo.Document;
 

public interface DocumentMapper {

     

    List<Document> findAllFile();

	void saveDocument(Document document);

	List<Document> findByFileFid(Integer fid);

	Document findByid(Integer id);

	void delFileById(Integer id);
  
}
