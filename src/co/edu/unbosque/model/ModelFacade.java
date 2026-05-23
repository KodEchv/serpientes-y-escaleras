package co.edu.unbosque.model;

import co.edu.unbosque.model.persistence.CasillaDAO;
import co.edu.unbosque.model.persistence.ComodinDAO;
import co.edu.unbosque.model.persistence.EscaleraDAO;
import co.edu.unbosque.model.persistence.GestorColisiones;
import co.edu.unbosque.model.persistence.GestorTurnos;
import co.edu.unbosque.model.persistence.JugadorDAO;
import co.edu.unbosque.model.persistence.MovimientoDAO;
import co.edu.unbosque.model.persistence.RankingDAO;
import co.edu.unbosque.model.persistence.SerpienteDAO;
import co.edu.unbosque.model.persistence.TableroDAO;

/**
 * Fachada del modelo del juego "Escaleras y Serpientes a lo Bosque".
 * Agrupa los DAOs de persistencia y los gestores del juego.
 * El Controlador interactua exclusivamente con esta clase.
 */
public class ModelFacade {

    private TableroDAO tableroDAO;
    private JugadorDAO jugadorDAO;
    private GestorTurnos gestorTurnos;
    private GestorColisiones gestorColisiones;
    private RankingDAO rankingDAO;
    private MovimientoDAO movimientoDAO;
    private CasillaDAO casillaDAO;
    private SerpienteDAO serpienteDAO;
    private EscaleraDAO escaleraDAO;
    private ComodinDAO comodinDAO;

    public ModelFacade() {
        this.tableroDAO       = new TableroDAO();
        this.jugadorDAO       = new JugadorDAO();
        this.gestorTurnos     = new GestorTurnos();
        this.gestorColisiones = new GestorColisiones();
        this.rankingDAO       = new RankingDAO();
        this.movimientoDAO    = new MovimientoDAO();
        this.casillaDAO       = new CasillaDAO();
        this.serpienteDAO     = new SerpienteDAO();
        this.escaleraDAO      = new EscaleraDAO();
        this.comodinDAO       = new ComodinDAO();
    }

    public TableroDAO getTableroDAO() { return tableroDAO; }
    public void setTableroDAO(TableroDAO tableroDAO) { this.tableroDAO = tableroDAO; }

    public JugadorDAO getJugadorDAO() { return jugadorDAO; }
    public void setJugadorDAO(JugadorDAO jugadorDAO) { this.jugadorDAO = jugadorDAO; }

    public GestorTurnos getGestorTurnos() { return gestorTurnos; }
    public void setGestorTurnos(GestorTurnos gestorTurnos) { this.gestorTurnos = gestorTurnos; }

    public GestorColisiones getGestorColisiones() { return gestorColisiones; }
    public void setGestorColisiones(GestorColisiones gestorColisiones) { this.gestorColisiones = gestorColisiones; }

    public RankingDAO getRankingDAO() { return rankingDAO; }
    public void setRankingDAO(RankingDAO rankingDAO) { this.rankingDAO = rankingDAO; }

    public MovimientoDAO getMovimientoDAO() { return movimientoDAO; }
    public void setMovimientoDAO(MovimientoDAO movimientoDAO) { this.movimientoDAO = movimientoDAO; }

    public CasillaDAO getCasillaDAO() { return casillaDAO; }
    public void setCasillaDAO(CasillaDAO casillaDAO) { this.casillaDAO = casillaDAO; }

    public SerpienteDAO getSerpienteDAO() { return serpienteDAO; }
    public void setSerpienteDAO(SerpienteDAO serpienteDAO) { this.serpienteDAO = serpienteDAO; }

    public EscaleraDAO getEscaleraDAO() { return escaleraDAO; }
    public void setEscaleraDAO(EscaleraDAO escaleraDAO) { this.escaleraDAO = escaleraDAO; }

    public ComodinDAO getComodinDAO() { return comodinDAO; }
    public void setComodinDAO(ComodinDAO comodinDAO) { this.comodinDAO = comodinDAO; }
}
