package data;

public class CostTableDTO {
    private CostRowDTO committed;
    private CostRowDTO realized;

    public CostTableDTO() {
    }

    public CostTableDTO(CostRowDTO committed, CostRowDTO realized) {
        this.committed = committed;
        this.realized = realized;
    }

    public CostRowDTO getCommitted() {
        return committed;
    }

    public void setCommitted(CostRowDTO committed) {
        this.committed = committed;
    }

    public CostRowDTO getRealized() {
        return realized;
    }

    public void setRealized(CostRowDTO realized) {
        this.realized = realized;
    }
}
