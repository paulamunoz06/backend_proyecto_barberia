INSERT INTO rol (rol_nombre) VALUES
('ADMINISTRADOR'),
('BARBERO'),
('CLIENTE');

INSERT INTO usuario (usuario_id, usuario_password) VALUES
('1','$2a$10$Kw2ocP2gcltMoeM3COftfO5ArT6IA2fRk2xSSAhKRYji24PnMw4LC'),
('2','$2a$10$1JXL0cE8SqemQjCwrq6BV.KRbJrTNfxzfRSHiMyFxLH5SRpxVc/SG'),
('3','$2a$10$w7uxBwLXwYL.EQ4FREnyoex6UAEvENxBpXV4VFiPHhhOexSWdRQIO'),
('4','$2a$10$Kw2ocP2gcltMoeM3COftfO5ArT6IA2fRk2xSSAhKRYji24PnMw4LC');

INSERT INTO usuario_roles (usuario_id, rol_id) VALUES
('1',1),
('2',2),
('3',2),
('4',3);

INSERT INTO microservicio_permissions
(microservice_name, endpoint_pattern, http_method, required_roles, description)
VALUES
    ('catalogo-service', '/api/asignar*', 'GET', 'CLIENTE', ''),
    ('catalogo-service', '/api/barbero', 'GET', 'CLIENTE,ADMINISTRADOR', ''),
    ('catalogo-service', '/api/barbero/activos', 'GET', 'CLIENTE,ADMINISTRADOR', ''),
    ('catalogo-service', '/api/barbero/servicio/*', 'GET', 'CLIENTE,ADMINISTRADOR', ''),
    ('catalogo-service', '/api/barbero/*', 'GET', 'CLIENTE,BARBERO,ADMINISTRADOR', ''),
    ('catalogo-service', '/api/barbero', 'POST', 'ADMINISTRADOR', ''),
    ('catalogo-service', '/api/barbero/*', 'PUT', 'ADMINISTRADOR', ''),
    ('catalogo-service', '/api/barbero/*', 'DELETE', 'ADMINISTRADOR', ''),
    ('catalogo-service', '/api/franja/horario/*', 'GET', 'BARBERO,ADMINISTRADOR', 'Buscar horario por fecha'),
    ('catalogo-service', '/api/franja/barbero/*', 'GET', 'BARBERO,ADMINISTRADOR', 'Buscar franjas por barbero'),
    ('catalogo-service', '/api/franja/barbero/*/*', 'GET', 'BARBERO,ADMINISTRADOR', 'Buscar franjas por barbero desde una hora'),
    ('catalogo-service', '/api/franja/barbero/*/*/*/*', 'GET', 'BARBERO,ADMINISTRADOR', 'Validar duración continua'),
    ('catalogo-service', '/api/franja', 'POST', 'ADMINISTRADOR', 'Crear una franja horaria'),
    ('catalogo-service', '/api/franja/*', 'PUT', 'ADMINISTRADOR', 'Actualizar franja horaria'),
    ('catalogo-service', '/api/franja/*', 'GET', 'BARBERO,ADMINISTRADOR', 'Actualizar franja horaria'),
    ('catalogo-service', '/api/franja/*', 'DELETE', 'ADMINISTRADOR', 'Eliminar franja horaria'),
    ('catalogo-service', '/api/horario/*', 'GET', 'BARBERO,ADMINISTRADOR', 'Buscar horario laboral por fecha'),
    ('catalogo-service', '/api/horario/*/*', 'GET', 'BARBERO,ADMINISTRADOR', 'Buscar horario por barbero y fecha'),
    ('catalogo-service', '/api/horario', 'POST', 'ADMINISTRADOR', 'Crear horario laboral'),
    ('catalogo-service', '/api/horario/*', 'PUT', 'ADMINISTRADOR', 'Actualizar horario laboral'),
    ('catalogo-service', '/api/categoria/categorias','GET','CLIENTE,BARBERO,ADMINISTRADOR','Listar todas las categorías'),
    ('catalogo-service', '/api/servicio', 'GET', 'CLIENTE,BARBERO,ADMINISTRADOR', 'Listar todos los servicios'),
    ('catalogo-service', '/api/servicio/categoria/*', 'GET', 'CLIENTE,BARBERO,ADMINISTRADOR', 'Listar servicios por categoría'),
    ('catalogo-service', '/api/servicio/barbero/*', 'GET', 'BARBERO,ADMINISTRADOR', 'Listar servicios por categoría'),
    ('catalogo-service', '/api/servicio/*', 'GET', 'CLIENTE,BARBERO,ADMINISTRADOR', 'Buscar un servicio por ID'),
    ('catalogo-service', '/api/servicio', 'POST', 'ADMINISTRADOR', 'Crear un nuevo servicio'),
    ('catalogo-service', '/api/servicio/*', 'PUT', 'ADMINISTRADOR', 'Actualizar un servicio existente'),
    ('catalogo-service', '/api/servicio/*', 'DELETE', 'ADMINISTRADOR', 'Eliminar un servicio por ID'),
    ('identidad-acceso', '/api/usuario/barbero', 'POST', 'ADMINISTRADOR', 'Crear un barbero y usuario'),
    ('identidad-acceso', '/api/usuario/cliente', 'POST', 'ADMINISTRADOR', 'Crear un cliente y usuario');