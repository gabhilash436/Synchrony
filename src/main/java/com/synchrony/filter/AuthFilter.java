package com.synchrony.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.synchrony.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthFilter extends OncePerRequestFilter {

	private UserService userService;
	
	public AuthFilter(UserService userService) {
		this.userService = userService;
	}

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		if (request.getServletPath().startsWith(("/images"))) {
			String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION).substring("BASIC".length() + 1);
			String userCredentials = new String(Base64.decodeBase64(authHeader), StandardCharsets.UTF_8);
			String userName = userCredentials.split(":")[0];
			request.setAttribute("userId", userName);
			if (userService.findByUserName(userName) == null) {
				throw new RuntimeException("user not found");
			}
		}
		chain.doFilter(request, response);

	}

}
