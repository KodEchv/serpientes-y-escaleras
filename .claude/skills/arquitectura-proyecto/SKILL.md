# Arquitectura Proyecto Java Universidad

Usar normalmente esta estructura:

src/
└── co/edu/unbosque/
    ├── controller/
    ├── model/
    │   ├── persistence/
    │   └── facade/
    ├── view/
    │   └── facade/
    ├── util/
    │   ├── exception/
    │   └── structure/

Reglas:
- DAO dentro de persistence.
- DTO dentro de model.
- Fachadas para model y view.
- Main dentro de controller.
- Compatibilidad con Eclipse.
- bin/ como salida de compilados.
- archivos/ para persistencia.