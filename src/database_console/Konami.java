package database_console;

/**

 @author R-Mule
 */
import java.util.Map;
import java.util.TreeMap;

public class Konami {

    private int[] code =
    {
        38, 38, 40, 40, 37, 39, 37, 39, 66, 65
    };
    private Map<Integer, Integer>[] graph;
    private int currentNode = 0;

    public Konami() {
        graph = generateSequenceMap(code);
    }

    public boolean checkKonami(int keyPressed) {
        Integer nextNode = graph[currentNode].get(keyPressed);

        //Set currentNode to nextNode or to 0 if no matching sub-sequence exists
        currentNode = (nextNode == null ? 0 : nextNode);

        return currentNode == code.length - 1;
    }

    private Map<Integer, Integer>[] generateSequenceMap(int[] sequence) {

        //Create map
        Map<Integer, Integer>[] graph = new Map[sequence.length];
        for (int i = 0; i < sequence.length; i++)
        {
            graph[i] = new TreeMap<Integer, Integer>();
        }

        //i is delta
        for (int i = 0; i < sequence.length; i++)
        {
            loop:
            for (int j = i; j < sequence.length - 1; j++)
            {
                if (sequence[j - i] == sequence[j])
                {
                    //  System.out.println("If at Node "+j+" you give me seq["+(j-i+1) 
                    //          + "] OR " + (sequence[j-i+1]) + " , goto Node " + (j-i+1));

                    //Ensure that the longest possible sub-sequence is recognized
                    Integer value = graph[j].get(sequence[j - i + 1]);
                    if (value == null || value < j - i + 1)
                    {
                        graph[j].put(sequence[j - i + 1], j - i + 1);
                    }
                }
                else
                {
                    break loop;
                }
            }
        }
        return graph;
    }
}
