import java.util.ArrayList;
import java.util.Arrays;

public class Cell {

    private int x;
    private int y;
    private ArrayList<int[][]> opzioni;
    private ArrayList<int[][]> optCostanti;
    private boolean collapse;
    private int valore;
    private int[][] value;
    private ArrayList<String> dictionary;
    public Cell(ArrayList<String> dictionary) {
        this.opzioni = new ArrayList<>();

        stringToMat(dictionary);
        this.optCostanti = new ArrayList<>();

        optCostanti.addAll(opzioni);
    }

    public Cell(int x, int y, int value, boolean collapse, ArrayList<String> dictionary) {
        this.x = x;
        this.y = y;
        this.dictionary = dictionary;
        this.value = new int[3][];
        this.collapse = collapse;
        this.opzioni = new ArrayList<>();
        this.optCostanti = new ArrayList<>();
        stringToMat(dictionary);
        optCostanti.addAll(opzioni);
    }

    // takes the options from the dictionary and puts them into the option variable
    private void stringToMat(ArrayList<String> dictionary) {
        this.opzioni.clear();  // Remove existing options
        for (String s : dictionary) {
            s = s.replaceAll("[{}]", ""); // Remove curly brackets
            String[] nums = s.split(",");
            if (nums.length != 9) {
                System.err.println("Formato non valido: " + s);
                continue;
            }
            int[][] mat = new int[3][3];
            try {
                for (int i = 0; i < 9; i++) {
                    mat[i / 3][i % 3] = Integer.parseInt(nums[i].trim());
                }
                opzioni.add(mat);
            } catch (NumberFormatException e) {
                System.err.println("Errore di parsing nella stringa: " + s);
            }
        }
    }


    public void collapseRandom() {
        if (!collapse && !opzioni.isEmpty()) {
            int index = (int) (Math.random() * opzioni.size());
            value = opzioni.get(index);
            collapse = true;
            opzioni.clear();
            opzioni.add(value);
        }
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int[][] getValue() {
        return value;
    }

    public void setValue(int[][] value) {
        this.value = value;
    }

    public ArrayList<int[][]> getOpzioni() {
        return opzioni;
    }

    public void setOpzioni(ArrayList<int[][]> opzioni) {
        this.opzioni = opzioni;
    }

    public boolean isCollapse() {
        return collapse;
    }

    public void setCollapse(boolean collapse) {
        this.collapse = collapse;
    }

    public ArrayList<int[][]> getOptCostanti() {
        return optCostanti;
    }

    public void setOptCostanti(ArrayList<int[][]> optCostanti) {
        this.optCostanti = optCostanti;
    }

    public ArrayList<String> getDictionary() {
        return dictionary;
    }

    public void setDictionary(ArrayList<String> dictionary) {
        this.dictionary = dictionary;
    }

    public void printValue() {
        for (int i = 0; i < 3; i++) {
            System.out.println("");
            for (int j = 0; j < 3; j++) {
                System.out.print(value[i][j] + "\t");
            }
        }
        System.out.println();
    }


}
