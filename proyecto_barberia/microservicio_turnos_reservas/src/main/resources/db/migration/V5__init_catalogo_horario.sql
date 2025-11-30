CREATE TABLE cliente (
    cliente_id VARCHAR(20) PRIMARY KEY,
    cliente_nombre VARCHAR(50) NOT NULL,
    cliente_email VARCHAR(50) NOT NULL,
    cliente_telefono BIGINT NOT NULL,
    cliente_estado VARCHAR(20) NOT NULL CHECK (cliente_estado IN ('ACTIVO', 'INACTIVO'))
);

CREATE TABLE reserva (
    reserva_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    cliente_id VARCHAR(20) NOT NULL,
    reserva_fecha DATE NOT NULL,

    CONSTRAINT fk_reserva_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(cliente_id)
);


CREATE TABLE estado (
    estado_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    estado_nombre VARCHAR(50) NOT NULL
);

CREATE TABLE turno (
    turno_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    reserva_id INT NULL,
    cliente_id VARCHAR(20) NOT NULL,
    servicio_id INT NOT NULL,
    barbero_id VARCHAR(20) NOT NULL,
    estado_id INT NOT NULL,
    turno_descripcion VARCHAR(50) NOT NULL,
    turno_hora_inicio TIME NULL,
    turno_hora_fin TIME NULL,
    turno_fecha DATE NOT NULL,

    CONSTRAINT fk_turno_reserva FOREIGN KEY (reserva_id) REFERENCES reserva(reserva_id),
    CONSTRAINT fk_turno_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(cliente_id),
    CONSTRAINT fk_turno_estado  FOREIGN KEY (estado_id)  REFERENCES estado(estado_id)
);

CREATE TABLE tipo_incidencia (
    tipoincidencia_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    tipoincidencia_nombre VARCHAR(50) NOT NULL
);

CREATE TABLE incidencia (
    incidencia_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    turno_id INT NOT NULL,
    tipoincidencia_id INT NOT NULL,
    tipoincidencia_descripcion VARCHAR(100) NOT NULL,

    CONSTRAINT fk_incidencia_turno FOREIGN KEY (turno_id) REFERENCES turno(turno_id),

    CONSTRAINT fk_incidencia_tipoincidencia FOREIGN KEY (tipoincidencia_id) REFERENCES tipo_incidencia(tipoincidencia_id)
);
