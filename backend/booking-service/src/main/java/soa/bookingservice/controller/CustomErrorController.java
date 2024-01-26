package soa.bookingservice.controller;

import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import soa.bookingservice.model.ErrorResponse;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

	private final ErrorAttributes errorAttributes;

	public CustomErrorController(ErrorAttributes errorAttributes) {
		this.errorAttributes = errorAttributes;
	}

	@RequestMapping("/error")
	@ResponseBody
	public ResponseEntity<ErrorResponse> handleError(HttpServletRequest request) {
		// Если ошибка 404, то возвращаем 404
		if (request.getAttribute("javax.servlet.error.status_code").equals(404)) {
			return ResponseEntity.status(405)
					.body(ErrorResponse.of(405,  "Такого метода нет"));
		}
		// Возвращаем 500
		return ResponseEntity.status(500)
				.body(ErrorResponse.of(500, "что-то произошло иди логи смотри"));
	}

}