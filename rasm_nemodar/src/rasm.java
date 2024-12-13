import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import javax.swing.*;
import java.util.ArrayList;
import java.awt.Toolkit;


public class rasm extends JFrame{
    private ArrayList<ArrayList<Double>> points;
    private double scaleFactor = 60.0;

    public rasm(ArrayList<ArrayList<Double>> points) {
        this.points = points;
        initializeWindow();
    }

    private void initializeWindow() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        setTitle("Graph Plotter");
        setSize(toolkit.getScreenSize());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        createDrawingPanel();
        setVisible(true);
    }

    private void createDrawingPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGraph(g);
            }
        };

        getContentPane().add(panel);
        panel.setBackground(Color.WHITE);
        panel.addMouseWheelListener(this::onMouseWheelMoved);
    }

    private void onMouseWheelMoved(MouseWheelEvent event) {
        int rotation = event.getWheelRotation();
        scaleFactor = (rotation < 0) ? scaleFactor * 1.1 : scaleFactor / 1.1;
        repaint();
    }

    private void drawGraph(Graphics g) {
        int originX = getWidth() / 2;
        int originY = getHeight() / 2;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawAxes(g2d, originX, originY);
        plotPoints(g2d, originX, originY);
    }

    private void drawAxes(Graphics2D g2d, int originX, int originY) {
        g2d.setColor(Color.BLUE);
        g2d.drawLine(0, originY, getWidth(), originY);
        g2d.drawLine(originX, 0, originX, getHeight());
    }

    private void plotPoints(Graphics2D g2d, int originX, int originY) {
        g2d.setColor(Color.RED);
        for (int i = 0; i < points.size(); i++) {
            ArrayList<Double> point = points.get(i);
            int pixelX = originX + (int) (point.get(0) * scaleFactor);
            int pixelY = originY - (int) (point.get(1) * scaleFactor);

            g2d.fillOval(pixelX - 3, pixelY - 3, 6, 6);

            if (i > 0) {
                ArrayList<Double> previousPoint = points.get(i - 1);
                int prevPixelX = originX + (int) (previousPoint.get(0) * scaleFactor);
                int prevPixelY = originY - (int) (previousPoint.get(1) * scaleFactor);
                g2d.draw(new Line2D.Double(prevPixelX, prevPixelY, pixelX, pixelY));
            }
        }
    }

}
