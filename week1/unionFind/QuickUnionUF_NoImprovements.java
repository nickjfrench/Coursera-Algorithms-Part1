/* *****************************************************************************
 *  Name:              Nicholas French
 *  Created On:        19/7/2023
 **************************************************************************** */

package week1.unionFind;

public class QuickUnionUF_NoImprovements {
    private int[] id; // Same as QuickFindUF, except each value is its parent.

    public QuickUnionUF_NoImprovements(int N) {
        // Initialise all values to their index
        id = new int[N];
        for (int i = 0; i < N; i++)
            id[i] = i;
    }

    private int root(int i) {
        // Chase the parent pointers until the root is reached.
        while (i != id[i])
            i = id[i];
        return i; // return root.
    }

    public boolean connected(int p, int q) {
        // To check if nodes are connected, check their roots are the same.
        return root(p) == root(q);
    }

    public void union(int p, int q) {
        // Change the root of p to root of q.
        int i = root(p);
        int j = root(q);
        id[i] = j;
    }

    public static void main(String[] args) {

    }
}
