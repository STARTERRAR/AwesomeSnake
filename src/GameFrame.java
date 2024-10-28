import javax.swing.JFrame;

public class GameFrame extends JFrame{

    GameFrame(){

        //Create app window

        this.add(new GamePanel());
        this.setTitle("Awesome Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
