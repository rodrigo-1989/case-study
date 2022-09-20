package com.example.demo.services;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.demo.dto.RespuestaDto;

@Service
@CrossOrigin
public class CloudinaryService implements ImagenService{
	private Logger log = LoggerFactory.getLogger(CloudinaryService.class);
	Cloudinary cloudinary;
//	@Value("${cloudinary.net.cloudname}")
//	private String cloudName;
//	@Value("${cloudinary.net.apikey}")
//	private String apiKey;
//	@Value("${cloudinary.net.apisecret}")
//	private String apiSecret;


	public CloudinaryService() {
		Map<String, String> valuesMap = new HashMap<>();
		valuesMap.put("cloud_name", "djxpuwlz0");
		valuesMap.put("api_key", "265979327338123");
		valuesMap.put("api_secret", "F2kO0PdCt6Vl6Tu9g-GWUgJYV5w");
		cloudinary = new Cloudinary(valuesMap);
	}
	@Transactional
	public RespuestaDto eliminarI(String id) {
		try {
			cloudinary.uploader().destroy(id, ObjectUtils.emptyMap());
			return new RespuestaDto(true, "Eminación de la imagen sin problema",null,null,null,null,null,null,null);
		} catch (Exception e) {
			log.error("Error al eliminar la imagen ", e.getLocalizedMessage());
			return new RespuestaDto(false, e.getMessage(),null,null,null,null,null,null,null);
		}
	}

}
