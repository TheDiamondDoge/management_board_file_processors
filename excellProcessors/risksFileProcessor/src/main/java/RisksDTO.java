import java.util.List;

public class RisksDTO {
    private List<Risk> risks;
    private List<Error> errors;

    public RisksDTO() {
    }

    public RisksDTO(List<Risk> risks, List<Error> errors) {
        this.risks = risks;
        this.errors = errors;
    }

    public List<Risk> getRisks() {
        return risks;
    }

    public void setRisks(List<Risk> risks) {
        this.risks = risks;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }
}
