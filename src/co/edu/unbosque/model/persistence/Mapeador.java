package co.edu.unbosque.model.persistence;

import co.edu.unbosque.model.Casilla;
import co.edu.unbosque.model.CasillaDTO;
import co.edu.unbosque.model.Comodin;
import co.edu.unbosque.model.ComodinDTO;
import co.edu.unbosque.model.Escalera;
import co.edu.unbosque.model.EscaleraDTO;
import co.edu.unbosque.model.Jugador;
import co.edu.unbosque.model.JugadorDTO;
import co.edu.unbosque.model.Movimiento;
import co.edu.unbosque.model.MovimientoDTO;
import co.edu.unbosque.model.Ranking;
import co.edu.unbosque.model.RankingDTO;
import co.edu.unbosque.model.Serpiente;
import co.edu.unbosque.model.SerpienteDTO;
import co.edu.unbosque.utils.structure.MyLinkedList;
import co.edu.unbosque.utils.structure.Node;

public class Mapeador {

    private Mapeador() { }

    // =========================================================================
    // Jugador
    // =========================================================================

    public static Jugador jugadorDTOAEntidad(JugadorDTO dto) {
        return new Jugador(dto.getNombre(), dto.getPosicionActual(), dto.isTieneEscudo(),
                           dto.isTieneDobleTurno(), dto.isPierdeTurno(), dto.getCantidadTurnos());
    }

    public static JugadorDTO jugadorEntidadADTO(Jugador entidad) {
        JugadorDTO dto = new JugadorDTO(entidad.getNombre());
        dto.setPosicionActual(entidad.getPosicionActual());
        dto.setTieneEscudo(entidad.isTieneEscudo());
        dto.setTieneDobleTurno(entidad.isTieneDobleTurno());
        dto.setPierdeTurno(entidad.isPierdeTurno());
        dto.setCantidadTurnos(entidad.getCantidadTurnos());
        return dto;
    }

    public static MyLinkedList<JugadorDTO> listaJugadorEntidadADTO(MyLinkedList<Jugador> lista) {
        MyLinkedList<JugadorDTO> resultado = new MyLinkedList<>();
        listaJugadorEntidadADTORecursivo(lista.getFirst(), resultado);
        return resultado;
    }

    public static MyLinkedList<Jugador> listaJugadorDTOAEntidad(MyLinkedList<JugadorDTO> lista) {
        MyLinkedList<Jugador> resultado = new MyLinkedList<>();
        listaJugadorDTOAEntidadRecursivo(lista.getFirst(), resultado);
        return resultado;
    }

    private static void listaJugadorEntidadADTORecursivo(Node<Jugador> nodo,
            MyLinkedList<JugadorDTO> resultado) {
        if (nodo == null) return;
        resultado.addLast(jugadorEntidadADTO(nodo.getInfo()));
        listaJugadorEntidadADTORecursivo(nodo.getNext(), resultado);
    }

    private static void listaJugadorDTOAEntidadRecursivo(Node<JugadorDTO> nodo,
            MyLinkedList<Jugador> resultado) {
        if (nodo == null) return;
        resultado.addLast(jugadorDTOAEntidad(nodo.getInfo()));
        listaJugadorDTOAEntidadRecursivo(nodo.getNext(), resultado);
    }

    // =========================================================================
    // Casilla
    // =========================================================================

    public static Casilla casillaDTOAEntidad(CasillaDTO dto) {
        return new Casilla(dto.getNumeroCasilla(), dto.getTipo());
    }

    public static CasillaDTO casillaEntidadADTO(Casilla entidad) {
        return new CasillaDTO(entidad.getNumeroCasilla(), entidad.getTipo());
    }

    public static MyLinkedList<CasillaDTO> listaCasillaEntidadADTO(MyLinkedList<Casilla> lista) {
        MyLinkedList<CasillaDTO> resultado = new MyLinkedList<>();
        listaCasillaEntidadADTORecursivo(lista.getFirst(), resultado);
        return resultado;
    }

    public static MyLinkedList<Casilla> listaCasillaDTOAEntidad(MyLinkedList<CasillaDTO> lista) {
        MyLinkedList<Casilla> resultado = new MyLinkedList<>();
        listaCasillaDTOAEntidadRecursivo(lista.getFirst(), resultado);
        return resultado;
    }

    private static void listaCasillaEntidadADTORecursivo(Node<Casilla> nodo,
            MyLinkedList<CasillaDTO> resultado) {
        if (nodo == null) return;
        resultado.addLast(casillaEntidadADTO(nodo.getInfo()));
        listaCasillaEntidadADTORecursivo(nodo.getNext(), resultado);
    }

