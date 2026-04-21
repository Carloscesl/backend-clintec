package com.terreneitors.backendclintec.shared.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private String       codigo;
    private String       mensaje;
    private String       ruta;
    private List<String> detalles;   // solo se llena en errores de campos @Valid

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    // Constructor privado — se construye solo con el Builder
    private ErrorResponse() {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final ErrorResponse r = new ErrorResponse();

        public Builder codigo(String codigo) {
            r.codigo = codigo;
            return this;
        }

        public Builder mensaje(String mensaje) {
            r.mensaje = mensaje;
            return this;
        }

        public Builder ruta(String ruta) {
            r.ruta = ruta;
            return this;
        }

        public Builder detalles(List<String> detalles) {
            r.detalles = detalles;
            return this;
        }

        public Builder timestamp() {
            r.timestamp = LocalDateTime.now();
            return this;
        }

        public ErrorResponse build() {
            return r;
        }
    }

    // Getters
    public String        getCodigo()    { return codigo;    }
    public String        getMensaje()   { return mensaje;   }
    public String        getRuta()      { return ruta;      }
    public List<String>  getDetalles()  { return detalles;  }
    public LocalDateTime getTimestamp() { return timestamp; }
}
