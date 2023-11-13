package com.synchrony.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.synchrony.entity.Imgur;
import com.synchrony.entity.User;
import com.synchrony.entity.UserImage;
import com.synchrony.repository.UserImageRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImageService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private UserService userService;

	@Autowired
	private UserImageRepository userImageRepo;
	
	@Value("${imgur.uploadUrl:https://api.imgur.com/3/upload}")
	private String uploadURL;

	@Value("${imgur.deleteUrl:https://api.imgur.com/3/image/}")
	private String deleteURL;
	
	public UserImage upload(String userId,MultipartFile image) throws URISyntaxException, IOException {
		User user = userService.findByUserName(userId);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, "Client-ID 901b5073590a486");
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE);
		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
		parts.add("image", image.getResource());
		RequestEntity<?> requestEntity = RequestEntity.post(uploadURL).headers(headers)
				.body(parts);
		log.debug("Uploading  to imgur {}", image.getOriginalFilename());
		Imgur response = restTemplate.exchange(requestEntity, Imgur.class).getBody();
		UserImage userImages = userImageRepo.findByUserId(user.getId());
		if(userImages== null) {
			userImages = new UserImage();
			userImages.setUserId(user.getId());
		}
		if(userImages.getImages() == null) {
			userImages.setImages(new ArrayList());
		}
		userImages.getImages().add((String)response.getData().get("deleteHash"));
		return userImageRepo.save(userImages);
	}

	public boolean delete(String userId, String imageHash) throws URISyntaxException, IOException {
		User user = userService.findByUserName(userId);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, "Client-ID 901b5073590a486");
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE);
		RequestEntity<?> requestEntity = RequestEntity.delete(deleteURL+"/"+imageHash).headers(headers).build();
		log.debug("Deleting image from imgur with id {}", imageHash);
		UserImage userImages = userImageRepo.findByUserId(user.getId());
		userImages.getImages().add(userImages.getImages().remove(userImages.getImages().indexOf(imageHash)));
		userImageRepo.save(userImages);
		return restTemplate.exchange(requestEntity,Object.class).getStatusCode() == HttpStatusCode.valueOf(200);
	}
}
