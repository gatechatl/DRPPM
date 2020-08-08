package stjude.projects.jinghuizhang.pcgpaltsplice;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class SortedList extends LinkedList<Double>
{
    private Comparator<Double> comparator;

    public SortedList(final Comparator<Double> comparator)
    {
        this.comparator = comparator;
    }

    /**
    * this ignores the index and delegates to .add() 
    * so it will be sorted into the correct place immediately.
    */
    @Override
    public void add(int index, Double element)
    {
        this.add(element);     
    }

    @Override
    public boolean add(final Double e)
    {
        final boolean result = super.add(e);
        Collections.sort(this, this.comparator);
        return result;
    }
}