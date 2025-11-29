CREATE TABLE rol (
    rol_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    rol_nombre VARCHAR(50) NOT NULL
);

CREATE TABLE usuario (
    usuario_id VARCHAR(20) PRIMARY KEY,
    usuario_password VARCHAR(500) NOT NULL
);

CREATE TABLE usuario_roles (
    usuario_id VARCHAR(20) NOT NULL,
    rol_id INT NOT NULL,

    PRIMARY KEY (usuario_id, rol_id),

    CONSTRAINT fk_user FOREIGN KEY (usuario_id) REFERENCES usuario(usuario_id),
    CONSTRAINT fk_role FOREIGN KEY (rol_id) REFERENCES rol(rol_id)
);

CREATE TABLE microservicio_permissions (
    id BIGSERIAL PRIMARY KEY,
    microservice_name VARCHAR(100) NOT NULL,
    endpoint_pattern VARCHAR(200) NOT NULL,
    http_method VARCHAR(10) DEFAULT '*',
    required_roles TEXT,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
