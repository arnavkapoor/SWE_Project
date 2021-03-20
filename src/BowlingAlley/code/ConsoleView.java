import java.awt.*;
import javax.swing.*;
import java.awt.event.*;


public class ConsoleView implements ActionListener {

    // create a frame
    static JFrame win;
    private JButton play;

    static JProgressBar bar;
    private boolean chancePlayed;
    private Bowler currentThrower;			// = the thrower who just took a throw

    public ConsoleView(Bowler currentThrower) {

        chancePlayed = false;
        win = new JFrame("Accuracy Bar - Stop closest to the center -- " + currentThrower.getNick());
        win.getContentPane().setLayout(new BorderLayout());
        ((JPanel) win.getContentPane()).setOpaque(false);

        bar = new JProgressBar( JProgressBar.VERTICAL, 0, 100);
        bar.setValue(50);

        play = new JButton("Play");
        play.addActionListener(this);

        JPanel panel = new JPanel();

        panel.add(bar);
        panel.add(play);

        win.getContentPane().add("Center", panel);

        win.pack();

        // Center Window on Screen
        Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
        win.setLocation(
                ((screenSize.width) / 2) - ((win.getSize().width) / 2),
                ((screenSize.height) / 2) - ((win.getSize().height) / 2));
        win.show();
    }

    public double getUpdate() {

        int curval = 0, movement_dir = 1;

        while(!chancePlayed) {
            bar.setValue(curval);
            curval += movement_dir;
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(curval == 100) movement_dir = -1;
            else if(curval == 0)  movement_dir = 1;
        }

        double skill = (Math.abs(curval - 50)/(50.0));
        return (1.0 - skill);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(play)) {
            chancePlayed = true;
        }
    }

    public void hideWindow() {
        win.hide();
    }
}
