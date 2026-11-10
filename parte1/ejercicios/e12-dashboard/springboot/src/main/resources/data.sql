-- E1.2: datos de ejemplo (dos proveedores; un contrato vigente y otro vencido)
INSERT INTO proveedores (ruc, razon_social, limite_credito) VALUES
  ('1790012345001', 'Importadora XL', 50000.00),
  ('0990012345001', 'Aceros del Pacífico', 20000.00);
INSERT INTO contratos (numero, descripcion, valor, proveedor_id, fecha_inicio, fecha_vencimiento) VALUES
  ('CT-2026-001', 'Suministro anual', 12500.50, 1, DATE '2026-01-01', DATE '2027-12-31'),
  ('CT-2025-009', 'Mantenimiento', 3000.00, 2, DATE '2025-01-01', DATE '2025-12-31');
