import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.util.ArrayList;

public class GraphicGraph extends Graph{

    private final JFrame window;

    GraphicGraph() {
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(600, 600);
        window.setLocationRelativeTo(null);
        window.setLayout(new BorderLayout());
    }

    public void drawGraph() {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        window.add(scrollPane, BorderLayout.CENTER);
        GraphicHelper graphicHelper = new GraphicHelper(this);
        DrawingGraph label = new DrawingGraph(graphicHelper, window);
        JPanel drawPanel = new JPanel();
        drawPanel.setLayout(new BorderLayout());
        drawPanel.add(label, "Center");
        drawPanel.setPreferredSize(new Dimension(GraphicHelper.ACE*30, (Graph.graphHeight-1)*(h+30)+30));
        new Mover(drawPanel);
        scrollPane.setViewportView(drawPanel);

        window.add(label.saveButton, BorderLayout.SOUTH);

        window.setVisible(true);
    }
}
