package com.loginapp.sendemail;
import java.util.List;
import lombok.Data;

@Data

public class FormData {
    private String name;
    private String email;
    private String phone;
    private String businessType;
    private String turnover;
    private boolean vat;
    private boolean payroll;
    private boolean bookkeeping;
    private String additionalInfo;
    private String countryOfTrade;
    private String fxCurrency;
    private boolean submit;
    private List<String> services; // 如果你需要也可以存储到数据库
    private String businessTypeOther; // 自定义业务类型
    private String otherServicesText; // 自定义其他服务

    // 记得生成 Getter & Setter，可以使用 Lombok：@Data 注解
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getBusinessType() { return businessType; }
    public void setBusinessType(String businessType) { this.businessType = businessType; }

    public String getTurnover() { return turnover; }
    public void setTurnover(String turnover) { this.turnover = turnover; }

    public boolean isVat() { return vat; }
    public void setVat(boolean vat) { this.vat = vat; }

    public boolean isPayroll() { return payroll; }
    public void setPayroll(boolean payroll) { this.payroll = payroll; }

    public boolean isBookkeeping() { return bookkeeping; }
    public void setBookkeeping(boolean bookkeeping) { this.bookkeeping = bookkeeping; }

    public String getAdditionalInfo() { return additionalInfo; }
    public void setAdditionalInfo(String additionalInfo) { this.additionalInfo = additionalInfo; }

    public String getCountryOfTrade() { return countryOfTrade; }
    public void setCountryOfTrade(String countryOfTrade) { this.countryOfTrade = countryOfTrade; }

    public String getFxCurrency() { return fxCurrency; }
    public void setFxCurrency(String fxCurrency) { this.fxCurrency = fxCurrency; }

    public boolean isSubmit() { return submit; }
    public void setSubmit(boolean submit) { this.submit = submit; }

    public List<String> getServices() { return services; }
    public void setServices(List<String> services) { this.services = services; }

    public String getBusinessTypeOther() { return businessTypeOther; }
    public void setBusinessTypeOther(String businessTypeOther) { this.businessTypeOther = businessTypeOther; }

    public String getOtherServicesText() { return otherServicesText; }
    public void setOtherServicesText(String otherServicesText) { this.otherServicesText = otherServicesText; }
}
