package com.hrothwell.shiritori.controllers;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hrothwell.shiritori.exceptions.ShiritoriJoinException;

@ControllerAdvice
@Controller
public class ShiritoriErrorController implements ErrorController{
	private Logger log = LoggerFactory.getLogger(getClass());
	
	/**
	 * If they are being redirected to the /error endpoint (bad url, exception that wasn't already handled was thrown)
	 * @param r
	 * @param model
	 * @return
	 */
	@RequestMapping("/error")
	public String handleError(HttpServletRequest r, Model model) {
		log.error("Error is being handled by handleError");
		Object status = r.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		String statusCode = status != null ? status.toString() : "Unknown";
		model.addAttribute("httpStatusCode", statusCode);
		
		return "error.html";
	}
	
	/**
	 * Error that is thrown when a user tries to join a room but the game doesn't exist or user's password is incorrect.
	 * We want the user to remain on the page when this happens, the messgae returned is displayed to them in an alert window
	 * @param r - request
	 * @param e - the exception we threw
	 * @param model - model to attach to view 
	 * @return - generic "json" text result that is populated using our model. 
	 */
	@ExceptionHandler(ShiritoriJoinException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleException(HttpServletRequest r, ShiritoriJoinException e, Model model) {
		log.error("Error is being handled by handleException");
		model.addAttribute("json", e.getMessage());
		return "jsonResult.txt";
	}
}
