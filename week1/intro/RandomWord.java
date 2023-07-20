/* *****************************************************************************
 *  Name:              Nicholas French
 *  Created On:        18/7/2023
 **************************************************************************** */

package week1.intro;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        String champion = "";
        for (int i = 1; !StdIn.isEmpty(); i++) {
            String s = StdIn.readString();

            // Using Knuth's Algorithm: Randomly sample from set, with equal probability.
            if (StdRandom.bernoulli(1.0 / i)) {
                champion = s;
            }
        }

        System.out.println(champion);
    }
}
