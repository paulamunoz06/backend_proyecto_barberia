INSERT INTO cliente (cliente_id, cliente_nombre, cliente_email, cliente_telefono, cliente_estado)
VALUES ('4', 'Juan Perez', 'juan@example.com', 3209021633, 'ACTIVO');

INSERT INTO reserva (cliente_id, reserva_fecha)
VALUES
('4', '2025-11-21'),
('4', '2025-11-22'),
('4', '2025-12-01');

INSERT INTO estado (estado_nombre)
VALUES
('CONFIRMADO'),
('EN_CURSO'),
('CANCELADO'),
('COMPLETADO');

INSERT INTO turno (reserva_id, cliente_id, servicio_id, barbero_id, estado_id, turno_descripcion, turno_fecha_hora_inicio, turno_fecha_hora_fin)
VALUES
(1, '4', 1, '2', 1, 'Corte básico', '2025-02-25', '2025-02-25'),
(2, '4', 2, '3', 2, 'Corte premium', '2025-02-26', '2025-02-26');

INSERT INTO tipo_incidencia (tipoincidencia_nombre)
VALUES
('Retraso del cliente'),
('Problema técnico'),
('Insumos faltantes'),
('Emergencia de salud'),
('Otro');

INSERT INTO incidencia (turno_id, tipoincidencia_id, tipoincidencia_descripcion)
VALUES
(1, 1, 'Cliente llegó tarde'),
(1, 3, 'Máquina 0 defectuosa'),
(2, 4, 'Quemadura de segundo grado');