    private static void listaCasillaDTOAEntidadRecursivo(Node<CasillaDTO> nodo,
            MyLinkedList<Casilla> resultado) {
        if (nodo == null) return;
        resultado.addLast(casillaDTOAEntidad(nodo.getInfo()));
        listaCasillaDTOAEntidadRecursivo(nodo.getNext(), resultado);
    }

    // =========================================================================
    // Serpiente
    // =========================================================================

    public static Serpiente serpienteDTOAEntidad(SerpienteDTO dto) {
        return new Serpiente(dto.getPosicionCabeza(), dto.getPosicionCola());
    }

    public static SerpienteDTO serpienteEntidadADTO(Serpiente entidad) {
        return new SerpienteDTO(entidad.getPosicionCabeza(), entidad.getPosicionCola());
    }

    public static MyLinkedList<SerpienteDTO> listaSerpienteEntidadADTO(MyLinkedList<Serpiente> lista) {
        MyLinkedList<SerpienteDTO> resultado = new MyLinkedList<>();
        listaSerpienteEntidadADTORecursivo(lista.getFirst(), resultado);
        return resultado;
    }

    public static MyLinkedList<Serpiente> listaSerpienteDTOAEntidad(MyLinkedList<SerpienteDTO> lista) {
        MyLinkedList<Serpiente> resultado = new MyLinkedList<>();
        listaSerpienteDTOAEntidadRecursivo(lista.getFirst(), resultado);
        return resultado;
    }

    private static void listaSerpienteEntidadADTORecursivo(Node<Serpiente> nodo,
            MyLinkedList<SerpienteDTO> resultado) {
        if (nodo == null) return;
        resultado.addLast(serpienteEntidadADTO(nodo.getInfo()));
        listaSerpienteEntidadADTORecursivo(nodo.getNext(), resultado);
    }

    private static void listaSerpienteDTOAEntidadRecursivo(Node<SerpienteDTO> nodo,
            MyLinkedList<Serpiente> resultado) {
        if (nodo == null) return;
        resultado.addLast(serpienteDTOAEntidad(nodo.getInfo()));
        listaSerpienteDTOAEntidadRecursivo(nodo.getNext(), resultado);
    }

    // =========================================================================
    // Escalera
    // =========================================================================

    public static Escalera escaleraDTOAEntidad(EscaleraDTO dto) {
        return new Escalera(dto.getPosicionBase(), dto.getPosicionCima());
    }

    public static EscaleraDTO escaleraEntidadADTO(Escalera entidad) {
        return new EscaleraDTO(entidad.getPosicionBase(), entidad.getPosicionCima());
    }

    public static MyLinkedList<EscaleraDTO> listaEscaleraEntidadADTO(MyLinkedList<Escalera> lista) {
        MyLinkedList<EscaleraDTO> resultado = new MyLinkedList<>();
        listaEscaleraEntidadADTORecursivo(lista.getFirst(), resultado);
        return resultado;
    }

    public static MyLinkedList<Escalera> listaEscaleraDTOAEntidad(MyLinkedList<EscaleraDTO> lista) {
        MyLinkedList<Escalera> resultado = new MyLinkedList<>();
        listaEscaleraDTOAEntidadRecursivo(lista.getFirst(), resultado);
        return resultado;
    }

    private static void listaEscaleraEntidadADTORecursivo(Node<Escalera> nodo,
            MyLinkedList<EscaleraDTO> resultado) {
        if (nodo == null) return;
        resultado.addLast(escaleraEntidadADTO(nodo.getInfo()));
        listaEscaleraEntidadADTORecursivo(nodo.getNext(), resultado);
    }

    private static void listaEscaleraDTOAEntidadRecursivo(Node<EscaleraDTO> nodo,
            MyLinkedList<Escalera> resultado) {
        if (nodo == null) return;
        resultado.addLast(escaleraDTOAEntidad(nodo.getInfo()));
        listaEscaleraDTOAEntidadRecursivo(nodo.getNext(), resultado);
    }

    // =========================================================================
    // Comodin
    // =========================================================================

    public static Comodin comodinDTOAEntidad(ComodinDTO dto) {
        return new Comodin(dto.getPosicion());
    }

    public static ComodinDTO comodinEntidadADTO(Comodin entidad) {
        return new ComodinDTO(entidad.getPosicion());
    }

    public static MyLinkedList<ComodinDTO> listaComodinEntidadADTO(MyLinkedList<Comodin> lista) {
        MyLinkedList<ComodinDTO> resultado = new MyLinkedList<>();
        listaComodinEntidadADTORecursivo(lista.getFirst(), resultado);
        return resultado;
    }

