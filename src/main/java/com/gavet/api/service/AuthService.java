package com.gavet.api.service;

import java.util.Locale;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gavet.api.dto.AuthResponse;
import com.gavet.api.dto.LoginClienteRequest;
import com.gavet.api.dto.RegistroClienteRequest;
import com.gavet.api.dto.UsuarioResponse;
import com.gavet.api.entity.Cliente;
import com.gavet.api.repository.ClienteRepository;

@Service
public class AuthService {

    private final ClienteRepository clienteRepository;

    public AuthService(
            ClienteRepository clienteRepository
    ) {

        this.clienteRepository =
                clienteRepository;
    }

    /*
     * Registra un cliente nuevo en MySQL.
     */
    @Transactional
    public AuthResponse registrar(
            RegistroClienteRequest request
    ) {

        if (request == null) {

            return new AuthResponse(
                false,
                "Los datos del registro son obligatorios",
                null
            );
        }

        String nombre =
                limpiar(
                    request.getNombreCompleto()
                );

        String correo =
                limpiar(
                    request.getCorreo()
                ).toLowerCase(
                    Locale.ROOT
                );

        String telefono =
                limpiar(
                    request.getTelefono()
                );

        String password =
                request.getPassword() == null
                    ? ""
                    : request.getPassword();

        if (nombre.length() < 3) {

            return new AuthResponse(
                false,
                "Ingrese un nombre válido",
                null
            );
        }

        if (
            correo.isBlank() ||
            !correo.contains("@") ||
            !correo.contains(".")
        ) {

            return new AuthResponse(
                false,
                "Ingrese un correo válido",
                null
            );
        }

        if (
            telefono.length() != 9 ||
            !telefono.matches("\\d{9}") ||
            !telefono.startsWith("9")
        ) {

            return new AuthResponse(
                false,
                "Ingrese un celular válido de 9 dígitos",
                null
            );
        }

        if (password.length() < 6) {

            return new AuthResponse(
                false,
                "La contraseña debe tener mínimo 6 caracteres",
                null
            );
        }

        /*
         * Las cuentas desactivadas también ocupan
         * el correo. Deben recuperarse en el login.
         */
        if (
            clienteRepository
                .existsByEmailIgnoreCase(
                    correo
                )
        ) {

            return new AuthResponse(
                false,
                "El correo ya se encuentra registrado. Inicia sesión para recuperar la cuenta si fue eliminada.",
                null
            );
        }

        if (
            clienteRepository
                .existsByTelefono(
                    telefono
                )
        ) {

            return new AuthResponse(
                false,
                "El celular ya se encuentra registrado",
                null
            );
        }

        Cliente cliente =
                new Cliente();

        cliente.setNombreCompleto(
            nombre
        );

        cliente.setEmail(
            correo
        );

        cliente.setTelefono(
            telefono
        );

        /*
         * Contraseña guardada directamente
         * para esta versión académica.
         */
        cliente.setPassword(
            password
        );

        cliente.setActivo(
            true
        );

        Cliente clienteGuardado =
                clienteRepository.save(
                    cliente
                );

        return new AuthResponse(
            true,
            "Cuenta creada correctamente",
            convertirUsuario(
                clienteGuardado
            )
        );
    }

    /*
     * Verifica el correo y contraseña.
     *
     * También detecta una cuenta desactivada para
     * ofrecer su recuperación desde Android.
     */
    @Transactional(readOnly = true)
    public AuthResponse iniciarSesion(
            LoginClienteRequest request
    ) {

        if (request == null) {

            return new AuthResponse(
                false,
                "Ingrese sus credenciales",
                null
            );
        }

        String correo =
                limpiar(
                    request.getCorreo()
                ).toLowerCase(
                    Locale.ROOT
                );

        String password =
                request.getPassword() == null
                    ? ""
                    : request.getPassword();

        if (
            correo.isBlank() ||
            password.isBlank()
        ) {

            return new AuthResponse(
                false,
                "Ingrese su correo y contraseña",
                null
            );
        }

        /*
         * Busca la cuenta sin filtrar por activo.
         */
        Optional<Cliente> clienteEncontrado =
                clienteRepository
                    .findByEmailIgnoreCase(
                        correo
                    );

        if (clienteEncontrado.isEmpty()) {

            return new AuthResponse(
                false,
                "Correo o contraseña incorrectos",
                null
            );
        }

        Cliente cliente =
                clienteEncontrado.get();

        boolean passwordCorrecta =
                password.equals(
                    cliente.getPassword()
                );

        if (!passwordCorrecta) {

            return new AuthResponse(
                false,
                "Correo o contraseña incorrectos",
                null
            );
        }

        /*
         * La contraseña es correcta, pero la
         * cuenta fue desactivada.
         */
        if (
            !Boolean.TRUE.equals(
                cliente.getActivo()
            )
        ) {

            return new AuthResponse(
                false,
                "Tu cuenta se encuentra desactivada",
                null,
                true
            );
        }

        return new AuthResponse(
            true,
            "Inicio de sesión correcto",
            convertirUsuario(
                cliente
            )
        );
    }

    /*
     * Reactiva una cuenta únicamente cuando el
     * correo y la contraseña son correctos.
     */
    @Transactional
    public AuthResponse reactivarCuenta(
            LoginClienteRequest request
    ) {

        if (request == null) {

            return new AuthResponse(
                false,
                "Ingrese sus credenciales",
                null
            );
        }

        String correo =
                limpiar(
                    request.getCorreo()
                ).toLowerCase(
                    Locale.ROOT
                );

        String password =
                request.getPassword() == null
                    ? ""
                    : request.getPassword();

        if (
            correo.isBlank() ||
            password.isBlank()
        ) {

            return new AuthResponse(
                false,
                "Ingrese su correo y contraseña",
                null
            );
        }

        Optional<Cliente> clienteEncontrado =
                clienteRepository
                    .findByEmailIgnoreCase(
                        correo
                    );

        if (clienteEncontrado.isEmpty()) {

            return new AuthResponse(
                false,
                "Correo o contraseña incorrectos",
                null
            );
        }

        Cliente cliente =
                clienteEncontrado.get();

        if (
            !password.equals(
                cliente.getPassword()
            )
        ) {

            return new AuthResponse(
                false,
                "Correo o contraseña incorrectos",
                null
            );
        }

        if (
            Boolean.TRUE.equals(
                cliente.getActivo()
            )
        ) {

            return new AuthResponse(
                true,
                "La cuenta ya se encontraba activa",
                convertirUsuario(
                    cliente
                )
            );
        }

        cliente.setActivo(
            true
        );

        Cliente clienteReactivado =
                clienteRepository.save(
                    cliente
                );

        return new AuthResponse(
            true,
            "Cuenta recuperada correctamente",
            convertirUsuario(
                clienteReactivado
            )
        );
    }

    private UsuarioResponse convertirUsuario(
            Cliente cliente
    ) {

        return new UsuarioResponse(
            cliente.getIdCliente(),
            cliente.getNombreCompleto(),
            cliente.getEmail(),
            cliente.getTelefono()
        );
    }

    private String limpiar(
            String texto
    ) {

        return texto == null
            ? ""
            : texto.trim();
    }
}