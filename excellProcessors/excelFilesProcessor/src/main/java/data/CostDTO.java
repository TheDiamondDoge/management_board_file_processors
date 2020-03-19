package data;

import java.util.Date;

public class CostDTO {
    private Date updated;
    private CostTableDTO charged;
    private CostTableDTO capex;

    public CostDTO() {
    }

    public CostDTO(Date updated, CostTableDTO charged, CostTableDTO capex) {
        this.updated = updated;
        this.charged = charged;
        this.capex = capex;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public CostTableDTO getCharged() {
        return charged;
    }

    public void setCharged(CostTableDTO charged) {
        this.charged = charged;
    }

    public CostTableDTO getCapex() {
        return capex;
    }

    public void setCapex(CostTableDTO capex) {
        this.capex = capex;
    }
}