    public static MyLinkedList<Comodin> listaComodinDTOAEntidad(MyLinkedList<ComodinDTO> lista) {
        MyLinkedList<Comodin> resultado = new MyLinkedList<>();
        listaComodinDTOAEntidadRecursivo(lista.getFirst(), resultado);
        return resultado;
    }

    private static void listaComodinEntidadADTORecursivo(Node<Comodin> nodo,
            MyLinkedList<ComodinDTO> resultado) {
        if (nodo == null) return;
        resultado.addLast(comodinEntidadADTO(nodo.getInfo()));
        listaComodinEntidadADTORecursivo(nodo.getNext(), resultado);
    }

    private static void listaComodinDTOAEntidadRecursivo(Node<ComodinDTO> nodo,
            MyLinkedList<Comodin> resultado) {
        if (nodo == null) return;
        resultado.addLast(comodinDTOAEntidad(nodo.getInfo()));
        listaComodinDTOAEntidadRecursivo(nodo.getNext(), resultado);
    }

    // =========================================================================
    // Movimiento
    // =========================================================================

    public static Movimiento movimientoDTOAEntidad(MovimientoDTO dto) {
        return new Movimiento(dto.getNombreJugador(), dto.getDado(),
                              dto.getPosAntes(), dto.getPosDespues(), dto.getEvento());
    }

    public static MovimientoDTO movimientoEntidadADTO(Movimiento entidad) {
        return new MovimientoDTO(entidad.getNombreJugador(), entidad.getDado(),
                                 entidad.getPosAntes(), entidad.getPosDespues(), entidad.getEvento());
    }

    public static MyLinkedList<MovimientoDTO> listaMovimientoEntidadADTO(MyLinkedList<Movimiento> lista) {
        MyLinkedList<MovimientoDTO> resultado = new MyLinkedList<>();
        listaMovimientoEntidadADTORecursivo(lista.getFirst(), resultado);
        return resultado;
    }

    public static MyLinkedList<Movimiento> listaMovimientoDTOAEntidad(MyLinkedList<MovimientoDTO> lista) {
        MyLinkedList<Movimiento> resultado = new MyLinkedList<>();
        listaMovimientoDTOAEntidadRecursivo(lista.getFirst(), resultado);
        return resultado;
    }

    private static void listaMovimientoEntidadADTORecursivo(Node<Movimiento> nodo,
            MyLinkedList<MovimientoDTO> resultado) {
        if (nodo == null) return;
        resultado.addLast(movimientoEntidadADTO(nodo.getInfo()));
        listaMovimientoEntidadADTORecursivo(nodo.getNext(), resultado);
    }

    private static void listaMovimientoDTOAEntidadRecursivo(Node<MovimientoDTO> nodo,
            MyLinkedList<Movimiento> resultado) {
        if (nodo == null) return;
        resultado.addLast(movimientoDTOAEntidad(nodo.getInfo()));
        listaMovimientoDTOAEntidadRecursivo(nodo.getNext(), resultado);
    }

    // =========================================================================
    // Ranking
    // =========================================================================

    public static Ranking rankingDTOAEntidad(RankingDTO dto) {
        return new Ranking(dto.getNombreJugador(), dto.getPosicionFinal(),
                           dto.getCantidadTurnos(), dto.isGano());
    }

    public static RankingDTO rankingEntidadADTO(Ranking entidad) {
        return new RankingDTO(entidad.getNombreJugador(), entidad.getPosicionFinal(),
                              entidad.getCantidadTurnos(), entidad.isGano());
    }

    public static MyLinkedList<RankingDTO> listaRankingEntidadADTO(MyLinkedList<Ranking> lista) {
        MyLinkedList<RankingDTO> resultado = new MyLinkedList<>();
        listaRankingEntidadADTORecursivo(lista.getFirst(), resultado);
        return resultado;
    }

    public static MyLinkedList<Ranking> listaRankingDTOAEntidad(MyLinkedList<RankingDTO> lista) {
        MyLinkedList<Ranking> resultado = new MyLinkedList<>();
        listaRankingDTOAEntidadRecursivo(lista.getFirst(), resultado);
        return resultado;
    }

    private static void listaRankingEntidadADTORecursivo(Node<Ranking> nodo,
            MyLinkedList<RankingDTO> resultado) {
        if (nodo == null) return;
        resultado.addLast(rankingEntidadADTO(nodo.getInfo()));
        listaRankingEntidadADTORecursivo(nodo.getNext(), resultado);
    }

    private static void listaRankingDTOAEntidadRecursivo(Node<RankingDTO> nodo,
            MyLinkedList<Ranking> resultado) {
        if (nodo == null) return;
        resultado.addLast(rankingDTOAEntidad(nodo.getInfo()));
        listaRankingDTOAEntidadRecursivo(nodo.getNext(), resultado);
    }
}
