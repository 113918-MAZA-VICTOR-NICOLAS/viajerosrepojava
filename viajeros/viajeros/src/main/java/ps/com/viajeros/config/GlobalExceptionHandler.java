package ps.com.viajeros.config;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ps.com.viajeros.dtos.common.ErrorApi;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        logger.error("Error ocurrido: ", e);
        return new ResponseEntity<>("Error al procesar la solicitud: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorApi> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ErrorApi errorApi = new ErrorApi(
                LocalDateTime.now().toString(), // Asegúrate de que el timestamp tenga el formato correcto
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getBindingResult().getFieldError().getDefaultMessage() // Mensaje de error específico
        );
        return new ResponseEntity<>(errorApi, HttpStatus.BAD_REQUEST);
    }

    // Aquí puedes agregar manejadores para otros tipos de excepciones si es necesario

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorApi> handleUserNotValidException(DataIntegrityViolationException ex) {
        ErrorApi errorApi = new ErrorApi(
                LocalDateTime.now().toString(), // Asegúrate de que el timestamp tenga el formato correcto
                HttpStatus.BAD_REQUEST.value(),
                "User Not Valid",
                ex.getMessage() // Mensaje de error específico para UserNotValidException
        );
        return new ResponseEntity<>(errorApi, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(DuplicateModelException.class)
    public ResponseEntity<ErrorApi> handleDuplicateModelException(DuplicateModelException ex) {
        ErrorApi errorApi = new ErrorApi(
                LocalDateTime.now().toString(), // Asegúrate de que el timestamp tenga el formato correcto
                HttpStatus.CONFLICT.value(),
                "Duplicate Model",
                ex.getMessage() // Mensaje de error específico para DuplicateModelException
        );
        return new ResponseEntity<>(errorApi, HttpStatus.CONFLICT);
    }
    public static class DuplicateModelException extends RuntimeException {
        public DuplicateModelException(String message) {
            super(message);
        }
    }


}

