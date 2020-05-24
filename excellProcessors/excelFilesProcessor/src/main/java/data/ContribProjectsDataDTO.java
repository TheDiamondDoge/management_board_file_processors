package data;

import java.util.Date;
import java.util.List;

public class ContribProjectsDataDTO {
    private List<ContributingProjectDTO> offer;
    private List<ContributingProjectDTO> products;
    private Date maxDate;
    private Date minDate;

    public ContribProjectsDataDTO() {
    }

    public ContribProjectsDataDTO(List<ContributingProjectDTO> offer, List<ContributingProjectDTO> products, Date maxDate, Date minDate) {
        this.offer = offer;
        this.products = products;
        this.maxDate = maxDate;
        this.minDate = minDate;
    }

    public List<ContributingProjectDTO> getOffer() {
        return offer;
    }

    public void setOffer(List<ContributingProjectDTO> offer) {
        this.offer = offer;
    }

    public List<ContributingProjectDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ContributingProjectDTO> products) {
        this.products = products;
    }

    public Date getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }

    public Date getMinDate() {
        return minDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }
}
