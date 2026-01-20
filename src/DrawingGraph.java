import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class DrawingGraph extends JComponent {

    private final GraphicHelper graphicHelper;
    public JButton saveButton;
    public JButton loadButton;
    public JFrame window;
    public String saveNodeFileName;

    public Dimension getPreferredSize() {
        return new Dimension(
                GraphicHelper.ACE * 100,  // width
                (Graph.graphHeight - 1) * 150 // height
        );
    }

    public DrawingGraph(GraphicHelper helper, JFrame window) {
        this.graphicHelper = helper;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e.getX(), e.getY());
            }
        });

        saveButton = new JButton("Save Selected");
        saveButton.addActionListener(e -> saveSelectedNodes());
        loadButton = new JButton("Load Selection");
        loadButton.addActionListener(e -> loadSelectedNodes());
        this.window = window;
    }

    private void saveSelectedNodes() {
        ArrayList<String> selectedData = new ArrayList<>();
        ArrayList<GraphNode>[] sorted = graphicHelper.getSortedGraph();

        for (ArrayList<GraphNode> graphNodes : sorted) {
            for (GraphNode node : graphNodes) {
                if (node.selected) {
                    selectedData.add(String.valueOf(node.data));
                }
            }
        }

        File file = new File(saveNodeFileName); // save in current directory
        try (PrintWriter out = new PrintWriter(file)) {
            for (String s : selectedData) {
                out.println(s);
            }
            JOptionPane.showMessageDialog(window,
                    "Saved " + selectedData.size() + " selected nodes to " + file.getAbsolutePath());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadSelectedNodes() {
        File file = new File(saveNodeFileName);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(window, "No saved selection found!");
            return;
        }

        // First, clear all previous selections
        ArrayList<GraphNode>[] sorted = graphicHelper.getSortedGraph();
        for (ArrayList<GraphNode> graphNodes : sorted) {
            for (GraphNode node : graphNodes) {
                node.selected = false;
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                for (ArrayList<GraphNode> graphNodes : sorted) {
                    for (GraphNode node : graphNodes) {
                        if (String.valueOf(node.data).equals(line)) {
                            node.selected = true;
                        }
                    }
                }
            }
            repaint();
            JOptionPane.showMessageDialog(window, "Loaded selection from " + file.getAbsolutePath());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleClick(int mx, int my) {
        ArrayList<GraphNode>[] sorted = graphicHelper.getSortedGraph();

        for (ArrayList<GraphNode> graphNodes : sorted) {
            for (GraphNode node : graphNodes) {

                int cx = node.x + 30;
                int cy = node.y + 30;

                int dx = mx - cx;
                int dy = my - cy;

                if (dx * dx + dy * dy <= 30 * 30) {
                    node.selected = !node.selected; // toggle
                    repaint();
                    return; // only toggle one node
                }
            }
        }
    }

    public static String addNewlinesEveryX(String input, int x) {
        if (input == null || x <= 0) {
            throw new IllegalArgumentException("x must be greater than 0");
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            if (i > 0 && i % x == 0) {
                sb.append('\n');
            }
            sb.append(input.charAt(i));
        }

        return sb.toString();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        ArrayList<GraphNode>[] sorted = graphicHelper.getSortedGraph();
        graphicHelper.updateX();
        int invert = 0;

        for (ArrayList<GraphNode> graphNodes : sorted) {
            for (GraphNode node : graphNodes) {

                // 🔹 fill background if selected
                if (node.selected) {
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillOval(node.x, node.y, 60, 60);
                }

                // 🔹 draw border
                g.setColor(Color.BLACK);
                g.drawOval(node.x, node.y, 60, 60);

                // text
                String word = addNewlinesEveryX(String.valueOf(node.data), 7);
                int x = node.x + 10;
                int y = node.y + 20;

                FontMetrics fm = g.getFontMetrics();
                int lineHeight = fm.getHeight();

                for (String line : word.split("\n")) {
                    g.drawString(line, x, y);
                    y += lineHeight;
                }

                // edges
                for (GraphNode neighbor : node.neighbors) {
                    g.setColor(invert % 2 == 0 ? Color.GREEN : Color.RED);

                    if (!neighbor.neighbors.contains(node)) {
                        if (neighbor.level >= node.level) {
                            drawArrowLine(g, node.x + 30, node.y + 60,
                                    neighbor.x + 30, neighbor.y);
                        } else {
                            drawArrowLine(g, node.x + 30, node.y,
                                    neighbor.x + 30, neighbor.y + 60);
                        }
                    } else if (neighbor.level >= node.level) {
                        g.setColor(Color.BLUE);
                        drawArrowLine(g, node.x + 30, node.y + 60,
                                neighbor.x + 30, neighbor.y);
                        drawArrow(g, neighbor.x + 30, neighbor.y,
                                node.x + 30, node.y + 60);
                    }

                    invert++;
                    g.setColor(Color.BLACK);
                }
            }
        }
    }

    private void drawArrowLine(Graphics g, int x1, int y1, int x2, int y2) {
        int dx = x2 - x1, dy = y2 - y1;
        double D = Math.sqrt(dx*dx + dy*dy);
        double xm = D - 5, xn = xm, ym = 5, yn = -5, x;
        double sin = dy / D, cos = dx / D;

        x = xm*cos - ym*sin + x1;
        ym = xm*sin + ym*cos + y1;
        xm = x;

        x = xn*cos - yn*sin + x1;
        yn = xn*sin + yn*cos + y1;
        xn = x;

        int[] xpoints = {x2, (int) xm, (int) xn};
        int[] ypoints = {y2, (int) ym, (int) yn};

        g.drawLine(x1, y1, x2, y2);
        g.fillPolygon(xpoints, ypoints, 3);
    }

    private void drawArrow(Graphics g, int x1, int y1, int x2, int y2) {
        int dx = x2 - x1, dy = y2 - y1;
        double D = Math.sqrt(dx*dx + dy*dy);
        double xm = D - 5, xn = xm, ym = 5, yn = -5, x;
        double sin = dy / D, cos = dx / D;

        x = xm*cos - ym*sin + x1;
        ym = xm*sin + ym*cos + y1;
        xm = x;

        x = xn*cos - yn*sin + x1;
        yn = xn*sin + yn*cos + y1;
        xn = x;

        int[] xpoints = {x2, (int) xm, (int) xn};
        int[] ypoints = {y2, (int) ym, (int) yn};

        g.setColor(Color.blue);
        g.fillPolygon(xpoints, ypoints, 3);
    }
}
