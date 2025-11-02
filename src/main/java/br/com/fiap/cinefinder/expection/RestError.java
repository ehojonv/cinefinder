package br.com.fiap.cinefinder.expection;

import java.time.LocalDateTime;

public record RestError(
    String message,
    int status,
    LocalDateTime timestamp,
    String path
) {}