import java.util.Scanner;
import java.io.File;

// Followed this tutorial on YouTube:
// https://www.youtube.com/watch?v=WHuRRpvNcDU

public class Run {
    public static void main(String[] args) {
        /*
        GraphicTree tree = new GraphicTree(511);
        for (int i = 0; i < 511; i++) {
            tree.add((int) (Math.random() * 100));
        }

        Tree.levelOrderTraversal(tree.root);
        tree.getReady(tree.root);
        tree.drawTree();
         */

        try {
            String folder = "lib/hunting-horn";
            File file = new File(folder + "/map.txt");
            Scanner scanner = new Scanner(file);
            GraphicGraph graph = new GraphicGraph();
            graph.saveNodeFileName = folder + "/saved.txt";
            graph.rareNodeFileName = folder + "/rare.txt";

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] splitResult = line.split(",");
                graph.addEdge(new GraphNode(splitResult[0]), new GraphNode(splitResult[1]));
            }
            graph.drawGraph();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
