import java.io.Serializable;

class Square implements Serializable {
    int row;
    int column;

    public Square(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public String toString() {
        return "(" + row + ", " + column + ")";
    }

    public boolean equals(Object o) {
        if (o instanceof Square) {
            Square other = (Square) o;
            return row == other.row && column == other.column;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return row * 1000 + column;
    }
}