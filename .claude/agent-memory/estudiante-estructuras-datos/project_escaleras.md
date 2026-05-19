---
name: project-escaleras
description: Proyecto "Escaleras y Serpientes a lo Bosque" — juego Java puro para Eclipse, Universidad El Bosque
metadata:
  type: project
---

Proyecto universitario de Estructura de Datos: juego Escaleras y Serpientes en Java puro, Eclipse, sin librerías externas.

**Paquete raíz:** `co.edu.unbosque`

**Paquete persistencia:** `co.edu.unbosque.model.persistencie` (con typo intencional en el nombre del paquete — así está en el proyecto original)

**DTOs ya existentes (co.edu.unbosque.model):**
- JugadorDTO: nombre, posicionActual, tieneEscudo, tieneDobleTurno, pierdeTurno, cantidadTurnos
- MovimientoDTO: nombreJugador, dado, posAntes, posDespues, evento
- CasillaDTO + enum TipoCasilla {NORMAL, SERPIENTE, ESCALERA, COMODIN}
- SerpienteDTO: posicionCabeza, posicionCola
- EscaleraDTO: posicionBase, posicionCima
- ComodinDTO v2: solo posicion:int — sin tipo, sin posicionDestino, sin pesoArista.
  El efecto se decide en runtime por GestorComodines segun dado comodin (1-6).

**DAO ya existente:** TableroDAO en co.edu.unbosque.model.persistencie — gestiona Graph de 100 vértices

**Estructuras disponibles (co.edu.unbosque.utils.structure):**
- Node<E>, MyLinkedList<E>, MyDoubleLinkedList<E>, MyDequeList<E>
- Queue<E> interfaz, QueueImpl<E> FIFO (enqueue=insertFirst, dequeue=removeLast)
- Stack<E> interfaz, StackImpl<E> LIFO (push=insertFirst, pop=removeFirst)
- AVLTree<T>: insert(int key, T value), search(int key), inorderArray(), inorderString(), isEmpty()
- Graph, Vertex<T>, Edge

**Excepciones (co.edu.unbosque.utils.exception):**
- EstructuraVaciaException, PosicionInvalidaException, PartidaNoIniciadaException

**Clases generadas (co.edu.unbosque.model.persistencie):**
- RankingDTO (en co.edu.unbosque.model) — implementa Comparable<RankingDTO>
- JugadorDAO interfaz
- JugadorDAOImpl — usa MyLinkedList<JugadorDTO>
- RankingDAO — usa AVLTree<RankingDTO>, clave = posicionFinal*1000 - cantidadTurnos
- GestorTurnos — usa QueueImpl<JugadorDTO> + MyLinkedList<StackImpl<MovimientoDTO>>
- GestorColisiones — busca serpiente más cercana hacia atrás recursivamente
- HistorialDAO — usa MyDoubleLinkedList<MovimientoDTO>
- GestorComodines — usa NaryTree<String>(6 hijos) como árbol de decisión de efectos;
  métodos: activarEfecto(), verificarSinConflicto(), búsquedas bidireccionales recursivas

**Reglas de comodines (v2 — activas):**
- 7 comodines exactos por partida (GeneradorTablero.CANTIDAD_COMODINES = 7)
- Efecto por dado comodin: 1=escalera cercana, 2=serpiente cercana,
  3=+10, 4=-10, 5=x2, 6=/2 (entero, min 1)
- Búsqueda bidireccional: si empate distancia, prefiere adelante
- TableroDAO.aplicarComodines() solo marca tipo=COMODIN, sin arista especial
- TableroDAO.moverJugador() instancia GestorComodines y delega cuando casilla==COMODIN

**Vista generada (co.edu.unbosque.view):**
- VentanaPrincipal: JFrame 1100x700, no redim., CardLayout con 4 claves (MENU/CONFIG/JUEGO/GANADOR), define toda la paleta de colores como constantes static final
- PanelMenuInicio: BoxLayout vertical, fondo COLOR_FONDO_OSCURO, boton JUGAR navega a CONFIG
- PanelConfiguracion: BorderLayout, formulario con botones toggle 2/3/4 jugadores + JTextField nombres + JComboBox serpientes/escaleras (8-12); metodos: obtenerNombresJugadores(), obtenerCantidadJugadores/Serpientes/Escaleras(), setListenerIniciar()
- PanelTablero: paintComponent con boustrophedon, metodos obtenerNumeroCasilla/obtenerFilaColumna, actualizar(CasillaDTO[], JugadorDTO[]), leyenda en franja inferior 30px
- PanelDado: sub-JPanel con paintComponent dibuja cuadrado redondeado 80x80, setValorDado(), habilitarBoton(), setListenerLanzar()
- PanelInfoJugador: paintComponent, actualizar(JugadorDTO, int), muestra circulo color jugador + nombre + casilla + [ESCUDO]/[x2]
- PanelRanking: GridLayout(4,1), actualizar(JugadorDTO[]) con burbuja recursiva (ordenarBurbuja + burbujaPasada), lider resaltado amarillo
- PanelHistorial: JTextArea Monospaced 11pt no editable en JScrollPane, agregarEvento() con auto-scroll
- PanelControlJuego: BoxLayout Y, contiene Info(110px)+Dado(140px)+Ranking(120px)+Historial(flexible)+botonFinalizar(40px), ancho 265px; getters de cada subpanel + setListenerFinalizar()
- PanelJuego: BorderLayout, tablero CENTER + control EAST(265px); actualizarEstado(CasillaDTO[], JugadorDTO[], int indiceActual, int dado, String evento)
- PanelGanador: BoxLayout Y, fondo oscuro; cargar(JugadorDTO, Object[] rankingOrdenado, int mov, int serp, int esc) — itera Object[] con instanceof RankingDTO; botones NUEVA PARTIDA (-> MENU) y SALIR (System.exit)
- ViewFacade: co.edu.unbosque.view.facade; wraps VentanaPrincipal; actualizarTablero recibe indiceActual adicional vs spec original

