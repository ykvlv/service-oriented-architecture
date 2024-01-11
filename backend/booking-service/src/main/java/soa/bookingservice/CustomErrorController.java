package soa.bookingservice;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import soa.bookingservice.model.common.ErrorResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Grigory Yakovlev (Grigory.Yakovlev@lanit-tercom.ru) created on 11.01.2024.
 */
@Controller
public class CustomErrorController implements ErrorController {

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
				.body(ErrorResponse.of(500,  "500 ошибочка, здравствуйте"));
	}

}