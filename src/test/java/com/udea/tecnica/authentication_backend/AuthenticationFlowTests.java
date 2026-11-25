package com.udea.tecnica.authentication_backend;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/** Verifica los criterios de aceptación del proyecto contra la API real. */
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AuthenticationFlowTests {

    @Autowired
    private MockMvc mvc;

    @Test
    void registraIniciaSesionYConsultaUsuarios() throws Exception {
        register("Ana Pérez", "ana@correo.com", "Clave123", "Clave123").andExpect(status().isCreated());

        MockHttpSession session = login("ANA@correo.com ", "Clave123");

        mvc.perform(get("/api/users/1").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Ana Pérez"))
                .andExpect(jsonPath("$.passwordHash").doesNotExist());
        mvc.perform(get("/api/users").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
        mvc.perform(get("/api/users/99").session(session)).andExpect(status().isNotFound());
    }

    @Test
    void noPermiteCorreoDuplicado() throws Exception {
        register("Ana Pérez", "ana@correo.com", "Clave123", "Clave123").andExpect(status().isCreated());
        register("Otra Ana", "Ana@Correo.com", "Clave123", "Clave123")
                .andExpect(status().isConflict());
    }

    @Test
    void validaContrasenaEnElServidor() throws Exception {
        register("Ana", "ana@correo.com", "clave123", "clave123").andExpect(status().isBadRequest());
        register("Ana", "ana@correo.com", "Clave", "Clave").andExpect(status().isBadRequest());
        register("Ana", "ana@correo.com", "Clave123", "Clave124").andExpect(status().isBadRequest());
    }

    @Test
    void contrasenaIncorrectaDevuelveMensajeGenerico() throws Exception {
        register("Ana Pérez", "ana@correo.com", "Clave123", "Clave123");
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content(credentials("ana@correo.com", "Mala1234")))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Correo o contraseña inválidos"));
    }

    @Test
    void sinSesionNoHayAccesoYTrasLogoutTampoco() throws Exception {
        mvc.perform(get("/api/users")).andExpect(status().isUnauthorized());

        register("Ana Pérez", "ana@correo.com", "Clave123", "Clave123");
        MockHttpSession session = login("ana@correo.com", "Clave123");

        mvc.perform(post("/api/auth/logout").session(session)).andExpect(status().isNoContent());
        mvc.perform(get("/api/users/1").session(session)).andExpect(status().isUnauthorized());
    }

    private org.springframework.test.web.servlet.ResultActions register(
            String name, String email, String password, String confirm) throws Exception {
        String body = """
                {"fullName":"%s","email":"%s","password":"%s","confirmPassword":"%s"}
                """.formatted(name, email, password, confirm);
        return mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(body));
    }

    private MockHttpSession login(String email, String password) throws Exception {
        MvcResult result = mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content(credentials(email, password)))
                .andExpect(status().isOk())
                .andReturn();
        return (MockHttpSession) result.getRequest().getSession(false);
    }

    private static String credentials(String email, String password) {
        return """
                {"email":"%s","password":"%s"}
                """.formatted(email, password);
    }
}
