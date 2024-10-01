package ps.com.viajeros.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


@RestController
@RequestMapping("/api/files")
@Tag(name = "File Upload", description = "Operaciones para subir archivos")
public class FileUploadController {

    private static final String UPLOAD_DIRECTORY = "uploads/";

    @Operation(summary = "Subir un archivo",
            description = "Permite subir un archivo al servidor",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Archivo subido correctamente"),
                    @ApiResponse(responseCode = "400", description = "No se ha seleccionado ningún archivo"),
                    @ApiResponse(responseCode = "500", description = "Error al subir el archivo", content = @Content(schema = @Schema(implementation = String.class)))
            })
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("No se ha seleccionado ningún archivo", HttpStatus.BAD_REQUEST);
        }

        try {
            String fileName = file.getOriginalFilename();
            File dest = new File(UPLOAD_DIRECTORY + fileName);
            file.transferTo(dest);
            return new ResponseEntity<>("Archivo subido correctamente: " + fileName, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Error al subir el archivo", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}