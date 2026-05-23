package co.edu.unbosque.view;

public class ViewFacade {

    private VentanaPrincipal ventana;
    private PanelMenuInicio panelMenu;
    private PanelConfiguracion panelConfiguracion;
    private PanelJuego panelJuego;
    private PanelGanador panelGanador;

    public ViewFacade() {
        ventana            = new VentanaPrincipal();
        panelMenu          = ventana.getPanelMenu();
        panelConfiguracion = ventana.getPanelConfiguracion();
        panelJuego         = ventana.getPanelJuego();
        panelGanador       = ventana.getPanelGanador();
    }

    public VentanaPrincipal getVentana() { return ventana; }
    public void setVentana(VentanaPrincipal ventana) { this.ventana = ventana; }

    public PanelMenuInicio getPanelMenu() { return panelMenu; }
    public void setPanelMenu(PanelMenuInicio panelMenu) { this.panelMenu = panelMenu; }

    public PanelConfiguracion getPanelConfiguracion() { return panelConfiguracion; }
    public void setPanelConfiguracion(PanelConfiguracion panelConfiguracion) { this.panelConfiguracion = panelConfiguracion; }

    public PanelJuego getPanelJuego() { return panelJuego; }
    public void setPanelJuego(PanelJuego panelJuego) { this.panelJuego = panelJuego; }

    public PanelGanador getPanelGanador() { return panelGanador; }
    public void setPanelGanador(PanelGanador panelGanador) { this.panelGanador = panelGanador; }
}
