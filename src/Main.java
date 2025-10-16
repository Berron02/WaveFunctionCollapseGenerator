import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        final int ROW = Integer.parseInt(args[1]);
        final int COL = Integer.parseInt(args[2]);
        ArrayList<String> dictionary = new ArrayList<>();
        try {
            File myObj = new File("../dizionario.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                //System.out.println(data);
                dictionary.add(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        Cell[][] grid = new Cell[ROW][COL];

        //--INIZIALIZZAZIONE GRIGLIA--
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                grid[i][j] = new Cell(i, j, -1, false, dictionary);
            }
        }

        // Scegli un punto di partenza casuale
        int startX = (int) (Math.random() * ROW);
        int startY = (int) (Math.random() * COL);

        // Scegli una configurazione casuale
        int random = (int) (Math.random() * grid[startX][startY].getOptCostanti().size());
        int[][] init = new Cell(dictionary).getOptCostanti().get(random);

        // Collassa la cella iniziale
        //System.out.println("Cella iniziale (" + startX + ", " + startY + ") - Configurazione: " + random);
        grid[startX][startY].setValue(init);
        grid[startX][startY].setCollapse(true);
        grid[startX][startY].getOpzioni().clear();
        grid[startX][startY].getOpzioni().add(init);
        //grid[startX][startY].printValue();

        // Propaga le restrizioni ai vicini
        propagate(grid, startX, startY, ROW, COL);

        // Continua finché tutte le celle sono collassate
        while (true) {
            Cell next = getLowestEntropyCell(grid);
            if (next == null) break; // tutte le celle sono collassate

            next.collapseRandom();
            propagate(grid, next.getX(), next.getY(), ROW, COL);
        }

        // Stampa il risultato finale
        //System.out.println("\n--- Griglia finale come ID blocchi ---");
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                int id = changeCellToValue(grid[i][j].getValue());
                //System.out.printf("%2d ", id);
            }
            //System.out.println();
        }
        String fileN = "output.txt";
        try (PrintWriter writer = new PrintWriter(fileN)) {
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    int id = changeCellToValue(grid[i][j].getValue());
                    writer.printf("%2d ", id);
                }
                writer.println(); // nuova riga dopo ogni riga della griglia
            }
            //System.out.println("Griglia salvata in output.txt");
            System.out.println(fileN);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static int changeCellToValue(int[][] value) {
        int[][][] blocks = {
                {{0, 1, 0}, {0, 1, 1}, {0, 0, 0}}, // B1
                {{0, 1, 0}, {1, 1, 0}, {0, 0, 0}}, // B2
                {{0, 0, 0}, {1, 1, 0}, {0, 1, 0}}, // B3
                {{0, 0, 0}, {0, 1, 1}, {0, 1, 0}}, // B4
                {{0, 0, 0}, {1, 1, 1}, {0, 0, 0}}, // B5
                {{0, 1, 0}, {0, 1, 0}, {0, 1, 0}}, // B6
                {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}}, // B7
                {{0, 1, 0}, {1, 1, 1}, {0, 1, 0}}, // B8
                {{0, 0, 0}, {0, 1, 1}, {0, 0, 0}}, // B9
                {{0, 0, 0}, {1, 1, 0}, {0, 0, 0}}, // B10
                {{0, 1, 0}, {0, 1, 0}, {0, 0, 0}}, // B11
                {{0, 0, 0}, {0, 1, 0}, {0, 1, 0}}, // B12
        };

        for (int i = 0; i < blocks.length; i++) {
            if (comparePattern(value, blocks[i])) {
                return i; // Restituisce da 1 a 12
            }
        }

        return -1; // Nessuna corrispondenza
    }

    public static boolean comparePattern(int[][] a, int[][] b) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (a[i][j] != b[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }




    public static Cell getLowestEntropyCell(Cell[][] grid) {
        Cell minCell = null;
        int min = Integer.MAX_VALUE;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                Cell c = grid[i][j];
                if (!c.isCollapse() && c.getOpzioni().size() < min && c.getOpzioni().size() > 0) {
                    min = c.getOpzioni().size();
                    minCell = c;
                }
            }
        }
        return minCell;
    }

    public static void propagate(Cell[][] grid, int x, int y, int ROW, int COL) {
        ArrayList<int[]> stack = new ArrayList<>();
        stack.add(new int[]{x, y});

        while (!stack.isEmpty()) {
            int[] pos = stack.remove(0);
            int cx = pos[0];
            int cy = pos[1];
            Cell current = grid[cx][cy];

            int[][] directions = {
                    {-1, 0}, // sopra
                    {1, 0},  // sotto
                    {0, 1},  // destra
                    {0, -1}  // sinistra
            };

            for (int d = 0; d < directions.length; d++) {
                int nx = cx + directions[d][0];
                int ny = cy + directions[d][1];

                if (nx >= 0 && nx < ROW && ny >= 0 && ny < COL) {
                    Cell neighbor = grid[nx][ny];

                    if (!neighbor.isCollapse()) {

                        // Lista temporanea per opzioni valide in base al vicino
                        ArrayList<int[][]> newOptions = new ArrayList<>();

                        for (int[][] option : neighbor.getOpzioni()) {
                            boolean valid = false;
                            for (int[][] currentOption : current.getOpzioni()) {
                                if (isCompatible(currentOption, option, d)) {
                                    valid = true;
                                    break;
                                }
                            }
                            if (valid) newOptions.add(option);
                        }

                        if (newOptions.size() < neighbor.getOpzioni().size()) {
                            neighbor.setOpzioni(newOptions);
                            stack.add(new int[]{nx, ny});
                        }
                    }
                }
            }
        }
    }

    public static boolean isCompatible(int[][] a, int[][] b, int dir) {
        switch (dir) {
            case 0: // sopra
                for (int i = 0; i < 3; i++)
                    if (a[0][i] != b[2][i]) return false;
                break;
            case 1: // sotto
                for (int i = 0; i < 3; i++)
                    if (a[2][i] != b[0][i]) return false;
                break;
            case 2: // destra
                for (int i = 0; i < 3; i++)
                    if (a[i][2] != b[i][0]) return false;
                break;
            case 3: // sinistra
                for (int i = 0; i < 3; i++)
                    if (a[i][0] != b[i][2]) return false;
                break;
        }
        return true;
    }


}