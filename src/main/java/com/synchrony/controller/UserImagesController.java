package com.synchrony.controller;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.synchrony.entity.Imgur;
import com.synchrony.entity.UserImage;
import com.synchrony.service.ImageService;

import jakarta.servlet.http.HttpServletRequest;

@RestController()
@RequestMapping("/images")
public class UserImagesController {
	
	@Autowired
	private ImageService imageService;
	
	
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<UserImage> upload(@RequestPart("image") MultipartFile image, HttpServletRequest request) throws URISyntaxException, IOException{
		return ResponseEntity.ok(imageService.upload(request.getAttribute("userId").toString(),image));
	}
	
	@DeleteMapping("/{deleteHash}")
	public boolean delete(@PathVariable("deleteHash") String imageHash, HttpServletRequest request) throws URISyntaxException, IOException{
		return imageService.delete(request.getAttribute("userId").toString(), imageHash);
	}
	
}
