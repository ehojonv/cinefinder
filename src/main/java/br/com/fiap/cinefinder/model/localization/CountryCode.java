package br.com.fiap.cinefinder.model.localization;

public enum CountryCode {
    US("United States"),
    BR("Brazil"),
    FR("France");  
    
    private String countryName;

    CountryCode(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryName() {
        return countryName;
    }
}

