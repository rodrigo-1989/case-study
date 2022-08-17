package com.example.demo.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
@SuppressWarnings({"rawtypes"})
@CrossOrigin
public class CloudinaryService {

	Cloudinary cloudinary;
//	@Value("${cloudinary.net.cloudname}")
//	private String cloudName;
//	@Value("${cloudinary.net.apikey}")
//	private String apiKey;
//	@Value("${cloudinary.net.apisecret}")
//	private String apiSecret;

	private Map<String, String> valuesMap = new HashMap<>();

	public CloudinaryService() {
		valuesMap.put("cloud_name", "djxpuwlz0");
		valuesMap.put("api_key", "265979327338123");
		valuesMap.put("api_secret", "F2kO0PdCt6Vl6Tu9g-GWUgJYV5w");
		
		cloudinary = new Cloudinary(valuesMap);
	}
	
	public Map upload(MultipartFile multipartFile) throws IOException {
		File file =convert(multipartFile);
		Map result = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
		file.delete();
		return result;
	}

	public Map delete(String id) throws IOException {
		Map result = cloudinary.uploader().destroy(id, ObjectUtils.emptyMap());
		return result;
	}

	private File convert(MultipartFile multipartFile) throws IOException {
		File file = new File(multipartFile.getOriginalFilename());
		FileOutputStream fo;

		fo = new FileOutputStream(file);
		fo.write(multipartFile.getBytes());
		fo.close();
		return file;

	}

}
