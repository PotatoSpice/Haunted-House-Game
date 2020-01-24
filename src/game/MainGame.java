package game;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

public class MainGame {
    
    // GUI & Graficos
    private JFrame window;
    private JLabel background;
    public final int width = 320;
    public final int height = 240;
    private int scale = 1;
    
    public final GraphicsConfiguration config =
            GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();
    
    // Jogo
    private HauntedHouseGame manualGame;
    
    public MainGame(String name, int scale) {
        // Jogo
        manualGame = new HauntedHouseGame(this);
        
        // Janela Principal
        window = new JFrame(config);
        window.setLayout(new java.awt.BorderLayout(0, 0));
        window.setResizable(false);
        window.setTitle(name); // "HauntedHouseGame"
        // window.addWindowListener(new FrameClose());
        window.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        if (scale > 0 && scale < 3) {
            window.setSize(width * scale, height * scale);
            this.scale = scale;
        } else {
            window.setSize(width, height);
            this.scale = 1;
        }
        window.setVisible(true);
        
        // Imagem de Fundo
        background = new JLabel();
        background.setIcon(new ImageIcon("C:\\Users\\Asus\\Desktop\\EngInformatica\\ED\\pepe.gif"));
        background.setLayout(new java.awt.BorderLayout(0, 0));
        
        // CRIAR BOTOES DO MENU
        // - PLAY
        //      - ESCOLHER DIFICULDADE
        //      - DETALHES DO JOGADOR (para classificação)
        //      - etc.
        //          - Classe do jogo manual iniciado
        //          - VISUALIZAR MAPA VISTO (JMenuBar ou uma keybind)
        // - SIMULAR JOGO (warning spoilers!)
        // - VISUALIZAR MAPA
        // - CARREGAR MAPA (ficheiro JSON) 
        // (anteriores redirecionados para este se não houver mapa)
        
        window.add(background);
    }
    
    public int getScale() {
        return scale;
    }
    
    public final JFrame getMainWindow() {
        return window;
    }
}
