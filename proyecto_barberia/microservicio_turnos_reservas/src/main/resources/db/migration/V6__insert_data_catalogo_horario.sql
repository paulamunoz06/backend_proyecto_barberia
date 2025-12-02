INSERT INTO cliente (cliente_id, cliente_nombre, cliente_email, cliente_telefono, cliente_estado)
VALUES
('14', 'Juan Perez', 'jumaco59@gmail.com', 3209021633, 'ACTIVO'),
('15', 'María Gómez', 'jumaco59@gmail.com', 3104567890, 'ACTIVO'),
('16', 'Luis Fernández', 'jumaco59@gmail.com', 3217890123, 'ACTIVO'),
('17', 'Carolina Rojas', 'jumaco59@gmail.com', 3156789012, 'ACTIVO'),
('18', 'Andrés Castillo', 'jumaco59@gmail.com', 3223456789, 'ACTIVO'),
('19', 'Paola Martínez', 'jumaco59@gmail.com', 3192345678, 'ACTIVO');

INSERT INTO reserva (cliente_id, reserva_fecha)
VALUES
('14', '2025-12-02'),
('14', '2025-12-03'),
('14', '2025-12-04'),
('15', '2025-12-03'),
('15', '2025-12-05'),
('16', '2025-12-02'),
('16', '2025-12-06'),
('17', '2025-12-04'),
('18', '2025-12-07'),
('19', '2025-12-05');

INSERT INTO estado (estado_nombre)
VALUES
('CONFIRMADO'),
('EN_CURSO'),
('CANCELADO'),
('COMPLETADO'),
('NO_PRESENTADO');

INSERT INTO turno (reserva_id, cliente_id, servicio_id, barbero_id, estado_id, turno_descripcion, turno_hora_inicio, turno_hora_fin, turno_fecha)
VALUES
(1, '14', 1, '2', 1, 'Corte básico', '08:00', '08:40','2025-12-02'),
(1, '14', 2, '3', 2, 'Corte preqmium', '08:40', '11:00','2025-12-02'),
(3, '14', 2, '3', 2, 'Corte premium', '10:00', '10:30','2025-12-04'),
(4, '15', 1, '4', 1, 'Corte Clásico', '08:00', '08:30','2025-12-03'),
(5, '15', 5, '2', 1, 'Diseño de Barba', '08:30', '09:10','2025-12-05'),
(6, '16', 3, '3', 1, 'Corte Tijera', '09:00', '09:45','2025-12-02'),
(7, '16', 6, '6', 2, 'Afeitado Premium', '10:00', '10:40','2025-12-06'),
(8, '17', 2, '5', 1, 'Corte Fade', '11:00', '11:40','2025-12-04'),
(9, '18', 8, '10', 1, 'Keratina', '08:00', '09:00','2025-12-07'),
(10, '19', 12, '13', 1, 'Exfoliación Facial', '09:00', '09:25','2025-12-05'),
(null, '15', 4, '2', 1, 'Afeitado Tradicional', '14:00', '14:25','2025-12-03'),
(null, '16', 7, '3', 1, 'Tinte Cabello', '14:30', '15:20','2025-12-02'),
(null, '17', 10, '8', 1, 'Mascarilla Facial', '15:00', '15:30','2025-12-04'),
(null, '18', 1, '6', 1, 'Corte Clásico', '16:00', '16:30','2025-12-07'),
(null, '19', 5, '9', 1, 'Diseño de Barba', '17:00', '17:40','2025-12-05');

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
(2, 4, 'Quemadura de segundo grado'),
(3, 1, 'Cliente llegó 5 minutos tarde'),
(4, 2, 'Problema con la silla hidráulica'),
(4, 5, 'Observación general: cliente pidió ajuste especial'),
(5, 3, 'Faltó cera modeladora durante el servicio'),
(6, 1, 'Cliente llegó con retraso de 10 minutos'),
(6, 4, 'Ligero mareo del cliente'),
(7, 2, 'Navaja de precisión no estaba disponible'),
(8, 5, 'Cliente solicitó cambio de estilo en medio del procedimiento'),
(9, 3, 'Falta de guantes desechables al iniciar el turno'),
(10, 1, 'Cliente llegó tarde por tráfico'),
(11, 2, 'La máquina presentó sonido inusual'),
(11, 5, 'Cliente pidió registro fotográfico del resultado'),
(12, 3, 'Se agotó el tinte del tono solicitado'),
(13, 4, 'Cliente sufrió irritación leve por mascarilla'),
(14, 1, 'Cliente tardó 7 minutos en llegar'),
(15, 2, 'Problemas con la iluminación de la estación');