INSERT INTO administrador (administrador_id, administrador_nombre)
VALUES
    ('1','Juan Pérez');

INSERT INTO categoria (categoria_nombre, categoria_descripcion)
VALUES
    ('Cortes', 'Servicios de corte de cabello'),
    ('Afeitado', 'Servicios de afeitado y barba'),
    ('Tratamientos', 'Servicios de cuidado capilar'),
    ('Faciales', 'Servicios de estética facial');

INSERT INTO ocupacion (ocupacion_nombre)
VALUES
    ('DISPONIBLE'),
    ('EN_SERVICIO'),
    ('AUSENTE');

INSERT INTO barbero (barbero_id, administrador_id, ocupacion_id, barbero_nombre, barbero_email, barbero_telefono, barbero_estado, barbero_nacimiento, barbero_fotografia)
VALUES
    ('2','1',1,'Carlos Sánchez','carlos@example.com',3001234567,'ACTIVO','1990-05-10',NULL),
    ('3','1',1,'Ana López','ana@example.com',3009876543,'ACTIVO','1992-08-20',NULL),
    ('4','1',1,'Luis Herrera','luis.herrera@example.com',3001112222,'ACTIVO','1989-04-15',NULL),
    ('5','1',1,'Marcos Díaz','marcos.diaz@example.com',3002223333,'ACTIVO','1991-11-07',NULL),
    ('6','1',1,'Felipe Rojas','felipe.rojas@example.com',3003334444,'ACTIVO','1995-02-19',NULL),
    ('7','1',1,'David Molina','david.molina@example.com',3004445555,'ACTIVO','1993-07-12',NULL),
    ('8','1',1,'Esteban Silva','esteban.silva@example.com',3005556666,'ACTIVO','1988-09-03',NULL),
    ('9','1',1,'Kevin Torres','kevin.torres@example.com',3006667777,'ACTIVO','1996-01-25',NULL),
    ('10','1',1,'Oscar Mejía','oscar.mejia@example.com',3007778888,'ACTIVO','1994-03-17',NULL),
    ('11','1',1,'Ricardo Pardo','ricardo.pardo@example.com',3008889999,'ACTIVO','1990-12-30',NULL),
    ('12','1',1,'Francisco Murillo','francisco.murillo@example.com',3009990000,'ACTIVO','1987-06-29',NULL),
    ('13','1',1,'Tomás Herrera','tomas.herrera@example.com',3011112222,'ACTIVO','1997-05-22',NULL);

INSERT INTO servicio (categoria_id, administrador_id, servicio_descripcion, servicio_duracion, servicio_precio, servicio_preparacion, servicio_nombre)
VALUES
    (1,'1','Corte clásico tradicional',30,15000,10,'Corte Clásico'),
    (1,'1','Corte degradado profesional',40,20000,10,'Corte Fade'),
    (1,'1','Corte con tijera avanzado',45,25000,10,'Corte Tijera'),

    (2,'1','Afeitado al ras con toalla caliente',25,18000,5,'Afeitado Tradicional'),
    (2,'1','Perfilado y arreglo de barba',35,22000,5,'Diseño de Barba'),
    (2,'1','Afeitado premium con tratamiento',40,26000,5,'Afeitado Premium'),

    (3,'1','Tinte natural para el cabello',50,35000,10,'Tinte Cabello'),
    (3,'1','Aplicación de keratina corta duración',60,45000,10,'Keratina'),
    (3,'1','Limpieza profunda del cuero cabelludo',20,20000,5,'Limpieza Capilar'),

    (4,'1','Mascarilla facial revitalizante',30,20000,5,'Mascarilla Facial'),
    (4,'1','Depilación y diseño de cejas',20,18000,5,'Cejas'),
    (4,'1','Tratamiento exfoliante facial',25,22000,5,'Exfoliación Facial');

INSERT INTO especializacion (servicio_id, barbero_id) VALUES
-- Carlos (2)
(1,'2'), (2,'2'), (4,'2'), (5,'2'),

-- Ana (3)
(2,'3'), (3,'3'), (6,'3'), (10,'3'),

-- Luis (4)
(1,'4'), (3,'4'), (5,'4'), (7,'4'),

-- Marcos (5)
(2,'5'), (4,'5'), (8,'5'), (9,'5'),

-- Felipe (6)
(1,'6'), (6,'6'), (11,'6'), (12,'6'),

-- David (7)
(1,'7'), (2,'7'), (5,'7'), (9,'7'),

-- Esteban (8)
(3,'8'), (4,'8'), (10,'8'), (12,'8'),

-- Kevin (9)
(2,'9'), (6,'9'), (7,'9'), (11,'9'),

-- Oscar (10)
(1,'10'), (4,'10'), (8,'10'), (12,'10'),

-- Ricardo (11)
(3,'11'), (5,'11'), (7,'11'), (9,'11'),

-- Francisco (12)
(2,'12'), (8,'12'), (10,'12'), (11,'12'),

-- Tomás (13)
(1,'13'), (3,'13'), (6,'13'), (12,'13');


INSERT INTO horario (horario_id, administrador_id)
VALUES
('2025-12-01','1'),
('2025-12-02','1'),
('2025-12-03','1'),
('2025-12-04','1'),
('2025-12-05','1'),
('2025-12-06','1'),
('2025-12-07','1'),
('2025-12-08','1'),
('2025-12-09','1'),
('2025-12-10','1'),
('2025-12-11','1'),
('2025-12-12','1');


INSERT INTO franja (horario_id, franja_inicio, franja_fin) VALUES
('2025-12-01','08:00','13:00'),
('2025-12-01','14:00','20:00'),

('2025-12-02','08:00','13:00'),
('2025-12-02','14:00','20:00'),

('2025-12-03','08:00','13:00'),
('2025-12-03','14:00','20:00'),

('2025-12-04','08:00','13:00'),
('2025-12-04','14:00','20:00'),

('2025-12-05','08:00','13:00'),
('2025-12-05','14:00','20:00'),

('2025-12-06','08:00','13:00'),
('2025-12-06','14:00','20:00'),

('2025-12-07','08:00','13:00'),
('2025-12-07','14:00','20:00'),

('2025-12-08','08:00','13:00'),
('2025-12-08','14:00','20:00'),

('2025-12-09','08:00','13:00'),
('2025-12-09','14:00','20:00'),

('2025-12-10','08:00','13:00'),
('2025-12-10','14:00','20:00'),

('2025-12-11','08:00','13:00'),
('2025-12-11','14:00','20:00'),

('2025-12-12','08:00','13:00'),
('2025-12-12','14:00','20:00');

INSERT INTO trabajo (barbero_id, franja_id)
SELECT b.barbero_id, f.franja_id
FROM barbero b
JOIN franja f ON 1=1
ORDER BY b.barbero_id, f.franja_id;
