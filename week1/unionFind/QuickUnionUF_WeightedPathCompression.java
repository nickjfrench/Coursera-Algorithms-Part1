/* *****************************************************************************
 *  Name:              Nicholas French
 *  Created On:        19/7/2023
 **************************************************************************** */

package week1.unionFind;

public class QuickUnionUF_WeightedPathCompression {
    private int[] id; // Same as QuickFindUF, except each value is its parent.
    private int[] sz; // Weighted: Maintain new array to count number of objects rooted to at i.

    public QuickUnionUF_WeightedPathCompression(int N) {
        // Initialise all values to their index
        id = new int[N];
        for (int i = 0; i < N; i++)
            id[i] = i;
    }

    private int root(int i) {
        // Chase the parent pointers until the root is reached.
        while (i != id[i]) {
            // Grandparent Path Compression:
            // Simple, not as effective as to the root compression,
            // but doesn't require doing another loop to get each root().
            id[i] = id[id[i]];

            // Unchanged
            i = id[i];
        }
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
        if (i == j) return;

        // Weighted: always point smaller tree to larger.
        if (sz[i] < sz[j]) {
            // If j tree taller, i points to j. Then combine root sizes.
            id[i] = j;
            sz[j] += sz[i];
        }
        else {
            // i is taller than j.
            id[j] = i;
            sz[i] += sz[j];
        }
    }

    public static void main(String[] args) {

    }
}
