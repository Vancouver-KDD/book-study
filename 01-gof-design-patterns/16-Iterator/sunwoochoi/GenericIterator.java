import java.util.ArrayList;

/* Generic iterator for ArrayList */
public class GenericIterator<T> implements Iterator{
  private ArrayList<T> collection;
  private int idx;

  public GenericIterator(ArrayList<T> collection) {
    this.collection = collection;
    this.idx = 0;
  }
  @Override
  public boolean hasNext() {
    return idx == collection.size();
  }

  @Override
  public T next() {
    return collection.get(idx);
  }
}
