import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class FractalExplorer {

    private int displaySize;
    private JImageDisplay imageDisplay;
    private FractalGenerator fractalGenerator;
    private Rectangle2D.Double range;

    public FractalExplorer(int displaySize) {
        this.displaySize = displaySize;
        this.fractalGenerator = new MandelbrotFractal();
        range = new Rectangle2D.Double();
        fractalGenerator.getInitialRange(range);
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Fractal Explorer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton clearButton = new JButton("Сброс изображения");
        clearButton.addActionListener(e -> {
            fractalGenerator.getInitialRange(range);
            drawFractal();
        });
        this.imageDisplay = new JImageDisplay(displaySize, displaySize);
        imageDisplay.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                double x = FractalGenerator.getCoord(range.x,
                                                     range.x + range.width,
                                                     displaySize,
                                                     e.getX());
                double y = FractalGenerator.getCoord(range.y,
                                                     range.y + range.height,
                                                     displaySize,
                                                     e.getY());
                fractalGenerator.recenterAndZoomRange(range, x, y, 0.5);
                drawFractal();
            }
        });

        frame.setLayout(new BorderLayout());
        frame.add(imageDisplay, BorderLayout.CENTER);
        frame.add(clearButton, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    private void drawFractal() {
        System.out.println("drawFractal() start");
        for (int x = 0; x < imageDisplay.getWidth(); x++) {
            for (int y = 0; y < imageDisplay.getHeight(); y++) {
                double xCoord = FractalGenerator.getCoord(range.x,
                                                          range.x + range.width,
                                                          displaySize,
                                                          x);
                double yCoord = FractalGenerator.getCoord(range.y,
                                                          range.y + range.height,
                                                          displaySize,
                                                          y);
                int numIterations = fractalGenerator.numIterations(xCoord, yCoord);
                if (numIterations == -1) {
                    imageDisplay.drawPixel(x, y, 0);
                } else {
                    float hue = 0.723f + (float)numIterations/200f;
                    imageDisplay.drawPixel(x, y, Color.HSBtoRGB(hue, 1f, 1f));
                }
            }
        }
        imageDisplay.repaint();
        System.out.println("drawFractal() done");
    }

    public static void main(String[] args) {
        FractalExplorer fractalExplorer = new FractalExplorer(800);
        fractalExplorer.createAndShowGUI();
        fractalExplorer.drawFractal();
    }
}
