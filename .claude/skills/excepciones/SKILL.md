# Excepciones Personalizadas

Reglas:
- Crear excepciones dentro de util.exception.
- Nombres descriptivos.
- Extender Exception.
- Usar mensajes claros.

Ejemplos:
- DatoInvalidoException
- ListaVaciaException
- NodoNuloException

package co.edu.unbosque.util.excepcion;

/**
 * Se lanza cuando un campo obligatorio viene vacio o nulo.
 * <p>
 * Se usa en validaciones de la capa de controlador antes de procesar cualquier
 * operacion.
 * </p>
 *
 * @author UEb
 * @version 1.0
 */
public class DatosVaciosExcepcion extends BosqueAmigosExcepcion {

	private static final long serialVersionUID = 1L;

	/**
	 * Construye la excepcion indicando el campo que viene vacio.
	 *
	 * @param campo nombre del campo vacio (ej: "nickname", "contrasena", "mensaje")
	 */
	public DatosVaciosExcepcion(String campo) {
		super("El campo '" + campo + "' no puede estar vacio.");
	}
}
