package fa;

import java.util.Objects;

public class Pair<T,U> {
    public T first;
    public U second;

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    //TODO Add this for using as key in map / set
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(first, pair.first) &&
                Objects.equals(second, pair.second);
    }

    //TODO Add this for using as key in map / set
    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
