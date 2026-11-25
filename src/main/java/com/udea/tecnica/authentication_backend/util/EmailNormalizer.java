package com.udea.tecnica.authentication_backend.util;

import java.util.Locale;

/**
 * Única regla de normalización de correos: evita que "Ana@Correo.com " y
 * "ana@correo.com" se traten como cuentas distintas.
 */
public final class EmailNormalizer {
    private EmailNormalizer() {
    }

    public static String normalize(String email) {
        return email == null ? null : email.strip().toLowerCase(Locale.ROOT);
    }
}
