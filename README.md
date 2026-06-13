# Microservicio de Usuario

## server.port=8083

## Endpoints

### POST `/api/v1/usuarios`
Crea un usuario.

**JSON de entrada:**
```json
{
    "nombre": Filomeno,
    "apellido: "Rodriguez",
    "emailCliente": "f.rodriguez@gmail.com",
    "password": "12345",
    "rol": "USUARIO"
}
```

---

### GET `/api/v1/usuarios/{id}`
Lista todos los usuarios registrados en la base de datos.

---

### GET `/api/v1/usuarios/correo/{emailCliente}`
Lista a un usuario en especifico por su email.
