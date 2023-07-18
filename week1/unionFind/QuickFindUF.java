/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

package week1.unionFind;

public class QuickFindUF {

    private int[] id;
    // The data structure for QF-UF. Index = node id, value = connection.

    public QuickFindUF(int N) {
        // Initialise all values to their index, e.g. no connections.
        id = new int[N];
        for (int i = 0; i < N; i++) {
            id[i] = i;
        }
    }

    public boolean connected(int p, int q) {
        // Check if the two indexes have the same value.
        return id[p] == id[q];
    }

    public void union(int p, int q) {
        // Connect p to q. Update all sharing p's value to be the same as q's.

        int pid = id[p]; // Keep what will be changed, as id[p] will be updated.
        int qid = id[q]; // Update to this value.

        for (int i = 0; i < id.length; i++) {
            // Then loop through array and update all values sharing pid
            // to qid.
            if (id[i] == pid) id[i] = qid;
        }
    }

    public static void main(String[] args) {

    }
}
