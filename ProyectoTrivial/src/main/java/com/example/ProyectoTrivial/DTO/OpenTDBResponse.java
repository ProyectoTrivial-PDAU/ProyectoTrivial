package com.example.ProyectoTrivial.DTO;

import java.util.List;

/**
 * DTO que representa la respuesta de la API Open Trivia Database (OpenTDB).
 * response_code: 0 = éxito, 1 = sin resultados, 2 = parámetro inválido, 3 = token no encontrado, 4 = token vacío.
 */
public class OpenTDBResponse {
    private int response_code;
    private List<OpenTDBQuestion> results;

    public int getResponse_code() {
        return response_code;
    }

    public void setResponse_code(int response_code) {
        this.response_code = response_code;
    }

    public List<OpenTDBQuestion> getResults() {
        return results;
    }

    public void setResults(List<OpenTDBQuestion> results) {
        this.results = results;
    }
}
