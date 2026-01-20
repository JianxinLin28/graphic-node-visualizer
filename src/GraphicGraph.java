import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.util.ArrayList;

public class GraphicGraph extends Graph{

    private final JFrame window;
    public String saveNodeFileName;

    GraphicGraph() {
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(600, 600);
        window.setLocationRelativeTo(null);
        window.setLayout(new BorderLayout());
    }

    public void drawGraph() {
//        JScrollPane scrollPane = new JScrollPane();
//
//        window.add(scrollPane, BorderLayout.CENTER);
        GraphicHelper graphicHelper = new GraphicHelper(this);
        DrawingGraph drawing = new DrawingGraph(graphicHelper, window);
        drawing.saveNodeFileName = saveNodeFileName;

        JScrollPane scrollPane = new JScrollPane(drawing);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        window.add(scrollPane, BorderLayout.CENTER);   // main drawing area
        JPanel northPanel = new JPanel(); // default FlowLayout (left to right)
        northPanel.add(drawing.loadButton);
        northPanel.add(drawing.saveButton);

        window.add(northPanel, BorderLayout.NORTH);

        window.setVisible(true);
    }
}
