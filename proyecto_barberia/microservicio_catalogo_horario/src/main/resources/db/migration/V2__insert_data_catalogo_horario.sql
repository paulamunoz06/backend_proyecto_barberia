INSERT INTO administrador (administrador_id, administrador_nombre)
VALUES
    ('1','Juan Pérez');

INSERT INTO categoria (categoria_nombre, categoria_descripcion)
VALUES
    ('Cortes', 'Servicios de corte de cabello'),
    ('Afeitado', 'Servicios de afeitado y barba');

INSERT INTO ocupacion (ocupacion_nombre)
VALUES
    ('Disponible'),
    ('En servicio'),
    ('Inactivo');

INSERT INTO barbero (barbero_id, administrador_id, ocupacion_id, barbero_nombre, barbero_email, barbero_telefono, barbero_estado, barbero_nacimiento, barbero_fotografia)
VALUES
    ('2','1', 1, 'Carlos Sánchez', 'carlos@example.com', 3001234567, 'ACTIVO', '1990-05-10', NULL),
    ('3','1', 1, 'Ana López', 'ana@example.com', 3009876543, 'ACTIVO', '1992-08-20', NULL);

INSERT INTO servicio (categoria_id, administrador_id, servicio_descripcion, servicio_duracion, servicio_precio,servicio_nombre, servicio_preparacion)
VALUES
    (1, '1', 'Corte clásico', 30, 15000,'Corte Clásico',10),
    (1, '1', 'Corte fade', 40, 18000,'Corte Fade',2);

INSERT INTO especializacion (servicio_id, barbero_id)
VALUES
    (1, '2'),
    (2, '3');

INSERT INTO horario (horario_id, administrador_id)
VALUES
    ('2025-11-25', '1'),
    ('2025-11-2', '1');

INSERT INTO franja (horario_id, franja_inicio, franja_fin)
VALUES
    ('2025-11-25', '08:00', '10:00'),
    ('2025-11-25', '10:00', '12:00');

INSERT INTO trabajo (barbero_id, franja_id)
VALUES
    ('2', 1),
    ('3', 2);