**NOTA de firma de actualizarEstado:** El metodo PanelJuego.actualizarEstado recibe 5 parametros: (CasillaDTO[], JugadorDTO[], int indiceActual, int dadoActual, String eventoActual). ViewFacade.actualizarTablero tambien pasa indiceActual. El enunciado original tenia 4 params — se extendio para saber cual jugador es el activo.

**ModelFacade (co.edu.unbosque.model.facade):** centraliza acceso a TableroDAO, JugadorDAOImpl, GestorTurnos, GestorColisiones, RankingDAO, HistorialDAO.
- iniciarPartida(String[]) throws PosicionInvalidaException, EstructuraVaciaException — recrea todos los DAOs para resetear estado anterior
- lanzarDado() → int (guarda en ultimoDado)
- ejecutarTurno() throws PartidaNoIniciadaException, PosicionInvalidaException → String evento
- obtenerJugadorActual(), obtenerIndiceJugadorActual(), obtenerTodosJugadores() → JugadorDTO[]
- obtenerCasillasTablero() → CasillaDTO[101] (indice 0 no usado)
- verificarGanador() → JugadorDTO o null
- finalizarPartida(JugadorDTO), obtenerRankingFinal() → Object[]
- getUltimoDado(), getContadorSerpientes/Escaleras/Movimientos(), isPartidaEnCurso()
- Deteccion de colision: busca "COLISION con NombreX en casilla N" en el evento con extraerNombreColisionado()
- Contadores: evento.contains("SERPIENTE") && !contains("bloqueada") → serpientes++; contains("ESCALERA") → escaleras++

**Controlador (co.edu.unbosque.controller):** reemplaza a JuegoController — mismo paquete, misma responsabilidad.
- Atributos: modelo (ModelFacade), vista (ViewFacade), dadoLanzado (boolean), partidaActiva (boolean)
- Constructor recibe VentanaPrincipal, crea ViewFacade y ModelFacade, llama registrarListeners()
- Usa ActionListener anonimo (new ActionListener(){...}) — sin lambdas, compatible Java 7+
- accionIniciarPartida(): validarNombres recursivo, iniciarPartida(), habilitarBotonDado(true), actualizarVista(), mostrarJuego()
- accionLanzarDado(): guarda de partidaActiva y dadoLanzado, lanzarDado(), actualizarTablero (antes del turno), ejecutarTurno(), verificarGanador(), si gano → cerrarPartidaConGanador, sino → actualizarVista con nombre del siguiente
- accionFinalizarPartida(): determinarLiderRecursivo() sobre todos los jugadores → cerrarPartidaConGanador(lider)
- cerrarPartidaConGanador(JugadorDTO): finalizarPartida, habilitarBotonDado(false), mostrarGanador
- actualizarVista(String evento): metodo privado que llama vista.actualizarTablero con estado completo del modelo
- validarNombres(String[], int): recursivo — caso base indice>=length → true; si nulo/vacio → false; sino → siguiente
- determinarLiderRecursivo(JugadorDTO[], int, JugadorDTO): recursivo — caso base indice>=length → liderActual; compara posicion y avanza con el mayor

**AppMain:** actualizado — crea VentanaPrincipal y new Controlador(ventana) dentro del invokeLater.
**JuegoController.java:** debe eliminarse manualmente en Eclipse (fue reemplazado por Controlador.java).

**Conexiones visuales de tablero (serpientes/escaleras con imágenes PNG):**
- ModelFacade tiene obtenerConexionesSerpientes() y obtenerConexionesEscaleras() → int[][2]
  - Aristas de peso 2.0 = serpiente (cabeza → cola); peso 0.5 = escalera (base → cima)
  - Métodos privados recursivos: recolectarConexionesRecursivo, buscarDestinoAristaRecursivo, copiarConexionesRecursivo
  - Contador mutable int[]{0} como contenedor para la recursión (evita campo de instancia)
- PanelTablero carga serpiente.png / escalera.png desde archivos/imagenes/ en el constructor
  - Si el PNG no existe → fallback: rectángulo redondeado marrón/verde semitransparente
  - inicializarConexiones(int[][] serpientes, int[][] escaleras) guarda los pares y llama repaint()
  - dibujarConexiones → dibujarTodasSerpientes/dibujarTodasEscaleras (recursivos) → dibujarConexionImagen
  - dibujarConexionImagen: usa AffineTransform.translate(midX,midY) + rotate(atan2) + drawImage(longitud, alto)
  - La imagen se dibuja ANTES de los jugadores, DESPUÉS de las casillas (orden correcto en paintComponent)
- PanelJuego.inicializarConexionesTablero(int[][], int[][]) → delega a panelTablero.inicializarConexiones
- ViewFacade.inicializarConexionesTablero(int[][], int[][]) → delega a ventana.getPanelJuego()
- Controlador.accionIniciarPartida(): llama vista.inicializarConexionesTablero DESPUÉS de modelo.iniciarPartida y ANTES de actualizarVista

**Why:** Proyecto académico para Estructura de Datos, evaluado por el profesor. Debe verse como trabajo estudiantil auténtico.

**How to apply:** Mantener sin ciclos, todo recursivo, español camelCase, JavaDoc universitario en cada clase.
