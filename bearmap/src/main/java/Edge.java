public class Edge {
    private long src;
    private long dst;
    private long tag;

    Edge(long tag, long from, long to) {
        src = from;
        dst = to;
        this.tag = tag;
    }
}