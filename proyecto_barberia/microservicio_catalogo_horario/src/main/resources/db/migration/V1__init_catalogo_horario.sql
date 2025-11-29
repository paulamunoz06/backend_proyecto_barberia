
CREATE TABLE administrador (
    administrador_id VARCHAR(20) PRIMARY KEY,
    administrador_nombre VARCHAR(50) NOT NULL
);

CREATE TABLE categoria (
    categoria_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    categoria_nombre VARCHAR(50) NOT NULL,
    categoria_descripcion VARCHAR(100) NOT NULL
);

CREATE TABLE ocupacion (
    ocupacion_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    ocupacion_nombre VARCHAR(50) NOT NULL
);

CREATE TABLE barbero (
    barbero_id VARCHAR(20) PRIMARY KEY,
    administrador_id VARCHAR(20) NOT NULL,
    ocupacion_id INT,
    barbero_nombre VARCHAR(50) NOT NULL,
    barbero_email VARCHAR(50) NOT NULL,
    barbero_telefono BIGINT NOT NULL,
    barbero_estado VARCHAR(20) NOT NULL CHECK (barbero_estado IN ('ACTIVO','INACTIVO')),
    barbero_nacimiento DATE NOT NULL,
    barbero_fotografia VARCHAR(200),

    CONSTRAINT fk_barbero_admin FOREIGN KEY (administrador_id) REFERENCES administrador(administrador_id),

    CONSTRAINT fk_barbero_ocupacion FOREIGN KEY (ocupacion_id) REFERENCES ocupacion(ocupacion_id)
);

CREATE TABLE servicio (
    servicio_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    categoria_id INT NOT NULL,
    administrador_id VARCHAR(20) NOT NULL,
    servicio_descripcion VARCHAR(100) NOT NULL,
    servicio_duracion INT NOT NULL,
    servicio_precio REAL NOT NULL,
    servicio_preparacion INT NOT NULL,
    servicio_nombre VARCHAR(50) NOT NULL,

    CONSTRAINT fk_servicio_categoria FOREIGN KEY (categoria_id) REFERENCES categoria(categoria_id),

    CONSTRAINT fk_servicio_admin FOREIGN KEY (administrador_id) REFERENCES administrador(administrador_id)
);

CREATE TABLE especializacion (
    servicio_id INT NOT NULL,
    barbero_id VARCHAR(20) NOT NULL,

    PRIMARY KEY (servicio_id, barbero_id),

    CONSTRAINT fk_especializacion_servicio FOREIGN KEY (servicio_id) REFERENCES servicio(servicio_id),

    CONSTRAINT fk_especializacion_barbero FOREIGN KEY (barbero_id) REFERENCES barbero(barbero_id)
);

CREATE TABLE horario(
    horario_id DATE PRIMARY KEY,
    administrador_id VARCHAR(20) NOT NULL,

    CONSTRAINT fk_horario_admin FOREIGN KEY (administrador_id) REFERENCES administrador(administrador_id)
);

CREATE TABLE franja (
    franja_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    horario_id DATE NOT NULL,
    franja_inicio TIME NOT NULL,
    franja_fin TIME NOT NULL,

    CONSTRAINT franja_unica_por_horario UNIQUE (horario_id, franja_inicio, franja_fin),

    CONSTRAINT fk_franja_horario FOREIGN KEY (horario_id) REFERENCES horario(horario_id)
);

CREATE TABLE trabajo (
    barbero_id VARCHAR(20) NOT NULL,
    franja_id INT NOT NULL,

    PRIMARY KEY (barbero_id, franja_id),

    CONSTRAINT fk_trabajo_barbero FOREIGN KEY (barbero_id) REFERENCES barbero(barbero_id),

    CONSTRAINT fk_trabajo_franja FOREIGN KEY (franja_id) REFERENCES franja(franja_id)
);
