import algorithms.*;
import java.util.Arrays;

class Main {
    public static void main(String[] args) {
        // Uncomment this line if you want to read from a file
        In.open("public/test3.in");
        Out.compareTo("public/test3.out");

        int t = In.readInt();
        for (int i = 0; i < t; i++) {
            testCase();
        }
        
        // Uncomment this line if you want to read from a file
        In.close();
    }
    public static void testCase(){
        int n = In.readInt();
        String[] fragments = new String[n];
        for (int i = 0; i < n; i++) {
        fragments[i] = In.readWord();
        }

        int[][] G = new int[27 * 27][27 * 27];
        int[][] unG = new int[27 * 27][27 * 27];
        int[] dOut = new int[27 * 27];
        int[] dIn = new int[27 * 27];
        for (String s : fragments) {
            addToGraph(G, unG, dIn, dOut, s);
        }

        // check basic eulererian walk condition
        int mismatchedDeg = 0;
        int end = 0;
        int start = 0;
        for (int i = 0; i < 27 * 27; i++) {
            if (dOut[i] != dIn[i]) {
                // if (dOut[i] == 1 && dIn[i] == 0){
                //   if (start == 0){
                //     start++;
                //     continue;
                //   }
                // } else if (dOut[i] == 0 && dIn[i] == 1){
                //   if(end == 0){
                //     end++;
                //     continue;
                //   }
                // }
                // char c1 = (char) (i / 27 + 97);
                // char c2 = (char) (i % 27 + 97);
                // System.out.println("mismatch at " + c1 + c2);
                if(Math.abs(dOut[i] - dIn[i])> 1)mismatchedDeg++;
                mismatchedDeg++;
            }
        }
        if (mismatchedDeg > 2) {
            Out.println("no");
            return;
        }

        // find a vertex that has a connection
        int i = 0;
        while (dOut[i] == 0 && dIn[i] == 0)
            i++;
        boolean[] visited = new boolean[27 * 27];
        visited[i] = true;
        DFS(unG, dOut, dIn, visited, i);

        // check if all relevant vertices have ben visited.
        for (int j = 0; j < visited.length; j++) {
            // if there exists a vertex that appears in the password snippts, but
            // did not get visited by DFS, the graph is not connected and
            // therefore the password cannot be reconstructed.
            if (!visited[j] && !(dOut[j] == 0 && dIn[j] == 0)) {
                Out.println("no");
                return;
            }
        }
        Out.println("yes");

    }

    public static void DFS(int[][] G, int[] dOut, int[] dIn, boolean[] visited, int s) {
        // get all neighbors
        int[] voisins = new int[dOut[s] + dIn[s]];
        int j = 0;
        for (int i = 0; i < G.length; i++) {
            if (G[s][i] == 1) {
                voisins[j] = i;
                j++;
            }
        }
        // exécuter d'abord profondeur cherche pour tous les voisins
        for (int i : voisins) {
            if (!visited[i]) {
                visited[i] = true;
                DFS(G, dOut, dIn, visited, i);
            }

        }

    }

    public static int charToIndex(String s) {
        char c1 = s.charAt(0);
        char c2 = s.charAt(1);

        return ((int) c1 - 97) * 27 + ((int) c2 - 97);
    }

    public static void addToGraph(int[][] G, int[][] unG, int[] dIn, int[] dOut, String s) {
        String sub1 = s.substring(0, 2);
        String sub2 = s.substring(1);
        // System.out.println("Adding edge " + sub1 + "->" + sub2);
        G[charToIndex(sub1)][charToIndex(sub2)] = 1;
        unG[charToIndex(sub1)][charToIndex(sub2)] = 1;
        unG[charToIndex(sub2)][charToIndex(sub1)] = 1;

        dIn[charToIndex(sub2)] += 1;
        dOut[charToIndex(sub1)] += 1;

    }
}