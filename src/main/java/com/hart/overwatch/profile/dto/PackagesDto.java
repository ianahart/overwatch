package com.hart.overwatch.profile.dto;

public class PackagesDto {

    private FullPackageDto basic;

    private FullPackageDto standard;

    private FullPackageDto pro;

    public PackagesDto() {

    }

    public PackagesDto(FullPackageDto basic, FullPackageDto standard, FullPackageDto pro) {
        this.basic = basic;
        this.standard = standard;
        this.pro = pro;
    }

    public FullPackageDto getPro() {
        return pro;
    }

    public FullPackageDto getBasic() {
        return basic;
    }

    public FullPackageDto getStandard() {
        return standard;
    }

    public void setPro(FullPackageDto pro) {
        this.pro = pro;
    }

    public void setBasic(FullPackageDto basic) {
        this.basic = basic;
    }

    public void setStandard(FullPackageDto standard) {
        this.standard = standard;
    }

}
